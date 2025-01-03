package ru.example.JWT_Auth.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.example.JWT_Auth.model.enums.Role;

public class UserUpdateRequest {
	@NotBlank(message = "Username cannot be blank")
	@Size(max = 30, message = "Username must be less than 30 characters")
	private String username;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid email format")
	private String email;

	private Role role;

	// Геттеры и сеттеры

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}