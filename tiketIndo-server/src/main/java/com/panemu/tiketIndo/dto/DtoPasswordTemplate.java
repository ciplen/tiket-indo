package com.panemu.tiketIndo.dto;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
public class DtoPasswordTemplate {

	private int id;
	private String oldPasswd;
	private String newPasswd;
	private String confirmPasswd;

	public String getOldPasswd() {
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}

	public String getNewPasswd() {
		return newPasswd;
	}

	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}

	public String getConfirmPasswd() {
		return confirmPasswd;
	}

	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}
