package allclear.dto.requestDto.timetable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AddCustomTimetableSubjectRequestDto {
    String subjectName;
    List<ClassInfoRequestDto> classInfoRequestDtoList;
}
