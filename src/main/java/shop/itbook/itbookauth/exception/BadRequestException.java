package shop.itbook.itbookauth.exception;

/**
 * 잘못된 요청에 대한 Exception
 *
 * @author 강명관
 * @since 1.0
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
