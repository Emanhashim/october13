package com.bazra.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.User_Document;

@Repository
@Transactional(readOnly = true)
public interface UserDocumentRepository  extends JpaRepository<User_Document, Integer> {
	List<User_Document> findByUserInfo(UserInfo title);

}
