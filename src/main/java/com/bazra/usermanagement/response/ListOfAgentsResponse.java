package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.AgentINF;
import com.bazra.usermanagement.model.AgentInfo;

public class ListOfAgentsResponse {
	private List<AgentInfo> list;
	
	public List<AgentInfo> getList() {
		return list;
	}

	public void setList(List<AgentInfo> list) {
		this.list = list;
	}

	public ListOfAgentsResponse(List<AgentInfo> list) {
		super();
		List<AgentInfo> listt = new ArrayList<>();
		for (int i = 0; i < listt.size(); i++) {
			
		}
		this.list = list;
	}
	
}
