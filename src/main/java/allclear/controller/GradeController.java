package allclear.controller;

import org.springframework.web.bind.annotation.*;

import allclear.dto.responseDto.grade.GradeResponseDto;
import allclear.dto.responseDto.grade.SemesterGradeResponseDto;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.jwt.JwtTokenProvider;
import allclear.global.response.ApiResponse;
import allclear.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "전체 성적 조회", description = "전체 성적 조회")
    @GetMapping("/getGrade/{userId}")
    public ApiResponse<GradeResponseDto> getGrade(
            @PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {
        if (!jwtTokenProvider.compareMember(authorizationHeader, userId))
            return ApiResponse.onFailure(GlobalErrorCode._UNAUTHORIZED, null);
        return ApiResponse.onSuccess("전체 성적 조회에 성공했습니다", gradeService.getGrade(userId));
    }

    // 학기별 성적 조회
    @Operation(summary = "학기별 성적 조회", description = "학기별 성적 조회")
    @GetMapping("/getSemesterGrade/{semesterGradeId}")
    public ApiResponse<SemesterGradeResponseDto> getSemesterGrade(
            @PathVariable Long semesterGradeId) {
        return ApiResponse.onSuccess(
                "학기별 성적 조회에 성공했습니다", gradeService.getSemesterGrade(semesterGradeId));
    }
}
