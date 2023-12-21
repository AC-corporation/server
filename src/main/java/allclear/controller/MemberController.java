package allclear.controller;

import allclear.domain.Member.UserDetailsImpl;
import allclear.dto.requestDto.EmailIsValidRequestDto;
import allclear.dto.requestDto.LoginRequestDto;
import allclear.dto.requestDto.MemberSignupRequestDto;
import allclear.dto.requestDto.UpdateRequestDto;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.response.ApiResponse;
import allclear.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody MemberSignupRequestDto userSignupRequestDto){
        memberService.createMember(userSignupRequestDto);
        return ApiResponse.onSuccess("회원가입에 성공했습니다","");
    }

    @Operation(summary = "회원가입 - 이메일 인증")
    @PostMapping("/emailAuth")
    public ApiResponse emailAuth(String email){
        memberService.sendEmailCode(email);
        return ApiResponse.onSuccess("이메일 인증 코드를 발송했습니다","");
    }

    @Operation(summary = "회원가입 - 이메일 인증 코드 확인")
    @PostMapping("/emailIsValid")
    public ApiResponse emailIsValid(@RequestBody EmailIsValidRequestDto emailIsValidRequestDto){
        Boolean isValid = memberService.isEmailValid(emailIsValidRequestDto);
        if(isValid)
            return ApiResponse.onSuccess("이메일 인증에 성공했습니다","");
        else
            return ApiResponse.onFailure(GlobalErrorCode._INVALID_CODE,"");
    }


    //로그읜
    @Operation(summary = "로그인", description = "이메일, 비밀번호")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequestDto loginRequestDto){
        memberService.login(loginRequestDto);
        return ApiResponse.onSuccess("로그인에 성공했습니다","");
    }

    //업데이트
    @Operation(summary = "정보 업데이트", description = "유세인트 Id, Pwd 필요")
    @PostMapping("/update")
    public ApiResponse update(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody UpdateRequestDto updateRequestDto){
        memberService.updateMember(userDetails, updateRequestDto);
        return ApiResponse.onSuccess("정보 업데이트에 성공했습니다","");
    }


    //로그아웃
    @Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logout")
    public void logout(){

    }
}
