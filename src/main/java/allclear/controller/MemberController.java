package allclear.controller;

import allclear.dto.requestDto.member.*;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.response.ApiResponse;
import allclear.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags="api 정보 제공하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/test/ok")
    public ApiResponse test(){
        return ApiResponse.onSuccess("mapping test");
    }

    @Operation(summary = "test 유저 생성", description = "회원가입 서비스 작동하지 않을 때 test 유저 생성")
    @PostMapping("/test/createUser")
    public ApiResponse createTestMember() {
        return ApiResponse.onSuccess("테스트 유저 생성에 성공했습니다", memberService.createTestMember());
    }

    //회원가입
    @Operation(summary = "회원가입", description = "회원 생성")
    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody MemberSignupRequestDto userSignupRequestDto){
        Long memberId = memberService.createMember(userSignupRequestDto);
        return ApiResponse.onSuccess("회원가입에 성공했습니다", memberId);
    }

    @Operation(summary = "회원가입 - 이메일 인증")
    @PostMapping("/signup/emailAuth")
    public ApiResponse emailAuth(@RequestBody EmailAuthRequestDto emailAuthRequestDto){
        memberService.sendEmailCode(emailAuthRequestDto);
        return ApiResponse.onSuccess("이메일 인증 코드를 발송했습니다");
    }

    @Operation(summary = "회원가입 - 이메일 인증 코드 확인")
    @PostMapping("/signup/emailIsValid")
    public ApiResponse emailIsValid(@RequestBody EmailIsValidRequestDto emailIsValidRequestDto){
        Boolean isValid = memberService.isEmailValid(emailIsValidRequestDto);
        if(isValid)
            return ApiResponse.onSuccess("이메일 인증에 성공했습니다");
        else
            return ApiResponse.onFailure(GlobalErrorCode._INVALID_CODE);
    }


    //로그인
    @Operation(summary = "로그인", description = "이메일, 비밀번호")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequestDto loginRequestDto){
        Long memberId = memberService.login(loginRequestDto);
        return ApiResponse.onSuccess("로그인에 성공했습니다", memberId);
    }

    //로그아웃
    @Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logout/{userId}")
    public ApiResponse logout(@PathVariable Long userId){
        return ApiResponse.onSuccess("로그아웃에 성공했습니다");
    }

    //업데이트
    @Operation(summary = "정보 업데이트", description = "유저 Id, 유세인트 Id, Pwd 필요")
    @PutMapping("/{userId}")
    public ApiResponse update(@PathVariable Long userId, @RequestBody UpdateMemberRequestDto updateMemberRequestDto){
        memberService.updateMember(userId, updateMemberRequestDto);
        return ApiResponse.onSuccess("정보 업데이트에 성공했습니다");
    }

    //유저조회
    @Operation(summary = "유저 조회", description = "유저 조회")
    @GetMapping("/{userId}")
    public ApiResponse get(@PathVariable Long userId) { //인자 수정 필요
        return ApiResponse.onSuccess("유저 조회에 성공했습니다", memberService.getMember(userId));
    }

    //회원탈퇴
    @Operation(summary = "회원탈퇴", description = "회원탈퇴")
    @DeleteMapping("/{userId}")
    public ApiResponse delete(@PathVariable Long userId) {
        memberService.deleteMember(userId);
        return ApiResponse.onSuccess("회원 탈퇴에 성공했습니다");
    }
}
