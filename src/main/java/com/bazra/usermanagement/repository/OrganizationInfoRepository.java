package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.OrganizationInfo;
@Repository
public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, Integer> {
	Optional<OrganizationInfo> findByorgName(String name);

}
