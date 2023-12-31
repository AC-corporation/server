package allclear.domain.timetable;

import allclear.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Timetable {
    @Id
    @GeneratedValue
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "table_name")
    private String tableName; //시간표 이름
    private int year; //학년도
    private int semester; //학기

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    @Column(name = "timetable_subject_list")
    private List<TimetableSubject> timetableSubjectList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getTimetableList().add(this);
    }

    public void addTimetableSubject(TimetableSubject timetableSubject) {
        timetableSubjectList.add(timetableSubject);
        timetableSubject.setTimeTable(this);
    }


    //==생성 메서드==//
    public static Timetable createTimetable(Member member, String tableName, int year, int semester,
                                            TimetableSubject... timetableSubjects) {
        Timetable timeTable = new Timetable();
        timeTable.setMember(member);
        timeTable.setTableName(tableName);
        timeTable.setYear(year);
        timeTable.setSemester(semester);
        for (TimetableSubject timeTableSubject : timetableSubjects) {
            timeTable.addTimetableSubject(timeTableSubject);
        }
        return timeTable;
    }
}