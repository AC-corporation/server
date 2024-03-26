package allclear.dto.responseDto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReissueToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
