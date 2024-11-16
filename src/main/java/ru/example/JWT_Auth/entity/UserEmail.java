package ru.example.JWT_Auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEmail {
	private String email;
	private Boolean verified;

	/**
	 * 
	 */
	public UserEmail(User user) {
		this.email = user.getEmail();
		this.verified = user.getVerified();
	}

	public UserEmail() {
	}

	/**
	 * @param email
	 * @param verified
	 */
	public UserEmail(String email, Boolean verified) {
		this.email = email;
		this.verified = verified;
	}

	/**
	 * @param email verified = false
	 */
	public UserEmail(String email) {
		this.email = email;
		this.verified = false;
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

}
