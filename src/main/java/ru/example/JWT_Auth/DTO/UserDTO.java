package ru.example.JWT_Auth.DTO;

import ru.example.JWT_Auth.model.enums.Role;

public class UserDTO {
	private Long id;
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
	public UserDTO(Long id, String username, String email, Boolean verified, Role role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.verified = verified;
		this.role = role;
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
