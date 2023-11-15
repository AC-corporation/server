package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;

import javax.security.auth.Subject;
import java.util.List;

@Entity
@Getter
public class TimeTable {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "table_name")
    private String tableName; //시간표 이름
    private int year; //학년
    private int semester; //학기

   @OneToMany(mappedBy = "timeTable")
    @Column(name = "timetable_subject_list")
    private List<TimeTableSubject> timeTableSubjectList;

    @ManyToOne(fetch = FetchType.LAZY) // 추가 연관 관계 매핑
    @JoinColumn(name = "user_id")
    private User user;
}


