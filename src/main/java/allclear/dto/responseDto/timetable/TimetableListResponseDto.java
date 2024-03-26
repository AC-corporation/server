package allclear.dto.responseDto.timetable;

import java.util.List;

import allclear.domain.timetable.Timetable;
import lombok.Getter;

@Getter
public class TimetableListResponseDto {
    private List<TimetableResponseDto> timetableResponseDtoList;

    public TimetableListResponseDto(List<Timetable> timetableList) {
        timetableResponseDtoList = timetableList.stream().map(TimetableResponseDto::new).toList();
    }
}
