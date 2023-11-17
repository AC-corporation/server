package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class SemesterGrade {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "semester_grade_id")
    private Long semesterGradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id") // 추가
    private Grade grade;

    @JoinColumn(name = "semester_average_grade")
    private String semesterAverageGrade;

    @OneToMany(mappedBy = "semesterGrade")
    @Column(name = "semester_subject_list")
    private List<SemesterSubject> semesterSubjectList = new ArrayList<>();

    //==초기화 메서드==//
    public void setSemesterAverageGrade(String semesterAverageGrade) {
        this.semesterAverageGrade = semesterAverageGrade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    //==연관관계 메서드==//
    public void addSemesterSubject(SemesterSubject semesterSubject){
        semesterSubjectList.add(semesterSubject);
        semesterSubject.setSemesterGrade(this);
    }

    //==생성 메서드==//
    public static SemesterGrade createSemesterGrade(Grade grade, String semesterAverageGrade, SemesterSubject... semesterSubjects){
        SemesterGrade semesterGrade = new SemesterGrade();
        semesterGrade.setGrade(grade);
        semesterGrade.setSemesterAverageGrade(semesterAverageGrade);
        for (SemesterSubject semesterSubject : semesterSubjects) {
            semesterSubject.setSemesterGrade(semesterGrade);
            semesterGrade.addSemesterSubject(semesterSubject);
        }
        return semesterGrade;
    }
}

