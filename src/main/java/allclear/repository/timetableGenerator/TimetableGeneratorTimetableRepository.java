package allclear.repository.timetableGenerator;

import allclear.domain.member.Member;
import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableGeneratorTimetableRepository extends JpaRepository<TimetableGeneratorTimetable, Long> {
    Page<TimetableGeneratorTimetable> findAllByTimetableGenerator(TimetableGenerator timetableGenerator, Pageable pageable);
}
