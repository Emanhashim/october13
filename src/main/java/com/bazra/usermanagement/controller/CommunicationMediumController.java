package com.bazra.usermanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AdminRepository;
import com.bazra.usermanagement.repository.CommunicationMediumRepository;
import com.bazra.usermanagement.request.CreateCommunicationMediumRequest;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SuccessMessageResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/Medium")
public class CommunicationMediumController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	CommunicationMediumRepository mediaRepository;
	@Autowired
	AdminRepository adminRepository;

	@PostMapping("/CreateMedium")
	public ResponseEntity<?> createMedium(@RequestBody CreateCommunicationMediumRequest createMediaRequest,
			Authentication authentication) {
		System.out.println(createMediaRequest.getCommunicationMedium());
		Optional<CommunicationMedium> comOptional = mediaRepository.findBytitle(createMediaRequest.getCommunicationMedium());
		Account adminAccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		
		if (!adminAccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if (comOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Media already exists"));
		}
		if (createMediaRequest.getCommunicationMedium() == null) {
			System.out.println(createMediaRequest.getCommunicationMedium());
			return ResponseEntity.badRequest().body(new ResponseError("Title cannot be empty!"));
		}
		CommunicationMedium communicationMedium = new CommunicationMedium();
		communicationMedium.setTitle(createMediaRequest.getCommunicationMedium());
		communicationMedium.setAdminInfo(adminRepository.findByUsername(authentication.getName()).get());
		mediaRepository.save(communicationMedium);
		return ResponseEntity.ok(new SuccessMessageResponse("Media created successfully!!"));

	}

}
