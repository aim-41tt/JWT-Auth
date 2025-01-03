package ru.example.JWT_Auth.service;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.model.email.Email;
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
	

	
	@Async
	public void verifiedByUser(User user) {
		sendMessageToEmail(new EmailMessage(new Email(user),MessageType.VERIFICATION));
	}
	@Async
	public void verifiedByUserName(String username) {
		Optional<Email> userEmail = repository.findUserEmailByUsername(username);
		sendMessageToEmail(new EmailMessage(userEmail.get(),MessageType.VERIFICATION));
	}

	
	
	
	
	@Async
	public void resetPasswordByUser(User user) {
		sendMessageToEmail(new EmailMessage(new Email(user),MessageType.PASSWORD_RESET));
	}
	
	@Async
	public void resetPasswordByUserName(String username) {
		Optional<Email> userEmail = repository.findUserEmailByUsername(username);
		sendMessageToEmail(new EmailMessage(userEmail.get(),MessageType.PASSWORD_RESET));
	}
	
	

}
