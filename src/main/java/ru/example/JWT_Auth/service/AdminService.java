package ru.example.JWT_Auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class AdminService {

	private UserRepository userRepository;

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseGet(null);
	}
	
	public List<User> getUsersByid(List<Long> ids) {
		return userRepository.findAllById(ids);
	}
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public List<User> saveUsers(List<User> users){
		return userRepository.saveAll(users);
	}
	
}
