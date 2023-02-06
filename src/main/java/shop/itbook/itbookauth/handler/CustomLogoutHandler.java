package shop.itbook.itbookauth.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 로그아웃시 저장되어 있던 JWT 토큰을 BlackList로 관리하고, 발급되었던 토큰을 삭제하는 핸들러 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACK_LIST = "BLACK_LIST";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {

        String memberNo = request.getHeader("Auth-MemberNo");

        redisTemplate.opsForSet().add(
            BLACK_LIST,
            (String) redisTemplate.opsForHash().get("accessToken", memberNo)
        );

        redisTemplate.opsForSet().add(
            BLACK_LIST,
            (String) redisTemplate.opsForHash().get("refreshToken", memberNo)
        );

        redisTemplate.opsForHash().delete("accessToken", memberNo);
        redisTemplate.opsForHash().delete("refreshToken", memberNo);
    }
}
