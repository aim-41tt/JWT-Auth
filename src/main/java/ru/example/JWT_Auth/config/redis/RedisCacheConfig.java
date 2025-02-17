package ru.example.JWT_Auth.config.redis;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	// Настраиваем соединение с Redis сервером с использованием Lettuce
	@Bean
	protected RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

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
		// Определяем базовую конфигурацию кэша, например, время жизни кэша 10 минут
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(10));
		return RedisCacheManager.builder(connectionFactory).cacheDefaults(redisCacheConfiguration).build();
	}
}
