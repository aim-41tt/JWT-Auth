package ru.example.JWT_Auth.DTO.request.resetPassword;

public class ForgotPassword implements ResetPassword {

	private String password;

	public ForgotPassword(String password) {
		this.password = password;
	}

	public ForgotPassword() {
	}

	@Override
	public String getPassword() {
		return password;
	}

}
