package all.clear.dto.responseDto;

import all.clear.domain.grade.SemesterSubject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SemesterGradeDto {
    private String semesterAverageGrade; // 평균 학점
    private List<SemesterSubjectDto> semesterSubjectDtoList; // 학기 과목 리스트

    public SemesterGradeDto(String semesterAverageGrade, List<SemesterSubjectDto> semesterSubjectDtoList) {
        this.semesterAverageGrade = semesterAverageGrade;
        this.semesterSubjectDtoList = semesterSubjectDtoList;
    }

}
