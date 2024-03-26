package allclear.repository.grade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.grade.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findById(Long gradeId);
}
