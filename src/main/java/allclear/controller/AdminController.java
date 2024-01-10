package allclear.controller;

import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
import allclear.global.exception.GlobalException;
import allclear.global.response.ApiResponse;
import allclear.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags="api 정보 제공하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final SubjectService subjectService;

    @Operation(summary = "유세인트 과목 생성(초기화)")
    @PostMapping("/subject/initAll")
    public ApiResponse createSubject(@RequestBody CreateSubjectRequestDto requestDto) {
        try {
            subjectService.createSubject(requestDto);
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), "");
        }
        return ApiResponse.onSuccess("유세인트 과목 생성(초기화)에 성공했습니다", "");
    }

    @Operation(summary = "유세인트 과목 업데이트")
    @PutMapping("/subject/updateAll")
    public ApiResponse createSubject(@RequestBody UpdateSubjectRequestDto requestDto) {
        try {
            subjectService.updateSubject(requestDto);
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), "");
        }
        return ApiResponse.onSuccess("유세인트 과목 업데이트에 성공했습니다", "");
    }
}
