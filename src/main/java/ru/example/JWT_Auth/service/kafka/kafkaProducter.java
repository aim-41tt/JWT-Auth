package ru.example.JWT_Auth.service.kafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.JWT_Auth.entity.UserEmail;

@Service
public class kafkaProducter {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper MAPPER;
	private final String topicName = "email_verifier";

	/**
	 * @param kafkaTemplate
	 * @param mAPPER
	 */
	public kafkaProducter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mAPPER) {
		this.kafkaTemplate = kafkaTemplate;
		MAPPER = mAPPER;
	}

	@Async
	public CompletableFuture<Boolean> sendMessageToKafka(UserEmail userEmail) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				String userEmailJson = MAPPER.writeValueAsString(userEmail);
				kafkaTemplate.send(topicName, userEmailJson).get();
				System.out.println(userEmailJson);
				return true;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return false;
		});
	}
}
