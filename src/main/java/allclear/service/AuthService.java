package allclear.service;

import static allclear.global.exception.code.GlobalErrorCode._INVALID_REFRESHTOKEN;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import allclear.domain.auth.RefreshToken;
import allclear.dto.responseDto.jwt.JwtToken;
import allclear.dto.responseDto.jwt.ReissueToken;
import allclear.global.exception.GlobalException;
import allclear.global.jwt.JwtTokenProvider;
import allclear.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public ReissueToken refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new GlobalException(_INVALID_REFRESHTOKEN);
        }
        RefreshToken alreadyToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (alreadyToken != null) {
            Authentication authentication =
                    jwtTokenProvider.getAuthentication(alreadyToken.getAccessToken());
            JwtToken newJwtToken =
                    jwtTokenProvider.generateToken(
                            authentication, alreadyToken.getMember().getMemberId());
            ReissueToken reissueToken =
                    ReissueToken.builder()
                            .accessToken(newJwtToken.getAccessToken())
                            .refreshToken(newJwtToken.getRefreshToken())
                            .grantType(newJwtToken.getGrantType())
                            .build();
            alreadyToken.updateRefreshToken(
                    newJwtToken.getAccessToken(), newJwtToken.getRefreshToken());
            return reissueToken;
        } else {
            throw new GlobalException(_INVALID_REFRESHTOKEN);
        }
    }
}
