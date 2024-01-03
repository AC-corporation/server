package allclear.domain.subject;


import allclear.domain.timetable.TimetableSubject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ClassInfo {
    @Id @GeneratedValue
    @Column(name = "class_info_id")
    private Long id;
    private String professor; //교수명
    @Column(name = "class_time")
    private String classTime; //강의 시간
    @Column(name = "class_date")
    private String classDate; //강의 요일
    @Column(name = "class_room")
    private String classRoom; //강의 장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_subject_id")
    private TimetableSubject timetableSubject;

    static public ClassInfo createClassInfo(String professor, String classTime, String classDate, String classRoom) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setProfessor(professor);
        classInfo.setClassTime(classTime);
        classInfo.setClassDate(classDate);
        classInfo.setClassRoom(classRoom);
        return classInfo;
    }

}
