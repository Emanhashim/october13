package com.bazra.usermanagement.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.bazra.usermanagement.model.BusinessSector;
import com.bazra.usermanagement.model.BusinessType;
import com.bazra.usermanagement.model.OrganizationType;

import io.swagger.annotations.ApiModelProperty;

public class MasterSignupRequest2 {

    private String organizationName;

    private String organizationType;
    
    private String businessSector;
    private String role;

    private String businessType;
  
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent city ")
    private String tinNumber;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private BigDecimal capital;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String region;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String city;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String subcity;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String woreda;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String specificLocation;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String houseNumber;
    
    @NotBlank
    @ApiModelProperty(value= "This is MasterAgent Organization ")
    private String phoneNumber;
    
    private MultipartFile tinCertificate;
	private MultipartFile treadCertificate;
	
	private String name;
	private String fatherName;
	private String lastname;
	private String agentPhone;
	private String agentemail;
	private String ownerCity;
	private String ownerRegion;
	private String ownerSubcity;
	private String ownerWoreda;
	private String ownerSpecificLocation;
	private String ownerHouseNumber;
	
	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}	

	public String getAgentemail() {
		return agentemail;
	}

	public void setAgentemail(String agentemail) {
		this.agentemail = agentemail;
	}

	public String getOwnerCity() {
		return ownerCity;
	}

	public void setOwnerCity(String ownerCity) {
		this.ownerCity = ownerCity;
	}

	public String getOwnerRegion() {
		return ownerRegion;
	}

	public void setOwnerRegion(String ownerRegion) {
		this.ownerRegion = ownerRegion;
	}

	public String getOwnerSubcity() {
		return ownerSubcity;
	}

	public void setOwnerSubcity(String ownerSubcity) {
		this.ownerSubcity = ownerSubcity;
	}

	public String getOwnerWoreda() {
		return ownerWoreda;
	}

	public void setOwnerWoreda(String ownerWoreda) {
		this.ownerWoreda = ownerWoreda;
	}

	public String getOwnerSpecificLocation() {
		return ownerSpecificLocation;
	}

	public void setOwnerSpecificLocation(String ownerSpecificLocation) {
		this.ownerSpecificLocation = ownerSpecificLocation;
	}

	public String getOwnerHouseNumber() {
		return ownerHouseNumber;
	}

	public void setOwnerHouseNumber(String ownerHouseNumber) {
		this.ownerHouseNumber = ownerHouseNumber;
	}

	public MultipartFile getTinCertificate() {
		return tinCertificate;
	}

	public void setTinCertificate(MultipartFile tinCertificate) {
		this.tinCertificate = tinCertificate;
	}

	public MultipartFile getTreadCertificate() {
		return treadCertificate;
	}

	public void setTreadCertificate(MultipartFile treadCertificate) {
		this.treadCertificate = treadCertificate;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getBusinessSector() {
		return businessSector;
	}

	public void setBusinessSector(String businessSector) {
		this.businessSector = businessSector;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSubcity() {
		return subcity;
	}

	public void setSubcity(String subcity) {
		this.subcity = subcity;
	}

	public String getWoreda() {
		return woreda;
	}

	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}

	public String getSpecificLocation() {
		return specificLocation;
	}

	public void setSpecificLocation(String specificLocation) {
		this.specificLocation = specificLocation;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
    
    
    
    
    
    

}
