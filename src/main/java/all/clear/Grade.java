package all.clear;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Grade {
    @Id @GeneratedValue
    @Column(name = "GRADE_ID")
    private Long id;

    @Column(name = "GRADE_LIST")
    private List<SemesterGrade> gradeList;

    @Column(name = "TOTALCREDIT")
    private Long totalCredit; //총 이수 학점

    @Column(name = "AVERAGE_GRADE")
    private double averageGrade; //평균학점

    @Column(name = "AVERAGE_MAJORGRADE")
    private double averageMajorGrade; //전공 평균학점
}


