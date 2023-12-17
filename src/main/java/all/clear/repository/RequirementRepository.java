package all.clear.repository;

import all.clear.domain.requirement.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
//    Requirement findByUserId(Long userId);
}
