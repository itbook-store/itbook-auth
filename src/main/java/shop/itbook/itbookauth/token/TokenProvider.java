package shop.itbook.itbookauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import shop.itbook.itbookauth.dto.UserDetailsDto;

/**
 * JWT 토큰 발행을 담당하는 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties("jwt")
public class TokenProvider {

    private String secretKey;
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60L * 3;

    /**
     * JWT 토큰에 대해 발급하는 메서드 입니다.
     *
     * @param authentication 인가된 Authentication 객체 입니다.
     * @param expirationTime JWT 토큰에 대한 만료 시간입니다.
     * @return JWT 토큰
     */
    private String createToken(Authentication authentication, Long expirationTime) {

        Claims claims = Jwts.claims()
            .setIssuedAt(new Date())
            .setExpiration(createExpirationTime(expirationTime));

        UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();

        claims.put("memberNo", userDetailsDto.getMemberNo());
        claims.put("role", authentication.getAuthorities());

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    /**
     * JWT AccessToken을 발급하는 메서드 입니다.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    /**
     * JWT RefreshToken을 발급하는 메서드 입니다.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    /**
     * 토큰의 검증과, 암호화된 토큰을 복호화 하는 메서드 입니다.
     *
     * @param token the token
     * @return the string
     */
    public String validateAndExtract(String token) {

        Jwt jwt = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parse(token);


        log.info("body {}", jwt);
        log.info("jwt.getBody {}", jwt.getBody());

        return (String) jwt.getBody();
    }

    /**
     * 토큰의 만료시간을 계산해주는 메서드 입니다.
     *
     * @param expirationTime 각 토큰에 대한 만료시간 입니다.
     * @return 현재 시간부터 만료시간을 더한 Date 입니다.
     */
    public static Date createExpirationTime(Long expirationTime) {
        return Date.from(ZonedDateTime.now().plusSeconds(expirationTime).toInstant());
    }

    /**
     * AccessToken 에 대한 만료 시간을 얻어어는 메서드입니다.
     *
     * @return AccessToken ExpirationTime
     */
    public Date getAccessTokenExpirationTime() {
        return createExpirationTime(ACCESS_TOKEN_EXPIRATION_TIME);
    }

    /**
     * RefreshToken 에 대한 만료 시간을 얻어오는 메서드입니다.
     *
     * @return RefreshToken ExpirationTime
     */
    public Date getRefreshTokenExpirationTime() {
        return createExpirationTime(REFRESH_TOKEN_EXPIRATION_TIME);
    }

}
