package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.UserInfo;


@Repository
public interface AgentRepository extends JpaRepository<AgentInfo, Integer> {
	Optional<AgentInfo> findByUsername(String username);
	Optional<AgentInfo> findById(Integer i);
	List<AgentInfo> findByroles(Role role);
//	Optional<AgentInfo> findByEmail(String username);

}
