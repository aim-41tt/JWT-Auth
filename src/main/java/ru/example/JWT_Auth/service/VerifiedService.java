package ru.example.JWT_Auth.service;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.email.EmailMessage;
import ru.example.JWT_Auth.model.email.enums.MessageType;
import ru.example.JWT_Auth.repository.UserRepository;
import ru.example.JWT_Auth.service.kafka.kafkaProducter;

@Service
public class VerifiedService {

	private final UserRepository repository;
	private final kafkaProducter kafkaProducter;

	/**
	 * @param repository
	 * @param kafkaProducter
	 */
	public VerifiedService(UserRepository repository, kafkaProducter kafkaProducter) {
		this.repository = repository;
		this.kafkaProducter = kafkaProducter;
	}

	@Async
	public void sendMessageToEmail(EmailMessage userEmail) {
		kafkaProducter.sendMessageToKafka(userEmail);
	}

	public void verifiedByUser(User user) {
		sendMessageToEmail(new EmailMessage(user.getEmail(), MessageType.VERIFICATION, "http://exemple.ru"));
	}

	public void verifiedByUserName(String username) {
		Optional<String> userEmail = repository.findUserEmailByUsername(username);
		sendMessageToEmail(new EmailMessage(userEmail.get(), MessageType.VERIFICATION, "http://exemple.ru"));
	}

	public void resetPasswordByUser(User user) {
		String email = user.getVerified()?user.getEmail():null;
		if(email == null) {
			new Exception("dont verified email");
		}
		sendMessageToEmail(new EmailMessage(email, MessageType.PASSWORD_RESET, "http://exemple.ru"));
	}

	public void resetPasswordByUserName(String username) {
		Optional<String> userEmail = repository.findUserEmailByUsername(username);
		if (userEmail == null || userEmail.isEmpty()) {
			new Exception("dont verified email");
			return;
		}
		sendMessageToEmail(new EmailMessage(userEmail.get(), MessageType.PASSWORD_RESET, "http://exemple.ru"));
	}

}
