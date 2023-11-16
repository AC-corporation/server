package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class SemesterGrade {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "semester_grade_id")
    private Long semesterGradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id") // 추가
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
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


    //==연관관계 메서드==//
    public void addSemesterSubject(SemesterSubject semesterSubject){
        semesterSubjectList.add(semesterSubject);
        semesterSubject.setSemesterGrade(this);
    }

}

