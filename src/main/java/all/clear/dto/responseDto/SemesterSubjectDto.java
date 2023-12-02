package all.clear.dto.responseDto;


import all.clear.domain.grade.SemesterSubject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class SemesterSubjectDto {
    private String semesterSubjectName;
    private String semesterSubjectScore;

    public SemesterSubjectDto(SemesterSubject semesterSubject) {
        this.semesterSubjectName = semesterSubject.getSemesterSubjectName();
        this.semesterSubjectScore = semesterSubject.getSemesterSubjectScore();
    }

}
