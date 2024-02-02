package allclear.domain.grade;

import allclear.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue
    @Column(name = "grade_id")
    private Long gradeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "semester_grade_list")
    private List<SemesterGrade> semesterGradeList;

    @Column(name = "total_credit")
    private Double totalCredit; //총 이수 학점

    @Column(name = "average_grade")
    private String averageGrade;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.setGrade(this);
    }

    public void addSemesterGrade(SemesterGrade semesterGrade) {
        semesterGradeList.add(semesterGrade);
        semesterGrade.setGrade(this);
    }
}
