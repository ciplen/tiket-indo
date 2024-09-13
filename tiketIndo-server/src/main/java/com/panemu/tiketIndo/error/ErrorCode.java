package com.panemu.tiketIndo.error;

/**
 *
 * @author amrullah
 */
public enum ErrorCode {
	ER0001("This is an example of error message. If you choose other language, this error is also translated."),
	ER0002("Error Message With Parameters. Name: %s. Age: %s"),
	ER0003("Old password doesn't match"),
	ER0004("Old password can't be empty!"),
	ER0005("Email already exists, please use another one"),
	ER0006("Can't delete admin acount"),
	ER0007("Can't delete your own acount"),
	ER0008("Can't change password of username hellocountry!"),
	ER0009("%s already used, please use another"),
	ER0010("Invalid search criteria [%s : %s]"),
	/**
	 * Your data has been updated by other user. Please refresh!
	 */
	ER0100("Your data has been updated by other user. Please refresh!"),
	ER0400("Bad Request. %s"),
	/**
	 * You are not authenticated. Please login.
	 */
	ER0401("You are not authenticated. Please login."),
	/**
	 * You don't have access to the requested action. Please contact
	 * Administrator.
	 */
	ER0403("You don't have access to the requested action. Please contact Administrator.");

	private final String message;

	private ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
