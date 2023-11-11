package all.clear.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SemesterGrade {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "semester_grade_id")
    private Long semesterGradeId;

    @ManyToOne
    @JoinColumn(name = "grade_id") // 추가
    private Grade grade;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "total_credit")
    private Long totalCredit; //이수 학점

    @Column(name = "average_grade")
    private double averageGrade; //평균학점

    @Column(name = "major_average_grade")
    private double MajorAverageGrade; //전공 평균학점

    @OneToMany(mappedBy = "semesterGrade")
    @Column(name = "semester_subject_list")
    private List<SemesterSubject> semesterSubjectList = new ArrayList<>();
}

