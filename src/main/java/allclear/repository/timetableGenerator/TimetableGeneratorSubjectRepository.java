package allclear.repository.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableGeneratorSubjectRepository extends JpaRepository<TimetableGeneratorSubject, Long>  {
}
