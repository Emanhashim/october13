package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.OrganizationContact;
import com.bazra.usermanagement.model.OrganizationInfo;
@Repository
public interface OrganizationContactRepository extends JpaRepository<OrganizationContact, Integer> {
	List<OrganizationContact> findByorganizationInfo(OrganizationInfo organizationInfo);

}
