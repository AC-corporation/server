package allclear.repository.subject;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.service.timetableGenerator.SubjectSearchFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
public class SubjectSpecification {
    public static Specification<Subject> subjectFilter(SubjectListRequestDto requestDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Subject, ClassInfo> classInfoJoin = root.join("classInfoList");

            if (requestDto.getSearchString() != null && !requestDto.getSearchString().isEmpty()) {
                String likeSearchString = "%" + requestDto.getSearchString() + "%";

                predicates.add(cb.or(
                        cb.like(root.get("subjectName"), likeSearchString),
                        cb.like(root.get("department"), likeSearchString),
                        cb.like(root.get("majorClassification"), likeSearchString),
                        cb.like(root.get("multiMajorClassification"), likeSearchString),
                        cb.like(root.get("engineeringCertification"), likeSearchString),
                        cb.like(root.get("subjectTarget"), likeSearchString),
                        cb.like(root.get("subjectId").as(String.class), likeSearchString),
                        cb.like(root.get("credit").as(String.class), likeSearchString),
                        cb.like(classInfoJoin.get("professor"), likeSearchString),
                        cb.like(classInfoJoin.get("classDay"), likeSearchString)
                ));
            }

            if (requestDto.getSubjectTarget() != null && !requestDto.getSubjectTarget().isEmpty()) {
                predicates.add(cb.like(root.get("subjectTarget"), "%" + requestDto.getSubjectTarget() + "%"));
            }

            if (requestDto.getYear() != null && !requestDto.getYear().isEmpty()) {
                predicates.add(cb.like(root.get("year"), "%" + requestDto.getYear() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Subject> subjectFilter(SubjectSearchFilter searchFilter) {
        return null;
    }
}