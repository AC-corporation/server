package allclear.dto.responseDto.grade;

import allclear.domain.grade.SemesterGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@Builder
public class SemesterGradeResponseDto {
    private Long semesterGradeId;
    private String semesterAverageGrade; // 평균 학점
    private List<SemesterSubjectResponseDto> semesterSubjectDtoList; // 학기 과목 리스트

    public static SemesterGradeResponseDto from(SemesterGrade grade){
        List<SemesterSubjectResponseDto> semesterSubjectDtoList = grade.getSemesterSubjectList()
                .stream()
                .map(SemesterSubjectResponseDto::from)
                .toList();

        return SemesterGradeResponseDto.builder()
                .semesterGradeId(grade.getSemesterGradeId())
                .semesterAverageGrade(grade.getSemesterAverageGrade())
                .semesterSubjectDtoList(semesterSubjectDtoList)
                .build();
    }

}
