package allclear.repository.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableGeneratorTimetableRepository extends JpaRepository<TimetableGeneratorTimetable, Long> {
}
