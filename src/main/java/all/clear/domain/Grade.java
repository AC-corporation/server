package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Grade {
    @Id @GeneratedValue
    @Column(name = "grade_id")
    private Long gradeId;

    @OneToOne(mappedBy = "grade", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "grade")
    @Column(name = "semester_grade_list")
    private List<SemesterGrade> semesterGradeList = new ArrayList<>();

    @Column(name = "total_credit")
    private Long totalCredit; //총 이수 학점

//    @Column(name = "major_average_grade")
//    private double MajorAverageGrade; //전공 평균 학점

    public void setTotalCredit(Long totalCredit) {
        this.totalCredit = totalCredit;
    }

    //==연관관계 메서드==//
    public void setUser(User user){
        this.user = user;
        user.setGrade(this);
    }

    public void addSemesterGrade(SemesterGrade semesterGrade){
        semesterGradeList.add(semesterGrade);
        semesterGrade.setGrade(this);
    }

    //==조회 로직==//
    public double getAverageGrade(){
        double averageGrade = 0;
        for (SemesterGrade semesterGrade : semesterGradeList) {
            averageGrade += semesterGrade.getAverageGrade();
        }
        averageGrade /= semesterGradeList.stream().count();
        return averageGrade;
    }
}


