package allclear.domain.timetable;

import allclear.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "table_name")
    private String tableName; //시간표 이름
    @Column(name = "table_year")
    private Integer tableYear; //학년도
    private Integer semester; //학기

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
        timetableSubject.setTimetable(this);
    }


    //==생성 메서드==//
//    public static Timetable createTimetable(Member member, String tableName, int tableYear, int semester) {
//        Timetable timeTable = new Timetable();
//        timeTable.setMember(member);
//        timeTable.setTableName(tableName);
//        timeTable.setTableYear(tableYear);
//        timeTable.setSemester(semester);
//        return timeTable;
//    }

    public void updateTimetable(String tableName, int tableYear, int semester ){
        this.tableName = tableName;
        this.tableYear = tableYear;
        this.semester = semester;
    }
}