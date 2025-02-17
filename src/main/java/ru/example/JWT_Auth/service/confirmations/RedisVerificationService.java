package ru.example.JWT_Auth.service.confirmations;

import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

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

	/**
	 * Конструктор сервиса.
	 *
	 * @param redisTemplate бин для работы с Redis
	 */
	public RedisVerificationService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

	}

	/**
	 * Генерирует уникальный токен, сохраняет его в Redis с установленным временем
	 * жизни (TTL) и возвращает токен.
	 *
	 * @param emailMessage объект с данными письма для верификации
	 * @return сгенерированный токен (UUID в виде строки)
	 */
	public String generateAndSaveVerificationToken(EmailMessage emailMessage) {
		// Генерируем уникальный токен
		String token = UUID.randomUUID().toString();
		// Оборачиваем emailMessage в объект TokenData, который реализует Serializable
		TokenData tokenData = new TokenData(emailMessage);
		// Сохраняем токен в Redis с TTL 15 минут
		redisTemplate.opsForValue().set(token, tokenData, Duration.ofMinutes(TOKEN_LIFETIME_MINUTES));
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
		Object data = redisTemplate.opsForValue().get(token);
		if (data == null) {
			// Токен отсутствует или его TTL истек
			return null;
		}
		// Приводим извлеченный объект к нужному типу
		TokenData tokenData = (TokenData) data;
		// Удаляем токен, чтобы предотвратить повторное использование
		redisTemplate.delete(token);

		return tokenData.toString();
	}

	/**
	 * Класс для хранения данных токена. Обратите внимание, что объект должен
	 * реализовывать Serializable для корректной работы с Redis.
	 */
	private static class TokenData implements Serializable {
		private static final long serialVersionUID = 1L;

		private String email;
		private MessageType messageType;

		public TokenData(EmailMessage emailMessage) {
			this.email = emailMessage.getEmail();
			this.messageType = emailMessage.getMessageType();
		}

		public TokenData() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 * @return the messageType
		 */
		public MessageType getMessageType() {
			return messageType;
		}

		/**
		 * @param messageType the messageType to set
		 */
		public void setMessageType(MessageType messageType) {
			this.messageType = messageType;
		}

		@Override
		public String toString() {
			return "TokenData [email=" + email + ", messageType=" + messageType + "]";
		}

	}
}
