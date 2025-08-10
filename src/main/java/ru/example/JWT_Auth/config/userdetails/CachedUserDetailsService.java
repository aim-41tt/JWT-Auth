package ru.example.JWT_Auth.config.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.mapper.user.UserMapper;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.cahe.UserCacheService;

/**
 * @author aim_41tt
 * @version 1.0
 * @since 10.08.2025
 */
@Service("cachedUserDetailsService")
public class CachedUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserCacheService userCacheService;
	private final UserMapper userMapper;

	public CachedUserDetailsService(UserRepository userRepository, UserCacheService userCacheService,
			UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userCacheService = userCacheService;
		this.userMapper = userMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("Username cannot be null or empty");
		}

		UserDTO cachedUser = userCacheService.getCachedUser(username);
		if (cachedUser != null) {
			return userMapper.toUser(cachedUser);
		}

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		userCacheService.cacheUser(userMapper.toUserDTO(user));

		return user;
	}
}
