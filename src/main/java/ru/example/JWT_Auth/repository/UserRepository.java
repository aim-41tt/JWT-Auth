package ru.example.JWT_Auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.example.JWT_Auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
