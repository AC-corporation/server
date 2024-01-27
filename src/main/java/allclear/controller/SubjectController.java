package allclear.controller;

import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
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

    @Operation(summary = "과목 전체 조회")
    @GetMapping("/findAll")
    public ApiResponse getSubjectList(@RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ApiResponse.onSuccess("과목 전체 조회에 성공했습니다", subjectService.getSubjectList(page));
    }

    @Operation(summary = "과목 조건 검색 조회")
    @GetMapping("/search")
    public ApiResponse getSubjectSearch(@RequestBody SubjectListRequestDto requestDto,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) int page
                                        ) {
        return ApiResponse.onSuccess("과목 조건 검색에 성공했습니다", subjectService.getSubjectSearch(requestDto, page));
    }

    // 과목 정보 업데이트
    @Operation(summary = "유세인트 과목 업데이트", description = "학년도, 학기, 유세인트 Id, Pwd 필요")
    @PutMapping("/update")
    public ApiResponse update(@RequestBody UpdateSubjectRequestDto updateSubjectRequestDto){
        subjectService.updateSubject(updateSubjectRequestDto);
        return ApiResponse.onSuccess("정보 업데이트에 성공했습니다");
    }
}
