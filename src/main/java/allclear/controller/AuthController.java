package allclear.controller;

import org.springframework.web.bind.annotation.*;

import allclear.dto.requestDto.auth.ReissueRequestDto;
import allclear.dto.responseDto.jwt.ReissueToken;
import allclear.global.response.ApiResponse;
import allclear.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "인증 - 토큰 재발급")
    @PostMapping("/reissue")
    public ApiResponse<ReissueToken> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        ReissueToken reissueToken =
                authService.refreshAccessToken(reissueRequestDto.getRefreshToken());
        return ApiResponse.onSuccess("재발급 성공하셨습니다.", reissueToken);
    }
}
