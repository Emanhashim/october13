package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.CommunicationMedium;
@Repository
public interface CommunicationMediumRepository extends JpaRepository<CommunicationMedium, Integer> {
	Optional<CommunicationMedium> findBytitle(String account);

}
