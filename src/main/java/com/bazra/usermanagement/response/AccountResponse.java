package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountBalance;
import com.bazra.usermanagement.model.UserInfo;

public class AccountResponse {
	
	private String accountNumber;
	private BigDecimal balance;
	private long user_id;
	private UserInfo username;
    private String type;
    private boolean blocked;
    private List<Account> accounts;
    
    
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public UserInfo getUsername() {
		return username;
	}
	public void setUsername(UserInfo username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public AccountResponse( String accountNumber, BigDecimal balance, long user_id, UserInfo username,
			String type) {
		super();
		
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.user_id = user_id;
		this.username = username;
		this.type = type;
	}
	public AccountResponse(Account account,AccountBalance accountBalance) {
		this.accountNumber=account.getAccountNumber();
		this.balance=accountBalance.getBalance();
		this.user_id=account.getUser().getId();
		this.username=account.getUser();
		this.type=account.getType().getAccounttype();
		this.blocked=account.isStatus();
	}
	public AccountResponse(List<Account> accounts) {
    	List<Account> stringArrayList = new ArrayList<Account>();
    	stringArrayList.addAll(accounts);
//        stringArrayList.addAll(transaction2);
        
        this.accounts= stringArrayList;
        
        
    }

	
	
	
    
}
