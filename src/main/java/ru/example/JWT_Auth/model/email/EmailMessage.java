package ru.example.JWT_Auth.model.email;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.JWT_Auth.model.email.enums.MessageType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {

	private Email email;
	private MessageType messageType;

	public EmailMessage() {

	}

	/**
	 * @author aim_41tt
	 * @since 29 дек. 2024 г.
	 *
	 * @param email
	 * @param messageType
	 */
	public EmailMessage(Email email, MessageType messageType) {
		this.email = email;
		this.messageType = messageType;
	}

	/**
	 * @author aim_41tt
	 * @since 29 дек. 2024 г.
	 *
	 * @param email
	 */
	public EmailMessage(Email email) {
		this.email = email;
		this.messageType = MessageType.VERIFICATION;
	}

	/**
	 * @return the email
	 */
	public Email getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(Email email) {
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

	@Override
	public String toString() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(this);
		} catch (Exception e) {
			return "{\"error\":\"Failed to convert object to JSON\"}";
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, messageType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EmailMessage)) {
			return false;
		}
		EmailMessage other = (EmailMessage) obj;
		return Objects.equals(email, other.email) && messageType == other.messageType;
	}

	/**
	 * @author aim_41tt
	 * @since 3 янв. 2025 г.
	 *
	 * @return true is Empty
	 * @see ru.example.JWT_Auth.model.email.Email#isEmpty()
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return email.isEmpty();
	}

}