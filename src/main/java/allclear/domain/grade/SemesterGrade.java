package allclear.domain.grade;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterGrade {
    @Id
    @GeneratedValue
    @Column(name = "semester_grade_id")
    private Long semesterGradeId;

    @Column(name = "semester__title")
    private String semesterTitle; // 학기명 ex) 2024학년도1학기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "semester_average_grade")
    private String semesterAverageGrade;

    @OneToMany(mappedBy = "semesterGrade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemesterSubject> semesterSubjectList;

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    // ==연관관계 메서드==//
    public void addSemesterSubject(SemesterSubject semesterSubject) {

        semesterSubject.setSemesterGrade(this);
        if (semesterSubjectList == null) {
            semesterSubjectList = new ArrayList<>();
            semesterSubjectList.add(semesterSubject);
        } else {
            semesterSubjectList.add(semesterSubject);
        }
    }

    // ==생성 메서드==//
    public static SemesterGrade createSemesterGrade(
            Grade grade,
            String semesterAverageGrade,
            String semesterTitle,
            ArrayList<SemesterSubject> semesterSubjects) {
        SemesterGrade semesterGrade =
                SemesterGrade.builder()
                        .grade(grade)
                        .semesterAverageGrade(semesterAverageGrade)
                        .semesterTitle(semesterTitle)
                        .build();
        for (SemesterSubject semesterSubject : semesterSubjects) {
            semesterGrade.addSemesterSubject(semesterSubject);
        }
        return semesterGrade;
    }
}
