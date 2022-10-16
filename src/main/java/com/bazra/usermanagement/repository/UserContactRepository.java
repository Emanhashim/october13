package com.bazra.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.UserContact;
import com.bazra.usermanagement.model.UserInfo;
@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Integer> {
	List<UserContact> findByuserInfo(UserInfo userInfo);

}
