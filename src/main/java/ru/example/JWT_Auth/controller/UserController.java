package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.request.UserUpdateRequest;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetUserPassword;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
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

	/**
	 * Получение текущего пользователя.
	 *
	 * @param userDetails данные текущего пользователя из контекста безопасности.
	 * @return объект пользователя без поля password.
	 */
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User userDetails) {
		// Обнуляем пароль перед отправкой данных
		userDetails.setPassword(null);
		return ResponseEntity.ok(userDetails);
	}

	/**
	 * Обновление профиля пользователя.
	 *
	 * @param userDetails   данные текущего пользователя из контекста безопасности.
	 * @param updateRequest объект с обновляемыми данными.
	 * @return обновлённый профиль пользователя.
	 */
	@PutMapping("/update")
	public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal User userDetails,
			@RequestBody @Valid UserUpdateRequest updateRequest) {
		UserDTO updatedUser = userService.updateUserProfile(userDetails.getUsername(), updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

	/**
	 * Отправка запроса на верификацию email пользователя. Метод POST используется,
	 * так как это действие инициирует отправку письма.
	 *
	 * @param userDetails данные текущего пользователя из контекста безопасности.
	 * @return сообщение с инструкцией проверить почту.
	 */
	@PostMapping("/verified")
	public ResponseEntity<String> sendVerificationEmail(@AuthenticationPrincipal User userDetails) {
		userService.verifiedEmailUser(userDetails);
		return ResponseEntity.ok("Проверьте почту для подтверждения email.");
	}

	/**
	 * Сброс пароля пользователя. Метод POST используется, так как это инициирующее
	 * действие.
	 *
	 * @param userDetails   данные текущего пользователя из контекста безопасности.
	 * @param resetPassword объект с данными для сброса пароля.
	 * @return сообщение с инструкцией проверить почту.
	 */
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@AuthenticationPrincipal User userDetails,
			@RequestBody @Valid ResetUserPassword resetPassword) {
		try {
			userService.resetPasswordUser(userDetails, resetPassword);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Ошибка: " + e.getMessage());
		}
		return ResponseEntity.ok("Проверьте почту для дальнейших инструкций по сбросу пароля.");
	}

}
