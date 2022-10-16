package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.OrganizationType;
@Repository
public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Integer> {
	Optional<OrganizationType> findByorgType(String organizationType);
	Optional<OrganizationType> findByorgType(OrganizationType organizationType);
}
