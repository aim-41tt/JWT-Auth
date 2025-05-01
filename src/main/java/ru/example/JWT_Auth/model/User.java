package ru.example.JWT_Auth.model;

import java.util.Collection;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.enums.Role;

/**
 * Класс User — представляет сущность пользователя.
 *
 * <p>
 * Реализует интерфейс {@link UserDetails} для интеграции с Spring Security.
 * </p>
 *
 * @author aim_41tt
 * @version 1.1
 * @since 10.02.2025
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.UUID)
	private UUID id;

	@Column(unique = true, nullable = false, length = 30)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private Boolean verified;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private Boolean locked = false;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	/**
	 * @param username
	 * @param password
	 * @param email
	 * @param role
	 */
	public User(String username, String password, String email, Role role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	/**
	 * @param id
	 * @param username
	 * @param password
	 * @param email
	 * @param verified
	 * @param role
	 */
	public User(UUID id, String username, String password, String email, Boolean verified, Role role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.verified = verified;
		this.role = role;
	}

	/**
	 * @param id
	 * @param username
	 * @param password
	 * @param email
	 * @param verified
	 * @param role
	 * @param locked
	 */
	public User(UUID id, String username, String password, String email, Boolean verified, Role role, Boolean locked) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.verified = verified;
		this.role = role;
		this.locked = locked;
	}

	/**
	 * @param id
	 * @param username
	 * @param email
	 * @param verified
	 * @param role
	 */
	public User(UUID id, String username, String email, Boolean verified, Role role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.verified = verified;
		this.role = role;
	}

	/**
	 * @param username
	 * @param password
	 * @param email
	 */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public User(UserDTO userDTO, String password) {
		this.id = userDTO.getId();
		this.username = userDTO.getUsername();
		this.password = password;
		this.email = userDTO.getEmail();
		this.verified = userDTO.getVerified();
		this.role = userDTO.getRole();
	}

	public User(AdminUserDTO adminUserDTO) {
		this.id = adminUserDTO.getId();
		this.username = adminUserDTO.getUsername();
		this.password = null;
		this.email = adminUserDTO.getEmail();
		this.verified = adminUserDTO.getVerified();
		this.role = adminUserDTO.getRole();
		this.locked = adminUserDTO.getLocked();
	}

	@JsonIgnore
	public AdminUserDTO getAdminUserDTO() {
		return new AdminUserDTO(this);
	}

	public User() {
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
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	@Override
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

	@Override
	public int hashCode() {
		return Objects.hash(email, id, password, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(password, other.password) && role == other.role
				&& Objects.equals(username, other.username);
	}

	public boolean Valid() {
		return id != null && !username.isEmpty() && role != null && !email.isEmpty();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", verified=" + verified + ", role=" + role + "]";
	}

	public static class Builder {
		private UUID id;
		private String username;
		private String password;
		private String email;
		private Role role;
		private Boolean verified = false;

		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder role(Role role) {
			this.role = role;
			return this;
		}

		public Builder verified(Boolean verified) {
			this.verified = verified;
			return this;
		}

		// Метод для создания объекта User
		public User build() {
			return new User(id, username, password, email, verified, role);
		}
	}

}