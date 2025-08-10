package ru.example.JWT_Auth.mapper.user;

import org.springframework.stereotype.Component;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.model.User;

@Component
public class UserMapper {

	public User toUser(UserDTO dto) {
		return new User(dto);
	}

	public UserDTO toUserDTO(User user) {
		return new UserDTO(user);
	}

}
