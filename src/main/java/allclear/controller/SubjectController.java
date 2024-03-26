package allclear.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import allclear.dto.requestDto.subject.SubjectSearchRequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Operation(summary = "과목 단건 조회")
    @GetMapping("/{subjectId}")
    public ApiResponse getSubject(@PathVariable Long subjectId) {
        return ApiResponse.onSuccess("과목 단건 조회에 성공했습니다", subjectService.getSubject(subjectId));
    }

    @Operation(summary = "과목 전체 조회")
    @GetMapping("/findAll")
    public ApiResponse getSubjectList(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ApiResponse.onSuccess("과목 전체 조회에 성공했습니다", subjectService.getSubjectList(page));
    }

    @Operation(summary = "과목 조건 검색 조회") // 페이징
    @PostMapping("/search")
    public ApiResponse getSubjectSearch(
            @Valid @RequestBody SubjectSearchRequestDto request,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ApiResponse.onSuccess(
                "과목 조건 검색에 성공했습니다", subjectService.getSubjectSearch(request, page));
    }
}
