package allclear.controller;

import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags="api 정보 제공하는 컨트롤러")
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

    @Operation(summary = "과목 조건 검색 조회")
    @PostMapping("/list")
    public ApiResponse getSubjectList(@RequestBody SubjectListRequestDto requestDto) {
        return ApiResponse.onSuccess("과목 조건검색에 성공했습니다", subjectService.getSubjectList(requestDto));
    }
}
