package allclear.repository.requirement;

import allclear.domain.requirement.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
//    Requirement findByUserId(Long userId);
}
