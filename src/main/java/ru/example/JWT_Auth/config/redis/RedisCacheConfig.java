package ru.example.JWT_Auth.config.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	@Value("${TTL_REDIS_CACHE}")
	private static final Integer TTL = 10;	// Время жизни кэша 10 минут
	
    // Настраиваем RedisTemplate для работы с ключами и значениями
    @Bean
    protected RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Сериализуем ключи в строку
        template.setKeySerializer(new StringRedisSerializer());
        // Сериализуем значения в JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    // Настраиваем менеджер кэша для автоматического кэширования
    @Bean
    protected RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(TTL)); 

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .enableStatistics() // Включаем статистику кэша
                .build();
    }
}
