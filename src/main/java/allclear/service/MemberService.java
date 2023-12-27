package allclear.service;

import allclear.crawl.CrawlMemberInfo;
import allclear.domain.member.Member;
import allclear.domain.member.UserDetailsImpl;
import allclear.domain.grade.Grade;
import allclear.domain.requirement.Requirement;
import allclear.dto.requestDto.*;
import allclear.dto.responseDto.MemberResponseDto;
import allclear.global.email.EmailService;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.exception.GlobalExceptionHandler;
import allclear.repository.GradeRepository;
import allclear.repository.MemberRepository;
import allclear.repository.RequirementRepository;
import allclear.dto.requestDto.EmailIsValidRequestDto;
import allclear.dto.requestDto.LoginRequestDto;
import allclear.dto.requestDto.MemberSignupRequestDto;
import allclear.dto.requestDto.UpdateRequestDto;
import allclear.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }

    //user/test/error controller test용 method
    public void temp() {
        throw new GlobalExceptionHandler(GlobalErrorCode._FORBIDDEN);
    }

    @Transactional
    //로그인
    public void login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email);

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalExceptionHandler(GlobalErrorCode._PASSWORD_MISMATCH);
        }


        /**
         * 수정필요
         */

    }


    //회원가입
    @Transactional
    public void createMember(MemberSignupRequestDto request) throws GlobalException {
        String password = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .email(request.getEmail())
                .password(password)
                .build();

        String usaintId = request.getUsaintId();
        String usaintPassword = request.getUsaintPassword();

        CrawlMemberInfo crawlMemberInfo = null;

        //멤버 정보 크롤링
        try {
            crawlMemberInfo = new CrawlMemberInfo(usaintId, usaintPassword);
        } catch (GlobalException e) {
            //로그인 실패 || 유세인트 이용 불가 시 컨트롤러로 예외 던짐
            if (e.getErrorCode() == GlobalErrorCode._USAINT_LOGIN_FAILED
                    || e.getErrorCode() == GlobalErrorCode._USAINT_UNAVAILABLE)
                throw e;
            else { //로그인 성공, 크롤링 실패이므로 member 저장
                memberRepository.save(member);
                return;
            }
        }
        //크롤링 한 데이터 member에 저장
        Member newMember = crawlMemberInfo.getMember();
        member.setMemberName(newMember.getMemberName());
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
    }


    @Transactional
    //회원가입 - 이메일 인증 코드 보내기
    public void sendEmailCode(EmailAuthRequestDto emailAuthRequestDto) {
        String email = emailAuthRequestDto.getEmail();
        String subject = "AllClear 회원가입 인증 번호\n";
        String authCode = createCode(); //8글자 랜덤
        String text = "인증코드는 " + authCode + " 입니다\n";
        //저장 기능 구현 필요(이메일, 코드 쌍)
        emailService.sendEmail(email, subject, text);
    }


    @Transactional
    //회원가입 - 이메일 인증 코드 확인
    public boolean isEmailValid(EmailIsValidRequestDto request) {
        String email = request.getEmail();
        String code = request.getCode();
        /**
         * 수정필요
         * 이메일과 코드 쌍이 일치하면 true 반환
         * 일치하지 않으면 false 반환
         * boolean 값에 따라 exception은 contoller에서 발생
         */
        return true;
    }


    /**
     * 회원 정보 업데이트
     */
    // 크롤링 실패했을 때 처리 추가 필요
    // 유저 정보 없을 때 처리 추가 필요
    @Transactional
    public void updateMember(UserDetailsImpl userDetails, UpdateRequestDto updateRequestDto) throws GlobalException {
        Member member = findOne(userDetails.getUser().getMemberId());
        String usaintId = updateRequestDto.getUsaintId();
        String usaintPassword = updateRequestDto.getUsaintPassword();

        CrawlMemberInfo crawlInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //멤버 초기화
        Member newMember = crawlInfo.getMember();
        member.setMemberName(newMember.getMemberName());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        // member.setEmail(newMember.getEmail());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건 초기화
        requirementRepository.delete(member.getRequirement());
        Requirement newRequirement = crawlInfo.getRequirement();
        newRequirement.setMember(member);
        requirementRepository.save(newRequirement);

        //성적 초기화
        gradeRepository.delete(member.getGrade());
        Grade newGrade = crawlInfo.getGrade();
        newGrade.setMember(member);
        gradeRepository.save(newGrade);
    }

    public String createCode() {
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

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    /**
     * 유저 조회
     */
    public MemberResponseDto getMember(Long id) {
        return new MemberResponseDto(findOne(id));
    }
}
