package all.clear.repository;

import all.clear.domain.requirement.Requirement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class RequirementRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(Requirement requirement){
        em.persist(requirement);
    }

    public Requirement findOne(Long id){
        return em.find(Requirement.class, id);
    }

    public Requirement findByUserId(Long userId) {

    }

}
