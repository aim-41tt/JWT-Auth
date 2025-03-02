package ru.example.JWT_Auth.DTO.request.resetPassword;

public class ForgotPassword implements ResetPassword {

	private String password;

	/**
	 * @param password
	 */
	public ForgotPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
