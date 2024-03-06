package allclear.dto.responseDto.timetable;

import allclear.domain.timetable.Timetable;
import lombok.Getter;

import java.util.List;

@Getter
public class TimetableListResponseDto {
    private List<TimetableResponseDto> timetableResponseDtoList;

    public TimetableListResponseDto(List<Timetable> timetableList) {
       timetableResponseDtoList = timetableList
               .stream()
               .map(TimetableResponseDto::new)
               .toList();
    }
}
