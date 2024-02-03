package allclear.controller;

import allclear.dto.responseDto.grade.GradeResponseDto;
import allclear.dto.responseDto.grade.SemesterGradeResponseDto;
import allclear.global.response.ApiResponse;
import allclear.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;

    @Operation(summary = "전체 성적 조회", description = "전체 성적 조회")
    @GetMapping("/getGrade/{userId}")
    public ApiResponse<GradeResponseDto> getGrade(@PathVariable Long userId){
        return ApiResponse.onSuccess("전체 성적 조회에 성공했습니다",gradeService.getGrade(userId));
    }

    //학기별 성적 조회
    @Operation(summary = "학기별 성적 조회", description = "학기별 성적 조회")
    @GetMapping("/getSemesterGrade/{semesterGradeId}")
    public ApiResponse<SemesterGradeResponseDto> getSemesterGrade(@PathVariable Long semesterGradeId){
        return ApiResponse.onSuccess("학기별 성적 조회에 성공했습니다",gradeService.getSemesterGrade(semesterGradeId));
    }
}
