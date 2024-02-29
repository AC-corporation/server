package allclear.controller;

import allclear.dto.responseDto.requirement.RequirementResponseDto;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.jwt.JwtTokenProvider;
import allclear.global.response.ApiResponse;
import allclear.service.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requirement")
public class RequirementController {
    private final RequirementService requirementService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "졸업요건 조회", description = "졸업요건 조회")
    @GetMapping("/{userId}")
    public ApiResponse<RequirementResponseDto> getRequirement(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader){
        if(!jwtTokenProvider.compareMember(authorizationHeader,userId))
            return ApiResponse.onFailure(GlobalErrorCode._UNAUTHORIZED,null);
        return ApiResponse.onSuccess("졸업요건 조회에 성공했습니다",requirementService.getRequirement(userId));
    }
}
