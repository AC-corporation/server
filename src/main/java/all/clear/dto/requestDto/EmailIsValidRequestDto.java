package all.clear.dto.requestDto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class EmailIsValidRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    String email;
    @NotBlank(message = "인증 코드를 입력해주세요")
    String code;
}
