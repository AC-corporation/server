
package all.clear;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {
    @Id @GeneratedValue
    @Column(name = "SUBJECT_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "TIMETABLE_ID") // 추가
    private TimeTable timeTable;
    @Column(name = "SUBJECT_NAME")
    private String name; //과목 이름
    @Column(name = "PROFESSOR")
    private String professor;
    @Column(name = "CREDIT")
    private Long credit; //학점
    @Column(name = "REQ_LIST")
    private List<String> reqList; //졸업 요건
}


