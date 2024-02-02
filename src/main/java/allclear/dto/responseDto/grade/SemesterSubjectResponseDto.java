package allclear.dto.responseDto.grade;


import allclear.domain.grade.SemesterSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class SemesterSubjectResponseDto {
    private String semesterSubjectName;
    private String semesterSubjectScore;

    public static SemesterSubjectResponseDto from(SemesterSubject semesterSubject){
        return SemesterSubjectResponseDto.builder()
                .semesterSubjectName(semesterSubject.getSemesterSubjectName())
                .semesterSubjectScore(semesterSubject.getSemesterSubjectScore())
                .build();
    }

}
