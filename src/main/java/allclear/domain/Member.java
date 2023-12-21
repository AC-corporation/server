package allclear.domain;

import allclear.domain.grade.Grade;
import allclear.domain.requirement.Requirement;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
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

    @OneToOne(mappedBy = "member")
    private Requirement requirement;

    @OneToOne(mappedBy = "member")
    private Grade grade;

//    @OneToMany(mappedBy = "member")
//    @Column(name = "timetable_list")
//    private List<TimeTable> timeTableList = new ArrayList<>();
}


