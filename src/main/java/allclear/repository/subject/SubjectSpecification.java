package allclear.repository.subject;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.GetSubjectListRequestDto;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubjectSpecification {
    public static Specification<Subject> subjectFilter(GetSubjectListRequestDto requestDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (requestDto.getSubjectId() != null) {
                predicates.add(cb.equal(root.get("subjectId"), requestDto.getSubjectId()));
            }
            if (requestDto.getSubjectName() != null) {
                predicates.add(cb.like(root.get("subjectName"), "%" + requestDto.getSubjectName() + "%"));
            }
            if (requestDto.getProfessor() != null) {
                Join<Subject, ClassInfo> classInfoJoin = root.join("classInfoList");
                predicates.add(cb.like(classInfoJoin.get("professor"), "%" + requestDto.getProfessor() + "%"));
            }
            if (requestDto.getDepartment() != null) {
                predicates.add(cb.like(root.get("department"), "%" + requestDto.getDepartment() + "%"));
            }
            if (requestDto.getMajorClassification() != null) {
                predicates.add(cb.like(root.get("majorClassification"), "%" + requestDto.getMajorClassification() + "%"));
            }
            if (requestDto.getCredit() != null) {
                predicates.add(cb.equal(root.get("credit"), requestDto.getCredit()));
            }
            if (requestDto.getSubjectTarget() != null) {
                predicates.add(cb.like(root.get("subjectTarget"), "%" + requestDto.getSubjectTarget() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}