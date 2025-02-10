package ru.example.JWT_Auth.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.email.EmailMessage;
import ru.example.JWT_Auth.model.email.enums.MessageType;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.kafka.KafkaProducer;

/**
 * Сервис для проверки и верификации пользователей.
 *
 * <p>
 * Предоставляет методы для отправки email-сообщений и верификации пользователей по имени и email.
 * </p>
 *
 * @author aim_41tt
 * @version 1.0
 * @since 10.02.2025
 */
@Service
public class VerifiedService {

	private final UserRepository repository;
	private final KafkaProducer kafkaProducer;

	/**
	 * Конструктор VerifiedService — инициализирует сервис с репозиторием пользователей
	 * и продюсером Kafka.
	 *
	 * @param repository    Репозиторий пользователей.
	 * @param kafkaProducer Kafka-продюсер для отправки сообщений.
	 * @since 10.02.2025
	 */
	public VerifiedService(UserRepository repository, KafkaProducer kafkaProducer) {
		this.repository = repository;
		this.kafkaProducer = kafkaProducer;
	}

	/**
	 * Метод sendMessageToEmail — Асинхронно отправляет сообщение на email через Kafka.
	 *
	 * @param emailMessage Объект EmailMessage с данными для отправки.
	 * @since 10.02.2025
	 */
	@Async
	public void sendMessageToEmail(EmailMessage emailMessage) {
		kafkaProducer.sendMessageToKafka(emailMessage);
	}

	/**
	 * Метод verifiedByUser — Отправляет email для верификации пользователя.
	 *
	 * @param user Объект пользователя, которому отправляется письмо.
	 * @since 10.02.2025
	 */
	public void verifiedByUser(User user) {
		sendVerificationEmail(user.getEmail());
	}

	/**
	 * Метод verifiedByUserName — Отправляет email для верификации пользователя по имени.
	 *
	 * @param username Имя пользователя.
	 * @throws IllegalStateException если email пользователя не найден.
	 * @since 10.02.2025
	 */
	public void verifiedByUserName(String username) {
		String userEmail = repository.findUserEmailByUsername(username)
				.orElseThrow(() -> new IllegalStateException("Email пользователя не найден"));
		sendVerificationEmail(userEmail);
	}

	/**
	 * Метод resetPasswordByUser — Отправляет email для сброса пароля пользователю.
	 *
	 * @param user Объект пользователя.
	 * @throws IllegalStateException если email не верифицирован.
	 * @since 10.02.2025
	 */
	public void resetPasswordByUser(User user) {
		if (!user.getVerified()) {
			throw new IllegalStateException("Email не верифицирован");
		}
		sendResetPasswordEmail(user.getEmail());
	}

	/**
	 * Метод resetPasswordByUserName — Отправляет email для сброса пароля по имени пользователя.
	 * 
	 * @param username Имя пользователя.
	 * @throws IllegalStateException если email пользователя не найден.
	 * @since 10.02.2025
	 */
	public void resetPasswordByUserName(String username) {
		String userEmail = repository.findUserEmailByUsername(username)
				.orElseThrow(() -> new IllegalStateException("Email пользователя не найден"));
		sendResetPasswordEmail(userEmail);
	}

	/**
	 * Метод sendVerificationEmail — Отправляет email для верификации.
	 *
	 * @param email Email пользователя.
	 * @since 10.02.2025
	 */
	private void sendVerificationEmail(String email) {
		sendMessageToEmail(new EmailMessage(email, MessageType.VERIFICATION, "http://example.ru"));
	}

	/**
	 * Метод sendResetPasswordEmail — Отправляет email для сброса пароля.
	 *
	 * @param email Email пользователя.
	 * @since 10.02.2025
	 */
	private void sendResetPasswordEmail(String email) {
		sendMessageToEmail(new EmailMessage(email, MessageType.PASSWORD_RESET, "http://example.ru"));
	}
}
