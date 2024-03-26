package allclear.repository.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import allclear.domain.subject.Subject;

@Repository
public interface SubjectRepository
        extends JpaRepository<Subject, Long>,
                JpaSpecificationExecutor<Subject>,
                SubjectRepositoryCustom {
    Page<Subject> findAll(Pageable pageable);
}
