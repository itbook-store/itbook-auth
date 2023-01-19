package shop.itbook.itbookauth.exception;

/**
 * API 통신에 대한 응답이 제대로 오지 않을 경우의 Exception
 *
 * @author 강명관
 * @since 1.0
 */
public class RestApiServerException extends RuntimeException {

    public RestApiServerException(String message) {
        super(message);
    }
}
