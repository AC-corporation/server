package allclear.domain.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    @Column(name ="refresh_token_id")
    private Long id;

    @Column(name="access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;


}
