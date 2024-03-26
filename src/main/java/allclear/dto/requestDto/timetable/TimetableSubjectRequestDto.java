package allclear.dto.requestDto.timetable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TimetableSubjectRequestDto {
    String subjectName;
    Long subjectId; // 커스텀 과목: null, 유세인트: 과목 ID
    List<ClassInfoRequestDto> classInfoRequestDtoList;
}
