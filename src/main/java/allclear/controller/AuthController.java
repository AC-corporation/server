package allclear.controller;

import allclear.dto.requestDto.auth.ReissueRequestDto;
import allclear.dto.responseDto.jwt.JwtToken;
import allclear.global.response.ApiResponse;
import allclear.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "인증 - 토큰 재발급")
    @PostMapping("/reissue")
    public ApiResponse<JwtToken> reissue(@RequestBody ReissueRequestDto reissueRequestDto){
        JwtToken jwtToken = authService.refreshAccessToken(reissueRequestDto.getRefreshToken());
        return ApiResponse.onSuccess("재발급 성공하셨습니다.",jwtToken);
    }

}
