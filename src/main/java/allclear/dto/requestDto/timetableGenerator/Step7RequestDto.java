package allclear.dto.requestDto.timetableGenerator;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step7RequestDto {
    private int minCredit;
    private int maxCredit;
    private int minMajorCredit;
    private int maxMajorCredit;
    private List<Long> timetableGeneratorSubjectIdList;
}
