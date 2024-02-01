package allclear.dto.requestDto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReissueRequestDto {
    String refreshToken;
    String accessToken;
}
