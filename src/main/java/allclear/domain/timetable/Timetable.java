package allclear.domain.timetable;

import java.util.List;

import jakarta.persistence.*;

import allclear.domain.member.Member;
import lombok.*;

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

    @Setter
    @Column(name = "table_name")
    private String tableName; // 시간표 이름

    @Column(name = "table_year")
    private Integer tableYear; // 학년도

    private Integer semester; // 학기

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    @Column(name = "timetable_subject_list")
    private List<TimetableSubject> timetableSubjectList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // ==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getTimetableList().add(this);
    }

    public void addTimetableSubject(TimetableSubject timetableSubject) {
        timetableSubjectList.add(timetableSubject);
        timetableSubject.setTimetable(this);
    }
}
