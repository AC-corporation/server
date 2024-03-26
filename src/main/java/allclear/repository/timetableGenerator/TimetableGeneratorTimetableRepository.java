package allclear.repository.timetableGenerator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;

@Repository
public interface TimetableGeneratorTimetableRepository
        extends JpaRepository<TimetableGeneratorTimetable, Long> {
    Page<TimetableGeneratorTimetable> findAllByTimetableGenerator(
            TimetableGenerator timetableGenerator, Pageable pageable);
}
