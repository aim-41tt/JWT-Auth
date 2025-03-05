package ru.example.JWT_Auth.service.cahe;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.DTO.UserDTO;

@Service
public class UserCacheService {
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * @param redisTemplate
	 */
	public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void cacheUser(UserDTO user) {
			String key = "user:" + user.getUsername();
			redisTemplate.opsForValue().set(key, user);
	}

	public UserDTO getCachedUser(String username) {
			String key = "user:" + username;
			UserDTO user = (UserDTO) redisTemplate.opsForValue().get(key);
			return user;
	}

	public void removeUserFromCache(String username) {
		redisTemplate.delete("user:" + username);
	}
}
