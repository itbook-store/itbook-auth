package shop.itbook.itbookauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 로그인 실패시 발생하는 Exception 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernamePasswordNotMatchException extends RuntimeException {

    private static final String MESSAGE = "아이디 혹은 비밀번호가 틀렸습니다.";

    public UsernamePasswordNotMatchException() {
        super(MESSAGE);
    }
}
