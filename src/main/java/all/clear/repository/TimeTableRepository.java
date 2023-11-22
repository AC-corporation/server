package all.clear.repository;

import all.clear.domain.TimeTable;
import all.clear.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimeTableRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(TimeTable timeTable){
        em.persist(timeTable);
    }

    public TimeTable findOne(Long id){
        //timetable_id를 통해 timetable 찾아 반환
        return em.find(TimeTable.class, id);
    }

    public void delete(Long id){
        /**연관된 timeTableSubject 삭제 코드 추가해야함**/
        TimeTable timeTable = findOne(id);
        em.remove(timeTable);
    }
}
