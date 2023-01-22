package shop.itbook.itbookauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisTemplate<String, String> redisTemplate;

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException {

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        String uuid = UUID.randomUUID().toString();

        redisTemplate.opsForHash().put("refreshToken", uuid, refreshToken);

        response.addHeader("Authorization", accessToken);
        response.addHeader("Authorities",
            new ObjectMapper().writeValueAsString(authentication.getAuthorities()));
        response.addHeader("UUID", uuid);
    }
}
