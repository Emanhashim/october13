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

@Entity
@Table(name = "FullTransaction")
public class FullTransaction {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	 	@Column(name = "transactionId")
	    private int id;
	
	    private String toAccountNumber;
	   
	    private String fromAccountNumber;

	    private BigDecimal transactionAmount;
//	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	    private LocalDate transactionDateTime;
	 
	    private String transactiontype;
	
		
		
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
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
		public String getToAccountNumber() {
			return toAccountNumber;
		}
		public void setToAccountNumber(String toAccountNumber) {
			this.toAccountNumber = toAccountNumber;
		}
		public String getFromAccountNumber() {
			return fromAccountNumber;
		}
		public void setFromAccountNumber(String fromAccountNumber) {
			this.fromAccountNumber = fromAccountNumber;
		}
		public String getTransactiontype() {
			return transactiontype;
		}
		public void setTransactiontype(String transactiontype) {
			this.transactiontype = transactiontype;
		}
		
		
		
	    
	    

}
