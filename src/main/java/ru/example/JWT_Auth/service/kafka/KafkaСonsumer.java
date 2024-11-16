package ru.example.JWT_Auth.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.JWT_Auth.entity.User;
import ru.example.JWT_Auth.entity.UserEmail;
import ru.example.JWT_Auth.repository.UserRepository;

import java.util.Optional;

@Service
public class KafkaСonsumer {

    private final UserRepository userRepository;
    private final ObjectMapper MAPPER;

    public KafkaСonsumer(UserRepository userRepository, ObjectMapper mAPPER) {
        this.userRepository = userRepository;
        this.MAPPER = mAPPER;
    }

    @KafkaListener(topics = "verified_emails")
    private void UserAuthListener(ConsumerRecord<String, String> clientRequestMessages, Acknowledgment ack) {
        try {
            // Преобразуем JSON-сообщение в объект UserEmail
            UserEmail userEmail = MAPPER.readValue(clientRequestMessages.value(), UserEmail.class);

            // Находим пользователя по email
            Optional<User> userOptional = userRepository.findByEmail(userEmail.getEmail());

            if (userOptional.isPresent()) {
                // Если пользователь найден, обновляем поле verified и сохраняем в базе
                User user = userOptional.get();
                user.setVerified(userEmail.getVerified());
                userRepository.save(user);
            } else {
                // Логируем или обрабатываем случай, когда пользователь не найден
                System.out.println("Пользователь с email " + userEmail.getEmail() + " не найден.");
            }

            ack.acknowledge();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
