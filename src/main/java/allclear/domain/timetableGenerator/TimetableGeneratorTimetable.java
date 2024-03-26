package allclear.domain.timetableGenerator;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableGeneratorTimetable {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_timetable_id")
    private Long id;

    @OneToMany(
            mappedBy = "timetableGeneratorTimetable",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Column(name = "timetable_generator_timetable_subject_list")
    private List<TimetableGeneratorTimetableSubject> timetableGeneratorTimetableSubjectList;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_id")
    private TimetableGenerator timetableGenerator;

    // ==연관관계 메서드==//
    public void addTimetableGeneratorTimetableSubject(
            TimetableGeneratorSubject timetableGeneratorSubject) {
        timetableGeneratorTimetableSubjectList.add(
                TimetableGeneratorTimetableSubject.builder()
                        .timetableGeneratorTimetable(this)
                        .timetableGeneratorSubject(timetableGeneratorSubject)
                        .build());
    }

    public static TimetableGeneratorTimetable createTimetableGeneratorTimetable(
            List<TimetableGeneratorSubject> tgSubjectList) {
        TimetableGeneratorTimetable tgTimetable =
                TimetableGeneratorTimetable.builder()
                        .timetableGeneratorTimetableSubjectList(new ArrayList<>())
                        .build();
        for (TimetableGeneratorSubject tgSubject : tgSubjectList) {
            tgTimetable.addTimetableGeneratorTimetableSubject(tgSubject);
        }
        return tgTimetable;
    }
}
