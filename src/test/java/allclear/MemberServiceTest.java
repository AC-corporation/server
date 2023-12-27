package allclear;

import allclear.domain.member.Member;
import allclear.dto.requestDto.MemberSignupRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


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
                "실제 usaintId 입력",
                "실제 usaintPasswd 입력"
        );

        //when
        memberService.createMember(request);

        //then
        Member member = memberRepository.findByEmail("test@example.com");

        assertEquals(member.getEmail(), "test@example.com");
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
            assertEquals(e.getErrorCode(), GlobalErrorCode._USAINT_LOGIN_FAILED);
        }
    }

    @Test
    public void 회원삭제() {
        //given
        Member member = new Member();
        member.setPassword("");
        member.setEmail("test@example.com");
        memberRepository.save(member);

        Long memberId = member.getMemberId();

        //when
        memberService.deleteMember(memberId);

        //then
        assertEquals(memberRepository.findByEmail("test@example.com"), null);
    }
}
