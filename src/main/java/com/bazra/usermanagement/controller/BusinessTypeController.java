package com.bazra.usermanagement.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.AuthenticationType;
import com.bazra.usermanagement.model.BusinessType;
import com.bazra.usermanagement.repository.AdminRepository;
import com.bazra.usermanagement.repository.BusinessTypeRepository;
import com.bazra.usermanagement.request.CreateAuthTypeRequest;
import com.bazra.usermanagement.request.CreateBusinessType;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.UpdateResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/BusinessType")
public class BusinessTypeController {
	@Autowired
	BusinessTypeRepository businessTypeRepository;
	@Autowired
	AdminRepository adminRepository;
	@PostMapping("/CreateBusinessType")
	@ApiOperation(value ="WE CAN CREATE AUTHENTICATION TYPES")
    public ResponseEntity<?> createBusinessType(@RequestBody CreateBusinessType createBusinessType,Authentication authorization) {
    	Optional<BusinessType> accOptional =businessTypeRepository.findBybusinessType(createBusinessType.getBusinessType());
    	if (accOptional.isPresent()) {
    		return ResponseEntity.badRequest().body(new ResponseError("Business type already exist!"));
		}
    	BusinessType businessType = new BusinessType();
    	businessType.setBusinessType(createBusinessType.getBusinessType());
    	businessType.setCreatedDateTime(LocalDate.now());
    	businessType.setCreatorid(adminRepository.findByUsername(authorization.getName()).get());
    	businessTypeRepository.save(businessType);
    	return ResponseEntity.ok(new UpdateResponse("Business type created successfully"));
	}

}
