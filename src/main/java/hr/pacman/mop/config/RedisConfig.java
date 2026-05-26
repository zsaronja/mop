package hr.pacman.mop.config;

import hr.pacman.mop.dto.CartResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CartResponse> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, CartResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // JSON serializer
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(serializer);

        template.setKeySerializer(template.getStringSerializer());

        template.afterPropertiesSet();

        return template;
    }
}