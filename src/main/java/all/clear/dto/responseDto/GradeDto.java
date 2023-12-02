package all.clear.dto.responseDto;

import all.clear.domain.grade.Grade;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GradeDto {
    private Long totalCredit; // 전체 이수 학점
    private String averageGrade; // 전체 평균 학점
    private List<SemesterGradeDto> semesterGradeDtoList; // 학기별 성적 리스트
    public GradeDto(Grade grade) {
        this.totalCredit = grade.getTotalCredit();
        this.averageGrade = grade.getAverageGrade();
        List<SemesterGradeDto> semesterGradeDtoList = grade.getSemesterGradeList()
                .stream()
                .map(SemesterGradeDto::new)
                .collect(Collectors.toList());
        this.semesterGradeDtoList = semesterGradeDtoList;
    }
}
