package all.clear.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LoginRequesetDto {
    @NotBlank(message = "ID를 입력해 주세요.")
    private Long appId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String appPassword;

}
