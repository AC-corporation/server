package allclear.dto.requestDto.timetableGenerator;

import allclear.dto.requestDto.timetable.AddCustomTimetableSubjectRequestDto;
import allclear.dto.requestDto.timetable.ClassInfoRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step2RequestDto {
    String subjectName;
    List<ClassInfoRequestDto> classInfoRequestDtoList;
}
