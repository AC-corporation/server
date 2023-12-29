package allclear.repository;

import allclear.domain.grade.SemesterGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterGradeRepository extends JpaRepository<SemesterGrade, Long> {
}
