package allclear.dto.requestDto.timetable;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ClassInfoRequestDto {
    // 교수명
    private String professor;
    // 강의 요일
    private String classDay;
    // 강의 시작 시간
    private LocalTime startTime;
    // 강의 종료 시간
    private LocalTime endTime;
    // 강의 장소
    private String classRoom;
}
