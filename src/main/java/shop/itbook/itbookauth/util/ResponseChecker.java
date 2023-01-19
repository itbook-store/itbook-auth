package shop.itbook.itbookauth.util;

import org.springframework.http.HttpStatus;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;
import shop.itbook.itbookauth.exception.BadRequestException;
import shop.itbook.itbookauth.exception.RestApiServerException;
import shop.itbook.itbookauth.exception.UsernamePasswordNotMatchException;

/**
 * API 응답 ResponseDto를 검사하는 클래스 입니다.
 *
 * @author 최겸준
 * @since 1.0
 */
public class ResponseChecker {

    private ResponseChecker() {
    }

    /**
     * API 통신 시 응답에 대한 검사를 위한 메서드 입니다.
     *
     * @param statusCode 응답에 대한 상태코드입니다.
     * @param header     응답에 대한 메세지르 담고 있는 클래스 입니다.
     */
    public static void checkFail(HttpStatus statusCode, CommonResponseBody.CommonHeader header) {

        if (statusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new UsernamePasswordNotMatchException();
        }

        if (statusCode.equals(HttpStatus.BAD_REQUEST)) {
            throw new BadRequestException(header.getResultMessage());
        }

        if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            throw new RestApiServerException(header.getResultMessage());
        }
    }
}