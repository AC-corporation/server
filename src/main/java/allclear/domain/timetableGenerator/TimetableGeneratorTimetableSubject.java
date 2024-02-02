package allclear.domain.timetableGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableGeneratorTimetableSubject {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_timetable_subject_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_subject_id")
    private TimetableGeneratorSubject timetableGeneratorSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_timetable_id")
    private TimetableGeneratorTimetable timetableGeneratorTimetable;
}
