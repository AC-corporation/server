package allclear.service;

import allclear.crawl.CrawlMemberInfo;
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
import allclear.global.email.EmailService;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.grade.GradeRepository;
import allclear.repository.member.EmailCodeRepository;
import allclear.repository.member.MemberRepository;
import allclear.repository.requirement.RequirementRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import com.beust.ah.A;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RequirementRepository requirementRepository;
    @Autowired
    private final GradeRepository gradeRepository;
    @Autowired
    private final TimetableGeneratorRepository timetableGeneratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailCodeRepository emailCodeRepository;


    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }

    //로그인
    @Transactional
    public Long login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email);
        //member 조회
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalException(GlobalErrorCode._PASSWORD_MISMATCH);
        }

        return member.getMemberId();
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

        //시간표 생성기
        TimetableGenerator newTimetableGenerator = new TimetableGenerator();
        newTimetableGenerator.setMember(member);
        newTimetableGenerator.setTableYear(member.getLevel());
        newTimetableGenerator.setSemester(member.getSemester());
//        newTimetableGenerator.setPrevSubjectIdList(crawlMemberInfo.getPrevSubjectIdList());
//        newTimetableGenerator.setCurriculumSubjectIdList(crawlMemberInfo.getCurriculumSubjectIdList());

        //memberId 생성
        memberRepository.save(member);

        return member.getMemberId();
    }

    //회원가입 - 이메일 인증 코드 보내기
    @Transactional
    public void sendEmailCode(EmailAuthRequestDto emailAuthRequestDto) {
        String email = emailAuthRequestDto.getEmail();

        Optional<Member> foundEmail = Optional.ofNullable(memberRepository.findByEmail(email));
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

    //회원가입 - 이메일 인증 코드 확인
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
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMemberRequestDto) {
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
        member.setMemberName(newMember.getMemberName());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        // member.setEmail(newMember.getEmail());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건 초기화
        List<RequirementComponent> removeRequirementComponentList = requirement.getRequirementComponentList();
        for (int i = 0; i < removeRequirementComponentList.size(); i++) { // 연관관계 삭제
            removeRequirementComponentList.get(i).setRequirement(null);
        }
        removeRequirementComponentList.clear();
        requirementRepository.deleteById(requirement.getRequirementId()); // DB 삭제
        requirementRepository.flush(); // DB 반영

        //성적 초기화
        List<SemesterGrade> removeSemesterGradeList = grade.getSemesterGradeList();
        for (int i = 0; i < removeSemesterGradeList.size(); i++) {
            SemesterGrade removeSemesterGrade = removeSemesterGradeList.get(i);
            List<SemesterSubject> removeSemesterSubjectList = removeSemesterGrade.getSemesterSubjectList();
            for (int j = 0; j < removeSemesterSubjectList.size(); j++) {
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

    //test 유저 생성
    @Transactional
    public Long createTestMember() {
        Member member = new Member();
        member.setEmail("test@email.com");
        member.setPassword(passwordEncoder.encode(""));
        member.setMemberName("testUser");
        member.setLevel(3);
        member.setClassType("가");
        member.setMajor("소프트");
        member.setSemester(1);
        member.setUniversity("숭실대학교");

        Requirement requirement = new Requirement();
        requirement.setMember(member);
        RequirementComponent requirementComponent1 = new RequirementComponent();
        requirementComponent1.setRequirement(requirement);
        requirementComponent1.setRequirementArgument("testArgument1");
        requirementComponent1.setRequirementCategory("testCategory1");
        requirementComponent1.setRequirementCriteria(3.0);
        requirementComponent1.setRequirementComplete(1.0);
        requirementComponent1.setRequirementResult("부족");
        requirement.addRequirementComponent(requirementComponent1);
        RequirementComponent requirementComponent2 = new RequirementComponent();
        requirementComponent2.setRequirement(requirement);
        requirementComponent2.setRequirementArgument("testArgument2");
        requirementComponent2.setRequirementCategory("testCategory2");
        requirementComponent2.setRequirementCriteria(3.0);
        requirementComponent2.setRequirementComplete(3.0);
        requirementComponent2.setRequirementResult("충족");
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


        TimetableGenerator newTimetableGenerator = new TimetableGenerator();
        newTimetableGenerator.setMember(member);
        newTimetableGenerator.setTableYear(2024);
        newTimetableGenerator.setSemester(1);
//        newTimetableGenerator.setPrevSubjectIdList(crawlMemberInfo.getPrevSubjectIdList());
//        newTimetableGenerator.setCurriculumSubjectIdList(crawlMemberInfo.getCurriculumSubjectIdList());

        memberRepository.save(member);

        return member.getMemberId();
    }
}
