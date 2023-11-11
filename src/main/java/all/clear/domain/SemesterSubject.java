package all.clear.domain;

import jakarta.persistence.*;

@Entity
public class SemesterSubject {
    @Id @GeneratedValue
    @Column(name = "semester_subject_id")
    private Long semesterSubjectId;

    @Column(name = "semester_subject_name")
    private String semesterSubjectName;

    @Column(name = "semester_subject_score")
    private Double semesterSubjectScore;

    @ManyToOne
    @JoinColumn(name = "semester_grade_id")
    private SemesterGrade semesterGrade;
}
