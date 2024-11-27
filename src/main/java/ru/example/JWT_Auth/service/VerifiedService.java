package ru.example.JWT_Auth.service;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.entity.User;
import ru.example.JWT_Auth.entity.UserEmail;
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
	public void verifiedByUserEmail(UserEmail userEmail) {
		kafkaProducter.sendMessageToKafka(userEmail);
	}
	
	// dont use
	@Async
	public void verifiedByUser(User user) {
		verifiedByUserEmail(new UserEmail(user));
	}
	@Async
	public void verifiedByUserName(String username) {
		Optional<UserEmail> userEmail = repository.findUserEmailByUsername(username);
		verifiedByUserEmail(userEmail.get());
	}

}
