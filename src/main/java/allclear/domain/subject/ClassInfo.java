package allclear.domain.subject;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter
public class ClassInfo {
    @Id @GeneratedValue
    @Column(name = "class_info_id")
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
    @JoinColumn(name = "subject_id")
    private Subject subject;


    //==연관관계 메서드==//
    public void setSubject(Subject subject) {
        subject.getClassInfoList().add(this);
        this.subject = subject;
    }


    //==생성 메서드==//
    static public ClassInfo createClassInfo(String professor, String classDay,
                                            LocalTime startTime, LocalTime endTime, String classRoom) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setProfessor(professor);
        classInfo.setClassDay(classDay);
        classInfo.setStartTime(startTime);
        classInfo.setEndTime(endTime);
        classInfo.setClassRoom(classRoom);
        return classInfo;
    }
}
