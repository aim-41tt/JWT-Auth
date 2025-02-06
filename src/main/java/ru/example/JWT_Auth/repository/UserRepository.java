package ru.example.JWT_Auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.example.JWT_Auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	
	@Query("SELECT u.email " +
		       "FROM User u " +
		       "WHERE u.username = :username " +
			"AND u.verified = true")
	Optional<String> findUserEmailByUsername(@Param("username") String username);
	
//	@Query("SELECT new ru.example.UserEmail(u.email, u.verified) " +
//		       "FROM User u " +
//		       "WHERE u.id = :id")
//		Optional<UserEmail> findUserEmailById(@Param("id") Long id);


}
