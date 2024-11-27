package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.request.UserUpdateRequest;
import ru.example.JWT_Auth.entity.User;
import ru.example.JWT_Auth.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	

	/**
	 * @param userService
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User userDetails) {
		userDetails.setPassword(null);
		return ResponseEntity.ok(userDetails);
	}

	/**
	 * Обновить профиль пользователя.
	 *
	 * @param userDetails   данные текущего пользователя из контекста безопасности
	 * @param updateRequest объект с обновляемыми данными
	 * @return обновлённый профиль пользователя
	 */
	@PutMapping("/update")
	public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal User userDetails,
			@RequestBody @Valid UserUpdateRequest updateRequest) {
		UserDTO updatedUser = userService.updateUserProfile(userDetails.getUsername(), updateRequest);
		return ResponseEntity.ok(updatedUser);
	}
	
	@PutMapping("/verified")
	public ResponseEntity<String> updateProfile(@AuthenticationPrincipal User userDetails) {
		userService.verifiedEmailUser(userDetails);
		return ResponseEntity.ok("прооваверте почту");
	}

}
