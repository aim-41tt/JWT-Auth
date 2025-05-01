package ru.example.JWT_Auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.example.JWT_Auth.DTO.UserDTO;
import ru.example.JWT_Auth.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	@Query("SELECT u.email FROM User u WHERE u.username = :username AND u.verified = true")
	Optional<String> findUserEmailByUsername(@Param("username") String username);

	@Modifying
	@Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :id")
	void updatePassword(@Param("id") UUID id, @Param("newPassword") String newPassword);

	@Modifying
	@Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :id")
	void updatePasswordById(@Param("id") UUID id, @Param("newPassword") String newPassword);

	@Query("SELECT u.password FROM User u WHERE u.id = :id")
	Optional<String> findPasswordById(@Param("id") UUID id);
	
	@Modifying
	@Query("UPDATE User u SET u.verified = true WHERE u.email = :email AND u.verified = false")
	void verifyUserByEmail(@Param("email") String email);

	@Query("SELECT new ru.example.JWT_Auth.DTO.UserDTO(u) " + "FROM User u " + "WHERE u.id = :id")
	Optional<UserDTO> findUserDTOByid(UUID id);

}