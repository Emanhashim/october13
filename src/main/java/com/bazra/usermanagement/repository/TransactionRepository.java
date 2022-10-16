package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	Optional<Transaction> findBytransactionCode(String name);
	


}
