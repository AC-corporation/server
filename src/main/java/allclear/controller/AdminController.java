package allclear.controller;

import allclear.dto.requestDto.subject.InitSubjectRequestDto;
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


    @Operation(summary = "유세인트 전체 과목 생성(업데이트)", description = "학년도, 학기, 유세인트 Id, Pwd 필요")
    @PutMapping("/subject/initAll")
    public ApiResponse initSubject(@RequestBody InitSubjectRequestDto requestDto) {
        subjectService.initSubject(requestDto);
        return ApiResponse.onSuccess("유세인트 과목 생성(업데이트)에 성공했습니다");
    }
}
