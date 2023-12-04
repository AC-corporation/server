package all.clear.repository;

import all.clear.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public User findOne(Long id){
        //user_id를 통해 user 찾아 반환
        return em.find(User.class, id);
    }

    /**수정 필요**/
    public User findByAppId(Long appId){
        return  em.find(User.class, appId);
    }

}
