package allclear.repository.requirement;

import allclear.domain.requirement.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    Optional<Requirement> findById(Long userId);
}
