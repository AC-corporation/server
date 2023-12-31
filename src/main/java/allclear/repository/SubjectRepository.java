package allclear.repository;

import allclear.domain.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name); //이름으로 과목 찾기
    Subject findByProfessor(String professor); //교수명으로 과목 찾기
    Subject findByCredit(Integer credit); //학점으로 과목 찾기
}
