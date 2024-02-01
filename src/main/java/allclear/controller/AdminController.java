package allclear.controller;

import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final SubjectService subjectService;

    @Operation(summary = "유세인트 전체 과목 생성(초기화)")
    @PostMapping("/subject/initAll")
    public ApiResponse createSubject(@RequestBody CreateSubjectRequestDto requestDto) {
        subjectService.createSubject(requestDto);
        return ApiResponse.onSuccess("유세인트 과목 생성(초기화)에 성공했습니다");
    }

    @Operation(summary = "유세인트 과목 업데이트", description = "학년도, 학기, 유세인트 Id, Pwd 필요")
    @PutMapping("/subject/updateAll")
    public ApiResponse updateSubject(@RequestBody UpdateSubjectRequestDto requestDto) {
        subjectService.updateSubject(requestDto);
        return ApiResponse.onSuccess("유세인트 과목 업데이트에 성공했습니다");
    }
}
