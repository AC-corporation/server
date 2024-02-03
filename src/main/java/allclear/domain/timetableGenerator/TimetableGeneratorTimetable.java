package allclear.domain.timetableGenerator;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "timetableGeneratorTimetable", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "timetable_generator_timetable_subject_list")
    private List<TimetableGeneratorTimetableSubject> timetableGeneratorTimetableSubjectList = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_id")
    private TimetableGenerator timetableGenerator;


    //==연관관계 메서드==//
    public void addTimetableGeneratorTimetableSubject(TimetableGeneratorSubject timetableGeneratorSubject) {
        timetableGeneratorTimetableSubjectList.add(TimetableGeneratorTimetableSubject
                .builder()
                .timetableGeneratorTimetable(this)
                .timetableGeneratorSubject(timetableGeneratorSubject)
                .build()
        );
    }
}
