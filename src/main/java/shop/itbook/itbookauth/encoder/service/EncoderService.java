package shop.itbook.itbookauth.encoder.service;

import shop.itbook.itbookauth.encoder.dto.request.EncodeRequestDto;

/**
 * 인증 정책에 따른 비밀번호을 인코딩하기 위한 서비스 인터페이스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
public interface EncoderService {

    /**
     * BCrypt Password Encoder를 통해 비밀번호를 암호화 해주는 메서드 입니다.
     *
     * @param encodeRequestDto rawPassword dto
     * @return encodedPassword
     */
    String encodeToPassword(EncodeRequestDto encodeRequestDto);
}
