package com.bazra.usermanagement.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.FullTransaction;

public class UserTransactionResponse {
	private String transactionID;
	private String amount;
	private LocalDate date;
	private String fromAccount;
	private String toAccount;
	private String transactionType;
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
//	public TransactionResponse(List<FullTransaction> accounts) {
//    	List<FullTransaction> stringArrayList = new ArrayList<FullTransaction>();
//    	stringArrayList.addAll(accounts);
////        stringArrayList.addAll(transaction2);
//        
//        this.transaction= stringArrayList;
//        
//        
//    }
	

}
