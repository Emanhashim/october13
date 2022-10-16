package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.OrganizationDocument;
import com.bazra.usermanagement.model.OrganizationInfo;

public interface OrganizationDocumentRepository extends JpaRepository<OrganizationDocument, Integer> {
	List<OrganizationDocument> findByorganizationInfo(OrganizationInfo title);

}
