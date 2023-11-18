package all.clear.repository;

import all.clear.domain.grade.SemesterGrade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SemesterGradeRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(SemesterGrade semesterGrade){
        em.persist(semesterGrade);
    }

    public SemesterGrade findOne(Long id){
        return em.find(SemesterGrade.class, id);
    }


}
