package ru.example.JWT_Auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.DTO.request.UserUpdateRequest;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetPassword;
import ru.example.JWT_Auth.mapper.user.UserMapper;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.cahe.UserCacheService;
import ru.example.JWT_Auth.service.confirmations.VerifiedService;

/**
 * Сервис для управления пользователями.
 *
 * <p>
 * Содержит методы для получения и обновления профиля пользователя, а также для
 * верификации email и сброса пароля.
 * </p>
 * 
 * @author aim_41tt
 * @version 1.1
 * @since 10.02.2025
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final VerifiedService verifiedService;
	private final UserCacheService userCacheService;
	private final UserMapper userMapper;

	/**
	 * Конструктор UserService — инициализирует сервис с репозиторием пользователей
	 * и сервисом верификации email.
	 * <p>
	 * Создает экземпляр класса с указанными параметрами.
	 * </p>
	 *
	 * @param userRepository Репозиторий для работы с пользователями.
	 * @param verifiedService Сервис для верификации email.
	 * @param userCacheService Сервис кеширывания пользователей.
	 * @param userMapper Мапер пользователей.
	 * 
	 * @since 10.02.2025
	 * @since 11.08.2025
	 */
	public UserService(UserRepository userRepository, VerifiedService verifiedService,
			UserCacheService userCacheService, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.verifiedService = verifiedService;
		this.userCacheService = userCacheService;
		this.userMapper = userMapper;
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
		return userCacheService.getCachedUser(username);
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
	@Deprecated
	public UserDTO updateUserProfile(String username, UserUpdateRequest updateRequest)
			throws UsernameNotFoundException {
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
		UserDTO userDTO = userMapper.toUserDTO(updatedUser);
		userCacheService.cacheUser(userDTO);
		return userDTO;
	}

	/**
	 * Метод verifiedEmailUser — Отправляет email для верификации пользователя.
	 *
	 * @param user Объект пользователя.
	 * @since 10.02.2025
	 */
	public void verifiedEmailUser(User user) {
		verifiedService.verifiedByUser(user);
		userCacheService.removeUserFromCache(user.getUsername());
	}

	/**
	 * Метод resetPasswordUser — Отправляет email для сброса пароля пользователя.
	 *
	 * @param user Объект пользователя.
	 * @since 10.02.2025
	 */
	public void resetPasswordUser(User user, ResetPassword resetPassword) {
		verifiedService.resetPasswordByUser(user, resetPassword);
	}
}
