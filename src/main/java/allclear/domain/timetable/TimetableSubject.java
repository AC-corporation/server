package allclear.domain.timetable;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimetableSubject {
    @Id
    @GeneratedValue
    @Column(name = "timetable_subject_id")
    private Long timetableSubjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject; //null 인 경우 유저가 정의한 과목
    @Column(name = "subject_name")
    private String subjectName; //과목 이름
    @OneToMany(mappedBy = "timetableSubject", cascade = CascadeType.ALL)
    @Column(name = "timetable_class_info_list")
    private List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>(); //강의 시간, 요일, 강의실, 교수명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;


    //==연관관계 메서드==//
    public void addTimetableClassInfo(TimetableClassInfo timetableClassInfo) {
        this.getTimetableClassInfoList().add(timetableClassInfo);
        timetableClassInfo.setTimetableSubject(this);
    }

    //==생성 메서드==//

    /**
     * 실제 과목
     */
    public static TimetableSubject createActualTimeTableSubject(Subject subject) {
        TimetableSubject timeTableSubject = new TimetableSubject();
        timeTableSubject.setSubject(subject);
        timeTableSubject.setSubjectName(subject.getSubjectName());
        for (ClassInfo classInfo : subject.getClassInfoList()) {
            timeTableSubject.addTimetableClassInfo(
//                    TimetableClassInfo.createClassInfo(
//                            classInfo.getProfessor(),
//                            classInfo.getClassDay(),
//                            classInfo.getStartTime(),
//                            classInfo.getEndTime(),
//                            classInfo.getClassRoom()
//                    )
                    TimetableClassInfo.builder().professor(classInfo.getProfessor())
                            .classDay(classInfo.getClassDay()).startTime(classInfo.getStartTime())
                            .endTime(classInfo.getEndTime()).classRoom(classInfo.getClassRoom()).build()
            );
        }
        return timeTableSubject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }

    public void setTimetableClassInfoList(List<TimetableClassInfo> timetableClassInfoList){
        this.timetableClassInfoList = timetableClassInfoList;
    }
    /**
     * 유저가 정의한 과목
     */
    public static TimetableSubject createCustomTimeTableSubject(String subjectName,
                                                                List<TimetableClassInfo> timetableClassInfoList) {
        TimetableSubject timeTableSubject = new TimetableSubject();
        timeTableSubject.setSubject(null);
        timeTableSubject.setSubjectName(subjectName);
        for (TimetableClassInfo timetableClassInfo : timetableClassInfoList) {
            timeTableSubject.addTimetableClassInfo(timetableClassInfo);
        }
        return timeTableSubject;
    }

    public void setTimetable(Timetable timetable)
    {
        this.timetable = timetable;
    }
}
