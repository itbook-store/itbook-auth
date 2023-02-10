package shop.itbook.itbookauth.encoder.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인코딩 되지 않은 비밀번호 요청을 받는 DTO 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EncodeRequestDto {

    @NotNull(message = "비밀번호는 null일 수 없습니다.")
    private String rawPassword;
}
