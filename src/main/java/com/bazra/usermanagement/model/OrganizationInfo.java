package com.bazra.usermanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "organizationInfo")
public class OrganizationInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String orgName;
	private String tin;
	private BigDecimal capital;
	@ManyToOne
	@JoinColumn(name = "businessSector")
	private BusinessSector businessSector;
	@ManyToOne
	@JoinColumn(name = "businessType")
	private BusinessType businessType;
	@ManyToOne
	@JoinColumn(name = "orgType")
	private OrganizationType organizationType;
	private LocalDate registerDate;
	@ManyToOne
    @JoinColumn(name = "creatorid")
	private MasterAgentInfo creatorid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTin() {
		return tin;
	}
	public void setTin(String tin) {
		this.tin = tin;
	}
	public BigDecimal getCapital() {
		return capital;
	}
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}
	public BusinessSector getBusinessSector() {
		return businessSector;
	}
	public void setBusinessSector(BusinessSector businessSector) {
		this.businessSector = businessSector;
	}
	public BusinessType getBusinessType() {
		return businessType;
	}
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}
	public OrganizationType getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}
	public LocalDate getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}
	public MasterAgentInfo getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(MasterAgentInfo creatorid) {
		this.creatorid = creatorid;
	}
	
	

}
