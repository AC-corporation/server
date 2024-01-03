package allclear.dto.requestDto.member;


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
}
