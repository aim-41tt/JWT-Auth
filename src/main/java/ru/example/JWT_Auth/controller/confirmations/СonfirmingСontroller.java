package ru.example.JWT_Auth.controller.confirmations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.service.confirmations.RedisVerificationService;

@RestController
@RequestMapping("/api/v1/Сonfirming")
public class СonfirmingСontroller {

	@Autowired
	private RedisVerificationService verificationService;

	@GetMapping("/{token}")
	public ResponseEntity<?> сheckToken(@AuthenticationPrincipal User userDetails, @PathVariable String token) {
		return ResponseEntity.ok(verificationService.verifyToken(token));
	}

}
