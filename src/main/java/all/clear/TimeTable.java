package all.clear;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import javax.security.auth.Subject;
import java.util.List;

@Entity
public class TimeTable {
    @Id @GeneratedValue
    @Column(name = "TIMETABLE_ID")
    private Long id;

    @Column(name = "TABLE_NAME")
    private String tableName; //시간표 이름

    @Column(name = "YEAR")
    private int year; //학년
    @Column(name = "SEMESTER")
    private int semester; //학기

    @Column(name = "CLASS_LIST")
    private List<Subject> classList;


}


