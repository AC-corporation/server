package all.clear.domain;

import all.clear.domain.grade.Grade;
import all.clear.domain.requirement.Requirement;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "app_id", nullable = false)
    private Long appId;
    @Column(name = "app_password", nullable = false)
    private String appPassword;

    @Column(name = "user_name")
    private String userName;
    private String university;
    private String major;
    private String mail;

    @Column(name = "class_type")
    private String classType;
    private int year; //학년
    private int semester; //학기

    @OneToOne(mappedBy = "user")
    private Requirement requirement;

    @OneToOne(mappedBy = "user")
    private Grade grade;

    @OneToMany(mappedBy = "user")
    @Column(name = "timetable_list")
    private List<TimeTable> timeTableList = new ArrayList<>();
}


