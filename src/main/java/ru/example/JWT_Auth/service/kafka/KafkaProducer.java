package ru.example.JWT_Auth.service.kafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.JWT_Auth.model.email.EmailMessage;

/**
 * Сервис KafkaProducer — отправляет сообщения в Kafka.
 *
 * <p>
 * Позволяет асинхронно отправлять email-сообщения через Kafka в указанный топик.
 * </p>
 *
 * @author aim_41tt
 * @version 1.0
 * @since 10.02.2025
 */
@Service
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private static final String TOPIC_NAME = "verifier";

	/**
	 * Конструктор KafkaProducer — инициализирует KafkaTemplate и ObjectMapper.
	 *
	 * @param kafkaTemplate KafkaTemplate для отправки сообщений.
	 * @param objectMapper  ObjectMapper для сериализации объектов.
	 * @since 10.02.2025
	 */
	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * Метод sendMessageToKafka — отправляет email-сообщение в Kafka.
	 *
	 * <p>
	 * Метод выполняется асинхронно и использует {@link CompletableFuture} для обработки результата.
	 * В случае ошибки логирует исключение и возвращает {@code false}.
	 * </p>
	 *
	 * @param userEmail EmailMessage для отправки.
	 * @return CompletableFuture с результатом отправки (true — успех, false — ошибка).
	 * @since 10.02.2025
	 */
	@Async
	public CompletableFuture<Boolean> sendMessageToKafka(EmailMessage userEmail) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				String userEmailJson = objectMapper.writeValueAsString(userEmail);
				kafkaTemplate.send(TOPIC_NAME, userEmailJson).get();
				return true;
			} catch (JsonProcessingException e) {
				System.err.println("Ошибка сериализации EmailMessage: " + e.getMessage());
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Ошибка отправки сообщения в Kafka: " + e.getMessage());
				Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
			}
			return false;
		});
	}
}
