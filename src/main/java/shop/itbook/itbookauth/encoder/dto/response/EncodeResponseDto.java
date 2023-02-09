package shop.itbook.itbookauth.encoder.dto.response;

import lombok.AllArgsConstructor;

/**
 * 암호화된 비밀번호를 담는 객체 입니다.
 *
 * @author 강명관
 * @since 1.0
 */

@AllArgsConstructor
public class EncodeResponseDto {

    private String encodedPassword;
}
