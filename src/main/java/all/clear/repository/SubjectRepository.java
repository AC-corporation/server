package all.clear.repository;

import all.clear.domain.Subject;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubjectRepository {
    private final EntityManager em;

    public void save(Subject subject){
        if(subject.getSubjectId() == null)
            em.persist(subject);
            //id가 없다는 것은 새로 생성한 객체라는 뜻이므로 persist(신규등록)
        else
            em.merge(subject);
        //이미 DB에 있는 정보이므로 update처럼 merge기능 적용
    }

    public Subject findOne(Long id){
        //subject_id를 통해 subject 찾아 반환
        return em.find(Subject.class, id);
    }

    public List<Subject> findAll(){
        return em.createQuery("select s from Subject s", Subject.class).getResultList();
    }
}
