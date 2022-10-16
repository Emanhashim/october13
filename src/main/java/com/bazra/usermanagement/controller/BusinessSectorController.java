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
import com.bazra.usermanagement.model.BusinessSector;
import com.bazra.usermanagement.repository.AdminRepository;
import com.bazra.usermanagement.repository.BusinessSectorRepository;
import com.bazra.usermanagement.repository.BusinessTypeRepository;
import com.bazra.usermanagement.request.CreateAuthTypeRequest;
import com.bazra.usermanagement.request.CreateBusinessSector;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.UpdateResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/BusinessSector")
public class BusinessSectorController {
	@Autowired
	BusinessSectorRepository businessSectorRepository;
	@Autowired
	AdminRepository adminRepository;
	@PostMapping("/CreateBusinessSector")
	@ApiOperation(value ="WE CAN CREATE AUTHENTICATION TYPES")
    public ResponseEntity<?> createBusinessSector(@RequestBody CreateBusinessSector createBusinessSector,Authentication authorization) {
    	Optional<BusinessSector> accOptional =businessSectorRepository.findBysector(createBusinessSector.getBusinessSector());
    	if (accOptional.isPresent()) {
    		return ResponseEntity.badRequest().body(new ResponseError("Business Sector already exist!"));
		}
    	BusinessSector businessSector = new BusinessSector();
    	businessSector.setSector(createBusinessSector.getBusinessSector());
    	businessSector.setCreatedDateTime(LocalDate.now());
    	businessSector.setCreatorid(adminRepository.findByUsername(authorization.getName()).get());
    	businessSectorRepository.save(businessSector);
    	return ResponseEntity.ok(new UpdateResponse("Business Sector created successfully"));
	}
}
