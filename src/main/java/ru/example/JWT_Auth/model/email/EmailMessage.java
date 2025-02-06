package ru.example.JWT_Auth.model.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ru.example.JWT_Auth.model.email.enums.MessageType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {

	@NotEmpty(message = "Email не должен быть пустым")
	@Email(message = "Некорректный формат email")
	private String email;
	@JsonProperty("messageType")
	private MessageType messageType;
	private String link;

	
	
	/**
	 * @param email
	 * @param messageType
	 */
	public EmailMessage(
			@NotEmpty(message = "Email не должен быть пустым") @Email(message = "Некорректный формат email") String email,
			MessageType messageType) {
		this.email = email;
		this.messageType = messageType;
	}

	/**
	 * @param email
	 * @param messageType
	 * @param link
	 */
	public EmailMessage(
			@NotEmpty(message = "Email не должен быть пустым") @Email(message = "Некорректный формат email") String email,
			MessageType messageType, String link) {
		this.email = email;
		this.messageType = messageType;
		this.link = link;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

}