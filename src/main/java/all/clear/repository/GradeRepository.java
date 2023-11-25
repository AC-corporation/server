package all.clear.repository;

import all.clear.domain.grade.Grade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GradeRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(Grade grade) {
        em.persist(grade);
    }

    public Grade findOne(Long id) {
        return em.find(Grade.class, id);
    }

    public Grade findByUserId(Long userId) {
        return em.createQuery("select m from Grade m where m.user_id = :user_id", Grade.class)
                .setParameter("user_id", userId).getSingleResult();
    }
}
