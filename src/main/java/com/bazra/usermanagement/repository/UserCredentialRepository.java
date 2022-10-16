package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.AdminInfo;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.MasterAgentInfo;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.UserAuthentication;
import com.bazra.usermanagement.model.UserCredential;
import com.bazra.usermanagement.model.UserInfo;
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
	Optional<UserCredential> findByUserInfo(UserInfo userInfo);
	Optional<UserCredential> findByadminInfo(AdminInfo userInfo);
	Optional<UserCredential> findByagentInfo(AgentInfo userInfo);
	Optional<UserCredential> findBymerchantInfo(MerchantInfo userInfo);
	Optional<UserCredential> findBymasterAgentInfo(MasterAgentInfo userInfo);


}
