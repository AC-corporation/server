package allclear.repository.subject;

import allclear.domain.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    //개설 학과 기준 조회
    List<Subject> findByDepartment(String department);
    //이수 구분(주전공) 기준 조회
    List<Subject> findByMajorClassification(String majorClassification);
    //이수 구분(다전공) 기준 조회
    List<Subject> findByMultiMajorClassification(String multiMajorClassification);
    //수강 대상 기준 조회
    List<Subject> findBySubjectTarget(String subjectTarget);

}
