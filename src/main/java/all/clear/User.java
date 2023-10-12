package all.clear;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.sql.Time;
import java.util.List;

@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USAINT_ID")
    private Long usaintId;
    @Column(name = "USAINT_PASSWORD")
    private String usaintPassword;

    @Column(name = "USER_NAME")
    private String name;

    @Column(name = "UNIVERSITY")
    private String university;

    @Column(name = "MAJOR")
    private String major;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "CLASSTYPE")
    private String classType;

    @Column(name = "YEAR")
    private int year; //학년
    @Column(name = "SEMESTER")
    private int semester; //학기

    @Column(name = "REQUIREMENT")
    private Requirement requirement;
    @Column(name = "GRADE")
    private Grade grade;

    @Column(name = "TIMETABLE_LIST")
    private List<TimeTable> timeTable;

}


