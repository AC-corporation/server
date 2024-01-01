package allclear.domain.timetable;

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
    private String professor;
    @Column(name = "class_info_list")
    private List<String> classInfoList = new ArrayList<>(); //강의 시간 및 강의실

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
        timeTableSubject.setProfessor(subject.getProfessor());
        timeTableSubject.setClassInfoList(subject.getClassInfoList());
        return timeTableSubject;
    }

    /**
     * 유저가 정의한 과목
     */
    public static TimetableSubject createCustomTimeTableSubject(String subjectName, String professor,
                                                                List<String> classInfoList){
        TimetableSubject timeTableSubject = new TimetableSubject();
        timeTableSubject.setSubject(null);
        timeTableSubject.setSubjectName(subjectName);
        timeTableSubject.setProfessor(professor);
        timeTableSubject.setClassInfoList(classInfoList);
        return timeTableSubject;
    }
}
