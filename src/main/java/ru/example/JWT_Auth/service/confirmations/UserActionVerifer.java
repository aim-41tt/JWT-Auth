package ru.example.JWT_Auth.service.confirmations;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.JWT_Auth.DTO.request.resetPassword.ForgotPassword;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetPassword;
import ru.example.JWT_Auth.DTO.request.resetPassword.ResetUserPassword;
import ru.example.JWT_Auth.model.User;
import ru.example.JWT_Auth.repository.UserRepository;

@Service
public class UserActionVerifer {

	private final UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
//	private static final Logger logger = LoggerFactory.getLogger(UserActionVerifer.class);

	/**
	 * @param userRepository
	 * @param passwordEncoder
	 */
	public UserActionVerifer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void verificationUser(String email) {
		userRepository.verifyUserByEmail(email);
	}

	@Transactional
	private void resetPasswordUser(Long id, String oldPassword, String password) {
		User user = userRepository.findById(id).get();
		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			userRepository.updatePassword(id, passwordEncoder.encode(password));
		}

	}

	@Transactional
	private void updatePasswordById(Long id, String newPassword) {
		userRepository.updatePasswordById(id, passwordEncoder.encode(newPassword));
	}

	@Transactional
	public void resetPasswordUser(ResetPassword resetPassword, String email) {

		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("пользывателя с таким email не существует");
		}
		if (resetPassword instanceof ResetUserPassword) {
			ResetUserPassword rup = (ResetUserPassword) resetPassword;
			resetPasswordUser(user.getId(), rup.getOldPassword(), rup.getPassword());
		} else if (resetPassword instanceof ForgotPassword) {
			ForgotPassword fp = (ForgotPassword) resetPassword;
			updatePasswordById(user.getId(), fp.getPassword());
		} else {
			throw new IllegalArgumentException("Unsupported ResetPassword type");
		}
	}

}
