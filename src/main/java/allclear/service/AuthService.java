package allclear.service;

import allclear.domain.auth.RefreshToken;
import allclear.dto.responseDto.jwt.JwtToken;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.jwt.JwtTokenProvider;
import allclear.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static allclear.global.exception.code.GlobalErrorCode._INVALID_REFRESHTOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService{
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtToken refreshAccessToken(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken))
        {
            throw new GlobalException(_INVALID_REFRESHTOKEN);
        }
        RefreshToken alreadyToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(alreadyToken != null) {
            Authentication authentication = jwtTokenProvider.getAuthentication(alreadyToken.getAccessToken());
            JwtToken newJwtToken = jwtTokenProvider.generateToken(authentication);
            alreadyToken.updateRefreshToken(newJwtToken.getAccessToken(), newJwtToken.getRefreshToken());
            return newJwtToken;
        }
        else{
            throw new GlobalException(_INVALID_REFRESHTOKEN);
        }
    }
}