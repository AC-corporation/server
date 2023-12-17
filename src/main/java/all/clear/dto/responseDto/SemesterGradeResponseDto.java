package all.clear.dto.responseDto;

import all.clear.domain.grade.SemesterGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
public class SemesterGradeResponseDto {
    private String semesterAverageGrade; // 평균 학점
    private List<SemesterSubjectResponseDto> semesterSubjectDtoList; // 학기 과목 리스트

    public SemesterGradeResponseDto(SemesterGrade semesterGrade) {
        this.semesterAverageGrade = semesterGrade.getSemesterAverageGrade();
        List<SemesterSubjectResponseDto> semesterSubjectDtoList = semesterGrade.getSemesterSubjectList()
                .stream()
                .map(SemesterSubjectResponseDto::new)
                .collect(Collectors.toList());
        this.semesterSubjectDtoList = semesterSubjectDtoList;
    }

}
