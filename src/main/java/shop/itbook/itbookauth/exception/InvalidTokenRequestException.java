package shop.itbook.itbookauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 강명관
 * @since 1.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenRequestException extends RuntimeException {

    public InvalidTokenRequestException(String message) {
        super(message);
    }
}
