package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.BusinessType;
@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Integer> {
	Optional<BusinessType> findBybusinessType(String businessType);
	Optional<BusinessType> findBybusinessType(BusinessType businessType);
}
