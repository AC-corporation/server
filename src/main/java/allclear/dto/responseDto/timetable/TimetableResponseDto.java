package allclear.dto.responseDto.timetable;

import allclear.domain.timetable.Timetable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimetableResponseDto {
    private Long timetableId;
    private String tableName; //시간표 이름
    private int year; //학년도
    private int semester; //학기
    private List<TimetableSubjectResponseDto> timetableSubjectResponseDtoList;


    public TimetableResponseDto(Timetable timetable) {
        this.timetableId = timetable.getTimetableId();
        this.tableName = timetable.getTableName();
        this.year = timetable.getYear();
        this.semester = timetable.getSemester();
        List<TimetableSubjectResponseDto> timetableSubjectResponseDtoList = timetable.getTimetableSubjectList()
                .stream()
                .map(TimetableSubjectResponseDto::new)
                .collect(Collectors.toList());
        this.timetableSubjectResponseDtoList = timetableSubjectResponseDtoList;
    }
}
