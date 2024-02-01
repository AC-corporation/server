package allclear.dto.requestDto.member;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupRequestDto {
    private String email;
    private String password;
    private String usaintId;
    private String usaintPassword;

    @NotBlank(message = "권한을 입력해주세요 USER | ADMIN")
    private String role; // USER 혹은 ADMIN 으로
}
