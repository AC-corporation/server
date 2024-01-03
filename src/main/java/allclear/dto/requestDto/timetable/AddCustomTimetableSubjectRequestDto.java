package allclear.dto.requestDto.timetable;

import allclear.dto.responseDto.timetable.ClassInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AddCustomTimetableSubjectRequestDto {
    String subjectName;
    List<ClassInfoDto> classInfoDtoListList;
}
