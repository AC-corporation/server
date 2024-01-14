package allclear.service.timetableMaker;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SubjectSearchCriteria {
    private Long subjectId; //과목 번호
    private String subjectName; //과목 이름
    private String professor; //교수명
    private String department; //개설 학과
    private String majorClassification; //이수 구분(주전공)
    private Double credit; //학점
    private String subjectTarget; //수강 대상
    private String year; //수강 학년
    private List<UnavailableTimeRange> unavailableTimeRangeList; //불가능한 시간대
}
