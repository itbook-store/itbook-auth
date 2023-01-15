package shop.itbook.itbookauth.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.itbook.itbookauth.adaptor.RestTemplateAdaptor;
import shop.itbook.itbookauth.config.RequestServerProperties;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;
import shop.itbook.itbookauth.dto.response.MemberInfoResponseDto;
import shop.itbook.itbookauth.exception.UsernamePasswordNotMatchException;

/**
 * RestTemplate 을 이용하여 ShopServer 에서 회원 정보를 가져오기 위한 CustomUserDetailsService 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplateAdaptor adaptor;

    private final RequestServerProperties requestServerProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ResponseEntity<CommonResponseBody<MemberInfoResponseDto>> getMemberInfo =
            adaptor.getMemberInfoAdaptor(
                requestServerProperties.getServer() + "/api/service/members?memberId=" + username
            );

        if (Objects.isNull(getMemberInfo.getBody())) {
            throw new UsernamePasswordNotMatchException();
        }

        if (!getSuccessfulSign(getMemberInfo)) {
            throw new UsernamePasswordNotMatchException();
        }

        log.info("getSuccessfulSign {}", getSuccessfulSign(getMemberInfo));
        List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new User(
            getMemberInfo.getBody().getResult().getMemberId(),
            getMemberInfo.getBody().getResult().getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    private static Boolean getSuccessfulSign(
        ResponseEntity<CommonResponseBody<MemberInfoResponseDto>> getMemberInfo) {
        return getMemberInfo.getBody().getHeader().getIsSuccessful();
    }
}
