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
    @Id @GeneratedValue
    @Column(name = "timetable_generator_timetable")
    private Long id;

    @OneToMany(mappedBy = "timetableGeneratorTimetable", fetch = FetchType.LAZY)
    @Column(name = "timetable_generator_subject_list")
    private List<TimetableGeneratorSubject> timetableGeneratorSubjectList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_id")
    private TimetableGenerator timetableGenerator;
}
