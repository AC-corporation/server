package allclear.controller;

import allclear.dto.requestDto.subject.SubjectSearchRequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse getSubjectList(@RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ApiResponse.onSuccess("과목 전체 조회에 성공했습니다", subjectService.getSubjectList(page));
    }

    @Operation(summary = "과목 조건 검색 조회")
    @GetMapping("/search")
    public ApiResponse getSubjectSearch(@RequestBody SubjectSearchRequestDto requestDto,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) int page
                                        ) {
        return ApiResponse.onSuccess("과목 조건 검색에 성공했습니다", subjectService.getSubjectSearch(requestDto, page));
    }

}
