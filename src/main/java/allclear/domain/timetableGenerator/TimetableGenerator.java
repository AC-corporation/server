package allclear.domain.timetableGenerator;

import allclear.domain.timetable.Timetable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TimetableGenerator {
    @Id
    @Column(name = "timetable_generator_id")
    private Long id;

    @Column(name = "table_year")
    private Integer tableYear; //학년도
    private Integer semester; //학기

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_subject_list")
    private List<TimetableGeneratorSubject> timetableGeneratorSubjects = new ArrayList<>();


    public void addTimetableGeneratorSubject(TimetableGeneratorSubject timetableGeneratorSubject) {
        timetableGeneratorSubjects.add(timetableGeneratorSubject);
        timetableGeneratorSubject.setTimetableGenerator(this);
    }
}
