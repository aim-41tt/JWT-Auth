package ru.example.JWT_Auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.request.AuthenticationRequest;
import ru.example.JWT_Auth.DTO.request.RegisterRequest;
import ru.example.JWT_Auth.DTO.response.AuthenticationResponse;
import ru.example.JWT_Auth.config.JwtService;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.confirmations.VerifiedService;

/**
 * Сервис для аутентификации и регистрации пользователей.
 *
 * <p>
 * Предоставляет методы для регистрации, аутентификации и выдачи JWT-токенов.
 * </p>
 *
 * @author aim_41tt
 * @version 1.0
 * @since 10.02.2025
 */
@Service
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final VerifiedService verifiedService;

	/**
	 * Конструктор AuthenticationService — инициализирует сервис с необходимыми
	 * зависимостями.
	 *
	 * @param repository            Репозиторий пользователей.
	 * @param passwordEncoder       Кодировщик паролей.
	 * @param jwtService            Сервис для работы с JWT-токенами.
	 * @param authenticationManager Менеджер аутентификации.
	 * @param verifiedService       Сервис верификации пользователей.
	 * @since 10.02.2025
	 */
	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager, VerifiedService verifiedService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.verifiedService = verifiedService;
	}

	/**
	 * Метод register — Регистрирует нового пользователя.
	 *
	 * <p>
	 * Проверяет, существует ли пользователь с таким именем. Если нет — создает
	 * нового пользователя, шифрует его пароль, сохраняет в БД, отправляет письмо
	 * для верификации и генерирует JWT-токен.
	 * </p>
	 *
	 * @param request Запрос на регистрацию, содержащий имя пользователя, email и
	 *                пароль.
	 * @return AuthenticationResponse с JWT-токеном.
	 * @throws IllegalArgumentException если пользователь с таким именем уже
	 *                                  существует.
	 * @since 10.02.2025
	 */
	@Transactional
	public AuthenticationResponse register(RegisterRequest request) throws IllegalArgumentException {
		if (repository.findByUsername(request.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Пользователь с именем " + request.getUsername() + " уже существует.");
		}

		User user = new User.Builder().username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword())).email(request.getEmail()).role(Role.USER)
				.build();

		repository.save(user);
		verifiedService.verifiedByUser(user);

		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse.Builder().token(jwtToken).build();
	}

	/**
	 * Метод authenticate — Аутентифицирует пользователя по логину и паролю.
	 *
	 * <p>
	 * Проверяет учетные данные, используя {@link AuthenticationManager}. Если
	 * аутентификация успешна, возвращает JWT-токен.
	 * </p>
	 *
	 * @param request Запрос на аутентификацию, содержащий имя пользователя и
	 *                пароль.
	 * @return AuthenticationResponse с JWT-токеном.
	 * @throws UsernameNotFoundException если пользователь не найден.
	 * @throws RuntimeException          в случае других ошибок аутентификации.
	 * @since 10.02.2025
	 */
	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request)
			throws UsernameNotFoundException, RuntimeException {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = repository.findByUsername(request.getUsername()).orElseThrow(
					() -> new UsernameNotFoundException("Пользователь не найден: " + request.getUsername()));

			String jwtToken = jwtService.generateToken(user);
			return new AuthenticationResponse.Builder().token(jwtToken).build();

		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("Ошибка аутентификации: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Ошибка в процессе аутентификации.", e);
		}
	}
}
