package shop.itbook.itbookauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.itbook.itbookauth.adaptor.RestTemplateAdaptor;
import shop.itbook.itbookauth.filter.CustomAuthenticationFilter;
import shop.itbook.itbookauth.handler.CustomLoginFailureHandler;
import shop.itbook.itbookauth.handler.CustomLoginSuccessHandler;
import shop.itbook.itbookauth.manager.CustomAuthenticationManager;
import shop.itbook.itbookauth.service.CustomUserDetailsService;
import shop.itbook.itbookauth.token.TokenProvider;

/**
 * The type Security config.
 *
 * @author 강명관
 * @since 1.0
 */

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     * @author 강명관
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .anyRequest().permitAll()
            .and()
            .csrf()
            .disable()
            .cors()
            .disable()
            .formLogin()
            .loginProcessingUrl("/auth/login") // 이걸로 처리한다. Controller를 만들지 않고.
            .successHandler(customLoginSuccessHandler(null, null))
            .failureHandler(customFailureHandler())
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DB에 저장된 암호화된 비밀번호를 FrontServer 에서 넘어온 raw 비밀번호와 match 하기위한 PasswordEncoder 입니다.
     *
     * @return BCryptPasswordEncoder
     * @author 강명관
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CustomAuthenticationFilter 에서 사용할 CustomAuthenticationManager 입니다.
     *
     * @param restTemplateAdaptor     restTemplateAdaptor
     * @param requestServerProperties requestServerProperties
     * @return customAuthenticationManager
     * @author 강명관
     */
    @Bean
    public CustomAuthenticationManager customAuthenticationManager(
        RestTemplateAdaptor restTemplateAdaptor, RequestServerProperties requestServerProperties) {
        return new CustomAuthenticationManager(
            new CustomUserDetailsService(restTemplateAdaptor, requestServerProperties),
            passwordEncoder());
    }


    /**
     * JWT 인증을 위한 CustomAuthenticationFilter 입니다.
     *
     * @return customAuthenticationFilter
     * @author 강명관
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter("/auth/login");
        customFilter.setAuthenticationManager(customAuthenticationManager(null, null));
        customFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler(null, null));

        return customFilter;
    }

    /**
     * CustomAuthenticationFilter 에서 사용할 CustomLoginSuccessHandler 입니다.
     *
     * @param redisTemplate redisTemplate
     * @param tokenProvider JWT 토큰을 발행해주는 클래스 입니다.
     * @return customLoginSuccessHandler
     * @author 강명관
     */
    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler(
        RedisTemplate<String, String> redisTemplate, TokenProvider tokenProvider) {
        return new CustomLoginSuccessHandler(redisTemplate, tokenProvider);
    }

    /**
     * CustomAuthenticationFilter 에서 사용할 CustomLoginFailureHandler 입니다.
     *
     * @return customFailureHandler
     * @author 강명관
     */
    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return new CustomLoginFailureHandler();
    }


}
