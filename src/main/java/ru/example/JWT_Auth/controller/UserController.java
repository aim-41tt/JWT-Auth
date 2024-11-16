package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.example.JWT_Auth.entity.User;
import ru.example.JWT_Auth.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	/**
	 * @param verifiedService
	 * @param jwtService
	 * @param uuserService
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User userDetails) {
		return ResponseEntity.ok(userDetails);
	}

//	@PutMapping("/me")
//	public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UpdateUserRequest request,
//			@AuthenticationPrincipal JwtUserDetails userDetails) {
//		UserDto updatedUser = userService.updateUser(userDetails.getUsername(), request);
//		return ResponseEntity.ok(updatedUser);
//	}
//
//	@PutMapping("/me/password")
//	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request,
//			@AuthenticationPrincipal JwtUserDetails userDetails) {
//		userService.changePassword(userDetails.getUsername(), request);
//		return ResponseEntity.ok("Password updated successfully");
//	}
}
