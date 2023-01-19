package shop.itbook.itbookauth.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import shop.itbook.itbookauth.util.ResponseChecker;

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

    private static final String GET_MEMBER_API_PREFIX = "/api/service/members?memberId=";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ResponseEntity<CommonResponseBody<MemberInfoResponseDto>> getMemberInfo =
            adaptor.getMemberInfoAdaptor(
                requestServerProperties.getServer() + GET_MEMBER_API_PREFIX + username
            );

        if (Objects.isNull(getMemberInfo.getBody())) {
            throw new UsernamePasswordNotMatchException();
        }

        ResponseChecker.checkFail(
            getMemberInfo.getStatusCode(),
            Objects.requireNonNull(getMemberInfo.getBody()).getHeader()
        );

        List<SimpleGrantedAuthority> authorities = getMemberInfo.getBody().getResult().getRoleList()
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new User(
            getMemberInfo.getBody().getResult().getMemberId(),
            getMemberInfo.getBody().getResult().getPassword(),
            authorities
        );
    }
}
