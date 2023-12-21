package allclear.repository;

import allclear.domain.requirement.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
//    Requirement findByUserId(Long userId);
}
