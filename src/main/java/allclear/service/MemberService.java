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
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional
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

        String usaintId = request.getUsaintId();
        String usaintPassword = request.getUsaintPassword();


        //멤버 정보 크롤링
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo(usaintId, usaintPassword);

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
                .semester(newMember.getSemester()).build();
        member.getRoles().add(request.getRole());

        //졸업요건
        Requirement newRequirement = crawlMemberInfo.getRequirement();
        newRequirement.setMember(member);

        //성적
        Grade newGrade = crawlMemberInfo.getGrade();
        newGrade.setMember(member);

        //시간표 생성기
        TimetableGenerator newTimetableGenerator;
        newTimetableGenerator = TimetableGenerator.builder()
                .tableYear(member.getLevel())
                .semester(member.getSemester())
                .prevSubjectIdList(crawlMemberInfo.getPrevSubjectIdList())
                .curriculumSubjectIdList(crawlMemberInfo.getCurriculumSubjectIdList())
                .build();
        newTimetableGenerator.setMember(member);

        //memberId 생성
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

    //회원 정보 업데이트
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));
        Requirement requirement = requirementRepository.findById(member.getRequirement().getRequirementId()).orElse(null);
        Grade grade = gradeRepository.findById(member.getGrade().getGradeId()).orElse(null);

        String usaintId = requestDto.getUsaintId();
        String usaintPassword = requestDto.getUsaintPassword();
        CrawlMemberInfo crawlInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //멤버 초기화
        Member newMember = crawlInfo.getMember();
        member.updateMember(newMember.getUsername(), newMember.getUniversity(), newMember.getMajor(),
                newMember.getClassType(), newMember.getEmail(), newMember.getLevel(), newMember.getSemester());

        //졸업요건 초기화
        assert requirement != null;
        List<RequirementComponent> removeRequirementComponentList = requirement.getRequirementComponentList();
        for (RequirementComponent component : removeRequirementComponentList) { // 연관관계 삭제
            component.setRequirement(null);
        }
        removeRequirementComponentList.clear();
        requirementRepository.deleteById(requirement.getRequirementId()); // DB 삭제
        requirementRepository.flush(); // DB 반영
        Requirement newRequirement = crawlInfo.getRequirement();
        newRequirement.setMember(member);
        requirementRepository.save(newRequirement);

        //성적 초기화
        assert grade != null;
        List<SemesterGrade> removeSemesterGradeList = grade.getSemesterGradeList();
        for (SemesterGrade removeSemesterGrade : removeSemesterGradeList) {
            List<SemesterSubject> removeSemesterSubjectList = removeSemesterGrade.getSemesterSubjectList();
            for (SemesterSubject semesterSubject : removeSemesterSubjectList) {
                semesterSubject.setSemesterGrade(null);
            }
            removeSemesterSubjectList.clear();
            removeSemesterGrade.setGrade(null);
        }
        grade.getSemesterGradeList().clear();
        gradeRepository.deleteById(grade.getGradeId()); // DB 삭제
        gradeRepository.flush(); // DB 반영
        Grade newGrade = crawlInfo.getGrade();
        newGrade.setMember(member);
        gradeRepository.save(newGrade);

        //시간표 생성기 초기화
        TimetableGenerator timetableGenerator;
        timetableGenerator = timetableGeneratorRepository.findById(member.getTimetableGenerator().getId())
                .orElse(null);
        assert timetableGenerator != null;
        timetableGenerator.getPrevSubjectIdList().clear();
        timetableGenerator.getCurriculumSubjectIdList().clear();
        timetableGeneratorRepository.save(timetableGenerator);
        timetableGenerator.getPrevSubjectIdList().addAll(crawlInfo.getPrevSubjectIdList());
        timetableGenerator.getCurriculumSubjectIdList().addAll(crawlInfo.getCurriculumSubjectIdList());
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
    public void deleteMember(Long id) {
        memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        memberRepository.deleteById(id);
    }

    //유저 조회
    public MemberResponseDto getMember(Long id) {
        return new MemberResponseDto(memberRepository.findById(id).
                orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS)));
    }

    //test 유저 생성
    @Transactional
    public Long createTestMember() {
        Member member;
        member = Member.builder().email("test@email.com").password(passwordEncoder.encode("testPassword"))
                .username("testUser").level(3).classType("가").major("소프트").semester(1).university("숭실대학교").build();
        member.getRoles().add("USER");


        Requirement requirement = new Requirement();
        requirement.setMember(member);

        RequirementComponent requirementComponent1;
        requirementComponent1 = RequirementComponent.builder().requirement(requirement)
                .requirementArgument("testArgument1")
                .requirementCategory("testCategory1")
                .requirementCriteria(3.0)
                .requirementComplete(1.0)
                .requirementResult("부족").build();
        requirement.addRequirementComponent(requirementComponent1);

        RequirementComponent requirementComponent2;
        requirementComponent2 = RequirementComponent.builder().requirement(requirement)
                .requirementArgument("testArgument2")
                .requirementCategory("testCategory2")
                .requirementCriteria(3.0)
                .requirementComplete(3.0)
                .requirementResult("충족").build();
        requirement.addRequirementComponent(requirementComponent2);


        Grade grade = new Grade();
        grade.setMember(member);
        grade.setAverageGrade("4.5");
        grade.setTotalCredit(110.5);
        ArrayList<SemesterSubject> semesterSubjectList1 = new ArrayList<>();
        semesterSubjectList1.add(SemesterSubject.createSemesterSubject("testSubject1", "4.5"));
        semesterSubjectList1.add(SemesterSubject.createSemesterSubject("testSubject2", "4.5"));
        SemesterGrade semesterGrade1 = SemesterGrade.createSemesterGrade(grade, "4.5", semesterSubjectList1);
        grade.addSemesterGrade(semesterGrade1);
        ArrayList<SemesterSubject> semesterSubjectList2 = new ArrayList<>();
        semesterSubjectList2.add(SemesterSubject.createSemesterSubject("testSubject4", "4.0"));
        semesterSubjectList2.add(SemesterSubject.createSemesterSubject("testSubject5", "3.0"));
        SemesterGrade semesterGrade2 = SemesterGrade.createSemesterGrade(grade, "3.5", semesterSubjectList2);
        grade.addSemesterGrade(semesterGrade2);


        TimetableGenerator newTimetableGenerator;
        newTimetableGenerator = TimetableGenerator.builder()
                .tableYear(2024)
                .semester(1).build();
        newTimetableGenerator.setMember(member);
//        newTimetableGenerator.setPrevSubjectIdList(crawlMemberInfo.getPrevSubjectIdList());
//        newTimetableGenerator.setCurriculumSubjectIdList(crawlMemberInfo.getCurriculumSubjectIdList());

        memberRepository.save(member);

        return member.getMemberId();
    }
}
