package allclear.dto.requestDto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class EmailAuthRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    String email;
}
