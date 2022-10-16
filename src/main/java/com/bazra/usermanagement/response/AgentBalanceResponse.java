package com.bazra.usermanagement.response;

import java.math.BigDecimal;

import com.bazra.usermanagement.model.AgentINF;

public class AgentBalanceResponse {
	private BigDecimal balance;
	private String message;
	private BigDecimal amount;
	private AgentINF agentInfo;
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public AgentINF getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(AgentINF agentInfo) {
		this.agentInfo = agentInfo;
	}
	
	public AgentBalanceResponse(BigDecimal balance, String message, AgentINF userInfo, BigDecimal amount) {

		this.agentInfo = userInfo;
		this.balance = balance;
		this.message = message;
		this.amount = amount;
	}
}
