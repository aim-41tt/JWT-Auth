package ru.example.JWT_Auth.model.email;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.example.JWT_Auth.model.User;

/**
 * @author aim_41tt
 * @since 3 янв. 2025 г.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Email {
	private String email;
	private Boolean verified;

	public Email() {

	}

	/**
	 * @param email
	 */
	public Email(String email) {
		this.email = email;
	}

	public Email(User user) {
		this.email = user.getEmail();
		this.verified = user.getVerified();
	}

	/**
	 * @param email
	 * @param verified
	 */
	public Email(String email, Boolean verified) {
		this.email = email;
		this.verified = verified;
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

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Email)) {
			return false;
		}
		Email other = (Email) obj;
		return Objects.equals(email, other.email);
	}

	/**
	 * @return
	 * @see java.lang.String#isEmpty()
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return email.isEmpty();
	}
}
