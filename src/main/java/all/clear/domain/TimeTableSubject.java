package all.clear.domain;

import all.clear.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TimeTableSubject {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "timetable_subject_id")
    private Long timetableSubjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;


    //==생성 메서드==//
    public static TimeTableSubject createTimeTableSubject(Subject subject){
        TimeTableSubject timeTableSubject = new TimeTableSubject();
        timeTableSubject.setSubject(subject);
        return timeTableSubject;
    }
}
