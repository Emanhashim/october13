package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.BankTransfer;

@Repository
@Transactional(readOnly = true)
public interface BankTransferRepository extends JpaRepository<BankTransfer, Integer> {
	List<BankTransfer> findBytobankaccount(String account);
	List<BankTransfer> findByaccount(Account account);


}
