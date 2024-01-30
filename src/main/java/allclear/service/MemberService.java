package allclear.service;

import allclear.crawl.CrawlMemberInfo;
import allclear.domain.auth.RefreshToken;
import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.grade.SemesterSubject;
import allclear.domain.member.EmailCode;
import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.domain.requirement.RequirementComponent;
import allclear.dto.requestDto.member.*;
import allclear.dto.responseDto.MemberResponseDto;
import allclear.dto.responseDto.jwt.JwtToken;
import allclear.global.email.EmailService;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.jwt.JwtTokenProvider;
import allclear.repository.auth.RefreshTokenRepository;
import allclear.repository.grade.GradeRepository;
import allclear.repository.member.EmailCodeRepository;
import allclear.repository.member.MemberRepository;
import allclear.repository.requirement.RequirementRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RequirementRepository requirementRepository;
    @Autowired
    private final GradeRepository gradeRepository;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailCodeRepository emailCodeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }

    //로그인
    @Transactional
    public JwtToken login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalException(GlobalErrorCode._PASSWORD_MISMATCH);
        }

        // 1. email + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        refreshTokenRepository.save(RefreshToken.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build());

        return jwtToken;
    }

    //회원가입
    @Transactional
    public Long createMember(MemberSignupRequestDto request) {
        String password = passwordEncoder.encode(request.getPassword());
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(password);

        String usaintId = request.getUsaintId();
        String usaintPassword = request.getUsaintPassword();

        CrawlMemberInfo crawlMemberInfo = null;

        //멤버 정보 크롤링
        crawlMemberInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //크롤링 한 데이터 member에 저장
        assert crawlMemberInfo != null;
        Member newMember = crawlMemberInfo.getMember();
        member.setUsername(newMember.getUsername());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건
        Requirement newRequirement = crawlMemberInfo.getRequirement();
        newRequirement.setMember(member);

        //성적
        Grade newGrade = crawlMemberInfo.getGrade();
        newGrade.setMember(member);

        memberRepository.save(member);

        return member.getMemberId();
    }

    //회원가입 - 이메일 인증 코드 보내기
    @Transactional
    public void sendEmailCode(EmailAuthRequestDto emailAuthRequestDto) {
        String email = emailAuthRequestDto.getEmail();

        Optional<Member> foundEmail = memberRepository.findByEmail(email);
        if(foundEmail.isPresent()){
            //이메일 중복검사
            throw new GlobalException(GlobalErrorCode._DUPLICATE_EMAIL);
        }

        String subject = "AllClear 회원가입 인증 번호\n";
        String authCode = createCode(); //8글자 랜덤
        String text = "인증코드는 " + authCode + " 입니다\n";

        //email, code 저장
        EmailCode emailCode = EmailCode.builder()
                .email(email)
                .code(authCode)
                .build();
        emailCodeRepository.save(emailCode);

        //mail 전송
        emailService.sendEmail(email, subject, text);
    }

    //회원가입 - 이메일 인증 코드 확인
    @Transactional
    public boolean isEmailValid(EmailIsValidRequestDto request) {
        String email = request.getEmail();
        String code = request.getCode();

        Optional<EmailCode> targetEmailCode = emailCodeRepository.findByEmailAndCode(email, code);
        if(targetEmailCode.isPresent()){
            EmailCode emailCode = targetEmailCode.get();
            emailCodeRepository.delete(emailCode);
            return true;
        }
        else
            return false;
    }

    //회원 정보 업데이트
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMemberRequestDto){
        Member member = findOne(memberId);
        if (member == null) // 잘못된 id로 조회하는 경우
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        Requirement requirement = requirementRepository.findById(member.getRequirement().getRequirementId()).orElse(null);
        Grade grade = gradeRepository.findById(member.getGrade().getGradeId()).orElse(null);

        String usaintId = updateMemberRequestDto.getUsaintId();
        String usaintPassword = updateMemberRequestDto.getUsaintPassword();

        CrawlMemberInfo crawlInfo = null;
        crawlInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //멤버 초기화
        Member newMember = crawlInfo.getMember();
        member.setUsername(newMember.getUsername());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        // member.setEmail(newMember.getEmail());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건 초기화
        List<RequirementComponent> removeRequirementComponentList = requirement.getRequirementComponentList();
        for(int i = 0; i < removeRequirementComponentList.size(); i++){ // 연관관계 삭제
            removeRequirementComponentList.get(i).setRequirement(null);
        }
        removeRequirementComponentList.clear();
        requirementRepository.deleteById(requirement.getRequirementId()); // DB 삭제
        requirementRepository.flush(); // DB 반영

        //성적 초기화
        List<SemesterGrade> removeSemesterGradeList = grade.getSemesterGradeList();
        for(int i = 0; i < removeSemesterGradeList.size(); i++){
            SemesterGrade removeSemesterGrade = removeSemesterGradeList.get(i);
            List<SemesterSubject> removeSemesterSubjectList = removeSemesterGrade.getSemesterSubjectList();
            for(int j = 0; j < removeSemesterSubjectList.size(); j++){
                removeSemesterSubjectList.get(j).setSemesterGrade(null);
            }
            removeSemesterSubjectList.clear();
            removeSemesterGrade.setGrade(null);
        }
        grade.getSemesterGradeList().clear();
        gradeRepository.deleteById(grade.getGradeId()); // DB 삭제
        gradeRepository.flush(); // DB 반영

        Requirement newRequirement = crawlInfo.getRequirement();
        Grade newGrade = crawlInfo.getGrade();
        newRequirement.setMember(member);
        newGrade.setMember(member);
        requirementRepository.save(newRequirement);
        gradeRepository.save(newGrade);

    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0:
                    // a(97) ~ z(122)
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    // A(65) ~ Z(90)
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0 ~ 9
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

    //회원 탈퇴
    @Transactional
    public void deleteMember(Long id) {
        Member targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        memberRepository.deleteById(id);
    }

    //유저 조회
    public MemberResponseDto getMember(Long id) {
        Member targetMember = memberRepository.findById(id).
                orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS)); // 조회
        return new MemberResponseDto(findOne(id));
    }
}
