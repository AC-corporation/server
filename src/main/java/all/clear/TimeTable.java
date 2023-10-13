package all.clear;

import jakarta.persistence.*;

import javax.security.auth.Subject;
import java.util.List;

@Entity
public class TimeTable {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "TIMETABLE_ID")
    private Long id;

    @Column(name = "TABLE_NAME")
    private String tableName; //시간표 이름

    @Column(name = "YEAR")
    private int year; //학년
    @Column(name = "SEMESTER")
    private int semester; //학기

   // @OneToMany(mappedBy = "timeTable") // Subject 엔티티에 timeTable 필드 추가
    @Column(name = "CLASS_LIST")
    private List<Subject> classList;

    /*
    @ManyToOne // 추가 연관 관계 매핑
    @JoinColumn(name = "USER_ID")
    private User user;
     */

    @OneToMany(mappedBy = "timeTable")
    private List<TimeTableSubject> timeTableSubjectList;

}


