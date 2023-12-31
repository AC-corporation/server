package allclear.controller;

import allclear.dto.responseDto.requirement.RequirementResponseDto;
import allclear.global.response.ApiResponse;
import allclear.service.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requirement")
public class RequirementController {
    private final RequirementService requirementService;


    @Operation(summary = "졸업요건 조회", description = "졸업요건 조회")
    @GetMapping("/get/{userId}")
    public ApiResponse<RequirementResponseDto> get(@PathVariable Long userId){ //인자 수정 필요
        return ApiResponse.onSuccess("졸업요건 조회에 성공했습니다",requirementService.getRequirement(userId));
    }
}
