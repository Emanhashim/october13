package com.bazra.usermanagement.response;

import java.util.Optional;

import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.Role;

public class MerchantResponse {

	private Integer id;

	private String firstName;

    private String lastName;
  
    private String username;
   

    private Role roles;
	private Optional<MerchantInfo> agentInfos;
	
	 
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Role getRoles() {
		return roles;
	}
	public void setRoles(Role roles) {
		this.roles = roles;
	}
	public Optional<MerchantInfo> getAgentInfos() {
		return agentInfos;
	}
	public void setAgentInfos(Optional<MerchantInfo> agentInfos) {
		this.agentInfos = agentInfos;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
		
	public MerchantResponse(Optional<MerchantInfo> agentInfos) {
		super();
		this.id = agentInfos.get().getId();
		this.username = agentInfos.get().getUsername();
		this.roles = agentInfos.get().getRoles();
		this.firstName = agentInfos.get().getName();
		this.lastName = agentInfos.get().getLastName();

		
	}

}
