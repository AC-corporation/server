package allclear.domain.timetableGenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter
public class TimetableGeneratorClassInfo {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_class_info_id")
    private Long id;
    private String professor; //교수명
    @Column(name = "class_date")
    private String classDay; //강의 요일
    @Column(name = "start_time")
    private LocalTime startTime; //강의 시작 시간
    @Column(name = "end_time")
    private LocalTime endTime; //강의 종료 시간
    @Column(name = "class_room")
    private String classRoom; //강의 장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_subject_id")
    private TimetableGeneratorSubject timetableGeneratorSubject;


    //==생성 메서드==//
    static public TimetableGeneratorClassInfo createClassInfo(String professor, String classDay,
                                                     LocalTime startTime, LocalTime endTime, String classRoom) {
        TimetableGeneratorClassInfo classInfo = new TimetableGeneratorClassInfo();
        classInfo.setProfessor(professor);
        classInfo.setClassDay(classDay);
        classInfo.setStartTime(startTime);
        classInfo.setEndTime(endTime);
        classInfo.setClassRoom(classRoom);
        return classInfo;
    }
}
