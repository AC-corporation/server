package allclear.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateRequestDto {
    @NotBlank(message = "유저 아이디를 입력해주세요")
    Long userId;
    @NotBlank(message = "유세인트 아이디를 입력해주세요")
    String usaintId;
    @NotBlank(message = "유세인트 비밀번호를 입력해주세요")
    String usaintPassword;
}
