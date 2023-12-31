package allclear.domain.timetable;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TimetableSubject {
    @Id @GeneratedValue
    @Column(name = "timetable_subject_id")
    private Long timetableSubjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject; //null 인 경우 유저가 정의한 과목
    @Column(name = "subject_name")
    private String subjectName; //과목 이름
    @OneToMany(mappedBy = "timetableSubject")
    @Column(name = "class_info_list")
    private List<ClassInfo> classInfoList = new ArrayList<>(); //강의 시간, 요일, 강의실, 교수명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;


    //==생성 메서드==//

    /**
     * 실제 과목
     */
    public static TimetableSubject createActualTimeTableSubject(Subject subject){
        TimetableSubject timeTableSubject = new TimetableSubject();
        timeTableSubject.setSubject(subject);
        timeTableSubject.setSubjectName(subject.getSubjectName());
        timeTableSubject.setClassInfoList(subject.getClassInfoList());
        return timeTableSubject;
    }

    /**
     * 유저가 정의한 과목
     */
    public static TimetableSubject createCustomTimeTableSubject(String subjectName, List<ClassInfo> classInfoList){
        TimetableSubject timeTableSubject = new TimetableSubject();
        timeTableSubject.setSubject(null);
        timeTableSubject.setSubjectName(subjectName);
        timeTableSubject.setClassInfoList(classInfoList);
        return timeTableSubject;
    }
}
