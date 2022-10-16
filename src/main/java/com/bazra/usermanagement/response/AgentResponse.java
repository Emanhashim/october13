package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;

import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.OrganizationInfo;
import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.UserInfo;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.swagger.web.SwaggerApiListingReader;

public class AgentResponse {
	private int id;
    private String name;
    private String fatherName;
    private String lastName;
    private Role roles ;
    private String email;
    private String username;
    private String region;
    private String city;
    private String houseNo;
	private String subCity;
	private String woreda;
	private String specificLocation;

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
		
//	public AgentResponse(Optional<AgentInfo> agentInfos) {
//		super();
//		this.id = agentInfos.get().getId();
//		this.username = agentInfos.get().getUsername();
//		this.roles = agentInfos.get().getRoles();
//		this.firstName = agentInfos.get().getFirstName();
//		this.lastName = agentInfos.get().getLastName();
//		this.businessLNum =agentInfos.get().getBusinessLNum();
//		this.companyName=agentInfos.get().getCompanyName();
//		this.licenceNumber=agentInfos.get().getLicenceNumber();
//		
//	}

}
