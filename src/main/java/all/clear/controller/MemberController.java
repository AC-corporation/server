package all.clear.controller;

import all.clear.dto.requestDto.LoginRequestDto;
import all.clear.dto.requestDto.MemberSignupRequestDto;
import all.clear.global.response.ApiResponse;
import all.clear.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("signup")
    public ApiResponse signup(@RequestBody MemberSignupRequestDto userSignupRequestDto){
        memberService.createUser(userSignupRequestDto);
        return ApiResponse.onSuccess("");
    }


    //로그읜
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인")
    public ApiResponse login(@RequestBody LoginRequestDto loginRequestDto){
        memberService.login(loginRequestDto);
        return ApiResponse.onSuccess("");
    }



    //@Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logout")
    public void logout(){

    }
}
