package allclear.dto.requestDto.timetable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CreateTimetableRequestDto {
    @NotBlank(message = "시간표 이름을 입력해주세요")
    private String tableName;
    @NotBlank(message = "학년도를 입력해주세요")
    private Integer tableYear; //학년도
    @NotBlank(message = "학기를 입력해주세요")
    private Integer semester; //학기
    private List<Long> subjectIdList;
}
