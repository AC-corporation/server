package allclear.service;

import allclear.crawl.CrawlMemberInfo;
import allclear.crawl.Grade.CrawlGradeAndCurriculumInfo;
import allclear.crawl.LoginUsaint;
import allclear.crawl.Requirement.CrawlRequirementInfo;
import allclear.domain.auth.RefreshToken;
import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.grade.SemesterSubject;
import allclear.domain.member.EmailCode;
import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.domain.requirement.RequirementComponent;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetableGenerator.TimetableGenerator;
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
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RequirementRepository requirementRepository;
    private final GradeRepository gradeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailCodeRepository emailCodeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TimetableGeneratorRepository timetableGeneratorRepository;

    //로그인
    @Transactional
    public JwtToken login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));
        Long memberId = member.getMemberId();

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
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication,memberId);

        JwtToken response = JwtToken.builder().grantType(jwtToken.getGrantType())
                .refreshToken(jwtToken.getRefreshToken()).accessToken(jwtToken.getAccessToken())
                .memberId(memberId).build();

        RefreshToken alreadyToken = refreshTokenRepository.findByMember(member);
        if (alreadyToken == null) {
            refreshTokenRepository.save(RefreshToken.builder()
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .member(member)
                    .build());
        } else {
            alreadyToken.updateRefreshToken(jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        }

        return response;
    }

    //회원가입
    @Transactional
    public Long createMember(MemberSignupRequestDto request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent())
            throw new GlobalException(GlobalErrorCode._DUPLICATE_EMAIL);

        // Roles 입력 안했을 시에 예외처리
        if (!request.getRole().equals("USER")) {
            throw new GlobalException(GlobalErrorCode._INVALID_ROLE);
        }

        String password = passwordEncoder.encode(request.getPassword());

        String usaintId = request.getUsaintId();
        String usaintPassword = request.getUsaintPassword();

        // 유세인트 로그인
        LoginUsaint loginUsaint = new LoginUsaint(usaintId, usaintPassword);
        //멤버 정보 크롤링
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo(loginUsaint.getDriver());

        //크롤링 한 데이터 member에 저장
        Member newMember = crawlMemberInfo.getMember();

        Member member;
        member = Member.builder()
                .email(request.getEmail())
                .password(password)
                .username(newMember.getUsername())
                .university(newMember.getUniversity())
                .major(newMember.getMajor())
                .classType(newMember.getClassType())
                .level(newMember.getLevel())
                .semester(newMember.getSemester())
                .admissionYear(newMember.getAdmissionYear())
                .detailMajor(newMember.getDetailMajor())
                .prevSubjectIdList(new ArrayList<>())
                .timetableList(new ArrayList<>())
                .build();
        member.getRoles().add(request.getRole());

        //기본 시간표 추가
        Timetable timetable = Timetable
                .builder()
                .tableName("새 시간표")
                .tableYear(LocalDate.now().getYear())
                .semester(LocalDate.now().getMonthValue() <= 6 ? 1 : 2)
                .timetableSubjectList(new ArrayList<>())
                .build();
        timetable.setMember(member);

        memberRepository.save(member);
        member.updateBasicTimetableId(timetable.getTimetableId());
        memberRepository.save(member);
        return member.getMemberId();
    }

    //회원가입 - 이메일 인증 코드 보내기
    @Transactional
    public void sendEmailCode(EmailAuthRequestDto emailAuthRequestDto) {
        String email = emailAuthRequestDto.getEmail();


        Optional<Member> foundEmail = memberRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
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


    // 회원가입 - 이메일 인증 코드 확인
    @Transactional
    public boolean isEmailValid(EmailIsValidRequestDto request) {
        String email = request.getEmail();
        String code = request.getCode();

        Optional<EmailCode> targetEmailCode = emailCodeRepository.findByEmailAndCode(email, code);
        if (targetEmailCode.isPresent()) {
            EmailCode emailCode = targetEmailCode.get();
            emailCodeRepository.delete(emailCode);
            return true;
        } else
            return false;
    }

    //회원 학적 정보 업데이트
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto requestDto) {

        String usaintId = requestDto.getUsaintId();
        String usaintPassword = requestDto.getUsaintPassword();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));

        // 멤버 업데이트
        LoginUsaint loginUsaint = new LoginUsaint(usaintId, usaintPassword);
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo(loginUsaint.getDriver());

        Member newMember = crawlMemberInfo.getMember();
        member.updateMember(newMember.getUsername(), newMember.getUniversity(), newMember.getMajor(),
                newMember.getClassType(), newMember.getLevel(), newMember.getSemester(),
                newMember.getAdmissionYear(), newMember.getDetailMajor());
        memberRepository.save(member);
    }

    // 졸업 요건 업데이트
    @Transactional
    public void updateRequirement(Long memberId, UpdateMemberRequestDto requestDto) {

        Requirement requirement;
        String usaintId = requestDto.getUsaintId();
        String usaintPassword = requestDto.getUsaintPassword();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));

        // 졸업요건 업데이트
        LoginUsaint loginUsaint = new LoginUsaint(usaintId, usaintPassword);
        CrawlRequirementInfo crawlRequirementInfo = new CrawlRequirementInfo(loginUsaint.getDriver());
        Requirement newRequirement = crawlRequirementInfo.getRequirement();

        if (member.getRequirement() != null)
            requirement = requirementRepository.findById(member.getRequirement().getRequirementId()).orElse(null);
        else
            requirement = null;

        if (requirement != null) {
            List<RequirementComponent> removeRequirementComponentList = requirement.getRequirementComponentList();
            for (RequirementComponent removeComponent : removeRequirementComponentList) { // 연관 관계 삭제
                removeComponent.setRequirement(null);
            }
            removeRequirementComponentList.clear();
            requirementRepository.deleteById(requirement.getRequirementId()); // DB 삭제
            requirementRepository.flush(); // DB 반영
        }
        newRequirement.setMember(member);
        requirementRepository.save(newRequirement);
    }

    // 성적 및 교과과정 정보 업데이트
    @Transactional
    public void updateGradeAndCurriculum(Long memberId, UpdateMemberRequestDto requestDto) {

        Grade grade;
        String usaintId = requestDto.getUsaintId();
        String usaintPassword = requestDto.getUsaintPassword();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));

        // 성적 업데이트
        LoginUsaint loginUsaint = new LoginUsaint(usaintId, usaintPassword);
        CrawlGradeAndCurriculumInfo crawlGradeInfo = new CrawlGradeAndCurriculumInfo(loginUsaint.getDriver(), member);
        Grade newGrade = crawlGradeInfo.getGrade();

        if (member.getGrade() != null)
            grade = gradeRepository.findById(member.getGrade().getGradeId()).orElse(null);
        else
            grade = null;

        if (grade != null) {
            List<SemesterGrade> removeSemesterGradeList = grade.getSemesterGradeList();
            for (SemesterGrade removeSemesterGrade : removeSemesterGradeList) {
                List<SemesterSubject> removeSemesterSubjectList = removeSemesterGrade.getSemesterSubjectList();
                for (SemesterSubject removeSemesterSubject : removeSemesterSubjectList) {
                    removeSemesterSubject.setSemesterGrade(null);
                }
                removeSemesterSubjectList.clear();
                removeSemesterGrade.setGrade(null);
            }
            grade.getSemesterGradeList().clear();
            gradeRepository.deleteById(grade.getGradeId()); // DB 삭제
            gradeRepository.flush(); // DB 반영
        }
        newGrade.setMember(member);
        gradeRepository.save(newGrade);

        //시간표 생성기 업데이트

        TimetableGenerator timetableGenerator;

        if (member.getTimetableGenerator() != null)
            timetableGenerator = timetableGeneratorRepository.findById(member.getTimetableGenerator().getId())
                    .orElse(null);
        else
            timetableGenerator = null;

        if (timetableGenerator != null) {
            timetableGenerator.getPrevSubjectIdList().clear();
            timetableGenerator.getCurriculumSubjectIdList().clear();
            timetableGeneratorRepository.save(timetableGenerator);
            timetableGenerator.getPrevSubjectIdList().addAll(member.getPrevSubjectIdList());
            timetableGenerator.getCurriculumSubjectIdList().addAll(crawlGradeInfo.getCurriculumSubjectIdList());
        } else {
            timetableGenerator = TimetableGenerator
                    .builder()
                    .tableYear(member.getLevel())
                    .semester(member.getSemester())
                    .prevSubjectIdList(member.getPrevSubjectIdList())
                    .curriculumSubjectIdList(crawlGradeInfo.getCurriculumSubjectIdList())
                    .timetableGeneratorSubjectList(new ArrayList<>())
                    .timetableGeneratorTimetableList(new ArrayList<>())
                    .build();
            timetableGenerator.setMember(member);

        }
        timetableGeneratorRepository.save(timetableGenerator);
    }

    private String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(3);
            switch (idx) {
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append(random.nextInt(9));
            }

        }
        return key.toString();
    }

    //회원 탈퇴
    @Transactional
    public void deleteMember(Long id,DeleteMemberDto deleteMemberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));

        //비밀번호 확인
        if (!passwordEncoder.matches(deleteMemberDto.getPassword(), member.getPassword())) {
            throw new GlobalException(GlobalErrorCode._PASSWORD_MISMATCH);
        }

        memberRepository.deleteById(id);
    }

    //유저 조회
    public MemberResponseDto getMember(Long id) {
        return new MemberResponseDto(memberRepository.findById(id).
                orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS)));
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long id,ChangePasswordDto request){
        String password = request.getCurrentPassword();

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));
        Long memberId = member.getMemberId();

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalException(GlobalErrorCode._PASSWORD_MISMATCH);
        }

        member.changePassword(passwordEncoder.encode(request.getNewPassword()));

    }
}
