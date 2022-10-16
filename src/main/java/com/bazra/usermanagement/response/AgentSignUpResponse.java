package com.bazra.usermanagement.response;

public class AgentSignUpResponse {
	private String orgName;
	private String orgType;
	private String agentPhone;
	private String agentName;
	private String pin;
	
	
	
	
	public AgentSignUpResponse(String orgName, String orgType, String agentPhone, String agentName ,String pin) {
		
		this.orgName = orgName;
		this.orgType = orgType;
		this.agentPhone = agentPhone;
		this.agentName = agentName;
		this.pin = pin;
	}
	
	
	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getAgentPhone() {
		return agentPhone;
	}
	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
}
