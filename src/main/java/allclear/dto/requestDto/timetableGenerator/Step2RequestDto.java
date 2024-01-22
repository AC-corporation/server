package allclear.dto.requestDto.timetableGenerator;

import allclear.dto.requestDto.timetable.AddCustomTimetableSubjectRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step2RequestDto {
    @NotNull
    List<AddCustomTimetableSubjectRequestDto> customTimetableSubjectRequestDtoList;
}
