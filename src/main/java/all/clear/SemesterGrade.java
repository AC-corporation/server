package all.clear;

import jakarta.persistence.*;
import lombok.extern.java.Log;

import java.util.List;

@Entity
public class SemesterGrade {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "SEMESTER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRADE_ID") // 추가
    private Grade grade;

    @Column(name = "TOTALCREDIT")
    private Long totalCredit; //이수 학점

    @Column(name = "AVERAGE_GRADE")
    private double averageGrade; //평균학점

    @Column(name = "AVERAGE_MAJORGRADE")
    private double averageMajorGrade; //전공 평균학점

    @Column(name = "CLASS_LIST")
    private List<String> classList; //수강 과목 이름 리스트

    @Column(name = "SCORE_LIST")
    private List<Long> scoreList; //수강과목 점수
}

