package ru.example.JWT_Auth.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
@Transactional
public class AdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public AdminUserDTO getUserById(UUID id) {
		return userRepository.findById(id).map(User::getAdminUserDTO)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
	}

	@Transactional(readOnly = true)
	public List<AdminUserDTO> getUsersByid(List<UUID> ids) {
		return userRepository.findAllById(ids).stream().map(User::getAdminUserDTO).toList();
	}

	@Transactional(readOnly = true)
	public AdminUserDTO saveUser(AdminUserDTO aUserDTO) {
		User user = new User(aUserDTO);
		user.setPassword(passwordEncoder.encode(aUserDTO.getPassword()));
		return userRepository.save(user).getAdminUserDTO();
	}

	@Transactional(readOnly = true)
	public List<AdminUserDTO> saveUsers(List<AdminUserDTO> users) {
		List<User> entities = users.stream().map(dto -> {
			User u = dto.getUser();
			u.setPassword(passwordEncoder.encode(dto.getPassword()));
			return u;
		}).toList();
		return userRepository.saveAll(entities).stream().map(User::getAdminUserDTO).toList();
	}

	@Transactional(readOnly = true)
	public AdminUserDTO updateUser(UUID id, AdminUserDTO userDto) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		if (userDto.getUsername() != null) {
			existingUser.setUsername(userDto.getUsername());
		}
		if (userDto.getEmail() != null) {
			existingUser.setEmail(userDto.getEmail());
			existingUser.setVerified(false);
		}
		if (userDto.getRole() != null) {
			existingUser.setRole(userDto.getRole());
		}

		if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}

		return userRepository.save(existingUser).getAdminUserDTO();
	}

	public void deleteUser(UUID id) {
		if (!userRepository.existsById(id)) {
			throw new IllegalArgumentException("User not found: " + id);
		}
		userRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public AdminUserDTO updateUserRole(UUID id, Role role) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		user.setRole(role);
		return userRepository.save(user).getAdminUserDTO();
	}

	@Transactional(readOnly = true)
	public AdminUserDTO blockUser(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		if (!Boolean.TRUE.equals(user.getLocked())) {
			user.setLocked(true);
			userRepository.save(user);
		}
		return user.getAdminUserDTO();
	}

	@Transactional(readOnly = true)
	public AdminUserDTO unblockUser(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		if (Boolean.TRUE.equals(user.getLocked())) {
			user.setLocked(false);
			userRepository.save(user);
		}
		return user.getAdminUserDTO();
	}

	@Transactional(readOnly = true)
	public List<AdminUserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(User::getAdminUserDTO).toList();
	}

}
