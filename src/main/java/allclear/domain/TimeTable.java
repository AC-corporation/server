package allclear.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TimeTable {
    /**
     * 성적 조회, 졸업요건 조회 이후 추후 상의
     **/
    @Id
    @GeneratedValue
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "table_name")
    private String tableName; //시간표 이름
    private int level; //학년도
    private int semester; //학기

    @OneToMany(mappedBy = "timeTable")
    @Column(name = "timetable_subject_list")
    private List<TimeTableSubject> timeTableSubjectList = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY) // 추가 연관 관계 매핑
//    @JoinColumn(name = "member_id")
//    private Member member;


    //==연관관계 메서드==//
//    public void setMember(Member member) {
//        this.member = member;
//        member.getTimeTableList().add(this);
//    }

    public void addTimeTableSubject(TimeTableSubject timeTableSubject) {
        timeTableSubjectList.add(timeTableSubject);
        timeTableSubject.setTimeTable(this);
    }


    //==생성 메서드==//
    public static TimeTable createTimeTable(Member member, String tableName, int level, int semester, TimeTableSubject... timeTableSubjects) {
        TimeTable timeTable = new TimeTable();
       // timeTable.setMember(member);
        timeTable.setTableName(tableName);
        timeTable.setLevel(level);
        timeTable.setSemester(semester);
        for (TimeTableSubject timeTableSubject : timeTableSubjects) {
            timeTable.addTimeTableSubject(timeTableSubject);
        }
        return timeTable;
    }
}


