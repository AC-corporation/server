package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class SemesterSubject {
    @Id @GeneratedValue
    @Column(name = "semester_subject_id")
    private Long semesterSubjectId;

    @Column(name = "semester_subject_name")
    private String semesterSubjectName;

    @Column(name = "semester_subject_score")
    private double semesterSubjectScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_grade_id")
    private SemesterGrade semesterGrade;

    //==초기화 메서드==//
    public void setSemesterGrade(SemesterGrade semesterGrade) {
        this.semesterGrade = semesterGrade;
    }

    //==생성 메서드==//
    public static SemesterSubject createSemesterSubject(String semesterSubjectName, double semesterSubjectScore) {
        SemesterSubject semesterSubject = new SemesterSubject();
        semesterSubject.semesterSubjectName = semesterSubjectName;
        semesterSubject.semesterSubjectScore = semesterSubjectScore;
        return semesterSubject;
    }
}
