package ru.example.JWT_Auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminService {

	private static final Logger log = LoggerFactory.getLogger(AdminService.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public AdminUserDTO getUserById(UUID id) {
		log.debug("Requesting user by ID: {}", id);
		return userRepository.findById(id).map(User::getAdminUserDTO).orElseThrow(() -> {
			log.error("User with ID {} not found", id);
			return new IllegalArgumentException("User not found: " + id);
		});
	}

	@Transactional(readOnly = true)
	public List<AdminUserDTO> getUsersByid(List<UUID> ids) {
		log.debug("Requesting users by IDs: {}", ids);
		return userRepository.findAllById(ids).stream().map(User::getAdminUserDTO).toList();
	}

	public AdminUserDTO saveUser(AdminUserDTO aUserDTO) {
		log.info("Saving new user: {}", aUserDTO.getUsername());
		User user = new User(aUserDTO);
		user.setPassword(passwordEncoder.encode(aUserDTO.getPassword()));
		return userRepository.save(user).getAdminUserDTO();
	}

	public List<AdminUserDTO> saveUsers(List<AdminUserDTO> users) {
		log.info("Saving list of users. Count: {}", users.size());
		List<User> entities = users.stream().map(dto -> {
			User u = dto.getUser();
			u.setPassword(passwordEncoder.encode(dto.getPassword()));
			return u;
		}).toList();
		return userRepository.saveAll(entities).stream().map(User::getAdminUserDTO).toList();
	}

	public AdminUserDTO updateUser(UUID id, AdminUserDTO userDto) {
		log.info("Updating user with ID {}", id);
		User existingUser = userRepository.findById(id).orElseThrow(() -> {
			log.error("User with ID {} not found for update", id);
			return new IllegalArgumentException("User not found: " + id);
		});

		if (userDto.getUsername() != null) {
			log.debug("Updating username: {}", userDto.getUsername());
			existingUser.setUsername(userDto.getUsername());
		}
		if (userDto.getEmail() != null) {
			log.debug("Updating email: {}", userDto.getEmail());
			existingUser.setEmail(userDto.getEmail());
			existingUser.setVerified(false);
		}
		if (userDto.getRole() != null) {
			log.debug("Updating role: {}", userDto.getRole());
			existingUser.setRole(userDto.getRole());
		}

		if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
			log.debug("Updating password for user {}", id);
			existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}

		return userRepository.save(existingUser).getAdminUserDTO();
	}

	public void deleteUser(UUID id) {
		log.warn("Deleting user with ID {}", id);
		if (!userRepository.existsById(id)) {
			log.error("Attempted to delete non-existing user: {}", id);
			throw new IllegalArgumentException("User not found: " + id);
		}
		userRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public AdminUserDTO updateUserRole(UUID id, Role role) {
		log.info("Updating role of user with ID {} to {}", id, role);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.error("User with ID {} not found for role update", id);
			return new IllegalArgumentException("User not found: " + id);
		});
		user.setRole(role);
		return userRepository.save(user).getAdminUserDTO();
	}

	@Transactional
	public AdminUserDTO blockUser(UUID id) {
		log.info("Blocking user with ID {}", id);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.error("User with ID {} not found for blocking", id);
			return new IllegalArgumentException("User not found: " + id);
		});
		if (!Boolean.TRUE.equals(user.getLocked())) {
			log.debug("User {} was unlocked, now blocking", id);
			user.setLocked(true);
			userRepository.save(user);
		}
		return user.getAdminUserDTO();
	}

	@Transactional
	public AdminUserDTO unblockUser(UUID id) {
		log.info("Unblocking user with ID {}", id);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.error("User with ID {} not found for unblocking", id);
			return new IllegalArgumentException("User not found: " + id);
		});
		if (Boolean.TRUE.equals(user.getLocked())) {
			log.debug("User {} was blocked, now unblocking", id);
			user.setLocked(false);
			userRepository.save(user);
		}
		return user.getAdminUserDTO();
	}

	@Transactional(readOnly = true)
	public List<AdminUserDTO> getAllUsers() {
		log.debug("Requesting all users");
		return userRepository.findAll().stream().map(User::getAdminUserDTO).toList();
	}

}
