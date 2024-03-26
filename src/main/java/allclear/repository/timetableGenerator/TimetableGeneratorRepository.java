package allclear.repository.timetableGenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.timetableGenerator.TimetableGenerator;

@Repository
public interface TimetableGeneratorRepository extends JpaRepository<TimetableGenerator, Long> {}
