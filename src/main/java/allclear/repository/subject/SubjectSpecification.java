package allclear.repository.subject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubjectSpecification {
    private String searchString; // 검색할 문자열
    private Long subjectId; // 과목 번호 앞에서부터 8자리
    private String subjectName; // 과목 이름
    private String category1; // 전필, 교선 등 구분
    private String category2;
    private String professor; // 교수명
    private String department; // 개설 학과
    private String majorClassification; // 이수구분
    private List<String> majorClassificationList; // 이수 구분(주전공) 리스트
    private String liberalArtsClassification; // 교과영역 구분
    private Double credit; // 학점
    private String subjectTarget; // 수강 대상
    private String year; // 수강 학년
    private List<UnavailableTimeRange> unavailableTimeRangeList; // 불가능한 시간대

    @Getter
    @AllArgsConstructor
    public class UnavailableTimeRange {
        private String day; // 수업 요일
        private LocalTime startTime; // 수업 시작 시간
        private LocalTime endTime; // 수업 종료 시간
    }

    public static Specification<Subject> subjectFilter(SubjectSpecification specification) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Subject, ClassInfo> classInfoJoin = root.join("classInfoList");

            if (specification.getSearchString() != null
                    && !specification.getSearchString().isEmpty()) {
                String likeSearchString = "%" + specification.getSearchString() + "%";

                predicates.add(
                        cb.or(
                                cb.like(root.get("subjectName"), likeSearchString),
                                cb.like(root.get("department"), likeSearchString),
                                cb.like(root.get("majorClassification"), likeSearchString),
                                cb.like(root.get("multiMajorClassification"), likeSearchString),
                                cb.like(root.get("engineeringCertification"), likeSearchString),
                                cb.like(root.get("subjectTarget"), likeSearchString),
                                cb.like(root.get("liberalArtsClassification"), likeSearchString),
                                cb.like(root.get("subjectId").as(String.class), likeSearchString),
                                cb.like(root.get("credit").as(String.class), likeSearchString),
                                cb.like(classInfoJoin.get("professor"), likeSearchString),
                                cb.like(classInfoJoin.get("classDay"), likeSearchString)));
            }
            if (specification.getSubjectId() != null) {
                predicates.add(
                        cb.like(
                                root.get("subjectId").as(String.class),
                                specification.getSubjectId().toString() + "%"));
            }
            if (specification.getSubjectName() != null
                    && !specification.getSubjectName().isEmpty()) {
                predicates.add(
                        cb.like(
                                root.get("subjectName"),
                                "%" + specification.getSubjectTarget() + "%"));
            }
            if (specification.getProfessor() != null && !specification.getProfessor().isEmpty()) {
                predicates.add(
                        cb.like(
                                classInfoJoin.get("professor"),
                                "%" + specification.getProfessor() + "%"));
            }
            if (specification.getDepartment() != null && !specification.getDepartment().isEmpty()) {
                predicates.add(
                        cb.like(root.get("department"), "%" + specification.getDepartment() + "%"));
            }
            if ((specification.getCategory1() != null && !specification.getCategory1().isEmpty())
                    || (specification.getCategory2() != null
                            && !specification.getCategory2().isEmpty())) {
                List<Predicate> orPredicates = new ArrayList<>();
                if (specification.getCategory1() != null && !specification.getCategory1().isEmpty())
                    orPredicates.add(
                            cb.like(
                                    root.get("majorClassification"),
                                    "%" + specification.getCategory1() + "%"));
                if (specification.getCategory2() != null && !specification.getCategory2().isEmpty())
                    orPredicates.add(
                            cb.like(
                                    root.get("majorClassification"),
                                    "%" + specification.getCategory2() + "%"));
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }
            if (specification.getMajorClassificationList() != null
                    && !specification.getMajorClassificationList().isEmpty()) {
                List<Predicate> majorClassificationPredicates = new ArrayList<>();
                for (String majorClassification : specification.getMajorClassificationList()) {
                    majorClassificationPredicates.add(
                            cb.like(
                                    root.get("majorClassification"),
                                    "%" + majorClassification + "%"));
                }
                predicates.add(cb.or(majorClassificationPredicates.toArray(new Predicate[0])));
            }
            if (specification.getMajorClassification() != null
                    && !specification.getMajorClassification().isEmpty()) {
                predicates.add(
                        cb.like(
                                root.get("majorClassification"),
                                "%" + specification.getMajorClassification() + "%"));
            }
            if (specification.getLiberalArtsClassification() != null
                    && !specification.getLiberalArtsClassification().isEmpty()) {
                predicates.add(
                        cb.like(
                                root.get("liberalArtsClassification"),
                                "%" + specification.getLiberalArtsClassification() + "%"));
            }
            if (specification.getCredit() != null) {
                predicates.add(cb.like(root.get("credit"), "%" + specification.getCredit() + "%"));
            }
            if (specification.getSubjectTarget() != null
                    && !specification.getSubjectTarget().isEmpty()) {
                predicates.add(
                        cb.or(
                                cb.like(
                                        root.get("subjectTarget"),
                                        "%" + specification.getSubjectTarget() + "%"),
                                cb.like(root.get("subjectTarget"), "%" + "전체" + "%")));
            }
            if (specification.getYear() != null && !specification.getYear().isEmpty()) {
                predicates.add(
                        cb.or(
                                cb.like(
                                        root.get("subjectTarget"),
                                        "%" + specification.getYear() + "%"),
                                cb.like(root.get("subjectTarget"), "%" + "전체학년" + "%")));
            }
            if (specification.getUnavailableTimeRangeList() != null
                    && !specification.getUnavailableTimeRangeList().isEmpty()) {
                for (UnavailableTimeRange range : specification.getUnavailableTimeRangeList()) {
                    LocalTime adjustedStartTime = range.getStartTime().minusMinutes(15);
                    LocalTime adjustedEndTime = range.getEndTime().plusMinutes(15);

                    predicates.add(
                            cb.or(
                                    cb.notEqual(root.get("class_date"), range.getDay()),
                                    cb.not(
                                            cb.or(
                                                    cb.between(
                                                            root.get("start_time"),
                                                            adjustedStartTime,
                                                            adjustedEndTime),
                                                    cb.between(
                                                            root.get("end_time"),
                                                            adjustedStartTime,
                                                            adjustedEndTime)))));
                }
            }
            if (specification.getLiberalArtsClassification() != null
                    && !specification.getLiberalArtsClassification().isEmpty()) {
                predicates.add(
                        cb.like(
                                root.get("liberalArtsClassification"),
                                "%" + specification.getLiberalArtsClassification() + "%"));
            }
            predicates.add(cb.notLike(root.get("subjectTarget"), "%" + "순수외국인입학생" + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
