package allclear;

import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.grade.SemesterSubject;
import allclear.domain.member.Member;
import allclear.domain.member.UserDetailsImpl;
import allclear.domain.requirement.Requirement;
import allclear.domain.requirement.RequirementComponent;
import allclear.dto.requestDto.MemberSignupRequestDto;
import allclear.dto.requestDto.UpdateRequestDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.MemberRepository;
import allclear.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

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
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        UpdateRequestDto requestDto = new UpdateRequestDto("실제 usaintId 입력", "실제 usaintPasswd 입력");

        memberService.updateMember(userDetails, requestDto);

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
}
