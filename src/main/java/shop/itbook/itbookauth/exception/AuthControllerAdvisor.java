package shop.itbook.itbookauth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;

/**
 * 토큰 재발급시에 일어나는 에러를 핸들링 하는 클래스입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@RestControllerAdvice
public class AuthControllerAdvisor {

    /**
     * Auth서버 컨트롤러 단에서 발생하는 Exception을 처리하기위한 메서드 입니다.
     *
     * @param e Exception
     * @return 공용 응답 객체 Status 401
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {
        InvalidTokenRequestException.class,
        JwtException.class,
        ExpiredJwtException.class,
        Exception.class
    })
    public ResponseEntity<CommonResponseBody<Void>> invalidTokenExceptionHandler(
        Exception e) {

        CommonResponseBody<Void> exceptionCommonResponseBody = new CommonResponseBody<>(
            new CommonResponseBody.CommonHeader(
                e.getMessage()), null);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
            .body(exceptionCommonResponseBody);
    }
}
