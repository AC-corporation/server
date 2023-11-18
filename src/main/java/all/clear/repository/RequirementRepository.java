package all.clear.repository;

import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public List<RequirementComponent> getRequirementComponent(Long id){
        List<RequirementComponent> requirementComponentList= new ArrayList<>();
       //component serch 필요

        return requirementComponentList;
    }

}
