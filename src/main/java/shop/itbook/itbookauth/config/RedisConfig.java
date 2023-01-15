package shop.itbook.itbookauth.config;

import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis를 사용하기 위한 기본 설정입니다.
 *
 * @author 강명관
 * @since 1.0
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig implements BeanClassLoaderAware {
    private String host;
    private int port;
    private String password;
    private int database;
    private ClassLoader classLoader;

    private Long accessTokenExpirationTime;

    private Long refreshTokenExpirationTime;

    /**
     * Redis를 게정정보를 통해 연결시키는 @Bean
     *
     * @return the redis connection factory
     * @author 강명관 *
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(this.host);
        configuration.setPort(this.port);
        configuration.setPassword(this.password);
        configuration.setDatabase(this.database);

        return new LettuceConnectionFactory(configuration);
    }

    /**
     * RedisConnectionFactory 를 통해 생성된 Redis를 사용할 수 있게 해주는 @Bean
     *
     * @return the redis template
     * @author 강명관 *
     */
    @SuppressWarnings("java:S1452") // 레디스의 key value의 타입을 자유롭게 지정하기 위함.
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());


//        redisTemplate.expire("accessToken".getBytes(),
//            accessTokenExpirationTime, TimeUnit.SECONDS);
//        redisTemplate.expire("refreshToken".getBytes(),
//            refreshTokenExpirationTime, TimeUnit.SECONDS);

        return redisTemplate;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
