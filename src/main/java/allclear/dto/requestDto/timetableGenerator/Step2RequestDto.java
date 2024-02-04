package allclear.dto.requestDto.timetableGenerator;

import allclear.dto.requestDto.timetable.TimetableSubjectRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step2RequestDto {
    private List<TimetableSubjectRequestDto> timetableSubjectRequestDtoList;
}
