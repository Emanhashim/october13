package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountBalance;
import com.bazra.usermanagement.model.AccountType;
import com.bazra.usermanagement.model.AgentINF;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.CreditTransaction;
import com.bazra.usermanagement.model.DebitTransaction;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.Transaction;
import com.bazra.usermanagement.model.UserAddress;
import com.bazra.usermanagement.model.UserAuthentication;
import com.bazra.usermanagement.model.UserINF;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.repository.AccountBalanceRepository;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AccountTypeRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.AgentTransactionRepository;
import com.bazra.usermanagement.repository.CreditTransactionRepository;
import com.bazra.usermanagement.repository.DebitTransactionRepository;
import com.bazra.usermanagement.repository.FullTransactionRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.RoleRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.repository.UserAddressRepository;
import com.bazra.usermanagement.repository.UserAuthenticationRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.request.Accountrequest;
import com.bazra.usermanagement.request.AdminTransferRequest;
import com.bazra.usermanagement.request.AgentUpdateRequest;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.UserUpdateRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.AccountResponse;
import com.bazra.usermanagement.response.AgentBalanceResponse;
import com.bazra.usermanagement.response.AgentTransactionResponse;
import com.bazra.usermanagement.response.BalanceResponse;
import com.bazra.usermanagement.response.CommissionResponse;
import com.bazra.usermanagement.response.ListOfErrorResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.TotalNumberCommission;
import com.bazra.usermanagement.response.TransactionResponse;
import com.bazra.usermanagement.response.UpdateResponse;
import com.bazra.usermanagement.response.UserResponse;
import com.bazra.usermanagement.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin("*")
@RequestMapping("/Api/Accounts")
@Api(value = "Wallet  User's Activity  Endpoint", description = "This EndPoint Activities For Bazra  Wallet User's Activity")
@ApiResponses(value = { @ApiResponse(code = 404, message = "web user that a requested page is not available "),
		@ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
		@ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
		@ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
		@ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class UsersTransaction {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private AccountService accountService;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	MerchantRepository merchantRepository;
	@Autowired
	AccountBalanceRepository accountBalanceRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AgentRepository agentRepository;
	@Autowired
	FullTransactionRepository fullTransactionRepository;
	@Autowired
	DebitTransactionRepository debitTransactionRepository;
	@Autowired
	CreditTransactionRepository creditTransactionRepository;
	Account agentAccount;
	AccountBalance accountBalance;
	@Autowired
	UserAuthenticationRepository userAuthenticationRepository;
	@Autowired
	AccountTypeRepository accountTypeRepository;
	@Autowired
	AgentTransactionRepository agentTransactionRepository;
	@Autowired
	UserAddressRepository userAddressRepository;
	@Autowired
	RoleRepository roleRepository;

	public UserInfo getCurrentUser(@AuthenticationPrincipal UserInfo user) {
		return user;
	}

	@GetMapping("/All")
	@ApiOperation(value = "THIS ENDPOINT TO GET ALL USER Accounts WHO USE'S BAZRA WALLET")
	public List<Account> all(@RequestParam Optional<String> sortBy) {
		System.out.println(accountRepository.findAll());
		return accountRepository.findAll();
	}

	@PostMapping("/GetAccount")
	@ApiOperation(value = "THIS ENDPOINT TO GET USER DETAIL  BY ACCOUNT NUMBER OR PHONE NUMBER WHO USE'S BAZRA WALLET")
	public ResponseEntity<?> getAccount(@RequestBody Accountrequest accountRequest, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		Account account = accountService.getAccount(accountRequest.getAccountNumber());
		if (!accountRepository.findByAccountNumberEquals(accountRequest.getAccountNumber()).isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Doesnot Exist"));
		}
		try {
			accountBalance = accountBalanceRepository.findByaccount(account).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		return ResponseEntity.ok(new AccountResponse(account, accountBalance));
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO GET USERS BY ID SPECIFIC, like accountnumber,balance,user_id,username, type")
	public ResponseEntity<?> account(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if (!accountRepository.findByuser_id(id).isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Does not Exist"));
		}
		Account account = accountRepository.findByuser_id(id).get();
		try {
			accountBalance = accountBalanceRepository.findByaccount(account).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		return ResponseEntity.ok(new AccountResponse(account, accountBalance));
	}

	@GetMapping("/User")
	@ApiOperation(value = "THIS ENDPOINT TO GET ONLY USERS DATA EVERY DETAIL UNLIKE {id} endpoint")
	public List<?> allUsers(Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();

		List<UserInfo> userInfos = userRepository.findAll();
		List<UserINF> userInfo = new ArrayList<>();

		for (int i = 0; i < userInfos.size(); i++) {
			UserINF userINF = new UserINF();
			userINF.setFatherName(userInfos.get(i).getFatherName());
			userINF.setId(userInfos.get(i).getId());
			userINF.setLastname(userInfos.get(i).getLastname());
			userINF.setName(userInfos.get(i).getName());
			userINF.setRoles(userInfos.get(i).getRoles().getRolename());
			userINF.setUsername(userInfos.get(i).getUsername());

			userINF.setBirthDay(userInfos.get(i).getBirthDay());
			userINF.setEmail(userInfos.get(i).getEmail());
			userINF.setGender(userInfos.get(i).getGender());
			userINF.setLevels(userInfos.get(i).getLevels().name());
			userINF.setMotherName(userInfos.get(i).getMotherName());

			System.out.println(userINF.getUsername());
			System.out.println(userINF);
			userInfo.add(userINF);

		}

		userInfo.forEach(System.out::println);
		return userInfo;
	}

	@GetMapping("/Agents")
	@ApiOperation(value = "THIS ENDPOINT TO GET ONLY USERS DATA EVERY DETAIL UNLIKE {id} endpoint")
	public List<?> allAgents(Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		Optional<Role> agentRoleOptional = roleRepository.findByrolename("AGENT");
		if (!agentRoleOptional.isPresent()) {
			return (List<?>) ResponseEntity.badRequest()
					.body(new ListOfErrorResponse("Account Type AGENT Does not Exist"));
		}
		List<AgentInfo> agentInfos = agentRepository.findByroles(agentRoleOptional.get());
		List<AgentINF> agentInfo = new ArrayList<>();

		for (int i = 0; i < agentInfos.size(); i++) {
			AgentINF agentINF = new AgentINF();
			agentINF.setFatherName(agentInfos.get(i).getFatherName());
			agentINF.setId(agentInfos.get(i).getId());
			agentINF.setLastName(agentInfos.get(i).getLastName());
			agentINF.setName(agentInfos.get(i).getName());
			agentINF.setRoles(agentInfos.get(i).getRoles().getRolename());
			agentINF.setUsername(agentInfos.get(i).getUsername());
			agentInfo.add(agentINF);
		}

		return agentInfo;
	}

	@GetMapping("/Merchants")
	@ApiOperation(value = "THIS ENDPOINT TO GET ONLY USERS DATA EVERY DETAIL UNLIKE {id} endpoint")
	public List<?> allMerchants(Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		Optional<Role> agentRoleOptional = roleRepository.findByrolename("MERCHANT");
		if (!agentRoleOptional.isPresent()) {
			return (List<?>) ResponseEntity.badRequest()
					.body(new ListOfErrorResponse("Account Type MERCHANT Does not Exist"));
		}
		List<AgentInfo> agentInfos = agentRepository.findByroles(agentRoleOptional.get());
		List<AgentINF> agentInfo = new ArrayList<>();

		for (int i = 0; i < agentInfos.size(); i++) {
			AgentINF agentINF = new AgentINF();
			agentINF.setFatherName(agentInfos.get(i).getFatherName());
			agentINF.setId(agentInfos.get(i).getId());
			agentINF.setLastName(agentInfos.get(i).getLastName());
			agentINF.setName(agentInfos.get(i).getName());
			agentINF.setRoles(agentInfos.get(i).getRoles().getRolename());
			agentINF.setUsername(agentInfos.get(i).getUsername());
			agentInfo.add(agentINF);
		}

		return agentInfo;
	}

	@GetMapping("/User/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO GET USERS DETAIL INFORMATION BY ID SPECIFIC")
	public ResponseEntity<?> user(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		Optional<UserInfo> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No user found"));
		}
		UserInfo userInfo = userOptional.get();
		List<UserAddress> useraddressOptional = userAddressRepository.findByuser(userInfo);
		return ResponseEntity.ok(new UserResponse(userOptional.get(), useraddressOptional));
	}

	@GetMapping("/UserInfo")
	@ApiOperation(value = "THIS ENDPOINT TO GET USERS DETAIL INFORMATION BY ID SPECIFIC")
	public ResponseEntity<?> userInfo(Authentication authentication) {

		Optional<UserInfo> userOptional = userRepository.findByUsername(authentication.getName());

		if (!userOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid account"));
		}
		UserInfo userInfo = userOptional.get();
		List<UserAddress> useraddressOptional = userAddressRepository.findByuser(userInfo);
		return ResponseEntity.ok(new UserResponse(userOptional.get(), useraddressOptional));
	}

//	@GetMapping("/AgentInfo")
//	@ApiOperation(value = "THIS ENDPOINT TO GET USERS DETAIL INFORMATION BY ID SPECIFIC")
//	public ResponseEntity<?> agentInfo(Authentication authentication) {
//		Optional<AgentInfo> agentOptional = agentRepository.findByUsername(authentication.getName());
//		if (!agentOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("Not a valid account"));
//		}
//		return ResponseEntity.ok(new AgentResponse(agentOptional.get()));
//	}
//	@GetMapping("/Agent/{id}")
//	@ApiOperation(value = "THIS ENDPOINT TO GET USERS DETAIL INFORMATION BY ID SPECIFIC")
//	public ResponseEntity<?> agent(@PathVariable Integer id, Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
//
//		}
//		Optional<AgentInfo> agentOptional = agentRepository.findById(id);
//		if (!agentOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("No agent found"));
//		}
//
//		return ResponseEntity.ok(new AgentResponse(agentOptional));
//	}

//	@GetMapping("/Merchant/{id}")
//	@ApiOperation(value = "THIS ENDPOINT TO GET USERS DETAIL INFORMATION BY ID SPECIFIC")
//	public ResponseEntity<?> merchant(@PathVariable Integer id, Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
//
//		}
//		Optional<MerchantInfo> merchantOptional = merchantRepository.findById(id);
//		if (!merchantOptional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("No agent found"));
//		}
//
//		return ResponseEntity.ok(new MerchantResponse(merchantOptional));
//	}

	@PutMapping("/User/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO UPDATE  USERS INFORMATION SPECIFIC ID")
	public ResponseEntity<?> updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest,
			Authentication authentication, @PathVariable Integer id) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		Optional<UserInfo> optional = userRepository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No user found"));
		}
		UserInfo userInfo2 = optional.get();
		Optional<UserAuthentication> userauthOptional = userAuthenticationRepository.findByuserInfo(userInfo2);
		if (!userauthOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No user authentication found"));
		}
		UserAuthentication userAuthentication = userauthOptional.get();
		if (userUpdateRequest.getBirthDay() != null) {
			userInfo2.setBirthDay(userUpdateRequest.getBirthDay());
		}
//		if (userUpdateRequest.getCity()!=null) {
//			userInfo2.setCity(userUpdateRequest.getCity());
//		}

		if (userUpdateRequest.getPassword() != null) {
			userAuthentication.setAuthenticationValue(passwordEncoder.encode(userUpdateRequest.getPassword()));
		}
		if (userUpdateRequest.getEmail() != null) {
			userInfo2.setEmail(userUpdateRequest.getEmail());
		}
		if (userUpdateRequest.getFirstName() != null) {
			userInfo2.setName(userUpdateRequest.getFirstName());
		}
		if (userUpdateRequest.getGender() != null) {
			userInfo2.setGender(userUpdateRequest.getGender());
		}
//		if (userUpdateRequest.getHouseNo()!=null) {
//			userInfo2.setHouseNo(userUpdateRequest.getHouseNo());
//	
//		}
//		if (userUpdateRequest.getKebeleID()!=null) {
//			userInfo2.setKebeleID(userUpdateRequest.getKebeleID());
//		}
//		if (userUpdateRequest.getPhoto()!=null) {
//			userInfo2.setPhoto(userUpdateRequest.getPhoto());
//			
//		}
		if (userUpdateRequest.getLastName() != null) {
			userInfo2.setLastname(userUpdateRequest.getLastName());
		}
		if (userUpdateRequest.getLevels() != null) {
			userInfo2.setLevels(userUpdateRequest.getLevels());

		}
//		if (userUpdateRequest.getRegion()!=null) {
//			userInfo2.setRegion(userUpdateRequest.getRegion());
//			
//		}
//		if (userUpdateRequest.getRoles() != null) {
//			userInfo2.setRoles(userUpdateRequest.getRoles());
//
//		}
//		if (userUpdateRequest.getSubCity()!=null) {
//			userInfo2.setSubCity(userUpdateRequest.getSubCity());
//		}
//		if (userUpdateRequest.getUserType()!=null) {
//			userInfo2.setUserType(userUpdateRequest.getUserType());
//			
//		}
//		if (userUpdateRequest.getWoreda()!=null) {
//			userInfo2.setWoreda(userUpdateRequest.getWoreda());
//		}

		userRepository.save(userInfo2);

		return ResponseEntity.ok(new UpdateResponse("Updated successfully"));
	}

//	@PutMapping("/Agent/{id}")
//	@ApiOperation(value = "THIS ENDPOINT TO UPDATE  AGENTS INFORMATION SPECIFIC ID")
//	public ResponseEntity<?> updateAgentInfo(@RequestBody AgentUpdateRequest agentUpdateRequest,
//			Authentication authentication, @PathVariable Integer id) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
//
//		}
//
//		Optional<AgentInfo> optional = agentRepository.findById(id);
//		if (!optional.isPresent()) {
//			return ResponseEntity.badRequest().body(new ResponseError("No user found"));
//		}
//		AgentInfo agentInfo = optional.get();
//		if (agentUpdateRequest.getBusinessLNum() != null) {
//			agentInfo.setBusinessLNum(agentUpdateRequest.getBusinessLNum());
//		}
//		if (agentUpdateRequest.getCompanyName() != null) {
//			agentInfo.setCompanyName(agentUpdateRequest.getCompanyName());
//		}
//		if (agentUpdateRequest.getCity() != null) {
//			agentInfo.setCity(agentUpdateRequest.getCity());
//		}
//		if (agentUpdateRequest.getSubcity() != null) {
//			agentInfo.setSubCity(agentUpdateRequest.getSubcity());
//		}
//		if (agentUpdateRequest.getWoreda() != null) {
//			agentInfo.setWoreda(agentUpdateRequest.getWoreda());
//		}
//		if (agentUpdateRequest.getCompanytype() != null) {
//			agentInfo.setCompanyType(agentUpdateRequest.getCompanytype());
//		}
//
//		if (agentUpdateRequest.getPassword() != null) {
//			agentInfo.setPassword(passwordEncoder.encode(agentUpdateRequest.getPassword()));
//		}
//		if (agentUpdateRequest.getRegion() != null) {
//			agentInfo.setRegion(agentUpdateRequest.getRegion());
//		}
//		if (agentUpdateRequest.getFirstName() != null) {
//			agentInfo.setFirstName(agentUpdateRequest.getFirstName());
//		}
//		if (agentUpdateRequest.getLastName() != null) {
//			agentInfo.setLastName(agentUpdateRequest.getLastName());
//		}
//		if (agentUpdateRequest.getLicenceNumber() != null) {
//			agentInfo.setLicenceNumber(agentUpdateRequest.getLicenceNumber());
//		}
//		if (agentUpdateRequest.getRole() != null) {
//			agentInfo.setRoles(agentUpdateRequest.getRole());
//
//		}
//
//		agentRepository.save(agentInfo);
//
//		return ResponseEntity.ok(new UpdateResponse("Updated successfully"));
//	}

	@PutMapping("/Merchant/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO UPDATE  USERS INFORMATION SPECIFIC ID")
	public ResponseEntity<?> updateMerchant(@RequestBody AgentUpdateRequest agentUpdateRequest,
			Authentication authentication, @PathVariable Integer id) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		Optional<MerchantInfo> optional = merchantRepository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No user found"));
		}
		MerchantInfo merchantInfo = optional.get();

		merchantRepository.save(merchantInfo);

		return ResponseEntity.ok(new UpdateResponse("Updated successfully"));
	}

	@DeleteMapping("/User/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO DELETE USERS BY ID SPECIFIC")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		if (userRepository.findById(id).isPresent()) {
			Account account = accountRepository.findByuser_id(id).get();

			userRepository.deleteById(id);
			if (accountRepository.findByuser_id(id).isPresent()) {
				accountRepository.deleteById(account.getId());
			} else {
				return ResponseEntity.badRequest().body(new ResponseError("No Account found"));
			}
			return ResponseEntity.ok(new UpdateResponse("User Deleted successfully"));
		} else {
			return ResponseEntity.badRequest().body(new ResponseError("No user found"));
		}

	}

	@DeleteMapping("/Agent/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO DELETE USERS BY ID SPECIFIC")
	public ResponseEntity<?> deleteAgent(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		if (agentRepository.findById(id).isPresent()) {
			Account account = accountRepository.findByuser_id(id).get();

			agentRepository.deleteById(id);
			if (accountRepository.findByuser_id(id).isPresent()) {
				accountRepository.deleteById(account.getId());
			} else {
				return ResponseEntity.badRequest().body(new ResponseError("No Account found"));
			}
			return ResponseEntity.ok(new UpdateResponse("Agent Deleted successfully"));
		} else {
			return ResponseEntity.badRequest().body(new ResponseError("No Account found"));
		}

	}

	@DeleteMapping("/Merchant/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO DELETE USERS BY ID SPECIFIC")
	public ResponseEntity<?> deleteMerchant(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}
		if (merchantRepository.findById(id).isPresent()) {
			Account account = accountRepository.findByuser_id(id).get();

			merchantRepository.deleteById(id);
			if (accountRepository.findByuser_id(id).isPresent()) {
				accountRepository.deleteById(account.getId());
			} else {
				return ResponseEntity.badRequest().body(new ResponseError("No Account found"));
			}
			return ResponseEntity.ok(new UpdateResponse("Merchant Deleted successfully"));
		} else {
			return ResponseEntity.badRequest().body(new ResponseError("No Account found"));
		}

	}

	@GetMapping("/Block/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO BLOCK ACCOUNTS")
	public ResponseEntity<?> blockAccount(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		if (!accountRepository.findByuser_id(id).isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Does not Exist"));
		}
		Account account = accountRepository.findByuser_id(id).get();
		if (account.isStatus()) {
			account.setStatus(false);
			accountRepository.save(account);
			return ResponseEntity.ok(new UpdateResponse("Account UnBlocked"));
		} else {
			account.setStatus(true);
			accountRepository.save(account);
			return ResponseEntity.ok(new UpdateResponse("Account Blocked"));
		}

	}

	@PostMapping("/SendMoney")
	@ApiOperation(value = "THIS ALLOW USER TO SEND MONEY FROM ONE USER TO OTHER TRANSFER")
	public ResponseEntity<?> sendMoney(@RequestBody TransferRequest transferBalanceRequest,
			Authentication authentication) {

		return accountService.sendMoney(transferBalanceRequest, authentication.getName());
	}

	@PostMapping("/AdminSendMoney")
	@ApiOperation(value = "THIS ALLOW USER TO SEND MONEY FROM ONE USER TO OTHER TRANSFER")
	public ResponseEntity<?> adminSendMoney(@RequestBody AdminTransferRequest adminTransferRequest,
			Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));

		}

		return accountService.adminSendMoney(adminTransferRequest, authentication.getName());
	}

	@PostMapping("/Withdraw")
