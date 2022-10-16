package com.bazra.usermanagement.response;

public class TwoWayTransactionResponse {
	
	private String receiverMessage;
	private String senderMessage;
	
	
	public TwoWayTransactionResponse(String receiverMessage, String senderMessage) {
		super();
		this.receiverMessage = receiverMessage;
		this.senderMessage = senderMessage;
	}
	public String getReceiverMessage() {
		return receiverMessage;
	}
	public void setReceiverMessage(String receiverMessage) {
		this.receiverMessage = receiverMessage;
	}
	public String getSenderMessage() {
		return senderMessage;
	}
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	
	

}
