package ru.example.JWT_Auth.DTO;

public class authenticationResponse {

	private String token;

	/**
	 * @param token
	 */
	public authenticationResponse(String token) {
		this.token = token;
	}

	/**
	 * 
	 */
	public authenticationResponse() {
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
		public authenticationResponse build() {
			 return new authenticationResponse(token);
		}
	}
	
}
