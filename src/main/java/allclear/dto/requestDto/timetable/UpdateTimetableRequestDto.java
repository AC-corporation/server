package allclear.dto.requestDto.timetable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateTimetableRequestDto {
    private String tableName;
    private List<TimetableSubjectRequestDto> timetableSubjectRequestDtoList;
}