//    @RolesAllowed("AGENT")
	@ApiOperation(value = "HERE WE CAN WITHDRAW MONEY FROM USER'S ACCOUNT")
	public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest, Authentication authentication) {
//        System.out.println(authentication.getAuthorities());
		return accountService.withdraw(withdrawRequest, authentication.getName());
	}

	@PostMapping("/Deposit")
	@ApiOperation(value = "HERE WE DEPOSIT MONEY FROM AGENT TO USER")
	public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest, Authentication authentication) {

		return accountService.Deposit(depositRequest, authentication.getName());
	}

	@PostMapping("/Pay")
	@ApiOperation(value = "HERE WE CAN PAY FOR ANYTHING THAT WE PURCHASED")
	public ResponseEntity<?> pay(@RequestBody DepositRequest depositRequest, Authentication authentication) {

		return accountService.pay(depositRequest, authentication.getName());
	}

//    @GetMapping("/deposittransaction")
//    @ApiOperation(value ="This EndPoint To Get Deposit Transaction History. Get Method" )
//    public ResponseEntity<?> transactionDeposit(Authentication authentication) {
//
//
//    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//       
//          if (account == null) {
//              return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
//          }
//          List<Transaction> deposittransactions =new ArrayList<Transaction>();
//          List<Transaction> transactions= transactionRepository.findByaccountNumberEquals(authentication.getName());
//          List<Transaction> transactionssList = transactionRepository.findByfromAccountNumberEquals(authentication.getName());
//          for (int i = 0; i < transactions.size(); i++) {
//        	  System.out.println(transactions.get(i).getTransaction_type());
//        	  if(transactions.get(i).getTransaction_type().matches("Deposit")) {
//        		  deposittransactions.add(transactions.get(i));
//        		  deposittransactions.add(transactionssList.get(i));
//        		  System.out.println(deposittransactions);
//        	  }
//			
//          	}
//           return ResponseEntity.ok(new TransactionResponse(deposittransactions));
////        return accountService.findall(transactionRequest.getAccountNumber());
//    }
	@GetMapping("/Transaction")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> transaction(Authentication authentication) {

		Optional<Account> accountOptional = accountRepository.findByAccountNumberEquals(authentication.getName());

		if (!accountOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid user information"));
		}
		Account account = accountOptional.get();
		if (account == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
//		Account userAccount = accountRepository.findByUser(agentOptional.get()).get();
		return ResponseEntity.ok(
				new TransactionResponse(fullTransactionRepository.findByfromAccountNumber(account.getAccountNumber()),
						fullTransactionRepository.findBytoAccountNumber(account.getAccountNumber())));
//        return accountService.findall(transactionRequest.getAccountNumber());
	}

	@GetMapping("/AllTransactions")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> allTransactions(Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}

		return ResponseEntity.ok(new TransactionResponse(fullTransactionRepository.findAll()));

	}

	@GetMapping("/AllTransactions/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO GET EVERY SINGLE TRANSACTIONS MADE BY SPECIFIC USER BY ID")
	public List<?> userTransaction(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		String[] unauthorized = { "Unauthorized request" };
		String[] notrans = { "No Transaction found for the specified user" };
		// initialize an immutable list from array using asList method
		List<String> unauthorizedlist = Arrays.asList(unauthorized);
		List<String> notranslist = Arrays.asList(notrans);
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return unauthorizedlist;

		}
		List<Transaction> usertransaction = new ArrayList<>();
		List<DebitTransaction> debitTransactions = debitTransactionRepository
				.findByaccount(accountRepository.findById(id).get());
		List<CreditTransaction> creditTransactions = creditTransactionRepository
				.findByaccount(accountRepository.findById(id).get());
		for (int i = 0; i < debitTransactions.size(); i++) {
			usertransaction.add(debitTransactions.get(i).getTransaction());
		}
		for (int i = 0; i < creditTransactions.size(); i++) {
			usertransaction.add(creditTransactions.get(i).getTransaction());
		}
		if (usertransaction.isEmpty()) {
			return notranslist;
		}

		return usertransaction;
	}

