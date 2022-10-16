package com.bazra.usermanagement.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountBalance;
import com.bazra.usermanagement.model.AccountType;
import com.bazra.usermanagement.model.AddressElement;
import com.bazra.usermanagement.model.AdminInfo;
import com.bazra.usermanagement.model.AgentAddress;
import com.bazra.usermanagement.model.AgentContact;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.AuthenticationType;
import com.bazra.usermanagement.model.BazraBalance;
import com.bazra.usermanagement.model.BusinessSector;
import com.bazra.usermanagement.model.BusinessType;
import com.bazra.usermanagement.model.CommunicationMedium;
import com.bazra.usermanagement.model.Document_Type;
import com.bazra.usermanagement.model.Levels;
import com.bazra.usermanagement.model.LoginIDType;
import com.bazra.usermanagement.model.MasterAgentInfo;
import com.bazra.usermanagement.model.OrganizationAddress;
import com.bazra.usermanagement.model.OrganizationContact;
import com.bazra.usermanagement.model.OrganizationDocument;
import com.bazra.usermanagement.model.OrganizationInfo;
import com.bazra.usermanagement.model.OrganizationType;
import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.UserAuthentication;
import com.bazra.usermanagement.model.UserContact;
import com.bazra.usermanagement.model.UserCredential;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserRoles;
import com.bazra.usermanagement.repository.AccountBalanceRepository;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AccountTypeRepository;
import com.bazra.usermanagement.repository.AddressElementRepository;
import com.bazra.usermanagement.repository.AdminRepository;
import com.bazra.usermanagement.repository.AgentAddressRepository;
import com.bazra.usermanagement.repository.AgentContactRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.AuthTypeRepository;
import com.bazra.usermanagement.repository.BazraBalanceRepository;
import com.bazra.usermanagement.repository.BusinessSectorRepository;
import com.bazra.usermanagement.repository.BusinessTypeRepository;
import com.bazra.usermanagement.repository.CommunicationMediumRepository;
import com.bazra.usermanagement.repository.DocumentTypeRepository;
import com.bazra.usermanagement.repository.LoginIdTypeRepository;
import com.bazra.usermanagement.repository.MasterAgentRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.OrganizationAddressRepository;
import com.bazra.usermanagement.repository.OrganizationContactRepository;
import com.bazra.usermanagement.repository.OrganizationDocumentRepository;
import com.bazra.usermanagement.repository.OrganizationInfoRepository;
import com.bazra.usermanagement.repository.OrganizationTypeRepository;
import com.bazra.usermanagement.repository.RoleRepository;
import com.bazra.usermanagement.repository.SecurityQuestionsRepository;
import com.bazra.usermanagement.repository.UserAuthenticationRepository;
import com.bazra.usermanagement.repository.UserContactRepository;
import com.bazra.usermanagement.repository.UserCredentialRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.repository.UserRoleRepository;
import com.bazra.usermanagement.repository.UserSecurityRepository;
import com.bazra.usermanagement.request.AdminSignupRequest;
import com.bazra.usermanagement.request.MasterSignupRequest2;
import com.bazra.usermanagement.request.SignUpPinRequest;
import com.bazra.usermanagement.request.SignUpPinVerificationRequest;
import com.bazra.usermanagement.request.SignUpRequest;
import com.bazra.usermanagement.response.AdminSignupResponse;
import com.bazra.usermanagement.response.AgentSignUpResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.SignUpResponse;
import com.bazra.usermanagement.response.UpdateResponse;



