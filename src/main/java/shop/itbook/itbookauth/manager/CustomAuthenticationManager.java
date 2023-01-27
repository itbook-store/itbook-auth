package shop.itbook.itbookauth.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.itbook.itbookauth.dto.UserDetailsDto;
import shop.itbook.itbookauth.exception.UsernamePasswordNotMatchException;

/**
 * CustomFilter 를 통해 들어온 인증되지 않은 username, password 를
 * CustomUserDetailsService 에서 갖고온 DB 정보와 일치하는지 확인 후, 일치하다면 SuccessHandler
 * 일치하지 않는다면 FailureHandler로 보내는 역할을 담당하는 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        UserDetails userDetails =
            userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        if (!checkPassword(authentication, userDetails)) {
            throw new UsernamePasswordNotMatchException();
        }

        UserDetailsDto userDetailsDto = new UserDetailsDto(
            Long.valueOf(userDetails.getUsername()),
            String.valueOf(authentication.getPrincipal())
        );

        return new UsernamePasswordAuthenticationToken(
            userDetailsDto,
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );
    }

    private boolean checkPassword(Authentication authentication, UserDetails userDetails) {
        return passwordEncoder.matches((String) authentication.getCredentials(),
            userDetails.getPassword());
    }
}
