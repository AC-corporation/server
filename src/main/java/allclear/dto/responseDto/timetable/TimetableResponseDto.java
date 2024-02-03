package allclear.dto.responseDto.timetable;

import allclear.domain.timetable.Timetable;
import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetableSubject;
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
    private Integer year; //학년도
    private Integer semester; //학기
    private List<TimetableSubjectResponseDto> timetableSubjectResponseDtoList;


    public TimetableResponseDto(Timetable timetable) {
        this.timetableId = timetable.getTimetableId();
        this.tableName = timetable.getTableName();
        this.year = timetable.getTableYear();
        this.semester = timetable.getSemester();
        List<TimetableSubjectResponseDto> timetableSubjectResponseDtoList = timetable.getTimetableSubjectList()
                .stream()
                .map(TimetableSubjectResponseDto::new)
                .collect(Collectors.toList());
        this.timetableSubjectResponseDtoList = timetableSubjectResponseDtoList;
    }

    public TimetableResponseDto(TimetableGeneratorTimetable timetableGeneratorTimetable) {
        TimetableGenerator timetableGenerator = timetableGeneratorTimetable.getTimetableGenerator();
        this.timetableId = timetableGeneratorTimetable.getId();
        this.tableName = "새 시간표";
        this.year = timetableGenerator.getTableYear();
        this.semester = timetableGenerator.getSemester();
        this.timetableSubjectResponseDtoList = timetableGeneratorTimetable.getTimetableGeneratorTimetableSubjectList()
                .stream()
                .map(TimetableGeneratorTimetableSubject::getTimetableGeneratorSubject)
                .map(TimetableSubjectResponseDto::new)
                .collect(Collectors.toList());
        ;
    }
}
