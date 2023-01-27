package shop.itbook.itbookauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import shop.itbook.itbookauth.token.TokenProvider;

/**
 * 로그인 성공에 대한 처리를 담당하는 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisTemplate<String, String> redisTemplate;

    private final TokenProvider tokenProvider;

    private static final String HEADER_ACCESSTOKEN = "Authorization-AccessToken";
    private static final String HEADER_REFRESHTOKEN = "Authorization-RefreshToken";
    private static final String HEADER_NO = "Authorization-MemberNo";
    private static final String HEADER_AUTHORITIES = "Authorities";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException {

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        /* auth redis 토큰 저장 */
        redisTemplate.opsForHash().put("accessToken", authentication.getPrincipal(), accessToken);
        redisTemplate.opsForHash().put("refreshToken", authentication.getPrincipal(), refreshToken);

        response.addHeader(HEADER_ACCESSTOKEN, accessToken);
        response.addHeader(HEADER_REFRESHTOKEN, refreshToken);
        response.addHeader(HEADER_AUTHORITIES,
            new ObjectMapper().writeValueAsString(authentication.getAuthorities()));
        response.addHeader(HEADER_NO, (String) authentication.getPrincipal());
    }
}
