package allclear.repository.subject;

import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.GetSubjectListRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubjectSpecification implements Specification<Subject> {

    private final GetSubjectListRequestDto requestDto;

    public SubjectSpecification(GetSubjectListRequestDto requestDto) {
        this.requestDto = requestDto;
    }

    @Override
    public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (requestDto.getSubjectId() != null) {
            predicates.add(cb.equal(root.get("subjectId"), requestDto.getSubjectId()));
        }
        if (requestDto.getSubjectName() != null) {
            predicates.add(cb.equal(root.get("subjectName"), requestDto.getSubjectName()));
        }
        if (requestDto.getProfessor() != null) {
            predicates.add(cb.equal(root.get("professor"), requestDto.getProfessor()));
        }
        if (requestDto.getDepartment() != null) {
            predicates.add(cb.equal(root.get("department"), requestDto.getDepartment()));
        }
        if (requestDto.getMajorClassification() != null) {
            predicates.add(cb.equal(root.get("majorClassification"), requestDto.getMajorClassification()));
        }
        if (requestDto.getCredit() != null) {
            predicates.add(cb.equal(root.get("credit"), requestDto.getCredit()));
        }
        if (requestDto.getSubjectTarget() != null) {
            predicates.add(cb.equal(root.get("subjectTarget"), requestDto.getSubjectTarget()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
