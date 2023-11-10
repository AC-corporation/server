package all.clear.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Grade {
    @Id @GeneratedValue
    @Column(name = "grade_id")
    private Long gradeId;

    @OneToOne(mappedBy = "grade", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "grade")
    @Column(name = "semester_grade_list")
    private List<SemesterGrade> semesterGradeList;

    @Column(name = "total_credit")
    private Long totalCredit; //총 이수 학점

    @Column(name = "average_grade")
    private double averageGrade; //평균학점

    @Column(name = "major_average_grade")
    private double MajorAverageGrade; //전공 평균 학점
}

