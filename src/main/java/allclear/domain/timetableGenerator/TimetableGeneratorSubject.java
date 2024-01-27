package allclear.domain.timetableGenerator;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TimetableGeneratorSubject {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_subject_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject; //null 인 경우 유저가 정의한 과목
    @Column(name = "subject_name")
    private String subjectName; //과목 이름

    @Column(name = "is_selected")
    private boolean isSelected = false;
    @OneToMany(mappedBy = "timetableGeneratorSubject", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_class_info_list")
    private List<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_id")
    private TimetableGenerator timetableGenerator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_timetable_id")
    private TimetableGeneratorTimetable timetableGeneratorTimetable;

    //==연관관계 메서드==//
    public void addTimetableGeneratorClassInfo(TimetableGeneratorClassInfo classInfo) {
        this.timetableGeneratorClassInfoList.add(classInfo);
        classInfo.setTimetableGeneratorSubject(this);
    }

    //==생성 메서드==//

    /**
     * 실제 과목
     */
    public static TimetableGeneratorSubject createActualTimetableGeneratorSubject(Subject subject) {
        TimetableGeneratorSubject timetableGeneratorSubject = new TimetableGeneratorSubject();
        timetableGeneratorSubject.setSubject(subject);
        timetableGeneratorSubject.setSubjectName(subject.getSubjectName());
        for (ClassInfo classInfo : subject.getClassInfoList()) {
            timetableGeneratorSubject.addTimetableGeneratorClassInfo(
                    TimetableGeneratorClassInfo.createClassInfo(
                            classInfo.getProfessor(),
                            classInfo.getClassDay(),
                            classInfo.getStartTime(),
                            classInfo.getEndTime(),
                            classInfo.getClassRoom()
                    )
            );
        }
        return timetableGeneratorSubject;
    }

    /**
     * 유저가 정의한 과목
     */
    public static TimetableGeneratorSubject createCustomTimetableGeneratorSubject(String subjectName,
                                                                List<TimetableGeneratorClassInfo> classInfoList) {
        TimetableGeneratorSubject timetableGeneratorSubject = new TimetableGeneratorSubject();
        timetableGeneratorSubject.setSubject(null);
        timetableGeneratorSubject.setSubjectName(subjectName);
        for (TimetableGeneratorClassInfo classInfo : classInfoList) {
            timetableGeneratorSubject.addTimetableGeneratorClassInfo(classInfo);
        }
        return timetableGeneratorSubject;
    }
}
