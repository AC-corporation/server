package allclear.repository.timetable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.timetable.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {}
