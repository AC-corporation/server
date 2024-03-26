package allclear.dto.requestDto.timetableGenerator;

import java.util.List;

import allclear.dto.requestDto.timetable.TimetableSubjectRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step2RequestDto {
    private List<TimetableSubjectRequestDto> timetableSubjectRequestDtoList;
}
