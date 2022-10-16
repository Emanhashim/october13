package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.AdminInfo;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.MasterAgentInfo;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.UserAuthentication;
import com.bazra.usermanagement.model.UserInfo;

public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Integer> {
	Optional<UserAuthentication> findByuserInfo(UserInfo userInfo);
	Optional<UserAuthentication> findByadminInfo(AdminInfo userInfo);
	Optional<UserAuthentication> findByagentInfo(AgentInfo userInfo);
	Optional<UserAuthentication> findBymerchantInfo(MerchantInfo userInfo);
	Optional<UserAuthentication> findBymasterAgentInfo(MasterAgentInfo userInfo);

}
