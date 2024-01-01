package allclear.repository.timetable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableSubjectRepository extends JpaRepository<TimetableSubjectRepository, Long> {
}
