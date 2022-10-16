package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.AgentContact;
import com.bazra.usermanagement.model.AgentInfo;
@Repository
public interface AgentContactRepository extends JpaRepository<AgentContact, Integer> {
	List<AgentContact> findByagentInfo(AgentInfo agentInfo);

}
