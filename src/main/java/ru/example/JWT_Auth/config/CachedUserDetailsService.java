package ru.example.JWT_Auth.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.cahe.UserCacheService;

@Service
public class CachedUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserCacheService userCacheService;

	public CachedUserDetailsService(UserRepository userRepo, UserCacheService userCacheService) {
		this.userRepository = userRepo;
		this.userCacheService = userCacheService;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("Username cannot be null or empty");
		}

		// Сначала ищем пользователя в кэше
		UserDTO cachedUser = userCacheService.getCachedUser(username);
		if (cachedUser != null) {
			return convertToUserDetails(cachedUser);
		}

		// Если пользователя нет в кэше — берём из БД
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Кэшируем пользователя без пароля
		userCacheService.cacheUser(new UserDTO(user));

		return user;
	}
	
	@Transactional
	public UserDetails loadUserFullByUsername (String username) throws UsernameNotFoundException {
		User user = (User) loadUserByUsername(username);
		String password = user.getPassword();
		if (password == null || password.isEmpty()) {
			Optional<String> passwordOpt = userRepository.findPasswordById(user.getId());
			if (passwordOpt.isPresent()) {
				user.setPassword(passwordOpt.get());
			}
		}
		return user;
	}

	private UserDetails convertToUserDetails(UserDTO userDTO) {
		return new User(userDTO);
	}
}
