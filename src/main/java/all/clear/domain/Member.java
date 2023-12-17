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
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "app_id", nullable = false)
    private Long appId;
    @Column(name = "app_password", nullable = false)
    private String appPassword;

    @Column(name = "member_name")
    private String memberName;
    private String university;
    private String major;
    private String mail;

    @Column(name = "class_type")
    private String classType; //분반
    private int level; //학년
    private int semester; //학기

    @OneToOne(mappedBy = "member")
    private Requirement requirement;

    @OneToOne(mappedBy = "member")
    private Grade grade;

    @OneToMany(mappedBy = "member")
    @Column(name = "timetable_list")
    private List<TimeTable> timeTableList = new ArrayList<>();
}


