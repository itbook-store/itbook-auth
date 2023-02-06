package shop.itbook.itbookauth.reissue.service.impl;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import shop.itbook.itbookauth.dto.TokenDto;
import shop.itbook.itbookauth.dto.UserDetailsDto;
import shop.itbook.itbookauth.exception.InvalidTokenRequestException;
import shop.itbook.itbookauth.reissue.service.TokenService;
import shop.itbook.itbookauth.token.TokenProvider;

/**
 * 토큰 재발급에 대한 비지니스로직을 담당하는 서비스 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACK_LIST = "BLACK_LIST";

    private static final String NOT_REGISTRATION_TOKEN_EXCEPTION_MESSAGE = "등록되지 않은 토큰입니다.";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TokenDto reissueJwtToken(String refreshToken) {

        Claims claims = tokenProvider.validateAndExtract(refreshToken);

        String memberNo = String.valueOf(claims.get("memberNo"));

        String oldRefreshToken = (String) redisTemplate.opsForHash().get("refreshToken", memberNo);

        checkValidToken(refreshToken, oldRefreshToken);

        Authentication authenticationToken = createAuthenticationToken(memberNo, claims);

        String newAccessToken = tokenProvider.createAccessToken(authenticationToken);
        String newRefreshToken = tokenProvider.createRefreshToken(authenticationToken);
        Date newAccessTokenExpirationTime = tokenProvider.getAccessTokenExpirationTime();
        Date newRefreshTokenExpirationTime = tokenProvider.getRefreshTokenExpirationTime();

        String oldAccessToken = (String) redisTemplate.opsForHash().get("accessToken", memberNo);

        redisTemplate.opsForSet().add(BLACK_LIST, oldAccessToken);
        redisTemplate.opsForSet().add(BLACK_LIST, oldRefreshToken);

        redisTemplate.opsForHash().put("accessToken", memberNo, newAccessToken);
        redisTemplate.opsForHash().put("refreshToken", memberNo, newRefreshToken);

        return new TokenDto(
            newAccessToken,
            newRefreshToken,
            newAccessTokenExpirationTime,
            newRefreshTokenExpirationTime
        );
    }

    private static void checkValidToken(String refreshToken, String oldRefreshToken) {
        if (!refreshToken.equals(oldRefreshToken)) {
            log.info("여기서 걸리나?");
            log.info("refreshToken {}", refreshToken);
            log.info("oldRefreshToken {}", oldRefreshToken);
            throw new InvalidTokenRequestException(NOT_REGISTRATION_TOKEN_EXCEPTION_MESSAGE);
        }
    }

    private static Authentication createAuthenticationToken(String memberNo,
                                                            Claims claims) {
        return new UsernamePasswordAuthenticationToken(
            new UserDetailsDto(Long.valueOf(memberNo), null),
            null,
            getAuthorityList(claims)
        );
    }

    private static List<SimpleGrantedAuthority> getAuthorityList(Claims claims) {
        return ((List<Map<?, ?>>) claims.get("role")).stream()
            .map(o -> new SimpleGrantedAuthority(o.get("authority").toString()))
            .collect(Collectors.toList());
    }

}
