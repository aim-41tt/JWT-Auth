package ru.example.JWT_Auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AdminUserDTO getUserById(UUID id) {
		return userRepository.findById(id).orElseGet(null).getAdminUserDTO();
	}

	public List<AdminUserDTO> getUsersByid(List<UUID> ids) {
		return userRepository.findAllById(ids).stream().map(t -> t.getAdminUserDTO()).toList();
	}

	public AdminUserDTO saveUser(AdminUserDTO user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user.getUser()).getAdminUserDTO();
	}

	public List<AdminUserDTO> saveUsers(List<AdminUserDTO> users) {
		return userRepository.saveAll(users.stream().map(u -> u.getUser()).toList()).stream()
				.map(ua -> ua.getAdminUserDTO()).toList();
	}

	public AdminUserDTO updateUser(Long id, AdminUserDTO userAdm) {
		User user = new User(userAdm);
		if (user.Valid()) {
			return userRepository.save(user).getAdminUserDTO();
		}

		return userAdm;
	}

	public void deleteUser(UUID id) {
		userRepository.delete(userRepository.findById(id).get());
	}

	public AdminUserDTO updateUserRole(UUID id, Role role) {
		Optional<User> userOpt = userRepository.findById(id);
		User user = null;
		if (userOpt.isPresent()) {
			user = userOpt.get();
			user.setRole(role);
			userRepository.save(user);
		}
		return user.getAdminUserDTO();
	}

	public AdminUserDTO blockUser(UUID id) {
		Optional<User> userOpt = userRepository.findById(id);
		User user = null;
		if (userOpt.isPresent() && !userOpt.get().getLocked()) {
			user = userOpt.get();
			user.setLocked(true);
		}
		return user.getAdminUserDTO();
	}

	public AdminUserDTO unblockUser(UUID id) {
		Optional<User> userOpt = userRepository.findById(id);
		User user = null;
		if (userOpt.isPresent() && userOpt.get().getLocked()) {
			user = userOpt.get();
			user.setLocked(false);
		}
		return user.getAdminUserDTO();
	}

	public List<AdminUserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users == null || users.isEmpty()) {
			return new ArrayList<AdminUserDTO>();
		}
		return users.stream().map(u -> u.getAdminUserDTO()).toList();
	}

}
