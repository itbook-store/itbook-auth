package shop.itbook.itbookauth.reissue.service;

import shop.itbook.itbookauth.dto.TokenDto;

/**
 * 토큰 재발급에 대한 비지니스로직을 담당하는 서비스 인터페이스입니다.
 *
 * @author 강명관
 * @since 1.0
 */
public interface TokenService {

    /**
     * 토큰 재발급에 대한 비지니스로직을 담당하는 메서드 입니다.
     *
     * @param refreshToken 사용자에게 발급되었던 refreshToken
     * @return 새롭게 발급되는 TokenDto
     */
    TokenDto reissueJwtToken(String refreshToken);
}
