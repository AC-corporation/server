package all.clear.dto.responseDto;

import lombok.Data;

import java.util.List;

@Data
public class GradeDto {
    private Long totalCredit; // 전체 이수 학점
    private String averageGrade; // 전체 평균 학점
    private List<SemesterGradeDto> semesterGradeDtoList; // 학기별 성적 리스트
    public GradeDto(Long totalCredit, String averageGrade, List<SemesterGradeDto> semesterGradeDtoList) {
        this.totalCredit = totalCredit;
        this.averageGrade = averageGrade;
        this.semesterGradeDtoList = semesterGradeDtoList;
    }
}
