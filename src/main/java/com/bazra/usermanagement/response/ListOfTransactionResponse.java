package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.Transaction;

public class ListOfTransactionResponse {
	private List<Transaction> transaction;
	
	public List<Transaction> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}
	public ListOfTransactionResponse(List<Transaction> transaction,List<Transaction> transaction2) {
  	List<Transaction> stringArrayList = new ArrayList<Transaction>();
  	stringArrayList.addAll(transaction);
      stringArrayList.addAll(transaction2);
      
      this.transaction= stringArrayList;
      
      
  }
	
  public ListOfTransactionResponse(List<Transaction> accounts) {
  	List<Transaction> stringArrayList = new ArrayList<Transaction>();
  	stringArrayList.addAll(accounts);
//      stringArrayList.addAll(transaction2);
      
      this.transaction= stringArrayList;
      
      
  }


}
