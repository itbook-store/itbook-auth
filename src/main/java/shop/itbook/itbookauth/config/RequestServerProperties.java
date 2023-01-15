package shop.itbook.itbookauth.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * .properties 파일에 저장된 shop서버를 가져와 사용하기 위한 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@Setter
@ConfigurationProperties(prefix = "itbook")
@Configuration
public class RequestServerProperties {

    private String shopServer;

    public String getServer() {
        return this.shopServer;
    }
}
