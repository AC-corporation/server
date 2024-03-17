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
        String courseClassification = request.getCourseClassification(); // 교과목 분류
        String year = request.getYear(); // 검색 년도
        String searchText = request.getSearchString(); // 검색 문자
        QueryResults<Subject> results = null;

        checkInputValue(request); // 예외 처리 해주는 함수
        if (courseClassification != null && !courseClassification.isEmpty()) {
            results =  queryFactory.
                    select(subject).from(subject).where(yearCondition(request), anyCondition(request),
                            majorClassificationCondition(request), liberalArtsClassificationCondition(request),
                            subjectNameCondition(request))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        else if (year != null || searchText != null){
            results = queryFactory.select(subject).from(subject).where(yearCondition(request),
                        anyCondition(request))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
        }
        else {
            results = queryFactory.select(subject).from(subject)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

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
        BooleanExpression totalCondition = null; // 최종 반환되는 조건

        assert courseClassification != null;
        if (courseClassification.contains("전공")) {
            List<String> majorClassificationList = ChangeToUsaintSubjectName.change(request.getMajorName());;
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

        assert courseClassification != null;
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
        String courseClassification = request.getCourseClassification(); // 교과목 분류

        if (subjectName == null || subjectName.isEmpty()) {
            return null;
        }

        assert courseClassification != null;
        if (courseClassification.equals("교양필수")) {
            return subject.subjectName.like("%" + subjectName + "%");
        }

        return null;
    }

    // 예외 조건 처리하는 함수
    private void checkInputValue(SubjectSearchRequestDto request) {
        String courseClassification = request.getCourseClassification(); // 교과목 분류
        String year = request.getYear(); // 검색 년도
        String searchText = request.getSearchString(); // 검색 문자

        if (courseClassification != null) {
            if (courseClassification.contains("전공")) {
                if (request.getMajorName() == null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getElectivesYear() != null || request.getElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getRequiredElectivesName() != null || request.getRequiredElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            }
            else if (courseClassification.equals("교양필수")) {
                if (request.getMajorName() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getElectivesYear() != null || request.getElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            }
            else if (courseClassification.equals("교양선택")) {
                if (request.getMajorName() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getRequiredElectivesName() != null || request.getRequiredElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            }
            else if (courseClassification.contains("채플") || courseClassification.contains("교직")) {
                if (request.getMajorName() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getElectivesYear() != null || request.getElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
                if (request.getRequiredElectivesName() != null || request.getRequiredElectivesClassification() != null)
                    throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            }
        }
        else {
            if (request.getMajorName() != null)
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            if (request.getElectivesYear() != null)
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            if (request.getElectivesClassification() != null)
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            if (request.getRequiredElectivesClassification() != null)
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            if (request.getRequiredElectivesName() != null)
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
        }
    }
}