//	@GetMapping("/FullTransaction/{id}")
//	@ApiOperation(value = "THIS ENDPOINT TO GET EVERY SINGLE TRANSACTIONS MADE BY SPECIFIC USER BY ID")
//	public List<?> userFullTransaction(@PathVariable Integer id, Authentication authentication) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		String[] unauthorized = { "Unauthorized request" };
//		String[] notrans = { "No Transaction found for the specified user" };
//		// initialize an immutable list from array using asList method
//		List<String> unauthorizedlist = Arrays.asList(unauthorized);
//		List<String> notranslist = Arrays.asList(notrans);
//		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
//			return unauthorizedlist;
//
//		}
//		List<Transaction> usertransaction = new ArrayList<>();
//		List<DebitTransaction> debitTransactions = debitTransactionRepository
//				.findByaccount(accountRepository.findById(id).get());
//		List<CreditTransaction> creditTransactions = creditTransactionRepository
//				.findByaccount(accountRepository.findById(id).get());
//		for (int i = 0; i < debitTransactions.size(); i++) {
//			usertransaction.add(debitTransactions.get(i).getTransaction());
//		}
//		for (int i = 0; i < creditTransactions.size(); i++) {
//			usertransaction.add(creditTransactions.get(i).getTransaction());
//		}
//		if (usertransaction.isEmpty()) {
//			return notranslist;
//		}
//
//		return usertransaction;
//	}
	@GetMapping("/FullTransaction/{id}")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> allUserTransactions(@PathVariable Integer id) {

		Optional<UserInfo> agentOptional = userRepository.findById(id);
		if (!agentOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid user information"));
		}
		Account userAccount = accountRepository.findByUser(agentOptional.get()).get();
		return ResponseEntity.ok(new TransactionResponse(
				fullTransactionRepository.findByfromAccountNumber(userAccount.getAccountNumber())));

	}

	@GetMapping("/AllAgentTransactions")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> allAgentTransactions(Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		return ResponseEntity.ok(new AgentTransactionResponse(agentTransactionRepository.findAll()));

	}

	@GetMapping("/AllAgentTransactions/{id}")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> allAgentTransactions(@PathVariable Integer id) {
//		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//		if (!adminaccount.getType().matches("ADMIN")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
//		}
		Optional<AgentInfo> agentOptional = agentRepository.findById(id);
		if (!agentOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid agent information"));
		}
		Account agentAccount = accountRepository.findByAgent(agentOptional.get()).get();
		return ResponseEntity
				.ok(new AgentTransactionResponse(agentTransactionRepository.findByagentAccount(agentAccount)));

	}

	@GetMapping("/AllAccounts")
	@ApiOperation(value = "WE CAN GET ALL TRANSACTIONS THAT WE MADE ")
	public ResponseEntity<?> allTransactionss(Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return ResponseEntity.badRequest().body(new ResponseError("Unauthorized request"));
		}
		return ResponseEntity.ok(new AccountResponse(accountRepository.findAll()));

	}

	@GetMapping("/AllAccounts/{id}")
	@ApiOperation(value = "THIS ENDPOINT TO GET EVERY SINGLE TRANSACTIONS MADE BY SPECIFIC USER BY ID")
	public List<?> userTransactions(@PathVariable Integer id, Authentication authentication) {
		Account adminaccount = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
		String[] unauthorized = { "Unauthorized request" };
		String[] notrans = { "No Transaction found for the specified user" };
		// initialize an immutable list from array using asList method
		List<String> unauthorizedlist = Arrays.asList(unauthorized);
		List<String> notranslist = Arrays.asList(notrans);
		if (!adminaccount.getType().getAccounttype().matches("ADMIN")) {
			return unauthorizedlist;

		}
		List<Transaction> usertransaction = new ArrayList<>();
		List<DebitTransaction> debitTransactions = debitTransactionRepository
				.findByaccount(accountRepository.findById(id).get());
		List<CreditTransaction> creditTransactions = creditTransactionRepository
				.findByaccount(accountRepository.findById(id).get());
		for (int i = 0; i < debitTransactions.size(); i++) {
			usertransaction.add(debitTransactions.get(i).getTransaction());
		}
		for (int i = 0; i < creditTransactions.size(); i++) {
			usertransaction.add(creditTransactions.get(i).getTransaction());
		}
		if (usertransaction.isEmpty()) {
			return notranslist;
		}

		return usertransaction;
	}

	@GetMapping("/Balance")
	@ApiOperation(value = "WE CAN CHECK OUR CURRENT BALANCE OR THE REMAINING")
	public ResponseEntity<?> balance(Authentication authentication) {
		Account account = accountRepository.findByAccountNumberEquals(authentication.getName()).get();

		if (account == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		try {
			accountBalance = accountBalanceRepository.findByaccount(account).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balace not found"));
		}
		BigDecimal balance = accountBalance.getBalance();
		AccountType accountType = new AccountType();
		Optional<AccountType> accountUserType = accountTypeRepository.findByaccounttype("USER");
		if (!accountUserType.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Type USER not found"));
		}

		Optional<AccountType> accountAgentType = accountTypeRepository.findByaccounttype("AGENT");
		if (!accountUserType.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Type AGENT not found"));
		}

		Optional<AccountType> accountMerchantType = accountTypeRepository.findByaccounttype("MERCHANT");
		if (!accountUserType.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Type MERCHANT not found"));
		}

		Optional<AccountType> accountADMINType = accountTypeRepository.findByaccounttype("ADMIN");
		if (!accountUserType.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Type ADMIN not found"));
		}

		
		
		
	

		if (account.getType().equals(accountUserType.get())) {
			Optional<UserInfo> userInfos = userRepository.findByUsername(authentication.getName());
			UserINF userINF = new UserINF();
			userINF.setFatherName(userInfos.get().getFatherName());
			userINF.setId(userInfos.get().getId());
			userINF.setLastname(userInfos.get().getLastname());
			userINF.setName(userInfos.get().getName());
			userINF.setRoles(userInfos.get().getRoles().getRolename());
			userINF.setUsername(userInfos.get().getUsername());
			userINF.setBirthDay(userInfos.get().getBirthDay());
			userINF.setEmail(userInfos.get().getEmail());
			userINF.setGender(userInfos.get().getGender());
			userINF.setLevels(userInfos.get().getLevels().name());
			userINF.setMotherName(userInfos.get().getMotherName());
			return ResponseEntity.ok(
					new BalanceResponse(balance, "Your current balance equals " + balance, userINF, balance));
		} else if (account.getType().equals(accountAgentType.get())) {
			Optional<AgentInfo> agentInfos = agentRepository.findByUsername(authentication.getName());
			AgentINF agentINF = new AgentINF();
			agentINF.setFatherName(agentInfos.get().getFatherName());
			agentINF.setId(agentInfos.get().getId());
			agentINF.setLastName(agentInfos.get().getLastName());
			agentINF.setName(agentInfos.get().getName());
			agentINF.setRoles(agentInfos.get().getRoles().getRolename());
			agentINF.setUsername(agentInfos.get().getUsername());
			return ResponseEntity.ok(new AgentBalanceResponse(balance, "Your current balance equals " + balance,
					agentINF, balance));
		} else if (account.getType().equals(accountMerchantType.get())) {
			Optional<AgentInfo> agentInfos = agentRepository.findByUsername(authentication.getName());
			AgentINF agentINF = new AgentINF();
			agentINF.setFatherName(agentInfos.get().getFatherName());
			agentINF.setId(agentInfos.get().getId());
			agentINF.setLastName(agentInfos.get().getLastName());
			agentINF.setName(agentInfos.get().getName());
			agentINF.setRoles(agentInfos.get().getRoles().getRolename());
			agentINF.setUsername(agentInfos.get().getUsername());
			return ResponseEntity.ok(new AgentBalanceResponse(balance, "Your current balance equals " + balance,
					agentINF, balance));
		} 
		return ResponseEntity.badRequest().body(new ResponseError("Not a valid account type found!"));
	}

	@GetMapping("/Commission")
	@ApiOperation(value = "FOR BAZRA ADMIN TO GET THE COMMISSION FROM ALL ACTIVITIES")
	public ResponseEntity<?> commission(Authentication authentication) {
		String username = "+251" + authentication.getName().substring(authentication.getName().length() - 9);
		Account account = accountRepository.findByAccountNumberEquals(username).get();

		if (account == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (!account.getType().getAccounttype().matches("AGENT")) {
			return ResponseEntity.badRequest().body(new ResponseError("Not an agent account"));
		}

		BigDecimal commission = account.getCommission();

		return ResponseEntity.ok(new CommissionResponse(account.getCommission(),
				"Your total commission is " + commission, account.getUser(), account.getCommission()));
	}

	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}

//	@GetMapping("/TransactionPaged")
//	@ApiOperation(value = "HERE TO GET ALL TRANSACTION SORTED WAY ")
//	public ResponseEntity<Map<String, Object>> getAllTransactionPage(Authentication authentication,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
//			@RequestParam(defaultValue = "transactionId,desc") String[] sort) {
//
//		try {
//			List<Sort.Order> orders = new ArrayList<Sort.Order>();
//
//			if (sort[0].contains(",")) {
//
//				for (String sortOrder : sort) {
//					String[] _sort = sortOrder.split(",");
//					orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
//				}
//			} else {
//
//				orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
//			}
//			List<Transaction> transactions = new ArrayList<>();
//			List<Transaction> transactionss = new ArrayList<>();
//			Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
//			List<Transaction> trans = new ArrayList<>();
//			Page<Transaction> pageTuts;
//			Page<Transaction> pageTutss = null;
//			if (authentication.getName().isEmpty())
//				pageTuts = transactionRepository.findAll(pagingSort);
//
//			else {
//				pageTuts = transactionRepository.findByfromAccountNumberEquals(authentication.getName(), pagingSort);
//				pageTutss = transactionRepository.findByaccountNumberEquals(authentication.getName(), pagingSort);
//			}
//			System.out.println(pageTuts);
//			transactions = pageTuts.getContent();
//			transactionss = pageTutss.getContent();
//			for (int i = 0; i < transactionss.size(); i++) {
//				transactionss.get(i).setUserID(transactions.get(0).getUserID());
//			}
//			trans.addAll(transactions);
//			trans.addAll(transactionss);
//			Map<String, Object> response = new HashMap<>();
//			response.put("transactions", trans);
//			response.put("currentPage", pageTuts.getNumber());
//			response.put("totalTransaction", pageTuts.getTotalElements());
//			response.put("totalPages", pageTuts.getTotalPages());
//			return new ResponseEntity<>(response, HttpStatus.OK);
//
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@GetMapping("/TotalCommission")
	@ApiOperation(value = "This endPoint calculates total commission earned by an agent")
	public ResponseEntity<?> totalCommission(Authentication authentication) {
//		List<AgentInfo> agentInfo = agentRepository.findAll();
		Optional<AccountType> accountType = accountTypeRepository.findByaccounttype("AGENT");
		if (!accountType.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No account type AGENT"));
		}
		AccountType accountType2 = accountType.get();
		List<Account> agentInfo = accountRepository.findByType(accountType2);
		BigDecimal totalCommission = BigDecimal.ZERO;
		for (int i = 0; i < agentInfo.size(); i++) {

			totalCommission = totalCommission.add(agentInfo.get(i).getCommission());
			System.out.println(totalCommission);
		}

		return ResponseEntity
				.ok(new TotalNumberCommission(totalCommission, "Total commission paid " + totalCommission));
	}
//
//	@GetMapping("/TotalTransferFee")
//	@ApiOperation(value = "TOTAL TRANSFER FEE FOR ADMIN")
//	public ResponseEntity<?> totalTransactionfee(Authentication authentication) {
//		Account bazrAccount = accountRepository.findByAccountNumberEquals("091122334455").get();
//		BigDecimal tottransfee = bazrAccount.getBalance();
//
//		return ResponseEntity.ok(new TotalResponseTransactionfee(tottransfee, "Total transaction fee: " + tottransfee));
//	}

//	@GetMapping("/Revenue")
//	@ApiOperation(value = "HERE BAZRA WALLET ADMIN CHECK THE REVENUE")
//	public ResponseEntity<?> totalRevenue(Authentication authentication) {
//		Account bazrAccount = accountRepository.findByAccountNumberEquals("091122334455").get();
//		List<AgentInfo> agentsInfos = agentRepository.findAll();
//
//		BigDecimal totalCommission = new BigDecimal(0);
//		BigDecimal netIncome = new BigDecimal(0);
//		for (int i = 0; i < agentsInfos.size(); i++) {
//
//			totalCommission = totalCommission.add(accountRepository
//					.findByAccountNumberEquals(agentsInfos.get(i).getUsername()).get().getCommission());
//		}
//
//		netIncome = bazrAccount.getBalance().subtract(totalCommission);
//		if (bazrAccount.getBalance().compareTo(totalCommission) == -1) {
//			return ResponseEntity.ok(new RevenueResponse(netIncome, "You have a loss of: " + netIncome));
//		} else {
//			return ResponseEntity.ok(new RevenueResponse(netIncome, "You have a revenue of: " + netIncome));
//		}
//
//	}

}