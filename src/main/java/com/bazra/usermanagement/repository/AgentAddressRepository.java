package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.AgentAddress;
import com.bazra.usermanagement.model.AgentInfo;

public interface AgentAddressRepository extends JpaRepository<AgentAddress, Integer> {
//	Optional<UserAddress> findByuser(UserInfo userInfo);
	List<AgentAddress> findByagentInfo(AgentInfo agentInfo);

}
