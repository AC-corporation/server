package all.clear.controller;

import all.clear.dto.requestDto.LoginRequesetDto;
import all.clear.dto.requestDto.UserSignupRequestDto;
import all.clear.global.response.ApiResponse;
import all.clear.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ResponseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    //회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("signup")
    public ApiResponse signup(@RequestBody UserSignupRequestDto userSignupRequestDto){
        userService.createUser(userSignupRequestDto);
        return ApiResponse.onSuccess("");
    }


    //로그읜
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인")
    public ApiResponse login(@RequestBody LoginRequesetDto loginRequesetDto){
        userService.login(loginRequesetDto);
        return ApiResponse.onSuccess("");
    }



    @Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logout")
    public void logout(){

    }
}