@Service
public class SignUpService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AgentRepository agentRepository;
	@Autowired
	MerchantRepository merchantRepository;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AccountService accountservice;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserSecurityRepository userSecurityRepository;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	SecurityQuestionsRepository securityQuestionsRepository;
	@Autowired
	AccountTypeRepository accountTypeRepository;
	@Autowired
	MasterAgentRepository masterAgentRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	AccountBalanceRepository accountBalanceRepository;
	@Autowired
	BazraBalanceRepository bazraBalanceRepository;
	@Autowired
	LoginIdTypeRepository loginIdTypeRepository;
	@Autowired
	AuthTypeRepository authTypeRepository;
	@Autowired
	UserCredentialRepository userCredentialRepository;
	@Autowired
	UserAuthenticationRepository userAuthenticationRepository;
	@Autowired
	UserContactRepository userContactRepository;
	@Autowired
	private RandomNumber randomNumber;
	@Autowired
	CommunicationMediumRepository communicationMediumRepository;
	@Autowired
	BusinessSectorRepository businessSectorRepository;
	@Autowired
	OrganizationTypeRepository organizationTypeRepository;
	@Autowired
	OrganizationInfoRepository organizationInfoRepository;
	@Autowired
	BusinessTypeRepository businessTypeRepository;
	@Autowired
	OrganizationAddressRepository organizationAddressRepository;
	@Autowired
	AddressElementRepository addressElementRepository;
	@Autowired
	DocumentTypeRepository documentTypeRepository;
	@Autowired
	OrganizationDocumentRepository organizationDocumentRepository;
	@Autowired
	AgentAddressRepository agentAddressRepository;
	@Autowired
	AgentContactRepository agentContactRepository;
	@Autowired
	OrganizationContactRepository organizationContactRepository;

	@Value("${tin.upload.path}")
	private String tinPath;

	@Value("${tread.upload.path}")
	private String treadPath;

	public ResponseEntity<?> adminSignup(AdminSignupRequest request) {
		String phone = "+251" + request.getUsername().substring(request.getUsername().length() - 9);
		boolean userExists1 = adminRepository.findByUsername(phone).isPresent();
//		if (!adminRepository.findAll().isEmpty()) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Only one Admin Account is permitted"));
//		}
		if (userExists1) {
			return ResponseEntity.badRequest().body(new SignUpResponse("User already exists "));
		}
		String pass1 = request.getPassword();

		if (!userInfoService.checkString(pass1)) {
			return ResponseEntity.badRequest().body(
					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
		}

		if (request.getUsername().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
		}
//		if (!roleRepository.findByrolename("ADMIN").isPresent()) {
//			return ResponseEntity.badRequest().body(new UpdateResponse("No such Role found"));
//		}
		if ((!userInfoService.checkUsername(request.getUsername()))) {

			return ResponseEntity.badRequest()
					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
		}

		AdminInfo adminInfo = new AdminInfo(phone);
		adminInfo.setUsername(phone);
		adminInfo.setRoles("ADMIN");
		adminRepository.save(adminInfo);
		Optional<AdminInfo> adminOptional = adminRepository.findByUsername(phone);

		Optional<AccountType> accOptional = accountTypeRepository.findByaccounttype("ADMIN");
		if (!accOptional.isPresent()) {
			AdminInfo adminInfo2 = adminOptional.get();
			AccountType accountType = new AccountType();
			accountType.setAccounttype("ADMIN");
			accountType.setAdminInfo(adminInfo2);
			accountType.setCreatedDateTime(LocalDate.now());
			accountTypeRepository.save(accountType);

		}
		Optional<AccountType> accOptionall = accountTypeRepository.findByaccounttype("ADMIN");
		AccountType accountType = accOptionall.get();

		AdminInfo adminInfo2 = adminRepository.findByUsername(phone).get();
		Optional<Role> roleOptional = roleRepository.findByrolename("ADMIN");
		if (!roleOptional.isPresent()) {
			Role role = new Role();
			role.setAdmin(adminInfo);
			role.setCreatedDate(LocalDate.now());
			role.setRolename("ADMIN");
			roleRepository.save(role);
		}

		UserRoles userRoles = new UserRoles();
		userRoles.setCreated_date(LocalDate.now());
		userRoles.setRole(roleRepository.findByrolename("ADMIN").get());
		userRoles.setAdminInfo(adminInfo2);
		userRoleRepository.save(userRoles);
		Account account = new Account(phone);

		accountTypeRepository.save(accountType);
		AccountType accounttype = accountTypeRepository.findByaccounttype("ADMIN").get();
		account.setAccountNumber(phone);
		account.setCreationDate(LocalDate.now());
		account.setType(accountType);
		account.setAdmin(adminInfo2);
		account.setStatus(true);
		account.setType(accounttype);
		accountRepository.save(account);
		Account account2 = accountRepository.findByAdmin(adminInfo2).get();
		BazraBalance accountBalance = new BazraBalance();
		accountBalance.setBalance(new BigDecimal(1000));
		accountBalance.setAccount(account2);
		accountBalance.setCreateDate(LocalDate.now());
		bazraBalanceRepository.save(accountBalance);

//		Optional<LoginIDType> optional = loginIdTypeRepository.findBylogintype("PHONE");
//		if (!optional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("Login ID Type doesnot exist!"));
//		}
//		Optional<AuthenticationType> optionalauth = authTypeRepository.findByauthenticationtype("PIN");
//		if (!optionalauth.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("Authentication Type doesnot exist!"));
//		}
//		AuthenticationType authenticationType = optionalauth.get();
//		LoginIDType loginIDType = optional.get();

		AdminInfo userInfo2 = adminRepository.findByUsername(phone).get();
		UserCredential userCredential = new UserCredential();

		userCredential.setLoginID(phone);
		userCredential.setActive(true);
		userCredential.setLocalDate(LocalDate.now());
//		userCredential.setLoginIDType(loginIDType);
		userCredential.setAdminInfo(userInfo2);
		userCredentialRepository.save(userCredential);
		UserAuthentication userAuthentication = new UserAuthentication();
//		userAuthentication.setAuthenticationType(authenticationType);
		userAuthentication.setAuthenticationValue(passwordEncoder.encode(request.getPassword()));
		userAuthentication.setLocalDate(LocalDate.now());
		userAuthentication.setAdminInfo(userInfo2);
		userAuthenticationRepository.save(userAuthentication);

		return ResponseEntity.ok(new AdminSignupResponse(adminInfo2.getUsername(), "Successfully Registered"));
	}

	public ResponseEntity<?> createWallet(SignUpRequest request) {
		if (request.getPhone() == null) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone number "));
		}
		if (request.getEmail() == null) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your email "));
		}
		if (request.getFirstName() == null) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
		}
		if (request.getFatheName() == null) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father's name "));
		}
		if (request.getMotherName() == null) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your mother's name "));
		}
		if (request.getPhone() == null) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone number "));
		}

		if ((!userInfoService.checkBirthdate(request.getBirthDay()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Invalid date value"));
		}

		String username = "+251" + request.getPhone().substring(request.getPhone().length() - 9);
		Optional<UserInfo> userOptional = userRepository.findByUsername(username);
		boolean userExists1 = userOptional.isPresent();
//		boolean userExist1 = userRepository.findByEmail(request.getEmail()).isPresent();
//		boolean agentExist1 = agentRepository.findByEmail(request.getEmail()).isPresent();
//		boolean merchantExist1 = merchantRepository.findByEmail(request.getEmail()).isPresent();

		boolean roleexist = roleRepository.findByrolename("USER").isPresent();
		if (!roleexist) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Role not defined "));
		}
		Role role = roleRepository.findByrolename("USER").get();
//		if (userExist1 || agentExist1 || merchantExist1 ) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Email already in use!"));
//		}
		if (!userExists1) {
			return ResponseEntity.badRequest().body(new SignUpResponse("No User found "));
		}
		if ((!userInfoService.checkname(request.getFirstName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if ((!userInfoService.checkLastname(request.getFatheName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if ((!userInfoService.checkLastname(request.getMotherName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"MotherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
//		
		if (request.getEmail().isBlank()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your email "));
		}
		if (request.getFirstName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
		}
		if (request.getFatheName().isBlank()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father's name "));
		}
		if (request.getMotherName().isBlank()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your mother's name "));
		}
		if (request.getPhone().isBlank()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone number "));
		}

		if ((!userInfoService.checkBirthdate(request.getBirthDay()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Invalid date value"));
		}

		Optional<AccountType> accOptional = accountTypeRepository.findByaccounttype("USER");
		if (!accOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new UpdateResponse("No Account Type found"));
		}
		Optional<Role> roleOptional = roleRepository.findByrolename("USER");
		if (!roleOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Role USER doesnot exist!"));
		}
		Role userrRole = roleOptional.get();
		UserInfo userInfo = userOptional.get();
		userInfo.setBirthDay(request.getBirthDay());
		userInfo.setEmail(request.getEmail());
		userInfo.setFatherName(request.getFatheName());
		userInfo.setMotherName(request.getMotherName());
		userInfo.setRoles(userrRole);
		userInfo.setLevels(Levels.LEVEL_1);
		userInfo.setName(request.getFirstName());

		userRepository.save(userInfo);
		UserInfo userInfo2 = userRepository.findByUsername(username).get();
		UserRoles userRoles = new UserRoles();
		userRoles.setCreated_date(LocalDate.now());
		userRoles.setRole(roleRepository.findByrolename("USER").get());
		userRoles.setUserInfo(userInfo2);
		userRoleRepository.save(userRoles);
		Optional<CommunicationMedium> optional = communicationMediumRepository.findBytitle("SMS");
		Optional<CommunicationMedium> optional2 = communicationMediumRepository.findBytitle("EMAIL");

		if (optional.isPresent()) {
			UserContact userContact = new UserContact();
			userContact.setUserInfo(userInfo2);
			userContact.setCommunicationMedium(optional.get());
			userContact.setContact(request.getPhone());
			userContact.setIs_active(true);
			userContact.setRegistereDate(LocalDate.now());
			userContactRepository.save(userContact);
			if (optional2.isPresent()) {
				UserContact userContact2 = new UserContact();
				userContact2.setUserInfo(userInfo2);
				userContact2.setCommunicationMedium(optional.get());
				userContact2.setContact(request.getPhone());
				userContact2.setIs_active(true);
				userContact2.setRegistereDate(LocalDate.now());
				userContactRepository.save(userContact2);
			}
		}

		AccountType accountType = accOptional.get();
		Account account = new Account(userInfo2.getUsername());

		AccountBalance accountBalance = new AccountBalance();
		account.setUser(userInfo2);
		account.setType(accountType);
		account.setAccountNumber(userInfo2.getUsername());
		accountRepository.save(account);
		accountBalance.setAccount(account);
		accountBalance.setBalance(new BigDecimal(1000));
		accountBalance.setCreateDate(LocalDate.now());
		accountBalanceRepository.save(accountBalance);
		return ResponseEntity.ok(new SignUpResponse(userInfo2.getName(), userInfo2.getRoles(),
				"Successfully Registered", userInfo2.getName(), userInfo2.getFatherName(), userInfo.getLevels()));
	}

//	public ResponseEntity<?> signUpAgent(AgentSignUpRequest request) {
//		String phone = "+251" + request.getUsername().substring(request.getUsername().length() - 9);
//		boolean userExists1 = agentRepository.findByUsername(phone).isPresent();
////        Aagent = accountRepository.findByusername(request.getUsername());
//		if (userExists1) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
//		}
//		String pass1 = request.getPassword();
//		String pass2 = request.getConfirmPassword();
//
//		if (!pass1.matches(pass2)) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Passwords don't match!"));
//		}
//		if (accountRepository.findByAccountNumberEquals(phone).isPresent()) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
//		}
//
//		if (!userInfoService.checkString(pass1)) {
//			return ResponseEntity.badRequest().body(
//					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
//		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
//				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
//		}
//
//		if ((!userInfoService.checkname(request.getFirstName()))) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse(
//					"First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
//		}
//		if ((!userInfoService.checkLastname(request.getLastName()))) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse(
//					"FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
//		}
//
//		if (request.getFirstName().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
//		}
//		if (request.getLastName().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father name "));
//		}
//		if (request.getUsername().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
//		}
//		if (request.getLicenceNumber().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your licence number "));
//		}
//		if (request.getBusinessLNum().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business licence number "));
//		}
//
//		if ((!userInfoService.checkUsername(request.getUsername()))) {
//
//			return ResponseEntity.badRequest()
//					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
//		}
//
//		
//		AgentInfo agentInfo = new AgentInfo(request.getFirstName(), request.getLastName(),
//				passwordEncoder.encode(request.getPassword()), phone);
//		agentInfo.setLicenceNumber(request.getLicenceNumber());
//		agentInfo.setCompanyName(request.getCompanyName());
//		agentInfo.setBusinessLNum(request.getBusinessLNum());
//		agentInfo.setRoles("AGENT");
//
//		agentRepository.save(agentInfo);
//		Optional<AccountType> accOptional = accountTypeRepository.findByaccounttype("AGENT");
//		if (!accOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new UpdateResponse("No Account Type found "));
//		}
//		AccountType accountType = accOptional.get();
//		AgentInfo agentInfo2 = agentRepository.findByUsername(phone).get();
//		UserRoles userRoles = new UserRoles();
//		userRoles.setCreated_date(LocalDate.now());
//		userRoles.setRole(roleRepository.findByrolename("AGENT").get());
//		userRoles.setAgentInfo(agentInfo2);
//		userRoleRepository.save(userRoles);
//		Account account = new Account();
//		account.setAccountNumber(phone);
//		account.setAgent(agentInfo2);
//		account.setCommission(new BigDecimal(0));
//		account.setStatus(true);
//		account.setCreationDate(LocalDate.now());
//		account.setType(accountType);
//		
//		accountRepository.save(account);
//		Account account2 = accountRepository.findByAccountNumberEquals(phone).get();
//		AccountBalance accountBalance = new AccountBalance();
//		accountBalance.setBalance(new BigDecimal(1000));
//		accountBalance.setAccount(account2);
//		accountBalance.setCreateDate(LocalDate.now());
//		accountBalanceRepository.save(accountBalance);
//		return ResponseEntity.ok(new SignUpResponse(phone, agentInfo2.getRoles(),
//				"Successfully Registered", agentInfo2.getFirstName(), agentInfo2.getLastName()));
//	}

//	public ResponseEntity<?> signUpMerchant(AgentSignUpRequest request) {
//		String phone = "+251" + request.getUsername().substring(request.getUsername().length() - 9);
//		boolean userExists1 = merchantRepository.findByUsername(phone).isPresent();
////        Aagent = accountRepository.findByusername(request.getUsername());
//		if (userExists1) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
//		}
//		String pass1 = request.getPassword();
//		String pass2 = request.getConfirmPassword();
//
//		if (!pass1.matches(pass2)) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Passwords don't match!"));
//		}
//		if (accountRepository.findByAccountNumberEquals(phone).isPresent()) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
//		}
//
//		if (!userInfoService.checkString(pass1)) {
//			return ResponseEntity.badRequest().body(
//					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
//		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
//				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
//		}
//
//		if ((!userInfoService.checkname(request.getFirstName()))) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse(
//					"First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
//		}
//		if ((!userInfoService.checkLastname(request.getLastName()))) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse(
//					"FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
//		}
//
//		if (request.getFirstName().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
//		}
//		if (request.getLastName().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father name "));
//		}
//		if (request.getUsername().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
//		}
//		if (request.getLicenceNumber().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your licence number "));
//		}
//		if (request.getBusinessLNum().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business licence number "));
//		}
//
//		if ((!userInfoService.checkUsername(request.getUsername()))) {
//
//			return ResponseEntity.badRequest()
//					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
//		}
//
//		MerchantInfo merchantInfo = new MerchantInfo(request.getFirstName(), request.getLastName(),
//				passwordEncoder.encode(request.getPassword()), phone);
//		merchantInfo.setLicenceNumber(request.getLicenceNumber());
//		merchantInfo.setCompanyName(request.getCompanyName());
//		merchantInfo.setBusinessLNum(request.getBusinessLNum());
//		merchantInfo.setRoles("MERCHANT");
//
//		merchantRepository.save(merchantInfo);
//
//		Optional<AccountType> accOptional = accountTypeRepository.findByaccounttype("MERCHANT");
//		if (!accOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new UpdateResponse("No Account Type found "));
//		}
//		AccountType accountType = accOptional.get();
//		MerchantInfo merchantInfo1 = merchantRepository.findByUsername(phone).get();
//		UserRoles userRoles = new UserRoles();
//		userRoles.setCreated_date(LocalDate.now());
//		userRoles.setRole(roleRepository.findByrolename("MERCHANT").get());
//		userRoles.setMerchantInfo(merchantInfo1);
//		userRoleRepository.save(userRoles);
//		Account account = new Account();
//		account.setAccountNumber(phone);
//		account.setMerchant(merchantInfo1);
//		account.setCommission(new BigDecimal(0));
//		account.setStatus(true);
//		account.setCreationDate(LocalDate.now());
//		account.setType(accountType);
//
//		accountRepository.save(account);
//		Account account2 = accountRepository.findByAccountNumberEquals(phone).get();
//		AccountBalance accountBalance = new AccountBalance();
//		accountBalance.setBalance(new BigDecimal(1000));
//		accountBalance.setAccount(account2);
//		accountBalance.setCreateDate(LocalDate.now());
//		accountBalanceRepository.save(accountBalance);
//		return ResponseEntity.ok(new SignUpResponse(phone, merchantInfo1.getRoles(), "Successfully Registered",
//				merchantInfo1.getName(), merchantInfo.getLastName()));
//	}

//	public ResponseEntity<?> signUpMaster(MasterSignupRequest request) {
//		boolean userExists1 = masterAgentRepository.findByphoneNumber(request.getPhone()).isPresent();
//
//		if (userExists1) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
//		}
//
//		if (request.getPhone().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone number"));
//		}
//
//		if (request.getUsername().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your user name"));
//		}
//
//		if (request.getEmail().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your email"));
//		}
//
//		if (request.getRegion().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your region"));
//		}
//
//		if (request.getCity().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your city"));
//		}
//
//		if (request.getOrganizationType().isBlank()) {
//
//			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your organization type"));
//		}
//
//		AgentInfo agentInfo = new AgentInfo();
//		agentInfo.setFatherName(tinPath);
//
//		if ((!userInfoService.checkUsername(masterAgentInfo.getPhoneNumber()))) {
//
//			return ResponseEntity.badRequest()
//					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
//		}
//
//		if (accountRepository.findByAccountNumberEquals(request.getPhone()).isPresent()) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
//		}
//
//		masterAgentInfo.setRoles("MASTER");
//		Optional<AccountType> accOptional = accountTypeRepository.findByaccounttype("MASTER");
//		if (!accOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new UpdateResponse("No Account Type found "));
//		}
//		AccountType accountType = accOptional.get();
//		masterAgentRepository.save(masterAgentInfo);
//		Account account = new Account(masterAgentInfo.getPhoneNumber(), masterAgentInfo.getUsername());
//		account.setType(accountType);
//		accountservice.save(account);
//		AccountBalance accountBalance = new AccountBalance();
//		accountBalance.setAccount(account);
//		accountBalance.setBalance(new BigDecimal(1000));
//		accountBalance.setCreateDate(LocalDate.now());
//		accountBalanceRepository.save(accountBalance);
//		return ResponseEntity.ok(new MasterSignupResponse("Successfully Registered", masterAgentInfo.getUsername(),
//				masterAgentInfo.getRoles(), masterAgentInfo.getEmail(), masterAgentInfo.getOrganizationType()));
//		// return ResponseEntity.badRequest().body(new SignUpResponse("Enter your
//		// business licence number "));
//	}

	public ResponseEntity<?> signUpMaster2(MasterSignupRequest2 request, Authentication authentication)
			throws IOException {
		String agentusername = "+251" + request.getAgentPhone().substring(request.getAgentPhone().length() - 9);
//		String masterusername = "+251" + authentication.getName().substring(authentication.getName().length() - 9);
		Optional<AgentInfo> userExists1 = agentRepository.findByUsername(agentusername);
		Optional<UserInfo> userExists2 = userRepository.findByUsername(agentusername);
		Optional<MasterAgentInfo> masterAgentInfoopOptional = masterAgentRepository
				.findByuserName(authentication.getName());
		if (userExists1.isPresent() || userExists2.isPresent()) {
			System.out.println(userExists1.get().getFatherName());
			return ResponseEntity.badRequest().body(new SignUpResponse("Username already in use!"));
		}
//		if (!request.getTinNumber().isEmpty()) {
//			return ResponseEntity.badRequest().body(new SignUpResponse("Already passed this stage"));
//		}

		AgentInfo agentInfo = new AgentInfo();
		if (!masterAgentInfoopOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Not authorized for this request!"));
		}
		MasterAgentInfo masterAgentInfo = masterAgentInfoopOptional.get();
		if (request.getOrganizationName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your Organization Name"));
		}

		if (request.getOrganizationType().equals(null)) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your organization type"));
		}

		if (request.getBusinessSector().equals(null)) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business sector"));
		}

		if (request.getBusinessType().equals(null)) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business type"));
		}

		if (request.getTinNumber().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your tin number"));
		}

		if (request.getCapital().equals(null)) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your capital"));
		}

		String tinName = StringUtils.cleanPath(request.getTinCertificate().getOriginalFilename());
		String treadName = StringUtils.cleanPath(request.getTreadCertificate().getOriginalFilename());

		if (tinName.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("Tin Certificate must be attached"));
		}
		if (treadName.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("Tread Certificate must be attached"));
		}

		Optional<BusinessSector> businessSectorOptional = businessSectorRepository
				.findBysector(request.getBusinessSector());
		if (!businessSectorOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid business sector!"));
		}
		BusinessSector businessSector = businessSectorOptional.get();
		Optional<OrganizationType> orgTypeOptional = organizationTypeRepository
				.findByorgType(request.getOrganizationType());

		if (!orgTypeOptional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("Organization type " + request.getOrganizationType() + " doesnot exist!"));
		}
		if (!orgTypeOptional.get().getOrgType().matches(request.getRole())) {
			return ResponseEntity.badRequest().body(new ResponseError("Organization type and role must match!"));
		}
		OrganizationType organizationType = orgTypeOptional.get();
		Optional<BusinessType> businesstypeOptional = businessTypeRepository
				.findBybusinessType(request.getBusinessType());
		if (!businesstypeOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid business type!"));
		}
		Optional<AddressElement> optionaladdresselementCITY = addressElementRepository.findBytitle("CITY");
		if (!optionaladdresselementCITY.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element CITY found"));
		}
		Optional<AddressElement> optionaladdresselementREGION = addressElementRepository.findBytitle("REGION");
		if (!optionaladdresselementREGION.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element REGION found"));
		}

		Optional<AddressElement> optionaladdresselementSUBCITY = addressElementRepository.findBytitle("SUB CITY");
		if (!optionaladdresselementSUBCITY.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element SUB CITY found"));
		}
		Optional<AddressElement> optionaladdresselementWOREDA = addressElementRepository.findBytitle("WOREDA");
		if (!optionaladdresselementWOREDA.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element WOREDA found"));
		}
		Optional<AddressElement> optionaladdresselementSPECLOCATION = addressElementRepository
				.findBytitle("SPECIFIC LOCATION");
		if (!optionaladdresselementSPECLOCATION.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element SPECIFIC LOCATION found"));
		}
		Optional<AddressElement> optionaladdresselementHNUM = addressElementRepository.findBytitle("HOUSE NUMBER");
		if (!optionaladdresselementHNUM.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No address element HOUSE NUMBER found"));
		}
		Optional<CommunicationMedium> optionalEMAIL = communicationMediumRepository.findBytitle("EMAIL");

		if (!optionalEMAIL.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No communication medium EMAIL found"));
		}
		Optional<CommunicationMedium> optionalSMS = communicationMediumRepository.findBytitle("SMS");

		if (!optionalSMS.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No communication medium SMS found"));
		}
		Optional<Document_Type> documentTypeTINOptional = documentTypeRepository.findByName("TIN");
		if (!documentTypeTINOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No document type TIN found"));
		}
		Optional<Document_Type> documentTypeTreadOptional = documentTypeRepository.findByName("TREAD");
		if (!documentTypeTreadOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No document type TREAD found"));
		}
		AddressElement addressElementREGION = optionaladdresselementREGION.get();
		Optional<OrganizationInfo> organizationinfOptional = organizationInfoRepository
				.findByorgName(request.getOrganizationName());
		if (organizationinfOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Organization already registered"));
		}
		Optional<AgentInfo> agentOptional = agentRepository.findByUsername(agentusername);
		if (agentOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Agent username already in user"));
		}
		BusinessType businessType = businesstypeOptional.get();
		OrganizationInfo organizationInfo = new OrganizationInfo();

		organizationInfo.setOrgName(request.getOrganizationName());
		organizationInfo.setBusinessSector(businessSector);
		organizationInfo.setOrganizationType(organizationType);
		organizationInfo.setBusinessType(businessType);
		organizationInfo.setCapital(request.getCapital());
		organizationInfo.setCreatorid(masterAgentInfo);
		organizationInfo.setRegisterDate(LocalDate.now());
		organizationInfo.setTin(request.getTinNumber());

		organizationInfoRepository.save(organizationInfo);
		Optional<OrganizationInfo> organizationinfOptional1 = organizationInfoRepository
				.findByorgName(request.getOrganizationName());
		if (!organizationinfOptional1.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No organization found for this name!"));
		}
		OrganizationInfo organizationInfo2 = organizationinfOptional1.get();
		if (request.getCity() != null) {

			AddressElement addressElement = optionaladdresselementCITY.get();
			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();
			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElement)) {
						organizationAddress = optionalOrganizationAddresses.get(i);
					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();

					organizationAddress1.setAddress(request.getCity());
					organizationAddress1.setAddressElement(addressElement);
					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getCity());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress1.setAddress(request.getCity());
				organizationAddress1.setAddressElement(addressElement);
				organizationAddress1.setIsactive(true);

				organizationAddress1.setRegisteredDate(LocalDate.now());
				organizationAddress1.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress1);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getRegion() != null) {

//			AddressElement addressElement = optionaladdresselementREGION.get();
			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();
			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElementREGION)) {
						organizationAddress = optionalOrganizationAddresses.get(i);
					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();
					organizationAddress1.setAddress(request.getRegion());
					organizationAddress1.setAddressElement(addressElementREGION);

					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getRegion());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress1.setAddress(request.getRegion());
				organizationAddress1.setAddressElement(addressElementREGION);

				organizationAddress1.setIsactive(true);
				organizationAddress1.setRegisteredDate(LocalDate.now());
				organizationAddress1.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress1);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getSubcity() != null) {

			AddressElement addressElement = optionaladdresselementSUBCITY.get();

			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();

			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElement)) {
						organizationAddress = optionalOrganizationAddresses.get(i);

					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();
					organizationAddress1.setAddress(request.getSubcity());
					organizationAddress1.setAddressElement(addressElement);
					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getSubcity());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
