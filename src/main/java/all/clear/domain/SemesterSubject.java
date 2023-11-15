package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SemesterSubject {
    @Id @GeneratedValue
    @Column(name = "semester_subject_id")
    private Long semesterSubjectId;

    @Column(name = "semester_subject_name")
    private String semesterSubjectName;

    @Column(name = "semester_subject_score")
    private Double semesterSubjectScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_grade_id")
    private SemesterGrade semesterGrade;
}
