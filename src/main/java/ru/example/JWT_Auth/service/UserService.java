package ru.example.JWT_Auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.request.UserUpdateRequest;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;

/**
 * Сервис для управления пользователями.
 *
 * <p>
 * Содержит методы для получения и обновления профиля пользователя, а также для 
 * верификации email и сброса пароля.
 * </p>
 * 
 * @author aim_41tt
 * @version 1.0
 * @since 10.02.2025
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final VerifiedService verifiedService;

	/**
	 * Конструктор UserService — инициализирует сервис с репозиторием пользователей
	 * и сервисом верификации email.
	 *
	 * @param userRepository  Репозиторий для работы с пользователями.
	 * @param verifiedService Сервис для верификации email.
	 * @since 10.02.2025
	 */
	public UserService(UserRepository userRepository, VerifiedService verifiedService) {
		this.userRepository = userRepository;
		this.verifiedService = verifiedService;
	}

	/**
	 * Метод getUserProfile — Возвращает профиль пользователя по его имени.
	 *
	 * @param username Имя пользователя.
	 * @return Объект UserDTO с данными пользователя.
	 * @throws UsernameNotFoundException если пользователь не найден.
	 * @since 10.02.2025
	 */
	@Transactional
	public UserDTO getUserProfile(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return mapToDto(user);
	}

	/**
	 * Метод updateUserProfile — Обновляет профиль пользователя.
	 *
	 * <p>
	 * Если email изменяется, то статус верификации сбрасывается на false.
	 * </p>
	 *
	 * @param username      Имя пользователя.
	 * @param updateRequest Объект с новыми данными для обновления.
	 * @return Обновленный профиль пользователя в виде DTO.
	 * @throws UsernameNotFoundException если пользователь не найден.
	 * @since 10.02.2025
	 */
	@Transactional
	public UserDTO updateUserProfile(String username, UserUpdateRequest updateRequest) throws UsernameNotFoundException {
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

	/**
	 * Метод verifiedEmailUser — Отправляет email для верификации пользователя.
	 *
	 * @param user Объект пользователя.
	 * @since 10.02.2025
	 */
	public void verifiedEmailUser(User user) {
		verifiedService.verifiedByUser(user);
	}

	/**
	 * Метод resetPasswordUser — Отправляет email для сброса пароля пользователя.
	 *
	 * @param user Объект пользователя.
	 * @since 10.02.2025
	 */
	public void resetPasswordUser(User user) {
		verifiedService.resetPasswordByUser(user);
	}

	/**
	 * Метод mapToDto — Преобразует объект пользователя в DTO.
	 *
	 * @param user Объект пользователя.
	 * @return Объект UserDTO с данными пользователя.
	 * @since 10.02.2025
	 */
	private UserDTO mapToDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setVerified(user.getVerified());
		dto.setRole(user.getRole());
		return dto;
	}
}