//				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress.setAddress(request.getSubcity());
				organizationAddress.setAddressElement(addressElement);
				organizationAddress.setIsactive(true);
				organizationAddress.setRegisteredDate(LocalDate.now());
				organizationAddress.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getWoreda() != null) {

			AddressElement addressElement = optionaladdresselementWOREDA.get();
			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();
			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElement)) {
						organizationAddress = optionalOrganizationAddresses.get(i);
					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();
					organizationAddress1.setAddress(request.getWoreda());
					organizationAddress1.setAddressElement(addressElement);
					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getWoreda());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress1.setAddress(request.getWoreda());
				organizationAddress1.setAddressElement(addressElement);
				organizationAddress1.setIsactive(true);
				organizationAddress1.setRegisteredDate(LocalDate.now());
				organizationAddress1.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress1);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getSpecificLocation() != null) {

			AddressElement addressElement = optionaladdresselementSPECLOCATION.get();
			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();
			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElement)) {
						organizationAddress = optionalOrganizationAddresses.get(i);
					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();
					organizationAddress1.setAddress(request.getSpecificLocation());
					organizationAddress1.setAddressElement(addressElement);
					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getSpecificLocation());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress1.setAddress(request.getSpecificLocation());
				organizationAddress1.setAddressElement(addressElement);
				organizationAddress1.setIsactive(true);
				organizationAddress1.setRegisteredDate(LocalDate.now());
				organizationAddress1.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress1);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getHouseNumber() != null) {

			AddressElement addressElement = optionaladdresselementHNUM.get();
			List<OrganizationAddress> optionalOrganizationAddresses = organizationAddressRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationAddress organizationAddress = new OrganizationAddress();
			if (optionalOrganizationAddresses.size() >= 1) {
				for (int i = 0; i < optionalOrganizationAddresses.size(); i++) {
					if (optionalOrganizationAddresses.get(i).getAddressElement().equals(addressElement)) {
						organizationAddress = optionalOrganizationAddresses.get(i);
					}

				}
				if (organizationAddress.getAddress() == null) {
					OrganizationAddress organizationAddress1 = new OrganizationAddress();
					organizationAddress1.setAddress(request.getHouseNumber());
					organizationAddress1.setAddressElement(addressElement);
					organizationAddress1.setIsactive(true);
					organizationAddress1.setRegisteredDate(LocalDate.now());
					organizationAddress1.setOrganizationInfo(organizationInfo2);
					organizationAddressRepository.save(organizationAddress1);
				} else {
					organizationAddress.setAddress(request.getHouseNumber());
					organizationAddressRepository.save(organizationAddress);
				}

			} else {
				OrganizationAddress organizationAddress1 = new OrganizationAddress();
				organizationAddress1.setAddress(request.getHouseNumber());
				organizationAddress1.setAddressElement(addressElement);
				organizationAddress1.setIsactive(true);
				organizationAddress1.setRegisteredDate(LocalDate.now());
				organizationAddress1.setOrganizationInfo(organizationInfo2);
				organizationAddressRepository.save(organizationAddress1);

			}
//System.out.println(optionalUserAddress.isEmpty());

		}

		if (request.getTinCertificate() != null) {

			Document_Type document_Type = documentTypeTINOptional.get();
			String tinUploadDir = tinPath + organizationInfo2.getOrgName();

			String treaduploadDir = treadPath + organizationInfo2.getOrgName();

//			UserInfoService.saveTIN(tinuploadDir, tinName, tin);
//			UserInfoService.saveTREAD(treaduploadDir, treadName, tread);
			tinName = StringUtils.cleanPath(request.getTinCertificate().getOriginalFilename());
			UserInfoService.saveTIN(tinUploadDir, tinName, request.getTinCertificate());
			String path = tinPath + tinName;

			List<OrganizationDocument> organizationDocumentsList = organizationDocumentRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationDocument organizationDocument = new OrganizationDocument();

			if (organizationDocumentsList.size() >= 1) {
				for (int i = 0; i < organizationDocumentsList.size(); i++) {
					if (organizationDocumentsList.get(i).getDocumentType().equals(document_Type)) {
						organizationDocument = organizationDocumentsList.get(i);
					}

				}
				if (organizationDocument.getDocumentPath() == null) {
					tinName = StringUtils.cleanPath(request.getTinCertificate().getOriginalFilename());
					UserInfoService.saveTIN(tinUploadDir, tinName, request.getTinCertificate());
					path = tinPath + tinName;
					OrganizationDocument organizationDocument1 = new OrganizationDocument();
					organizationDocument1.setDocumentType(document_Type);
					organizationDocument1.setDocumentPath(path);
					organizationDocument1.setIs_valid(false);
					organizationDocument1.setUploadeDate(LocalDate.now());
					organizationDocument1.setOrganizationInfo(organizationInfo2);
					organizationDocumentRepository.save(organizationDocument1);
				} else {
					tinName = StringUtils.cleanPath(request.getTinCertificate().getOriginalFilename());
					UserInfoService.saveTIN(tinUploadDir, tinName, request.getTinCertificate());
					path = tinPath + tinName;
					organizationDocument.setUploadeDate(LocalDate.now());
					organizationDocument.setDocumentPath(path);
					organizationDocumentRepository.save(organizationDocument);
				}

			} else {
				tinName = StringUtils.cleanPath(request.getTinCertificate().getOriginalFilename());
				UserInfoService.saveTIN(tinUploadDir, tinName, request.getTinCertificate());
				path = tinPath + tinName;
				OrganizationDocument organizationDocument1 = new OrganizationDocument();
				organizationDocument1.setDocumentType(document_Type);
				organizationDocument1.setDocumentPath(path);
				organizationDocument1.setIs_valid(false);
				organizationDocument1.setUploadeDate(LocalDate.now());
				organizationDocument1.setOrganizationInfo(organizationInfo2);
				organizationDocumentRepository.save(organizationDocument1);
			}

		}

		if (request.getTreadCertificate() != null) {

			Document_Type document_Type = documentTypeTreadOptional.get();

			String treaduploadDir = treadPath + organizationInfo2.getOrgName();

//			UserInfoService.saveTIN(tinuploadDir, tinName, tin);
//			UserInfoService.saveTREAD(treaduploadDir, treadName, tread);
			treadName = StringUtils.cleanPath(request.getTreadCertificate().getOriginalFilename());
			UserInfoService.saveTIN(treaduploadDir, treadName, request.getTinCertificate());
			String path = treadPath + treadName;

			List<OrganizationDocument> organizationDocumentsList = organizationDocumentRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationDocument organizationDocument = new OrganizationDocument();

			if (organizationDocumentsList.size() >= 1) {
				for (int i = 0; i < organizationDocumentsList.size(); i++) {
					if (organizationDocumentsList.get(i).getDocumentType().equals(document_Type)) {
						organizationDocument = organizationDocumentsList.get(i);
					}

				}
				if (organizationDocument.getDocumentPath() == null) {
					treadName = StringUtils.cleanPath(request.getTreadCertificate().getOriginalFilename());
					UserInfoService.saveTIN(treaduploadDir, treadName, request.getTinCertificate());
					path = treadPath + treadName;
					OrganizationDocument organizationDocument1 = new OrganizationDocument();
					organizationDocument1.setDocumentType(document_Type);
					organizationDocument1.setDocumentPath(path);
					organizationDocument1.setIs_valid(false);
					organizationDocument1.setUploadeDate(LocalDate.now());
					organizationDocument1.setOrganizationInfo(organizationInfo2);
					organizationDocumentRepository.save(organizationDocument1);
				} else {
					treadName = StringUtils.cleanPath(request.getTreadCertificate().getOriginalFilename());
					UserInfoService.saveTIN(treaduploadDir, treadName, request.getTinCertificate());
					path = treadPath + treadName;
					organizationDocument.setUploadeDate(LocalDate.now());
					organizationDocument.setDocumentPath(path);
					organizationDocumentRepository.save(organizationDocument);
				}

			} else {
				treadName = StringUtils.cleanPath(request.getTreadCertificate().getOriginalFilename());
				UserInfoService.saveTIN(treaduploadDir, treadName, request.getTinCertificate());
				path = treadPath + treadName;
				OrganizationDocument organizationDocument1 = new OrganizationDocument();
				organizationDocument1.setDocumentType(document_Type);
				organizationDocument1.setDocumentPath(path);
				organizationDocument1.setIs_valid(false);
				organizationDocument1.setUploadeDate(LocalDate.now());
				organizationDocument1.setOrganizationInfo(organizationInfo2);
				organizationDocumentRepository.save(organizationDocument1);
			}

		}
		Optional<Role> roleAgentOptional = roleRepository.findByrolename("AGENT");
		if (!roleAgentOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Role AGENT doesnot exist!"));
		}
		Optional<OrganizationInfo> orgOptional = organizationInfoRepository
				.findByorgName(request.getOrganizationName());
		if (!orgOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No organization with this name exist!"));
		}
		Optional<Role> roleMerchantOptional = roleRepository.findByrolename("MERCHANT");
		if (!roleMerchantOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Role MERCHANT doesnot exist!"));
		}
		Role role = new Role();

