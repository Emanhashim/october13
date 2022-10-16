package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

public class ListOfErrorResponse {
	
	private List<?> list;

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public ListOfErrorResponse(String list) {
		super();
		List<String> listt = new ArrayList<>();
		listt.add(list);
		this.list = listt;
	}
	
	

}
