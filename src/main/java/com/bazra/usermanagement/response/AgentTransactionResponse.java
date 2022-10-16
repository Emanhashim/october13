package com.bazra.usermanagement.response;

import java.util.List;

import com.bazra.usermanagement.model.AgentTransaction;

public class AgentTransactionResponse {
	
	private List<AgentTransaction> agenttransaction;

	public List<AgentTransaction> getAgenttransaction() {
		return agenttransaction;
	}

	public void setAgenttransaction(List<AgentTransaction> agenttransaction) {
		this.agenttransaction = agenttransaction;
	}

	public AgentTransactionResponse(List<AgentTransaction> agenttransaction) {
		super();
		this.agenttransaction = agenttransaction;
	}
	
	

}
