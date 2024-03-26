package allclear;

import jakarta.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {
    /*
       @Autowired
       MemberService memberService;

       @Autowired
       MemberRepository memberRepository;

       @Test
       public void 회원가입_성공() {
           //given
           MemberSignupRequestDto request = new MemberSignupRequestDto(
                   "test@example.com", "",
                   "실제 usaintId 입력", "실제 usaintPasswd 입력"
           );

           //when
           memberService.createMember(request);

           //then
           Member member = memberRepository.findAll().get(0);

           assertNotEquals(null, member);
       }

       @Test
       public void 회원가입_로그인실패() {
           //given
           MemberSignupRequestDto request = new MemberSignupRequestDto("", "", "", "");

           //when
           try {
               memberService.createMember(request);
           } catch (GlobalException e) {

               //then
               assertEquals(GlobalErrorCode._USAINT_LOGIN_FAILED, e.getErrorCode());
           }
       }

       @Test
       public void 회원삭제() {
           //given
           Member member = createMember();
           memberRepository.save(member);

           Long memberId = member.getMemberId();

           //when
           memberService.deleteMember(memberId);

           //then
           assertEquals(memberRepository.findByEmail("test@example.com"), null);
       }

       @Test
       public void 업데이트() {
           //given
           Member member = createMember();
           memberRepository.save(member);

           Long memberId = member.getMemberId();

           //when
           UpdateMemberRequestDto requestDto = new UpdateMemberRequestDto("실제 usaintId 입력", "실제 usaintPasswd 입력");

           memberService.updateMember(memberId, requestDto);

           //then
           Member updateMember = memberRepository.findById(memberId).get();
           //기존 member는 classType = null
           assertNotEquals(null, updateMember.getClassType());
       }

       private Member createMember() {
           Member member = new Member();
           member.setPassword("1234");
           member.setEmail("test@example.com");
           member.setClassType(null);

           Grade grade = new Grade();
           grade.setMember(member);
           ArrayList<SemesterSubject> subjectList = new ArrayList<>();
           subjectList.add(SemesterSubject.createSemesterSubject("subject1", "3.0"));
           subjectList.add(SemesterSubject.createSemesterSubject("subject2", "4.0"));
           grade.addSemesterGrade(SemesterGrade.createSemesterGrade(grade, "3.5", subjectList));

           Requirement requirement = new Requirement();
           requirement.setMember(member);
           requirement.addRequirementComponent(new RequirementComponent());
           requirement.addRequirementComponent(new RequirementComponent());

           return member;
       }

    */
}
