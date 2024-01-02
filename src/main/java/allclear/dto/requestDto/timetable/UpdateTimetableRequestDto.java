package allclear.dto.requestDto.timetable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateTimetableRequestDto {
    private String tableName;
    private Integer tableYear; //학년도
    private Integer semester; //학기
}
