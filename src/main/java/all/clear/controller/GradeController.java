package all.clear.controller;

import all.clear.dto.responseDto.GradeResponseDto;
import all.clear.dto.responseDto.SemesterGradeResponseDto;
import all.clear.global.response.ApiResponse;
import all.clear.service.GradeService;
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

    //@Operation(summary = "전체 성적 조회", description = "전체 성적 조회")
    //@GetMapping("") // string 수정 필요
    public ApiResponse<GradeResponseDto> get(Long userId){ //인자 수정 필요
        return ApiResponse.onSuccess(gradeService.getGrade(userId));
    }

    //학기별 성적 조회
//    @GetMapping() //수정 필요
//    public ApiResponse<SemesterGradeResponseDto> getSemesterGrade(@PathVariable Long semesterGradeId){
//        return ApiResponse.onSuccess();
//        //SemesterGrade id로 찾아 반환
//        //semesterGrade 관련 repo, Service 필요
//    }
}
