package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.BusinessSector;
@Repository
public interface BusinessSectorRepository extends JpaRepository<BusinessSector, Integer> {
	Optional<BusinessSector> findBysector(String businessSector);
	Optional<BusinessSector> findBysector(BusinessSector businessSector);

}
