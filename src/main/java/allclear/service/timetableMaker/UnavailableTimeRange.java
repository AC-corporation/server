package allclear.service.timetableMaker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnavailableTimeRange {
    private String day; //수업 요일
    private LocalTime startTime; //수업 시작 시간
    private LocalTime endTime; //수업 종료 시간
}