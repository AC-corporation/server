package allclear.repository.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableGeneratorRepository extends JpaRepository<TimetableGenerator, Long> {
}
