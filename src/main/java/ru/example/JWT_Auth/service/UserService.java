package ru.example.JWT_Auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.request.UserUpdateRequest;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final VerifiedService verifiedService;

	/**
	 * @param userRepository
	 * @param verifiedService
	 */
	public UserService(UserRepository userRepository, VerifiedService verifiedService) {
		this.userRepository = userRepository;
		this.verifiedService = verifiedService;
	}

	@Transactional
	public UserDTO getUserProfile(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return mapToDto(user);
	}

	@Transactional
	public UserDTO updateUserProfile(String username, UserUpdateRequest updateRequest) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (updateRequest.getUsername() != null) {
			user.setUsername(updateRequest.getUsername());
		}
		if (updateRequest.getEmail() != null) {
			user.setVerified(false);
			user.setEmail(updateRequest.getEmail());
		}

		if (updateRequest.getRole() != null) {
			user.setRole(updateRequest.getRole());
		}

		User updatedUser = userRepository.save(user);
		return mapToDto(updatedUser);
	}

	public void verifiedEmailUser(User user) {
		verifiedService.verifiedByUser(user);
	}

	public void resetPasswordUser(User user) {
		verifiedService.resetPasswordByUser(user);
	}

	private UserDTO mapToDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setVerified(user.getVerified());
		dto.setRole(user.getRole());
		return dto;
	}
}