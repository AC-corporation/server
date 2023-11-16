package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.Subject;
import java.util.List;

@Entity
@Getter @Setter
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



    //==연관관계 메서드==//
    public void setUser(User user){
        this.user = user;
        user.getTimeTableList().add(this);
    }

    public void addTimeTableSubject(TimeTableSubject timeTableSubject){
        timeTableSubjectList.add(timeTableSubject);
        timeTableSubject.setTimeTable(this);
    }


    //==생성 메서드==//
    public static TimeTable createTimeTable(User user, String tableName, int year, int semester, TimeTableSubject... timeTableSubjects){
        TimeTable timeTable  = new TimeTable();
        timeTable.setUser(user);
        timeTable.setTableName(tableName);
        timeTable.setYear(year);
        timeTable.setSemester(semester);
        for(TimeTableSubject timeTableSubject : timeTableSubjects){
            timeTable.addTimeTableSubject(timeTableSubject);
        }
        return timeTable;
    }

    //==삭제 메서드==//
    public void remove(){
        //TimeTableSubject 접근
        for(TimeTableSubject timeTableSubject : timeTableSubjectList){
            timeTableSubject.remove();
        }
    }
}


