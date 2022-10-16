package com.bazra.usermanagement.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "User_Authentication")
public class UserAuthentication {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
	
	private String authenticationValue;
	
	@ManyToOne
	@JoinColumn(name = "authenticationType_id")
	private AuthenticationType authenticationType;
	@ManyToOne
	 @JoinColumn(name = "user_id")
	private UserInfo userInfo;
	@ManyToOne
	@JoinColumn(name = "admin_id")
	private AdminInfo adminInfo;
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private AgentInfo agentInfo;
	@ManyToOne
	@JoinColumn(name = "master_id")
	private MasterAgentInfo masterAgentInfo;
	
	@ManyToOne
	@JoinColumn(name = "merchant_id")
	private MerchantInfo merchantInfo;
	private LocalDate localDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthenticationValue() {
		return authenticationValue;
	}
	public void setAuthenticationValue(String authenticationValue) {
		this.authenticationValue = authenticationValue;
	}
	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	public AdminInfo getAdminInfo() {
		return adminInfo;
	}
	public void setAdminInfo(AdminInfo adminInfo) {
		this.adminInfo = adminInfo;
	}
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
	public MasterAgentInfo getMasterAgentInfo() {
		return masterAgentInfo;
	}
	public void setMasterAgentInfo(MasterAgentInfo masterAgentInfo) {
		this.masterAgentInfo = masterAgentInfo;
	}
	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}
	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}
	
	
	
	
}
