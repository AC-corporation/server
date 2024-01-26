package allclear.domain.timetableGenerator;

import allclear.domain.member.Member;
import allclear.domain.timetable.Timetable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TimetableGenerator {
    @Id
    @Column(name = "timetable_generator_id")
    private Long id;

    @Column(name = "table_year")
    private Integer tableYear; //학년도
    private Integer semester; //학기
    @Column(name = "prev_subject_id_list")
    private List<Long> prevSubjectIdList = new ArrayList<>(); //이전에 수강한 과목
    @Column(name = "curriculum_subject_id_list")
    private List<Long> curriculumSubjectIdList = new ArrayList<>(); //커리큘럼 과목

    @OneToMany(mappedBy = "timetableGenerator", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_subject_list")
    private List<TimetableGeneratorSubject> timetableGeneratorSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "timetableGenerator", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_timetable_list")
    private List<TimetableGeneratorTimetable> timetableGeneratorTimetableList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.setTimetableGenerator(this);
    }

    public void addTimetableGeneratorSubject(TimetableGeneratorSubject timetableGeneratorSubject) {
        timetableGeneratorSubjects.add(timetableGeneratorSubject);
        timetableGeneratorSubject.setTimetableGenerator(this);
    }
}
