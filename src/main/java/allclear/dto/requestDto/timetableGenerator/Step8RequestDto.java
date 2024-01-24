package allclear.dto.requestDto.timetableGenerator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step8RequestDto {
    @NotBlank
    Long timetableId;
}
