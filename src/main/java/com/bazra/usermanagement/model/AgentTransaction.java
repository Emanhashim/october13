package com.bazra.usermanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "AgentTransaction")
public class AgentTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	 	@Column(name = "transactionId")
	private int id;
	@ManyToOne
    @JoinColumn(name = "agentAccount")
	private Account agentAccount;
	@ManyToOne
    @JoinColumn(name = "userAccount")
	private Account customerAccount;
	private BigDecimal amount;
	private BigDecimal commission;
	private LocalDate time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Account getAgentAccount() {
		return agentAccount;
	}
	public void setAgentAccount(Account agentAccount) {
		this.agentAccount = agentAccount;
	}
	public Account getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(Account customerAccount) {
		this.customerAccount = customerAccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public LocalDate getTime() {
		return time;
	}
	public void setTime(LocalDate time) {
		this.time = time;
	}
	
	
	
}
