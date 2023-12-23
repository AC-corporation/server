package allclear.dto.responseDto.grade;

import allclear.domain.grade.Grade;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GradeResponseDto {
    private Long totalCredit; // 전체 이수 학점
    private String averageGrade; // 전체 평균 학점
    private List<SemesterGradeResponseDto> semesterGradeDtoList; // 학기별 성적 리스트
    public GradeResponseDto(Grade grade) {
        this.totalCredit = grade.getTotalCredit();
        this.averageGrade = grade.getAverageGrade();
        List<SemesterGradeResponseDto> semesterGradeDtoList = grade.getSemesterGradeList()
                .stream()
                .map(SemesterGradeResponseDto::new)
                .collect(Collectors.toList());
        this.semesterGradeDtoList = semesterGradeDtoList;
    }
}
