package all.clear.controller;

import all.clear.dto.responseDto.requirement.RequirementResponseDto;
import all.clear.global.response.ApiResponse;
import all.clear.service.RequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requirement")
public class RequirementController {
    private final RequirementService requirementService;


    //@Operation(summary = "졸업요건 조회", description = "졸업요건 조회")
    //@GetMapping("/look") // string 수정 필요
    public ApiResponse<RequirementResponseDto> get(Long userId){ //인자 수정 필요
        return ApiResponse.onSuccess(requirementService.getRequirement(userId));
    }
}
