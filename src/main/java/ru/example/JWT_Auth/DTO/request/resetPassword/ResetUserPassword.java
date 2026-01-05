package ru.example.JWT_Auth.DTO.request.resetPassword;

public class ResetUserPassword implements ResetPassword {

	private String oldPassword;
	private String password;

	/**
	 * @param oldPassword
	 * @param password
	 */
	public ResetUserPassword(String oldPassword, String password) {
		this.oldPassword = oldPassword;
		this.password = password;
	}

	public ResetUserPassword() {
	}

	/**
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password.equals(oldPassword) ? null : password;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

}
