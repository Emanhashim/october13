package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.FullTransaction;
import com.bazra.usermanagement.model.Transaction;

public class TransactionResponse {
//	@JsonProperty(value = "summary", required = true)
//    private List<Account> accounts;
	private List<FullTransaction> transaction;
	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	// public List<Account> getAccounts() {
//		return accounts;
//	}
//	public void setAccounts(List<Account> accounts) {
//		this.accounts = accounts;
//	}
	public List<FullTransaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<FullTransaction> transaction) {
		this.transaction = transaction;
	}

	public TransactionResponse(List<FullTransaction> transaction, List<FullTransaction> transaction2) {
		List<FullTransaction> stringArrayList = new ArrayList<FullTransaction>();
		stringArrayList.addAll(transaction);
		for (int i = 0; i < transaction2.size(); i++) {
			if (transaction2.get(i).getTransactiontype().matches("TRANSFER")) {
				transaction2.get(i).setTransactiontype("RECEIVE");
			}
			
		}
		stringArrayList.addAll(transaction2);

		this.transaction = stringArrayList;

	}
//
    public TransactionResponse(List<FullTransaction> accounts) {
    	List<FullTransaction> stringArrayList = new ArrayList<FullTransaction>();
    	stringArrayList.addAll(accounts);
//        stringArrayList.addAll(transaction2);
        
        this.transaction= stringArrayList;
        
        
    }


}
