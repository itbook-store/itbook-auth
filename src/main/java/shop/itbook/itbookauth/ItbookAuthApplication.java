package shop.itbook.itbookauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import shop.itbook.itbookauth.config.RedisConfig;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ItbookAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItbookAuthApplication.class, args);
    }

}
