package allclear.dto.requestDto.timetableGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step7RequestDto {
    List<Long> timetableGeneratorSubjectIdList;
}