package allclear.repository;

import allclear.domain.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name); //이름으로 과목 찾기
    Subject findByProfessor(String professor); //교수명으로 과목 찾기
    Subject findByClassInfoListLike(String classInfo); //수업 정보에 특정 문자열을 포함하는 과목 찾기
    Subject findByMajorClassification(String majorClassification); //이수구분(주전공)으로 과목 찾기
    Subject findByMultiMajorClassification(String multiMajorClassification); //이수구분(다전공)으로 과목 찾기
}
