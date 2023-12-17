package all.clear.repository;

import all.clear.domain.grade.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
