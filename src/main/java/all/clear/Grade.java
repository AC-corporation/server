package all.clear;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Grade {
    @Id @GeneratedValue
    @Column(name = "GRADE_ID")
    private Long id;

    @OneToOne(mappedBy = "grade") // 추가
    private User user;

    @OneToMany(mappedBy = "grade")
    @Column(name = "GRADE_LIST")
    private List<SemesterGrade> gradeList;

    @Column(name = "TOTALCREDIT")
    private Long totalCredit; //총 이수 학점

    @Column(name = "AVERAGE_GRADE")
    private double averageGrade; //평균학점

    @Column(name = "AVERAGE_MAJORGRADE")
    private double averageMajorGrade; //전공 평균 학점
}


