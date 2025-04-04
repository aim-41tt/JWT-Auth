package ru.example.JWT_Auth.DTO.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;

public class AdminUserDTO {
	private Long id;
	private String username;
	private String password;
	private String email;
	private Boolean verified;
	private Role role;
	private Boolean locked;

	/**
	 * @param id
	 * @param username
	 * @param password
	 * @param email
	 * @param verified
	 * @param role
	 * @param locked
	 */
	public AdminUserDTO(Long id, String username, String password, String email, Boolean verified, Role role,
			Boolean locked) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.verified = verified;
		this.role = role;
		this.locked = locked;
	}

	public AdminUserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = null;
		this.email = user.getEmail();
		this.verified = user.getVerified();
		this.role = user.getRole();
		this.locked = user.getLocked();
	}

	/**
	 * @param username
	 * @param email
	 * @param verified
	 * @param role
	 * @param locked
	 */
	public AdminUserDTO(String username, String email, Boolean verified, Role role, Boolean locked) {
		this.username = username;
		this.email = email;
		this.verified = verified;
		this.role = role;
		this.locked = locked;
	}

	@JsonIgnore
	public User getUser() {
		return new User(this);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	/**
	 * @return the locked
	 */
	public Boolean getLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

}
