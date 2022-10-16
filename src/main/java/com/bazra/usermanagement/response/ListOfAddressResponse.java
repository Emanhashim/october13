package com.bazra.usermanagement.response;

import java.util.ArrayList;
import java.util.List;

import com.bazra.usermanagement.model.AddressElement;

public class ListOfAddressResponse {

	private List<AddressElement> list = new ArrayList<>();

	public List<AddressElement> getList() {
		return list;
	}

	public void setList(List<AddressElement> list) {
		this.list = list;
	}

	public ListOfAddressResponse(List<AddressElement> langs) {

		this.list = langs;
	}
}
