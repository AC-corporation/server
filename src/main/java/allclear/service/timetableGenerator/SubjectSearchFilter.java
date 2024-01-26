package allclear.service.timetableGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class SubjectSearchFilter {
    private Long subjectId; //과목 번호
    private String subjectName; //과목 이름
    private String professor; //교수명
    private String department; //개설 학과
    private String majorClassification; //이수 구분(주전공)
    private Double credit; //학점
    private String subjectTarget; //수강 대상
    private String year; //수강 학년
    private List<UnavailableTimeRange> unavailableTimeRangeList; //불가능한 시간대

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class UnavailableTimeRange {
        private String day; //수업 요일
        private LocalTime startTime; //수업 시작 시간
        private LocalTime endTime; //수업 종료 시간
    }
}
