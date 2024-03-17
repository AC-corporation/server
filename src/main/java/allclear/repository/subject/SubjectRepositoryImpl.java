package allclear.repository.subject;

import allclear.crawl.subject.ChangeToUsaintSubjectName;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.SubjectSearchRequestDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static allclear.domain.subject.QSubject.subject;

@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Subject> search(SubjectSearchRequestDto request, Pageable pageable){
        QueryResults<Subject> results =  queryFactory.
                select(subject).from(subject).where(yearCondition(request), anyCondition(request),
                        majorClassificationCondition(request), liberalArtsClassificationCondition(request),
                        subjectNameCondition(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Subject> subjectList = results.getResults(); // 조회된 과목 리스트
        long totalCount = results.getTotal(); // 총 조회 과목 개수

        return new PageImpl<>(subjectList, pageable, totalCount);
    }

    private BooleanExpression yearCondition(SubjectSearchRequestDto request){ // 수강학년
        String year = request.getYear();

        if (year == null || year.isEmpty()) {
            return null;
        }
        return subject.subjectTarget.like("%" + year + "%").or(subject.subjectTarget.like("%" + "전체" + "%"));
    }

    private BooleanExpression anyCondition(SubjectSearchRequestDto request){ // 검색문자
        String searchText = request.getSearchString();

        if (searchText == null || searchText.isEmpty()) {
            return null;
        }

        return subject.subjectName.like("%" + searchText + "%")
                .or(subject.department.like("%" + searchText + "%"))
                .or(subject.majorClassification.like("%" + searchText + "%"))
                .or(subject.majorClassification.like("%" + searchText + "%"))
                .or(subject.engineeringCertification.like("%" + searchText + "%"))
                .or(subject.subjectTarget.like("%" + searchText + "%"))
                .or(subject.subjectId.stringValue().like("%" + searchText + "%"))
                .or(subject.credit.like("%" + searchText + "%"))
                .or(subject.classInfoList.any().professor.like("%" + searchText + "%"))
                .or(subject.classInfoList.any().classDay.like("%" + searchText + "%"));
    }

    private BooleanExpression majorClassificationCondition(SubjectSearchRequestDto request) { // 이수구분 주전공
        String courseClassification = request.getCourseClassification(); // 교과목 분류
        List<String> majorClassificationList = null;
        BooleanExpression totalCondition = null; // 최종 반환되는 조건

        if (courseClassification == null || courseClassification.isEmpty()) {
            return null;
        }
        if (courseClassification.contains("전공") && request.getMajorName() == null) // 교과목 분류가 전공일 경우 반드시 학과를 포함
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        if (request.getMajorName() != null) { // 전기, 전필, 전선, 전공별
            majorClassificationList = ChangeToUsaintSubjectName.change(request.getMajorName());
            if (majorClassificationList.isEmpty()) // 학과가 조회되지 않을 경우
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            if (courseClassification.equals("전공기초/필수")) {
                for (String majorClassification : majorClassificationList) {
                    BooleanExpression condition = subject.majorClassification.like("%" + "전기-" + majorClassification + "%")
                            .or(subject.majorClassification.like("%" + "전필-" + majorClassification + "%"));
                    totalCondition = (totalCondition == null) ? condition : totalCondition.or(condition);
                }
                return totalCondition;
            }
            else if (courseClassification.equals("전공선택")) {
                for (String majorClassification : majorClassificationList) {
                    BooleanExpression condition = subject.majorClassification.like("%" + "전선-" + majorClassification + "%");
                    totalCondition = (totalCondition == null) ? condition : totalCondition.or(condition);
                }
                return totalCondition;
            }
            else if (courseClassification.contains("전공별")) {
                for (String majorClassification : majorClassificationList) {
                    BooleanExpression condition = subject.majorClassification.like("%" + majorClassification + "%");
                    totalCondition = (totalCondition == null) ? condition : totalCondition.or(condition);
                }
                return totalCondition;
            }
        }
        else if (courseClassification.equals("교양필수")) {
            return subject.majorClassification.like("%" + "교필" + "%");
        }
        else if (courseClassification.equals("교양선택")) {
            return subject.majorClassification.like("%" + "교선" + "%");
        }
        else if (courseClassification.equals("채플")) {
            return subject.majorClassification.like("%" + "채플" + "%");
        }
        else if (courseClassification.equals("교직")) {
            return subject.majorClassification.like("%" + "교직" + "%");
        }

        return null;
    }

    private BooleanExpression liberalArtsClassificationCondition(SubjectSearchRequestDto request){ // 교과영역 구분
        String courseClassification = request.getCourseClassification(); // 교과목 분류

        if (courseClassification == null)
            return null;

        if (courseClassification.equals("교양필수")) {
            String liberalArtsClassification = request.getRequiredElectivesClassification();
            if (liberalArtsClassification == null)
                liberalArtsClassification = "";
            if (liberalArtsClassification.contains("전체"))
                return null;

            return subject.liberalArtsClassification.like("%" + liberalArtsClassification + "%");
        }
        else if (courseClassification.equals("교양선택")) {
            String liberalArtsClassificationYear = request.getElectivesYear();
            String liberalArtsClassification = request.getElectivesClassification();
            if (liberalArtsClassificationYear == null)
                liberalArtsClassificationYear = "";
            if (liberalArtsClassification == null)
                liberalArtsClassification = "";

            return subject.liberalArtsClassification.like("%" + liberalArtsClassificationYear + "%" + liberalArtsClassification + "%");
        }

        return null;
    }

    private BooleanExpression subjectNameCondition(SubjectSearchRequestDto request){ // 교과영역 구분(과목명), 교필
        String subjectName = request.getRequiredElectivesName();

        if (subjectName == null || subjectName.isEmpty()) {
            return null;
        }

        return subject.subjectName.like("%" + subjectName + "%");
    }
}
