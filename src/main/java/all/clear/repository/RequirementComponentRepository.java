package all.clear.repository;

import all.clear.domain.RequirementComponent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RequirementComponentRepository {
    private final EntityManager em;

    public void save(RequirementComponent requirementComponent){
        em.persist(requirementComponent);
    }

    public RequirementComponent findOne(Long id) {
        return em.find(RequirementComponent.class, id);
    }

}
