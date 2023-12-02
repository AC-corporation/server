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
public class SemesterGradeDto {
    private String semesterAverageGrade; // 평균 학점
    private List<SemesterSubjectDto> semesterSubjectDtoList; // 학기 과목 리스트

    public SemesterGradeDto(SemesterGrade semesterGrade) {
        this.semesterAverageGrade = semesterGrade.getSemesterAverageGrade();
        List<SemesterSubjectDto> semesterSubjectDtoList = semesterGrade.getSemesterSubjectList()
                .stream()
                .map(SemesterSubjectDto::new)
                .collect(Collectors.toList());
        this.semesterSubjectDtoList = semesterSubjectDtoList;
    }

}
