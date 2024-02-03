package allclear.domain.grade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterSubject {
    @Id
    @GeneratedValue
    @Column(name = "semester_subject_id")
    private Long semesterSubjectId;

    @Column(name = "semester_subject_name")
    private String semesterSubjectName;

    @Column(name = "semester_subject_score")
    private String semesterSubjectScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_grade_id")
    private SemesterGrade semesterGrade;


    //==초기화 메서드==//
    public void setSemesterGrade(SemesterGrade semesterGrade) {
        this.semesterGrade = semesterGrade;
    }


    //==생성 메서드==//
    public static SemesterSubject createSemesterSubject(String semesterSubjectName, String semesterSubjectScore) {
        return SemesterSubject.builder()
                .semesterSubjectName(semesterSubjectName)
                .semesterSubjectScore(semesterSubjectScore)
                .build();
    }
}
