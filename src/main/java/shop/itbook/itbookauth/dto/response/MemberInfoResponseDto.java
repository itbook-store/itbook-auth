package shop.itbook.itbookauth.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ShopServer로 부터 받아올 회원에 대한 정보 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {
    private Long memberNo;
    private String memberId;
    private String password;
    List<String> roleList;
}
