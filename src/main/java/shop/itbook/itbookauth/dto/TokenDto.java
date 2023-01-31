package shop.itbook.itbookauth.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰에 관한 정보들을 Front Server 에 넘겨주기 위한 DTO 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpirationTime;
    private Date refreshTokenExpirationTime;
}
