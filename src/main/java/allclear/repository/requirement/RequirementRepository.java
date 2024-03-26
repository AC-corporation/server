package allclear.repository.requirement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import allclear.domain.requirement.Requirement;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    Optional<Requirement> findById(Long userId);
}
