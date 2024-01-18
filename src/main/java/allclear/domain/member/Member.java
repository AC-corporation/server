package allclear.domain.member;

import allclear.domain.grade.Grade;
import allclear.domain.requirement.Requirement;
import allclear.domain.timetable.Timetable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "member_name")
    private String memberName;
    private String university;
    private String major;

    @Column(name = "class_type")
    private String classType; //분반
    private int level; //학년
    private int semester; //학기

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Requirement requirement;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Grade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Column(name = "timetable_list")
    private List<Timetable> timetableList = new ArrayList<>();
}


