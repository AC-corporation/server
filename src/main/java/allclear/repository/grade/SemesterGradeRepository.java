package allclear.repository.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.grade.SemesterGrade;

@Repository
public interface SemesterGradeRepository extends JpaRepository<SemesterGrade, Long> {}
