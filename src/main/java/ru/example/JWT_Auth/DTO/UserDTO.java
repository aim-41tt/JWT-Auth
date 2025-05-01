package ru.example.JWT_Auth.DTO;

import java.util.UUID;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;

public class UserDTO {
	private UUID id;
	private String username;
	private String email;
	private Boolean verified;
	private Role role;

	public UserDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param username
	 * @param email
	 * @param verified
	 * @param role
	 */
	public UserDTO(UUID id, String username, String email, Boolean verified, Role role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.verified = verified;
		this.role = role;
	}

	
	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.verified = user.getVerified();
		this.role = user.getRole();
	}
	/**
	 * @param username
	 * @param email
	 * @param verified
	 * @param role
	 */
	public UserDTO(String username, String email, Boolean verified, Role role) {
		this.username = username;
		this.email = email;
		this.verified = verified;
		this.role = role;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the verified
	 */
	public Boolean getVerified() {
		return verified;
	}

	/**
	 * @param verified the verified to set
	 */
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

}
