package allclear.domain.timetableGenerator;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import allclear.domain.member.Member;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimetableGenerator {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_id")
    private Long id;

    @Column(name = "table_year")
    private Integer tableYear; // 학년도

    private Integer semester; // 학기

    @Column(name = "prev_subject_id_list")
    private List<Long> prevSubjectIdList = new ArrayList<>(); // 이전에 수강한 과목

    @Column(name = "curriculum_subject_id_list")
    private List<Long> curriculumSubjectIdList = new ArrayList<>(); // 커리큘럼 과목

    @OneToMany(mappedBy = "timetableGenerator", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_subject_list")
    private List<TimetableGeneratorSubject> timetableGeneratorSubjectList = new ArrayList<>();

    @OneToMany(mappedBy = "timetableGenerator", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_timetable_list")
    private List<TimetableGeneratorTimetable> timetableGeneratorTimetableList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // ==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.setTimetableGenerator(this);
    }

    public void addTimetableGeneratorSubject(TimetableGeneratorSubject timetableGeneratorSubject) {
        timetableGeneratorSubjectList.add(timetableGeneratorSubject);
        timetableGeneratorSubject.setTimetableGenerator(this);
    }

    public void addTimetableGeneratorTimetable(
            TimetableGeneratorTimetable timetableGeneratorTimetable) {
        timetableGeneratorTimetableList.add(timetableGeneratorTimetable);
        timetableGeneratorTimetable.setTimetableGenerator(this);
    }

    public void initGenerator(int tableYear, int semester) {
        this.tableYear = tableYear;
        this.semester = semester;
        this.timetableGeneratorTimetableList = new ArrayList<>();
        this.timetableGeneratorSubjectList = new ArrayList<>();
    }
}
