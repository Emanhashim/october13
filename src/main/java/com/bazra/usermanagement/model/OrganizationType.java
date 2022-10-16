package com.bazra.usermanagement.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Organization_type")
@EntityListeners(AuditingEntityListener.class)
public class OrganizationType {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String orgType;
	private LocalDate createDate;
	@ManyToOne
    @JoinColumn(name = "creatorid")
	private AdminInfo creatorid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public AdminInfo getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(AdminInfo creatorid) {
		this.creatorid = creatorid;
	}
	
	
	
}
