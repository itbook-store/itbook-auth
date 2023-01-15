package shop.itbook.itbookauth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
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
}
