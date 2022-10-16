package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.FullTransaction;
@Repository
public interface FullTransactionRepository extends JpaRepository<FullTransaction, Integer> {
	List<FullTransaction> findBytoAccountNumber(String account);
	List<FullTransaction> findByfromAccountNumber(String account);
}
