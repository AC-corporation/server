package allclear.domain.member;

import allclear.domain.auth.RefreshToken;
import allclear.domain.grade.Grade;
import allclear.domain.requirement.Requirement;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetableGenerator.TimetableGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "member_name")
    private String username;
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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private TimetableGenerator timetableGenerator;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Column(name = "timetable_list")
    private List<Timetable> timetableList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;


    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setRequirement(Requirement requirement)
    {
        this.requirement = requirement;
    }

    public void setGrade(Grade grade){
        this.grade = grade;
    }

    public void setTimetableGenerator(TimetableGenerator timetableGenerator)
    {
        this.timetableGenerator = timetableGenerator;
    }

    public void updateMember(String username, String university, String major,String classType,String email,int level,int semester){
        this.username = username;
        this.university = university;
        this.email = email;
        this.major = major;
        this.classType = classType;
        this.level = level;
        this.semester = semester;
    }

}


