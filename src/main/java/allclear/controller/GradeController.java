package allclear.controller;

import allclear.dto.responseDto.grade.GradeResponseDto;
import allclear.global.response.ApiResponse;
import allclear.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;

    @Operation(summary = "전체 성적 조회", description = "전체 성적 조회")
    //@GetMapping("") // string 수정 필요
    public ApiResponse<GradeResponseDto> get(Long userId){ //인자 수정 필요
        return ApiResponse.onSuccess("전체 성적 조회에 성공했습니다",gradeService.getGrade(userId));
    }

    //학기별 성적 조회
//    @GetMapping() //수정 필요
//    public ApiResponse<SemesterGradeResponseDto> getSemesterGrade(@PathVariable Long semesterGradeId){
//        return ApiResponse.onSuccess();
//        //SemesterGrade id로 찾아 반환
//        //semesterGrade 관련 repo, Service 필요
//    }
}
