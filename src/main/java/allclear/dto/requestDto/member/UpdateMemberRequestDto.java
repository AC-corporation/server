package allclear.dto.requestDto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateMemberRequestDto {
    @NotBlank(message = "유세인트 아이디를 입력해주세요")
    String usaintId;
    @NotBlank(message = "유세인트 비밀번호를 입력해주세요")
    String usaintPassword;
}
