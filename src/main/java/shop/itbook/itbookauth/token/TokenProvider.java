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
            .setExpiration(Date.from(
                ZonedDateTime.now().plusSeconds(expirationTime).toInstant()));

        claims.put("principal", authentication.getPrincipal());
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

}
