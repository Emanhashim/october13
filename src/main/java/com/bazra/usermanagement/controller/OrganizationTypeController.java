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
import com.bazra.usermanagement.model.OrganizationType;
import com.bazra.usermanagement.repository.AdminRepository;
import com.bazra.usermanagement.repository.OrganizationTypeRepository;
import com.bazra.usermanagement.request.CreateAuthTypeRequest;
import com.bazra.usermanagement.request.CreateOrgTypeRequest;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.UpdateResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/OrgType")
public class OrganizationTypeController {
	@Autowired
	OrganizationTypeRepository organizationTypeRepository;
	@Autowired
	AdminRepository adminRepository;
	@PostMapping("/CreateOrgType")
	@ApiOperation(value ="WE CAN CREATE AUTHENTICATION TYPES")
    public ResponseEntity<?> createOrgType(@RequestBody CreateOrgTypeRequest createOrgTypeRequest,Authentication authorization) {
    	Optional<OrganizationType> accOptional =organizationTypeRepository.findByorgType(createOrgTypeRequest.getOrganizationType());
    	if (accOptional.isPresent()) {
    		return ResponseEntity.badRequest().body(new ResponseError("Organization type already exist!"));
		}
    	OrganizationType organizationType = new OrganizationType();
    	organizationType.setOrgType(createOrgTypeRequest.getOrganizationType());
    	organizationType.setCreateDate(LocalDate.now());
    	organizationType.setCreatorid(adminRepository.findByUsername(authorization.getName()).get());
    	organizationTypeRepository.save(organizationType);
    	return ResponseEntity.ok(new UpdateResponse("Organization Type created successfully"));
	}
}
