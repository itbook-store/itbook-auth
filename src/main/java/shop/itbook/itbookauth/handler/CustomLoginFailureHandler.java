package shop.itbook.itbookauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;

/**
 * 로그인 실패에 대한 처리를 담당하는 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private static final String MESSAGE = "아이디 혹은 비밀번호가 틀렸습니다.";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
        throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        CommonResponseBody<Void> commonResponseBody = new CommonResponseBody<>(
            new CommonResponseBody.CommonHeader(MESSAGE), null
        );

        response.getOutputStream()
            .println(new ObjectMapper().writeValueAsString(commonResponseBody));
    }
}
