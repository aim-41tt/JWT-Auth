package ru.example.JWT_Auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.example.JWT_Auth.repository.UserRepository;

@Configuration
public class AppConfig {
	private final UserRepository userRepo;

	/**
	 * @param userRepo
	 */
	public AppConfig(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			if (username == null || username.isEmpty()) {
				throw new UsernameNotFoundException("Username cannot be null or empty");
			}
			return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		};
	}

}
