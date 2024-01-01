package allclear.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AddTimetableSubjectRequestDto {
    @NotBlank(message = "과목 Id를 입력해주세요")
    Long subjectId;
}
