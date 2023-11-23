package all.clear.dto.responseDto;


import lombok.Data;

@Data
public class SemesterSubjectDto {
    private String semesterSubjectName;
    private String semesterSubjectScore;

    public SemesterSubjectDto(String semesterSubjectName, String semesterSubjectScore) {
        this.semesterSubjectName = semesterSubjectName;
        this.semesterSubjectScore = semesterSubjectScore;
    }

}
