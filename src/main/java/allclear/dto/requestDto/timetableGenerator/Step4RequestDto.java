package allclear.dto.requestDto.timetableGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step4RequestDto implements Step3to6requestDto {
    List<Long> subjectIdList;
}
