package ru.example.JWT_Auth.service.confirmations;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.request.resetPassword.ForgotPassword;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetPassword;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetUserPassword;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class UserActionVerifier {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	// private static final Logger logger = LoggerFactory.getLogger(UserActionVerifier.class);

	/**
	 * Конструктор для внедрения зависимостей.
	 * 
	 * @param userRepository  репозиторий для работы с пользователями
	 * @param passwordEncoder кодировщик паролей
	 */
	public UserActionVerifier(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Подтверждает пользователя по email.
	 *
	 * @param email email пользователя для подтверждения
	 */
	@Transactional
	public void verificationUser(String email) {
		userRepository.verifyUserByEmail(email);
	}

	/**
	 * Сбрасывает пароль пользователя с проверкой старого пароля.
	 *
	 * @param user        пользователь
	 * @param oldPassword старый пароль пользователя
	 * @param newPassword новый пароль, который необходимо установить
	 * @throws IllegalArgumentException если пользователь не найден или старый
	 *                                  пароль неверный
	 */
	@Transactional
	private void resetPasswordUser(User user, String oldPassword, String newPassword) {

		// Проверяем корректность введенного старого пароля
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Неверный старый пароль");
		}

		// Обновляем пароль, кодируя новый пароль перед сохранением
		userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
	}

	/**
	 * Обновляет пароль пользователя без проверки старого пароля. Используется,
	 * например, при восстановлении пароля через ForgotPassword.
	 *
	 * @param user        пользователь
	 * @param newPassword новый пароль, который необходимо установить
	 */
	@Transactional
	private void updatePasswordById(User user, String newPassword) {
		// Обновляем пароль, сразу кодируя его
		userRepository.updatePasswordById(user.getId(), passwordEncoder.encode(newPassword));
	}

	/**
	 * Сбрасывает пароль пользователя. В зависимости от типа запроса сброса пароля
	 * выполняется либо проверка старого пароля, либо обновление пароля без
	 * проверки.
	 *
	 * @param resetPassword объект запроса на сброс пароля
	 * @param email         email пользователя
	 * @throws UsernameNotFoundException если пользователь с указанным email не
	 *                                   найден
	 * @throws IllegalArgumentException  если тип запроса не поддерживается
	 */
	@Transactional
	public void resetPasswordUser(ResetPassword resetPassword, String email) {
		// Находим пользователя по email или выбрасываем исключение, если его нет
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Пользователя с таким email не существует"));

		// В зависимости от типа запроса на сброс пароля вызываем соответствующий метод
		if (resetPassword instanceof ResetUserPassword) {
			ResetUserPassword rup = (ResetUserPassword) resetPassword;
			resetPasswordUser(user, rup.getOldPassword(), rup.getPassword());
		} else if (resetPassword instanceof ForgotPassword) {
			ForgotPassword fp = (ForgotPassword) resetPassword;
			updatePasswordById(user, fp.getPassword());
		} else {
			throw new IllegalArgumentException("Unsupported ResetPassword type");
		}
	}
}
