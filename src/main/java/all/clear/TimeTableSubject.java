package all.clear;

import jakarta.persistence.*;

@Entity
public class TimeTableSubject {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "TIMETABLESUJECT_ID")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "TIMETABLE_ID")
    private TimeTable timeTable;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;

}
