package shop.itbook.itbookauth.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Authentication Token 의 Principal 에 저장할 Dto 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto implements Serializable {
    private Long memberNo;
    private String memberId;
}
