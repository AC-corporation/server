package allclear.dto.requestDto.timetableGenerator;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Step1RequestDto {
    @NotBlank(message = "학년도를 입력해주세요")
    private Integer tableYear; // 학년도

    @NotBlank(message = "학기를 입력해주세요")
    private Integer semester; // 학기
}
