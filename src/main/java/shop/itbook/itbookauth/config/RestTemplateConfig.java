package shop.itbook.itbookauth.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정을 하고 전역으로 사용하기 위한 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate을 설정하고 Bean으로 등록하는 메서드 입니다.
     *
     * @return 설정된 RestTemplate 객체를 반환합니다.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .setReadTimeout(Duration.ofSeconds(3L))
            .setConnectTimeout(Duration.ofSeconds(3L))
            .build();
    }
}