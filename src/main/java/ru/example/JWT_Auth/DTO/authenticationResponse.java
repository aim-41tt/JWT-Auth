package ru.example.JWT_Auth.DTO;

public class AuthenticationResponse {

	private String token;

	/**
	 * @param token
	 */
	public AuthenticationResponse(String token) {
		this.token = token;
	}

	/**
	 * 
	 */
	public AuthenticationResponse() {
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public static class Builder {
		private String token;

		public Builder token(String token) {
			this.token = token;
			return this;
		}

		// Метод для создания объекта User
		public AuthenticationResponse build() {
			 return new AuthenticationResponse(token);
		}
	}
	
}
