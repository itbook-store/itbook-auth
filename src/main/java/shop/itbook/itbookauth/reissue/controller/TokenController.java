package shop.itbook.itbookauth.reissue.controller;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.itbook.itbookauth.dto.TokenDto;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;
import shop.itbook.itbookauth.exception.InvalidTokenRequestException;
import shop.itbook.itbookauth.reissue.service.TokenService;

/**
 * 토큰 재발급을 담당하는 컨트롤러 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/token/reissue")
public class TokenController {

    private final TokenService tokenService;

    private static final String AUTH_HEADER = "Authorization";

    private static final String HEADER_PREFIX = "Bearer ";

    private static final String RESULT_MESSAGE = "토큰이 재발급 되었습니다.";

    private static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "잘못된 토큰 형식 요청입니다.";


    /**
     * 토큰 재발급을 담당하는 REST Controller 메서드 입니다.
     *
     * @param request request Header 를 통해 refreshToken 을 넘겨 받습니다.
     * @return 새롭게 발급된 TokenDto 입니다.
     */
    @PostMapping
    public ResponseEntity<CommonResponseBody<TokenDto>> jwtReissue(HttpServletRequest request) {

        String authHeader = request.getHeader(AUTH_HEADER);

        if (Objects.isNull(authHeader) || !authHeader.startsWith(HEADER_PREFIX)) {
            throw new InvalidTokenRequestException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }

        String refreshToken = authHeader.substring(7);

        TokenDto tokenDto = tokenService.reissueJwtToken(refreshToken);

        CommonResponseBody<TokenDto> commonResponseBody =
            new CommonResponseBody<>(
                new CommonResponseBody.CommonHeader(
                    RESULT_MESSAGE
                ),
                tokenDto
            );

        return ResponseEntity.ok().body(commonResponseBody);
    }
}