//		Role merchantRole = new Role();

		if (request.getRole().matches("AGENT")) {
			role = roleAgentOptional.get();
		} else {
			role = roleMerchantOptional.get();
		}

		agentInfo.setFatherName(request.getFatherName());
		agentInfo.setLastName(request.getLastname());
		agentInfo.setName(request.getName());
		agentInfo.setRoles(role);
		agentInfo.setUsername(agentusername);
		agentInfo.setOrganizationInfo(organizationInfo2);
		agentRepository.save(agentInfo);

		Optional<AgentInfo> agentOptionall = agentRepository.findByUsername(agentusername);
		if (!agentOptionall.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No Agent found"));
		}
		if (request.getPhoneNumber() != null) {
			Optional<CommunicationMedium> optional = communicationMediumRepository.findBytitle("SMS");

			if (!optional.isPresent()) {
				return ResponseEntity.badRequest().body(new ResponseError("No communication medium SMS found"));
			}
			CommunicationMedium communicationMedium = optional.get();
			List<OrganizationContact> optionalOrganizationContacts = organizationContactRepository
					.findByorganizationInfo(organizationInfo2);
			OrganizationContact organizationContact = new OrganizationContact();
			if (optionalOrganizationContacts.size() >= 1) {
				for (int i = 0; i < optionalOrganizationContacts.size(); i++) {
					if (optionalOrganizationContacts.get(i).getCommunicationMedium().equals(communicationMedium)) {
						organizationContact = optionalOrganizationContacts.get(i);
					}

				}
				if (organizationContact.getContact() == null) {
					OrganizationContact organizationContact1 = new OrganizationContact();
					organizationContact1.setCommunicationMedium(communicationMedium);
					organizationContact1.setContact(request.getPhoneNumber());
					organizationContact1.setOrganizationInfo(organizationInfo2);
					organizationContact1.setIs_active(false);
					organizationContact1.setRegistereDate(LocalDate.now());
					organizationContactRepository.save(organizationContact1);
				} else {
					organizationContact.setContact(request.getPhoneNumber());
					organizationContactRepository.save(organizationContact);
				}

			} else {
				OrganizationContact organizationContact1 = new OrganizationContact();
				organizationContact1.setCommunicationMedium(communicationMedium);
				organizationContact1.setContact(request.getPhoneNumber());
				organizationContact1.setOrganizationInfo(organizationInfo2);
				organizationContact1.setIs_active(false);
				organizationContact1.setRegistereDate(LocalDate.now());
				organizationContactRepository.save(organizationContact1);

			}
		}

		AgentInfo agentInfo2 = agentOptionall.get();
		if (request.getCity() != null) {
			Optional<AddressElement> optionaladdresselement = addressElementRepository.findBytitle("CITY");
			if (!optionaladdresselement.isPresent()) {
				return ResponseEntity.badRequest().body(new ResponseError("No address element CITY found"));
			}
			AddressElement addressElement = optionaladdresselement.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerCity());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerCity());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerCity());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}
		if (request.getOwnerHouseNumber() != null) {

			AddressElement addressElement = optionaladdresselementHNUM.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerHouseNumber());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerHouseNumber());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerHouseNumber());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}

		if (request.getOwnerRegion() != null) {

			AddressElement addressElement = optionaladdresselementREGION.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerRegion());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerRegion());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerRegion());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}

		if (request.getOwnerSubcity() != null) {

			AddressElement addressElement = optionaladdresselementSUBCITY.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerSubcity());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerSubcity());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerSubcity());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}

		if (request.getOwnerSpecificLocation() != null) {
			Optional<AddressElement> optionaladdresselement = addressElementRepository.findBytitle("SPECIFIC LOCATION");
			if (!optionaladdresselement.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No address element SPECIFIC LOCATION found"));
			}
			AddressElement addressElement = optionaladdresselement.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerSpecificLocation());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerSpecificLocation());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerSpecificLocation());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}

		if (request.getOwnerRegion() != null) {

			AddressElement addressElement = optionaladdresselementREGION.get();
			List<AgentAddress> optionalAgentAddresses = agentAddressRepository.findByagentInfo(agentInfo2);
			AgentAddress agentAddress = new AgentAddress();
			if (optionalAgentAddresses.size() >= 1) {
				for (int i = 0; i < optionalAgentAddresses.size(); i++) {
					if (optionalAgentAddresses.get(i).getAddressElement().equals(addressElement)) {
						agentAddress = optionalAgentAddresses.get(i);
					}

				}
				if (agentAddress.getAddress() == null) {
					AgentAddress agentAddress1 = new AgentAddress();
					agentAddress1.setAddress(request.getOwnerRegion());
					agentAddress1.setAddressElement(addressElement);
					agentAddress1.setIsactive(true);
					agentAddress1.setRegisteredDate(LocalDate.now());
					agentAddress1.setAgentInfo(agentInfo2);
					agentAddressRepository.save(agentAddress1);
				} else {
					agentAddress.setAddress(request.getOwnerRegion());
					agentAddressRepository.save(agentAddress);
				}

			} else {
				AgentAddress agentAddress1 = new AgentAddress();
				agentAddress1.setAddress(request.getOwnerRegion());
				agentAddress1.setAddressElement(addressElement);
				agentAddress1.setIsactive(true);
				agentAddress1.setRegisteredDate(LocalDate.now());
				agentAddress1.setAgentInfo(agentInfo2);
				agentAddressRepository.save(agentAddress1);

			}
		}

		if (request.getAgentemail() != null) {

			CommunicationMedium communicationMedium = optionalEMAIL.get();
			List<AgentContact> optionalAgentContacts = agentContactRepository.findByagentInfo(agentInfo2);
			AgentContact agentContact = new AgentContact();
			if (optionalAgentContacts.size() >= 1) {
				for (int i = 0; i < optionalAgentContacts.size(); i++) {
					if (optionalAgentContacts.get(i).getCommunicationMedium().equals(communicationMedium)) {
						agentContact = optionalAgentContacts.get(i);
					}

				}
				if (agentContact.getContact() == null) {
					AgentContact agentContact1 = new AgentContact();
					agentContact1.setAgentInfo(agentInfo2);
					agentContact1.setCommunicationMedium(communicationMedium);
					agentContact1.setContact(request.getAgentemail());
					agentContact1.setIs_active(false);
					agentContact1.setRegistereDate(LocalDate.now());
					agentContactRepository.save(agentContact1);
				} else {
					agentContact.setContact(request.getAgentemail());
					agentContactRepository.save(agentContact);
				}

			} else {
				AgentContact agentContact1 = new AgentContact();
				agentContact1.setAgentInfo(agentInfo2);
				agentContact1.setCommunicationMedium(communicationMedium);
				agentContact1.setContact(request.getAgentemail());
				agentContact1.setIs_active(false);
				agentContact1.setRegistereDate(LocalDate.now());
				agentContactRepository.save(agentContact1);

			}
		}

		if (request.getAgentPhone() != null) {

			CommunicationMedium communicationMedium = optionalSMS.get();
			List<AgentContact> optionalAgentContacts = agentContactRepository.findByagentInfo(agentInfo2);
			AgentContact agentContact = new AgentContact();
			if (optionalAgentContacts.size() >= 1) {
				for (int i = 0; i < optionalAgentContacts.size(); i++) {
					if (optionalAgentContacts.get(i).getCommunicationMedium().equals(communicationMedium)) {
						agentContact = optionalAgentContacts.get(i);
					}

				}
				if (agentContact.getContact() == null) {
					AgentContact agentContact1 = new AgentContact();
					agentContact1.setAgentInfo(agentInfo2);
					agentContact1.setCommunicationMedium(communicationMedium);
					agentContact1.setContact(request.getAgentPhone());
					agentContact1.setIs_active(false);
					agentContact1.setRegistereDate(LocalDate.now());
					agentContactRepository.save(agentContact1);
				} else {
					agentContact.setContact(request.getAgentPhone());
					agentContactRepository.save(agentContact);
				}

			} else {
				AgentContact agentContact1 = new AgentContact();
				agentContact1.setAgentInfo(agentInfo2);
				agentContact1.setCommunicationMedium(communicationMedium);
				agentContact1.setContact(request.getAgentPhone());
				agentContact1.setIs_active(false);
				agentContact1.setRegistereDate(LocalDate.now());
				agentContactRepository.save(agentContact1);

			}
		}
		Optional<LoginIDType> optional = loginIdTypeRepository.findBylogintype("PHONE");
		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Login ID Type doesnot exist!"));
		}
		Optional<AuthenticationType> optionalauth = authTypeRepository.findByauthenticationtype("PIN");
		if (!optionalauth.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Authentication Type doesnot exist!"));
		}
		Optional<AccountType> AGENTaccountTypeOptional = accountTypeRepository.findByaccounttype("AGENT");
		Optional<AccountType> MERCHANTaccountTypeOptional = accountTypeRepository.findByaccounttype("MERCHANT");
		if (!AGENTaccountTypeOptional.isPresent()) {

			return ResponseEntity.badRequest().body(new ResponseError("Account Type doesnot exist!"));
		}
		if (!MERCHANTaccountTypeOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Type doesnot exist!"));
		}
		AccountType AGENTaccounttype = AGENTaccountTypeOptional.get();
		AccountType MERCHANTaccounttype = MERCHANTaccountTypeOptional.get();
		String pin = "";
		for (int j = 0; j < 4; j++) {

			pin = pin + randomNumber.randomNumberGenerator(0, 9);
		}

		AuthenticationType authenticationType = optionalauth.get();
		LoginIDType loginIDType = optional.get();

		Account account = new Account(agentusername);
		account.setAccountNumber(agentusername);
		account.setCreationDate(LocalDate.now());
		if (request.getRole().matches("AGENT")) {
			account.setType(AGENTaccounttype);
			
		}
		else {
			account.setType(MERCHANTaccounttype);
		}
		
		account.setAgent(agentInfo2);
		
		account.setStatus(true);

		accountRepository.save(account);
		Optional<Account> accoutOptional = accountRepository.findByAccountNumberEquals(agentusername);
		if (!accoutOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No account found!"));
		}
		Account account2 = accoutOptional.get();
		AccountBalance accountBalance = new AccountBalance();
		accountBalance.setBalance(new BigDecimal(1000));
		accountBalance.setAccount(account2);
		accountBalance.setCreateDate(LocalDate.now());
		accountBalanceRepository.save(accountBalance);
		UserCredential userCredential = new UserCredential();
		System.out.println("Agent username" + agentusername);
		userCredential.setLoginID(agentusername);
		userCredential.setActive(true);
		userCredential.setLocalDate(LocalDate.now());
		userCredential.setLoginIDType(loginIDType);
		userCredential.setAgentInfo(agentInfo2);
		userCredentialRepository.save(userCredential);
		UserAuthentication userAuthentication = new UserAuthentication();
		userAuthentication.setAuthenticationType(authenticationType);
		userAuthentication.setAuthenticationValue(passwordEncoder.encode(pin));
		userAuthentication.setLocalDate(LocalDate.now());
		userAuthentication.setAgentInfo(agentInfo2);
		userAuthenticationRepository.save(userAuthentication);

		return ResponseEntity.ok(new AgentSignUpResponse(organizationInfo2.getOrgName(),
				organizationInfo2.getOrganizationType().getOrgType(), agentInfo2.getUsername(), agentInfo2.getName(),
				pin));
		// return ResponseEntity.badRequest().body(new SignUpResponse("Enter your
		// business licence number "));
	}

	public ResponseEntity<?> signUpUserPin(SignUpPinRequest request) {
		if ((!userInfoService.checkUsername(request.getPhone()))) {

			return ResponseEntity.badRequest()
					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 9 Digit"));
		}
		String phone = "+251" + request.getPhone().substring(request.getPhone().length() - 9);
		boolean userexist = accountRepository.findByAccountNumberEquals(phone).isPresent();
		boolean userExist = userRepository.findByUsername(phone).isPresent();
		boolean agentExist = agentRepository.findByUsername(phone).isPresent();
		boolean merchantExist = merchantRepository.findByUsername(phone).isPresent();
		boolean adminExist = adminRepository.findByUsername(phone).isPresent();

		if (userexist || agentExist || merchantExist || userExist || adminExist) {
			return ResponseEntity.badRequest().body(new ResponseError("Username already in use"));
		}
		if (userexist) {
			return ResponseEntity.badRequest().body(new ResponseError("Account already in use"));
		}
		String pin = "";
		for (int j = 0; j < 4; j++) {

			pin = pin + randomNumber.randomNumberGenerator(0, 9);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(phone);
		Optional<LoginIDType> optional = loginIdTypeRepository.findBylogintype("PHONE");
		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Login ID Type doesnot exist!"));
		}
		Optional<AuthenticationType> optionalauth = authTypeRepository.findByauthenticationtype("PIN");
		if (!optionalauth.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Authentication Type doesnot exist!"));
		}
		AuthenticationType authenticationType = optionalauth.get();
		LoginIDType loginIDType = optional.get();
		userRepository.save(userInfo);
		UserInfo userInfo2 = userRepository.findByUsername(phone).get();
		UserCredential userCredential = new UserCredential();

		userCredential.setLoginID(phone);
		userCredential.setActive(true);
		userCredential.setLocalDate(LocalDate.now());
		userCredential.setLoginIDType(loginIDType);
		userCredential.setUserInfo(userInfo2);
		userCredentialRepository.save(userCredential);
		UserAuthentication userAuthentication = new UserAuthentication();
		userAuthentication.setAuthenticationType(authenticationType);
		userAuthentication.setAuthenticationValue(pin);
		userAuthentication.setLocalDate(LocalDate.now());
		userAuthentication.setUserInfo(userInfo2);
		userAuthenticationRepository.save(userAuthentication);
		return ResponseEntity.ok(new UpdateResponse("Your Verification Code is: " + pin));
	}

	public ResponseEntity<?> signUpUserPinVerification(SignUpPinVerificationRequest request) {
		String phone = "+251" + request.getPhone().substring(request.getPhone().length() - 9);
		Optional<UserInfo> optional = userRepository.findByUsername(phone);

		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No user found!"));
		}
		UserInfo userInfo = optional.get();
		Optional<UserAuthentication> userOptional = userAuthenticationRepository.findByuserInfo(userInfo);
		if (!userOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("User Authentication not set!"));
		}

		UserAuthentication userAuthentication = userOptional.get();
		Optional<UserCredential> usercredOptional = userCredentialRepository.findByUserInfo(userInfo);
		if (!usercredOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("User Credential not set!"));
		}

		if (!userAuthentication.getAuthenticationValue().matches(request.getPin())) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a Valid PIN"));
		}
		userAuthentication.setAuthenticationValue(passwordEncoder.encode(userAuthentication.getAuthenticationValue()));
		userAuthenticationRepository.save(userAuthentication);
		return ResponseEntity.ok(new UpdateResponse("Verified successfully!"));
	}

	public ResponseEntity<?> adminMasterSignup(AdminSignupRequest request, Authentication authentication) {
		String phone = "+251" + request.getUsername().substring(request.getUsername().length() - 9);
		Optional<AdminInfo> adminOptional = adminRepository.findByUsername(phone);
		Optional<UserInfo> userOptional = userRepository.findByUsername(phone);
		Optional<AgentInfo> agentOptional = agentRepository.findByUsername(phone);
		Optional<MasterAgentInfo> masterOptional = masterAgentRepository.findByuserName(phone);
		AccountType accounttype = accountTypeRepository.findByaccounttype("MASTER AGENT").get();
		if (adminOptional.isPresent() || agentOptional.isPresent() || userOptional.isPresent()
				|| masterOptional.isPresent()) {

			return ResponseEntity.badRequest().body(new ResponseError("Username already in use!"));
		}
		Optional<LoginIDType> optional = loginIdTypeRepository.findBylogintype("PHONE");
		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Login ID Type doesnot exist!"));
		}
		Optional<AuthenticationType> optionalauth = authTypeRepository.findByauthenticationtype("PIN");
		if (!optionalauth.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Authentication Type doesnot exist!"));
		}
		Optional<AdminInfo> adminOptional2 = adminRepository.findByUsername(authentication.getName());
		if (!adminOptional2.get().getRoles().equals("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Not authorized for this request!"));
		}
		String pin = "";
		for (int j = 0; j < 4; j++) {

			pin = pin + randomNumber.randomNumberGenerator(0, 9);
		}
		Optional<Role> roleOptional = roleRepository.findByrolename("MASTER AGENT");
		if (!roleOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No role MASTER AGENT found!"));
		}
		Role masteragentRole = roleOptional.get();
		MasterAgentInfo masterAgentInfo = new MasterAgentInfo();
		masterAgentInfo.setUserName(phone);
		masterAgentInfo.setRoles(masteragentRole);
		AuthenticationType authenticationType = optionalauth.get();
		LoginIDType loginIDType = optional.get();

		Account account = new Account(phone);
		masterAgentRepository.save(masterAgentInfo);
		MasterAgentInfo masterAgentInfo2 = masterAgentRepository.findByuserName(phone).get();

		account.setAccountNumber(phone);
		account.setCreationDate(LocalDate.now());
		account.setType(accounttype);
		account.setMasterAgentInfo(masterAgentInfo2);
		account.setStatus(true);

		accountRepository.save(account);
		Optional<Account> accoutOptional = accountRepository.findByAccountNumberEquals(phone);
		if (!accoutOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No account found!"));
		}
		Account account2 = accoutOptional.get();
		AccountBalance accountBalance = new AccountBalance();
		accountBalance.setBalance(new BigDecimal(1000));
		accountBalance.setAccount(account2);
		accountBalance.setCreateDate(LocalDate.now());
		accountBalanceRepository.save(accountBalance);
		UserCredential userCredential = new UserCredential();

		userCredential.setLoginID(phone);
		userCredential.setActive(true);
		userCredential.setLocalDate(LocalDate.now());
		userCredential.setLoginIDType(loginIDType);
		userCredential.setMasterAgentInfo(masterAgentInfo2);
		userCredentialRepository.save(userCredential);
		UserAuthentication userAuthentication = new UserAuthentication();
		userAuthentication.setAuthenticationType(authenticationType);
		userAuthentication.setAuthenticationValue(passwordEncoder.encode(pin));
		userAuthentication.setLocalDate(LocalDate.now());
		userAuthentication.setMasterAgentInfo(masterAgentInfo2);
		userAuthenticationRepository.save(userAuthentication);
		return ResponseEntity.ok(new UpdateResponse("Your PIN Code is: " + pin));
	}

}
