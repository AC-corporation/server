package allclear.dto.responseDto.grade;


import allclear.domain.grade.SemesterSubject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class SemesterSubjectResponseDto {
    private String semesterSubjectName;
    private String semesterSubjectScore;

    public SemesterSubjectResponseDto(SemesterSubject semesterSubject) {
        this.semesterSubjectName = semesterSubject.getSemesterSubjectName();
        this.semesterSubjectScore = semesterSubject.getSemesterSubjectScore();
    }

}
