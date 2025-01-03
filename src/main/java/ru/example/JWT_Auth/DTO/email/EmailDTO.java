package ru.example.JWT_Auth.DTO.email;

import ru.example.JWT_Auth.model.email.EmailMessage;

/**
 * @author aim_41tt
 * @since 3 янв. 2025 г.
 *
 */
public class EmailDTO {

	private EmailMessage emailMessage;
	private Boolean isActionConfirmed;

	/**
	 * @param emailMessage
	 * @param isActionConfirmed
	 */
	public EmailDTO(EmailMessage emailMessage, Boolean isActionConfirmed) {
		this.emailMessage = emailMessage;
		this.isActionConfirmed = isActionConfirmed;
	}

	/**
	 * @return the emailMessage
	 */
	public EmailMessage getEmailMessage() {
		return emailMessage;
	}

	/**
	 * @param emailMessage the emailMessage to set
	 */
	public void setEmailMessage(EmailMessage emailMessage) {
		this.emailMessage = emailMessage;
	}

	/**
	 * @return the isActionConfirmed
	 */
	public Boolean getIsActionConfirmed() {
		return isActionConfirmed;
	}

	/**
	 * @param isActionConfirmed the isActionConfirmed to set
	 */
	public void setIsActionConfirmed(Boolean isActionConfirmed) {
		this.isActionConfirmed = isActionConfirmed;
	}

}
