package com.bazra.usermanagement.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "agent_contact")
public class AgentContact {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	private String contact;
	private Boolean is_active;
	
	@ManyToOne
	@JoinColumn(name = "contact_type_id")
	private CommunicationMedium communicationMedium;
	private LocalDate registereDate;
	@ManyToOne
	@JoinColumn(name = "agentId")
	private AgentInfo agentInfo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public CommunicationMedium getCommunicationMedium() {
		return communicationMedium;
	}
	public void setCommunicationMedium(CommunicationMedium communicationMedium) {
		this.communicationMedium = communicationMedium;
	}
	public LocalDate getRegistereDate() {
		return registereDate;
	}
	public void setRegistereDate(LocalDate registereDate) {
		this.registereDate = registereDate;
	}
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
	
	
}
