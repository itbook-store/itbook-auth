package shop.itbook.itbookauth.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import shop.itbook.itbookauth.dto.TokenDto;
import shop.itbook.itbookauth.dto.UserDetailsDto;
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

    private static final String HEADER_AUTHORITIES = "Authorities";
    private static final String HEADER_USER_DETAIL = "Authorities-UserDetails";
    private static final String HEADER_TOKEN = "Authorities-Token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException {

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        TokenDto tokenDto = new TokenDto(
            accessToken,
            refreshToken,
            tokenProvider.getAccessTokenExpirationTime(),
            tokenProvider.getRefreshTokenExpirationTime()
        );

        UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
        String memberNo = String.valueOf(userDetailsDto.getMemberNo());

        redisTemplate.opsForHash().put("accessToken", memberNo, accessToken);
        redisTemplate.opsForHash().put("refreshToken", memberNo, refreshToken);

        response.addHeader(HEADER_USER_DETAIL, objectToJsonString(authentication.getPrincipal()));
        response.addHeader(HEADER_TOKEN, objectToJsonString(tokenDto));
        response.addHeader(HEADER_AUTHORITIES, objectToJsonString(authentication.getAuthorities()));

    }

    /**
     * 객체를 httpHeader에 보내기 위한 파싱 메서드입니다.
     *
     * @param object json으로 변환하려는 Object
     * @return jsonString
     * @throws JsonProcessingException json parsing 에러
     */
    private String objectToJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
