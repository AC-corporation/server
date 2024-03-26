package allclear.dto.responseDto.grade;

import java.util.List;

import allclear.domain.grade.Grade;
import lombok.*;

@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradeResponseDto {
    private Double totalCredit; // 전체 이수 학점
    private String averageGrade; // 전체 평균 학점
    private List<SemesterGradeResponseDto> semesterGradeDtoList; // 학기별 성적 리스트

    public static GradeResponseDto from(Grade grade) {
        List<SemesterGradeResponseDto> semesterGradeDtoList =
                grade.getSemesterGradeList().stream().map(SemesterGradeResponseDto::from).toList();

        return GradeResponseDto.builder()
                .totalCredit(grade.getTotalCredit())
                .averageGrade(grade.getAverageGrade())
                .semesterGradeDtoList(semesterGradeDtoList)
                .build();
    }
}
