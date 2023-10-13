package all.clear;

import jakarta.persistence.*;

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

    @OneToOne
    @JoinColumn(name = "REQ_ID") // REQ 엔티티에 user 필드 추가 필요
    @Column(name = "REQUIREMENT")
    private Requirement requirement;

    @OneToOne
    @JoinColumn(name = "GRADE_ID") // Grade 엔티티에 user 필드 추가 필요
    @Column(name = "GRADE")
    private Grade grade;


    /*
    @OneToMany(mappedBy = "user") // 타임테이블 엔티티에 user FK 외래 키 추가 필요
    @Column(name = "TIMETABLE_LIST")
    private List<TimeTable> timeTable;
    */

}


