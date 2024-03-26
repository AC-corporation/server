package allclear.repository.timetableGenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.timetableGenerator.TimetableGeneratorSubject;

@Repository
public interface TimetableGeneratorSubjectRepository
        extends JpaRepository<TimetableGeneratorSubject, Long> {}
