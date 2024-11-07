package ru.example.JWT_Auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.DTO.AuthenticationRequest;
import ru.example.JWT_Auth.DTO.RegisterRequest;
import ru.example.JWT_Auth.DTO.authenticationResponse;
import ru.example.JWT_Auth.config.JwtService;
import ru.example.JWT_Auth.entity.Role;
import ru.example.JWT_Auth.entity.User;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public authenticationResponse register(RegisterRequest request) {
		if (repository.findByUsername(request.getUsername()).isPresent()) {
			return new authenticationResponse.Builder().token("User with username " + request.getUsername() + " already exists.").build();
		}

		var user = new User.Builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.email(request.getEmail())
				.role(Role.USER)
				.build();
		repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		return new authenticationResponse.Builder().token(jwtToken).build();
	}

	public authenticationResponse Authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		var user = repository.findByUsername(request.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));
		
		var jwtToken = jwtService.generateToken(user);
		return new authenticationResponse.Builder().token(jwtToken).build();
	}

}
