package ru.example.JWT_Auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.request.AuthenticationRequest;
import ru.example.JWT_Auth.DTO.request.RegisterRequest;
import ru.example.JWT_Auth.DTO.response.AuthenticationResponse;
import ru.example.JWT_Auth.config.JwtService;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.repository.UserRepository;

/**
 * 
 */
@Service
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final VerifiedService verifiedService;

	/**
	 * @param repository
	 * @param passwordEncoder
	 * @param jwtService
	 * @param authenticationManager
	 * @param verifiedService
	 */
	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager, VerifiedService verifiedService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.verifiedService = verifiedService;
	}

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) throws Exception {
		if (repository.findByUsername(request.getUsername()).isPresent()) {
			throw new Exception("User with username " + request.getUsername() + " already exists.");
		}

		User user = new User.Builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.email(request.getEmail()).role(Role.USER)
				.build();

		verifiedService.verifiedByUser(user);

		repository.save(user);
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse.Builder().token(jwtToken).build();
	}

	@Transactional
	public AuthenticationResponse Authenticate(AuthenticationRequest request) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = repository
					.findByUsername(request.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));

			return new AuthenticationResponse.Builder().token(jwtService.generateToken(user)).build();

		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("Authentication failed: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Authentication process encountered an error.", e);
		}
	}

}
