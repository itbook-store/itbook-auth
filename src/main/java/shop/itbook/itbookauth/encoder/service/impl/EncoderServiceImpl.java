package shop.itbook.itbookauth.encoder.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.itbook.itbookauth.encoder.dto.request.EncodeRequestDto;
import shop.itbook.itbookauth.encoder.service.EncoderService;

/**
 * 인증 정책에 따른 비밀번호을 인코딩하기 위한 서비스 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class EncoderServiceImpl implements EncoderService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodeToPassword(EncodeRequestDto encodeRequestDto) {
        return passwordEncoder.encode(encodeRequestDto.getRawPassword());
    }
}
