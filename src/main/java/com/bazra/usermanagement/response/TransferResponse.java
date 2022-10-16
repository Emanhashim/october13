package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class TransferResponse {
    private String accountNumber;

    private BigDecimal transactionAmount;

    private LocalDate transactionDateTime;
    
    private String message;
    
    private BigDecimal remainingAmount;
    
    
    public TransferResponse(  String toAccountNumber, BigDecimal amount, LocalDate localDate, String message, BigDecimal remaining) {
        
        this.accountNumber = toAccountNumber;
        this.transactionAmount = amount;
        this.transactionDateTime = localDate;
        this.message= message;
        this.remainingAmount = remaining;
       }
   


    public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}



	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}



	public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }


    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    public LocalDate getTransactionDateTime() {
        return transactionDateTime;
    }


    public void setTransactionDateTime(LocalDate transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
