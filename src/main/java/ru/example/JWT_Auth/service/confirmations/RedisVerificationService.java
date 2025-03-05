package ru.example.JWT_Auth.service.confirmations;

import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.DTO.request.resetPassword.ResetPassword;
import ru.example.JWT_Auth.model.email.EmailMessage;
import ru.example.JWT_Auth.model.email.enums.MessageType;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Сервис для генерации и проверки токенов верификации с использованием Redis.
 */
@Service
public class RedisVerificationService {

	// Время жизни токена в минутах
	private static final long TOKEN_LIFETIME_MINUTES = 15;

	// RedisTemplate для работы с Redis, настроенный в RedisCacheConfig
	private final RedisTemplate<String, Object> redisTemplate;
	private final UserActionVerifier actionVerifer;

	/**
	 * @param redisTemplate
	 * @param actionVerifer
	 */
	public RedisVerificationService(RedisTemplate<String, Object> redisTemplate, UserActionVerifier actionVerifer) {
		this.redisTemplate = redisTemplate;
		this.actionVerifer = actionVerifer;
	}

	/**
	 * Генерирует уникальный токен, сохраняет его в Redis с установленным временем
	 * жизни (TTL) и возвращает токен.
	 *
	 * @param emailMessage объект с данными письма для верификации
	 * @return сгенерированный токен (UUID в виде строки)
	 */
	public String generateAndSaveVerificationToken(EmailMessage emailMessage, ResetPassword... resetPassword) {
		// Генерируем уникальный токен
		String token = UUID.randomUUID().toString();
		// Оборачиваем emailMessage в объект TokenData, который реализует Serializable
		TokenData tokenData = new TokenData(emailMessage, resetPassword);
		// Сохраняем токен в Redis с TTL 15 минут
		redisTemplate.opsForValue().set(buildKeyTemplate(token), tokenData, Duration.ofMinutes(TOKEN_LIFETIME_MINUTES));
		return token;
	}

	/**
	 * Проверяет наличие токена в Redis. Если токен найден, извлекает связанные
	 * данные, удаляет запись, помечает email как подтвержденный (если тип сообщения
	 * VERIFICATION).
	 *
	 * @param token токен для проверки
	 * @return объект EmailMessage, если токен действителен, иначе null
	 */
	public String verifyToken(String token) {
		// Извлекаем объект TokenData по ключу token
		Object data = redisTemplate.opsForValue().get(buildKeyTemplate(token));
		if (data == null) {
			// Токен отсутствует или его TTL истек
			return null;
		}
		// Приводим извлеченный объект к нужному типу
		TokenData tokenData = (TokenData) data;
		// Удаляем токен, чтобы предотвратить повторное использование
		redisTemplate.delete(buildKeyTemplate(token));
		confirm(tokenData);
		return tokenData.toString();
	}

	private void confirm(TokenData tokenData) {
		switch (tokenData.getMessageType()) {
		case VERIFICATION: {
			actionVerifer.verificationUser(tokenData.getEmail());
			break;
		}
		case PASSWORD_RESET: {
			actionVerifer.resetPasswordUser(tokenData.getResetPassword(), tokenData.getEmail());
			break;
		}
		default:
			break;
		}
	}
	
	private String buildKeyTemplate(String text) {
		return "TOKEN-DATA:"+text;
	}

	/**
	 * Класс для хранения данных токена. Обратите внимание, что объект должен
	 * реализовывать Serializable для корректной работы с Redis.
	 */
	private static class TokenData implements Serializable {
		private static final long serialVersionUID = 1L;

		private String email;
		private MessageType messageType;
		private ResetPassword resetPassword;

		public TokenData(EmailMessage emailMessage, ResetPassword... resetPassword) {
			this.email = emailMessage.getEmail();
			this.messageType = emailMessage.getMessageType();
			this.resetPassword = (resetPassword != null && resetPassword.length > 0) ? resetPassword[0] : null;
			
		}

		@Override
		public String toString() {
			return "TokenData [email=" + email + ", messageType=" + messageType + ", resetPassword=" + resetPassword
					+ "]";
		}

		public TokenData() {
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @return the messageType
		 */
		public MessageType getMessageType() {
			return messageType;
		}

		/**
		 * @return the resetPassword
		 */
		public ResetPassword getResetPassword() {
			return resetPassword;
		}

	}
}
