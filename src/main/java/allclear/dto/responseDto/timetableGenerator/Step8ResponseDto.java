package allclear.dto.responseDto.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Step8ResponseDto {
    private List<TimetableResponseDto> timetableList;

    public Step8ResponseDto(List<TimetableGeneratorTimetable> timetableGeneratorTimetableList) {
        this.timetableList = timetableGeneratorTimetableList
                .stream()
                .map(TimetableResponseDto::new)
                .collect(Collectors.toList());
    }
}
