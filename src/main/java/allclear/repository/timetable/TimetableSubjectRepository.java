package allclear.repository.timetable;

import allclear.domain.timetable.TimetableSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableSubjectRepository extends JpaRepository<TimetableSubject, Long> {
}
