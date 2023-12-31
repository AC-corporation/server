package allclear.dto.requestDto.timetable;

import allclear.dto.responseDto.timetable.ClassInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateTimetableSubjectRequestDto {
    String subjectName;
    String professor;
    List<ClassInfoDto> classInfoDtoList;
}
