package allclear.repository.timetable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.timetable.TimetableSubject;

@Repository
public interface TimetableSubjectRepository extends JpaRepository<TimetableSubject, Long> {}
