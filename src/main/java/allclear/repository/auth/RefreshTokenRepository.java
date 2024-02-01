package allclear.repository.auth;

import allclear.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByRefreshToken(String refreshToken);
}
