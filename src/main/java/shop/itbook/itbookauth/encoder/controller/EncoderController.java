package shop.itbook.itbookauth.encoder.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;
import shop.itbook.itbookauth.encoder.dto.request.EncodeRequestDto;
import shop.itbook.itbookauth.encoder.dto.response.EncodeResponseDto;
import shop.itbook.itbookauth.encoder.service.EncoderService;

/**
 * 인증 정책에 따른 비밀번호를 인코딩하기 위한 컨트롤러 입니다.
 *
 * @author 강명관
 * @since 1.0
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/encode")
public class EncoderController {

    private final EncoderService encoderService;

    private static final String RESULT_MESSAGE = "비밀번호 인코딩에 성공하였습니다.";

    /**
     * 비밀번호 인코딩을 담당하는 API 입니다.
     *
     * @param encodeRequestDto rawPaasword
     * @return 공용 응답객체, 암호화된 비밀번호
     */
    @PostMapping
    public ResponseEntity<CommonResponseBody<EncodeResponseDto>> passwordEncode(
        @Valid @RequestBody EncodeRequestDto encodeRequestDto
    ) {
        String encodedPassword = encoderService.encodeToPassword(encodeRequestDto);

        CommonResponseBody<EncodeResponseDto> commonResponseBody =
            new CommonResponseBody<>(
                new CommonResponseBody.CommonHeader(
                    RESULT_MESSAGE
                ),
                new EncodeResponseDto(encodedPassword)
            );

        return ResponseEntity.ok().body(commonResponseBody);
    }
}
