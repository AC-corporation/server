package allclear.dto.requestDto.timetableGenerator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
