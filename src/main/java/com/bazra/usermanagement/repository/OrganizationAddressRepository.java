package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.OrganizationAddress;
import com.bazra.usermanagement.model.OrganizationInfo;

@Repository
public interface OrganizationAddressRepository extends JpaRepository<OrganizationAddress, Integer> {

	List<OrganizationAddress> findByorganizationInfo(OrganizationInfo organizationInfo);

}
