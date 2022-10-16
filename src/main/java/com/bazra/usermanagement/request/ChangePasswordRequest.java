package com.bazra.usermanagement.request;

public class ChangePasswordRequest {
	private String currentPass;
	private String newPass;
	private String confirmNewPass;
	public String getCurrentPass() {
		return currentPass;
	}
	public void setCurrentPass(String currentPass) {
		this.currentPass = currentPass;
	}
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	public String getConfirmNewPass() {
		return confirmNewPass;
	}
	public void setConfirmNewPass(String confirmNewPass) {
		this.confirmNewPass = confirmNewPass;
	}
	
	

}
