package ru.example.JWT_Auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.example.JWT_Auth.DTO.AuthenticationRequest;
import ru.example.JWT_Auth.DTO.RegisterRequest;
import ru.example.JWT_Auth.DTO.authenticationResponse;
import ru.example.JWT_Auth.service.AuthenticationService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthenticationService service;

	public AuthController(AuthenticationService service) {
		this.service = service;
	}

	@PostMapping("/register")
	public ResponseEntity<authenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<authenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.Authenticate(request));
	}

}
