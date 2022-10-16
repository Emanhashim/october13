package com.bazra.usermanagement.service;

import java.awt.datatransfer.FlavorListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountBalance;
import com.bazra.usermanagement.model.AccountType;
import com.bazra.usermanagement.model.AgentTransaction;
import com.bazra.usermanagement.model.BazraBalance;
import com.bazra.usermanagement.model.CreditTransaction;
import com.bazra.usermanagement.model.DebitTransaction;
import com.bazra.usermanagement.model.FullTransaction;
import com.bazra.usermanagement.model.Levels;
import com.bazra.usermanagement.model.Settings;
import com.bazra.usermanagement.model.Transaction;
import com.bazra.usermanagement.model.Transactiontype;
import com.bazra.usermanagement.repository.AccountBalanceRepository;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AccountTypeRepository;
import com.bazra.usermanagement.repository.AgentTransactionRepository;
import com.bazra.usermanagement.repository.BazraBalanceRepository;
import com.bazra.usermanagement.repository.CreditTransactionRepository;
import com.bazra.usermanagement.repository.DebitTransactionRepository;
import com.bazra.usermanagement.repository.FullTransactionRepository;
import com.bazra.usermanagement.repository.SettingRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.repository.TransactionTypeRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.request.AdminTransferRequest;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.DepositResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.TransferResponse;
import com.bazra.usermanagement.response.TwoWayTransactionResponse;
import com.bazra.usermanagement.response.UpdateResponse;
import com.bazra.usermanagement.response.WithdrawResponse;

@Service
public class AccountService {
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	AccountTypeRepository accountTypeRepository;

	@Autowired
	TransactionTypeRepository transactionTypeRepository;
	@Autowired
	AccountBalanceRepository accountBalanceRepository;
	AccountBalance fromaccountBalance;
	AccountBalance toaccountBalance;
	BazraBalance bazraBalance;
	AccountBalance agAccountBalance;
	@Autowired
	BazraBalanceRepository bazraBalanceRepository;
	@Autowired
	CreditTransactionRepository creditTransactionRepository;
	@Autowired
	DebitTransactionRepository debitTransactionRepository;
	@Autowired
	FullTransactionRepository fullTransactionRepository;
	@Autowired
	AgentTransactionRepository agentTransactionRepository;
	@Autowired
	RandomNumber randomNumber;
//	@Value("${withdrawfee}")
//	private BigDecimal withdrawfee;

	BigDecimal transactionfee;
	BigDecimal commissionfee;
	BigDecimal dailyTransferLimit;
	BigDecimal agentDepositLimit;
	BigDecimal maxBalanceL1;
	BigDecimal maxBalanceL2;
	BigDecimal maxBalanceL3;
//	BigDecimal daily = new BigDecimal(0);
	LocalDate daterepo;

	public Account save(Account account) {
		accountRepository.save(account);
		return accountRepository.findByAccountNumberEquals(account.getAccountNumber()).get();
	}

	public Account getAccount(String account) {
		Account accounts = accountRepository.findByAccountNumberEquals(account).get();
		return accounts;
	}

	public List<Account> findAll() {
		return accountRepository.findAll();
	}

//	public List<Transaction> findall(String accountnumber) {
//		Account account =accountRepository.findByAccountNumberEquals(accountnumber).get();
//		List<CreditTransaction> transactions = creditTransactionRepository.findByaccount(account);
//		List<DebitTransaction> debitTransaction = debitTransactionRepository.findByaccount(account);
//		
//		return transactions;
//	}

	public Account findByAccountNumber(String accountnumber) {
		Account account = accountRepository.findByAccountNumberEquals(accountnumber).get();
		return account;
	}

	public ResponseEntity<?> sendMoney(TransferRequest transferBalanceRequest, String name) {
		Optional<Account> optionfrom = accountRepository.findByAccountNumberEquals(name);
		if (!optionfrom.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Account"));
		}
//		String toAccountNumber = transferBalanceRequest.getToAccountNumber();
		Account fromAccount = optionfrom.get();
		String toAccountNumber = "+251" + transferBalanceRequest.getToAccountNumber()
				.substring(transferBalanceRequest.getToAccountNumber().length() - 9);

		Optional<Account> optionto = accountRepository.findByAccountNumberEquals(toAccountNumber);
		if (!optionto.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Receipent Account"));
		}
		Optional<AccountType> accountTypeOptional = accountTypeRepository.findByaccounttype("ADMIN");
		if (!accountTypeOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No account type admin found!"));
		}

		AccountType accountType = accountTypeOptional.get();
		List<Account> bazraAccount = accountRepository.findByType(accountType);
		int x = randomNumber.randomNumberGenerator(0,bazraAccount.size()-1);
		Transactiontype transactiontype = transactionTypeRepository.findBytransactionType("TRANSFER").get();
		if (fromAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}
		BigDecimal daily = new BigDecimal(0);

		LocalDate today = LocalDate.now(ZoneId.systemDefault());

		Levels levels = userRepository.findById(fromAccount.getUser().getId()).get().getLevels();

		if (levels == Levels.LEVEL_1) {
			Optional<Settings> setting1Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_1)");
			if (!setting1Optional.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No setting 'Transaction Fee(LEVEL_1)' found! "));
			}
			transactionfee = setting1Optional.get().getValue();
			Optional<Settings> setting2Optional = settingRepository.findBysettingName("Daily Transfer Limit");
			if (!setting2Optional.isPresent()) {
				return ResponseEntity.badRequest().body(new ResponseError("No setting 'Daily Transfer Limit' found! "));
			}
			dailyTransferLimit = setting2Optional.get().getValue();
		} else if (levels == Levels.LEVEL_2) {
			Optional<Settings> setting3Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_2)");
			if (!setting3Optional.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No setting 'Transaction Fee(LEVEL_2)' found! "));
			}
			transactionfee = setting3Optional.get().getValue();
			Optional<Settings> setting4Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_2)");
			if (!setting4Optional.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_2)' found! "));
			}
			dailyTransferLimit = setting4Optional.get().getValue();
		} else if (levels == Levels.LEVEL_3) {
			Optional<Settings> setting5Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_3)");
			if (!setting5Optional.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_2)' found! "));
			}

			transactionfee = setting5Optional.get().getValue();
			Optional<Settings> setting6Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_3)");
			if (!setting6Optional.isPresent()) {
				return ResponseEntity.badRequest()
						.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_3)' found! "));
			}
			dailyTransferLimit = setting6Optional.get().getValue();
		}

		BigDecimal amount = transferBalanceRequest.getAmount();
		Account toAccount;
		try {
			toAccount = accountRepository.findByAccountNumberEquals(toAccountNumber).get();
			if (toAccount.equals(fromAccount)) {
				return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
			}
			if (toAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type receipient"));
			}
			if (fromAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type account"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
		}

		if (toAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}

		if (toAccount == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
		}
		if (transferBalanceRequest.getAmount() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
		}

		if (amount.compareTo(new BigDecimal(5)) == -1) {
			return ResponseEntity.badRequest().body(new ResponseError("Minimum amount to transfer is 5"));
		}
		if (transferBalanceRequest.getMessage() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter your remark"));
		}
		Levels toAccountLevel = userRepository.findByUsername(toAccount.getUser().getUsername()).get().getLevels();
		Optional<Settings> settingOptional = settingRepository.findBysettingName("Balance Limit (LEVEL_1)");
		if (!settingOptional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_1)' found! "));
		}
		Optional<Settings> setting1Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_2)");
		if (!setting1Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_2)' found! "));
		}
		Optional<Settings> setting2Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_3)");
		if (!setting2Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_3)' found! "));
		}
		
		if (toAccountLevel.equals(Levels.LEVEL_1)) {
			maxBalanceL1 =settingOptional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_2)) {
			maxBalanceL1 = setting1Optional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_3)) {
			maxBalanceL1 = setting2Optional.get().getValue();
		}
		List<DebitTransaction> debitcurr = new ArrayList<DebitTransaction>();
		List<DebitTransaction> debitTransactions = debitTransactionRepository.findBycreateDate(today);

		for (int i = 0; i < debitTransactions.size(); i++) {
			if (debitTransactions.get(i).getAccount().getAccountNumber().equals(name)) {
				debitcurr.add(debitTransactions.get(i));

			}

		}
		BigDecimal minbalance = amount.add(transactionfee);
		try {
			fromaccountBalance = accountBalanceRepository.findByaccount(fromAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		try {
			toaccountBalance = accountBalanceRepository.findByaccount(toAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		try {
			bazraBalance = bazraBalanceRepository.findByaccount(bazraAccount.get(x)).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		String transactioncode = "";
		for (int j = 0; j < 6; j++) {

			transactioncode = transactioncode + randomNumber.randomNumberGenerator(0, 9);
		}
		if (debitcurr.isEmpty()) {
			if (fromaccountBalance.getBalance().compareTo(BigDecimal.ONE) == 1
					&& fromaccountBalance.getBalance().compareTo(minbalance) == 1) {

				daily = daily.add(transferBalanceRequest.getAmount());

				if (maxBalanceL1
						.compareTo(toaccountBalance.getBalance().add(transferBalanceRequest.getAmount())) == -1) {
					return ResponseEntity.badRequest().body(new ResponseError("Receiver not able to accept"));
				}
				if (dailyTransferLimit.compareTo(daily.negate()) == 1) {
					fromaccountBalance.setBalance(fromaccountBalance.getBalance().subtract(minbalance));
					fromAccount.setDaily(daily.negate().add(transferBalanceRequest.getAmount()));
					accountRepository.save(fromAccount);

					bazraBalance.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(amount)));
					toaccountBalance.setBalance(toaccountBalance.getBalance().add(amount));
					accountRepository.save(toAccount);
					accountRepository.save(bazraAccount.get(x));
					accountBalanceRepository.save(fromaccountBalance);
					accountBalanceRepository.save(toaccountBalance);
					AccountBalance toacc = accountBalanceRepository.findByaccount(toAccount).get();

					AccountBalance fracc = accountBalanceRepository.findByaccount(fromAccount).get();
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(fromAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(toAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(amount);
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(amount);
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);
					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(fromAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(toAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);

					return ResponseEntity
							.ok(new TransferResponse(toAccountNumber, amount, today, "Transfer successful! ",fracc.getBalance()));
				} else {

					return ResponseEntity.badRequest().body(new ResponseError("Daily Limit overpassed"));
				}
			}
		} else {
			daterepo = debitTransactions.get(debitTransactions.size() - 1).getCreateDate();

			if (fromaccountBalance.getBalance().compareTo(BigDecimal.ONE) == 1
					&& fromaccountBalance.getBalance().compareTo(minbalance) == 1) {

				for (int i = 0; i < debitcurr.size(); i++) {

					daily = daily.add(debitcurr.get(i).getTransaction().getTransactionAmount());

				}

				if (maxBalanceL1
						.compareTo(toaccountBalance.getBalance().add(transferBalanceRequest.getAmount())) == -1) {
					return ResponseEntity.badRequest().body(new ResponseError("Receiver not able to accept"));
				}
				if (dailyTransferLimit.compareTo(daily.negate().add(minbalance)) == 1) {
					fromaccountBalance.setBalance(fromaccountBalance.getBalance().subtract(minbalance));
					fromAccount.setDaily(daily.negate().add(transferBalanceRequest.getAmount()));
					accountRepository.save(fromAccount);
					AccountBalance fracc = accountBalanceRepository.findByaccount(fromAccount).get();

					bazraBalance.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(amount)));
					toaccountBalance.setBalance(toaccountBalance.getBalance().add(amount));
					accountBalanceRepository.save(fromaccountBalance);
					accountBalanceRepository.save(toaccountBalance);
					accountRepository.save(toAccount);
					accountRepository.save(bazraAccount.get(x));
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(fromAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(toAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(amount);
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(amount);
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);
					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(fromAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(toAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);

					return ResponseEntity
							.ok(new TransferResponse(toAccountNumber, amount, today, "Transfer successful! ",fracc.getBalance()));
				} else {

					return ResponseEntity.badRequest().body(new ResponseError("Daily Limit overpassed"));
				}

			}
		}

		return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
	}

	public ResponseEntity<?> adminSendMoney(AdminTransferRequest adminTransferRequest, String adminusername) {
		String frommAccount = "+251" + adminTransferRequest.getToAccountNumber()
				.substring(adminTransferRequest.getToAccountNumber().length() - 9);
		String tooAccount = "+251" + adminTransferRequest.getFromAccountNumber()
				.substring(adminTransferRequest.getFromAccountNumber().length() - 9);
		String adminAccount = "+251" + adminusername.substring(adminusername.length() - 9);
		Optional<Account> optionfrom = accountRepository.findByAccountNumberEquals(frommAccount);
		Optional<Account> optionAdmin = accountRepository.findByAccountNumberEquals(adminAccount);
		String toAccountNumber = adminTransferRequest.getToAccountNumber();
		Optional<Account> optionTo = accountRepository.findByAccountNumberEquals(tooAccount);
		Account toAccount;
		BigDecimal amount = adminTransferRequest.getAmount();
		if (!optionAdmin.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Admin Account"));
		}
		Account bazraAccount = optionAdmin.get();
		if (!optionfrom.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Account"));
		}
		if (!optionTo.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Destination Account"));
		}
		Account fromAccount = optionfrom.get();
		try {
			toAccount = accountRepository.findByAccountNumberEquals(toAccountNumber).get();
			if (toAccount.equals(fromAccount)) {
				return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
			}
			if (toAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type receipient"));
			}
			if (fromAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type account"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
		}
		Transactiontype transactiontype = transactionTypeRepository.findBytransactionType("TRANSFER").get();
		BigDecimal daily = new BigDecimal(0);
		if (fromAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}
		LocalDate today = LocalDate.now(ZoneId.systemDefault());

		Levels levels = userRepository.findById(fromAccount.getUser().getId()).get().getLevels();
		Optional<Settings> settingOptional = settingRepository.findBysettingName("Transaction Fee(LEVEL_1)");
		if (!settingOptional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_1)' found! "));
		}
		Optional<Settings> setting1Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_2)");
		if (!setting1Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_2)' found! "));
		}
		Optional<Settings> setting2Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_3)");
		if (!setting2Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_3)' found! "));
		}
		Optional<Settings> setting3Optional = settingRepository.findBysettingName("Daily Transfer Limit");
		if (!setting3Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit' found! "));
		}
		Optional<Settings> setting4Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_2)");
		if (!setting4Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_2)' found! "));
		}
		Optional<Settings> setting5Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_3)");
		if (!setting5Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_3)' found! "));
		}
		if (levels == Levels.LEVEL_1) {
			transactionfee = settingOptional.get().getValue();
			dailyTransferLimit = setting3Optional.get().getValue();
		} else if (levels == Levels.LEVEL_2) {
			transactionfee = setting1Optional.get().getValue();
			dailyTransferLimit = setting4Optional.get().getValue();
		} else if (levels == Levels.LEVEL_3) {
			transactionfee = setting2Optional.get().getValue();
			dailyTransferLimit = setting5Optional.get().getValue();
		}

		if (toAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}

		if (toAccount == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
		}
		if (adminTransferRequest.getAmount() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
		}

		if (amount.compareTo(new BigDecimal(5)) == -1) {
			return ResponseEntity.badRequest().body(new ResponseError("Minimum amount to transfer is 5"));
		}
		if (adminTransferRequest.getMessage() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter your remark"));
		}
		Levels toAccountLevel = userRepository.findByUsername(toAccount.getUser().getUsername()).get().getLevels();
		Optional<Settings> setting6Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_1)");
		if (!setting6Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_1)' found! "));
		}
		Optional<Settings> setting7Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_2)");
		if (!setting7Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_2)' found! "));
		}
		Optional<Settings> setting8Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_3)");
		if (!setting8Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_3)' found! "));
		}
		
		if (toAccountLevel.equals(Levels.LEVEL_1)) {
			maxBalanceL1 = setting6Optional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_2)) {
			maxBalanceL1 = setting7Optional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_3)) {
			maxBalanceL1 = setting8Optional.get().getValue();
		}
		List<Transaction> debitcurr = new ArrayList<Transaction>();
		List<DebitTransaction> debitTransactions = debitTransactionRepository.findBycreateDate(today);
		String transactioncode = "";
		for (int j = 0; j < 6; j++) {

			transactioncode = transactioncode + randomNumber.randomNumberGenerator(0, 9);
		}
		for (int i = 0; i < debitTransactions.size(); i++) {
			if (debitTransactions.get(i).getAccount().getAccountNumber().equals(fromAccount.getAccountNumber())) {
				debitcurr.add(debitcurr.get(i));

			}

		}
		BigDecimal minbalance = amount.add(transactionfee);
		try {
			fromaccountBalance = accountBalanceRepository.findByaccount(fromAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		try {
			toaccountBalance = accountBalanceRepository.findByaccount(toAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		try {
			bazraBalance = bazraBalanceRepository.findByaccount(bazraAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		if (debitcurr.isEmpty()) {
			if (fromaccountBalance.getBalance().compareTo(BigDecimal.ONE) == 1
					&& fromaccountBalance.getBalance().compareTo(minbalance) == 1) {

				daily = daily.add(adminTransferRequest.getAmount());

				if (maxBalanceL1.compareTo(toaccountBalance.getBalance().add(adminTransferRequest.getAmount())) == -1) {
					return ResponseEntity.badRequest().body(new ResponseError("Receiver not able to accept"));
				}
				if (dailyTransferLimit.compareTo(daily.negate()) == 1) {
					fromaccountBalance.setBalance(fromaccountBalance.getBalance().subtract(minbalance));
					fromAccount.setDaily(daily.negate().add(adminTransferRequest.getAmount()));
					accountRepository.save(fromAccount);
					bazraBalance.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(amount)));
					toaccountBalance.setBalance(toaccountBalance.getBalance().add(amount));
					accountRepository.save(toAccount);
					accountRepository.save(bazraAccount);
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(fromAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(toAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(amount);
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(amount);
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);
					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(fromAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(toAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);
					return ResponseEntity
							.ok(new TwoWayTransactionResponse("You have received " + amount + "$ from " + fromAccount,
									"Transfer successful! " + amount + "$ has been sent to " + toAccountNumber));
				} else {

					return ResponseEntity.badRequest().body(new ResponseError("Daily Limit overpassed"));
				}
			}
		} else {
			daterepo = debitTransactions.get(debitTransactions.size() - 1).getCreateDate();

			if (fromaccountBalance.getBalance().compareTo(BigDecimal.ONE) == 1
					&& fromaccountBalance.getBalance().compareTo(minbalance) == 1) {

				for (int i = 0; i < debitcurr.size(); i++) {

					daily = daily.add(debitcurr.get(i).getTransactionAmount());

				}

				if (maxBalanceL1.compareTo(toaccountBalance.getBalance().add(adminTransferRequest.getAmount())) == -1) {
					return ResponseEntity.badRequest().body(new ResponseError("Receiver not able to accept"));
				}
				if (dailyTransferLimit.compareTo(daily.negate().add(minbalance)) == 1) {
					fromaccountBalance.setBalance(fromaccountBalance.getBalance().subtract(minbalance));
					fromAccount.setDaily(daily.negate().add(adminTransferRequest.getAmount()));
					accountRepository.save(fromAccount);
					bazraBalance.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(amount)));
					toaccountBalance.setBalance(toaccountBalance.getBalance().add(amount));
					accountRepository.save(toAccount);
					accountRepository.save(bazraAccount);
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(fromAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(toAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(amount);
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(amount);
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);

					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(fromAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(toAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);
					return ResponseEntity
							.ok(new TwoWayTransactionResponse("You have received " + amount + "$ from " + fromAccount,
									"Transfer successful! " + amount + "$ has been sent to " + toAccountNumber));
				} else {

					return ResponseEntity.badRequest().body(new ResponseError("Daily Limit overpassed"));
				}

			}
		}

		return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
	}

	public ResponseEntity<?> withdraw(WithdrawRequest withdraw, String name) {
		String agenttAccount = "+251"
				+ withdraw.getFromAccountNumber().substring(withdraw.getFromAccountNumber().length() - 9);
		String frommAccount = "+251" + name.substring(name.length() - 9);
		Optional<Account> optionfrom = accountRepository.findByAccountNumberEquals(frommAccount);
		AccountType accountType = accountTypeRepository.findByaccounttype("ADMIN").get();
		
		
		List<Account> optionAdmin = accountRepository.findByType(accountType);
		System.out.println(optionAdmin.size());
		int x = randomNumber.randomNumberGenerator(0,optionAdmin.size()-1);
		Account frAccount;

		Optional<Account> optionTo = accountRepository.findByAccountNumberEquals(agenttAccount);

		if (optionAdmin.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No valid Admin Account"));
		}
		Account bazraAccount = optionAdmin.get(x);
		Account agAccount = optionTo.get();
		if (!optionfrom.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Account"));
		}
		if (!optionTo.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Destination Account"));
		}
		frAccount = optionfrom.get();
		
		if (agAccount.getType() != accountTypeRepository.findByaccounttype("AGENT").get()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid AGENT for withdraw"));
		}
		if (frAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type account"));
		}

		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		Transactiontype transactiontype = transactionTypeRepository.findBytransactionType("WITHDRAW").get();
		BigDecimal daily = new BigDecimal(0);

		if (frAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}
		Levels levels = userRepository.findById(frAccount.getUser().getId()).get().getLevels();
		Optional<Settings> setting8Optional = settingRepository.findBysettingName("commission fee(LEVEL_1)");
		if (!setting8Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'commission fee(LEVEL_1)' found! "));
		}
		Optional<Settings> setting9Optional = settingRepository.findBysettingName("commission fee(LEVEL_2)");
		if (!setting9Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'commission fee(LEVEL_2)' found! "));
		}
		Optional<Settings> setting6Optional = settingRepository.findBysettingName("commission fee(LEVEL_3)");
		if (!setting6Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'commission fee(LEVEL_3)' found! "));
		}
		Optional<Settings> setting1Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_1)");
		if (!setting1Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_1)' found! "));
		}
		Optional<Settings> setting2Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_2)");
		if (!setting2Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_2)' found! "));
		}
		Optional<Settings> setting3Optional = settingRepository.findBysettingName("Transaction Fee(LEVEL_3)");
		if (!setting3Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Transaction Fee(LEVEL_3)' found! "));
		}
		Optional<Settings> setting4Optional = settingRepository.findBysettingName("Daily Transfer Limit");
		if (!setting4Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit' found! "));
		}
		Optional<Settings> setting5Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_2)");
		if (!setting5Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_2)' found! "));
		}
		Optional<Settings> setting7Optional = settingRepository.findBysettingName("Daily Transfer Limit(LEVEL_3)");
		if (!setting7Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Daily Transfer Limit(LEVEL_3)' found! "));
		}
		if (levels == Levels.LEVEL_1) {
			commissionfee = setting8Optional.get().getValue();
			transactionfee = setting1Optional.get().getValue();
			dailyTransferLimit = setting4Optional.get().getValue();
		} else if (levels == Levels.LEVEL_2) {
			commissionfee = setting9Optional.get().getValue();
			transactionfee = setting2Optional.get().getValue();
			dailyTransferLimit = setting5Optional.get().getValue();
		} else if (levels == Levels.LEVEL_3) {
			commissionfee = setting6Optional.get().getValue();
			transactionfee = setting3Optional.get().getValue();
			dailyTransferLimit = setting7Optional.get().getValue();
		}

		if (!agAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}
		if (!agAccount.getType().getAccounttype().matches("AGENT")) {
			return ResponseEntity.badRequest().body(new ResponseError("Not an agent account"));
		}
		if (withdraw.getAmount() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
		}
		try {
			fromaccountBalance = accountBalanceRepository.findByaccount(frAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		try {
			agAccountBalance = accountBalanceRepository.findByaccount(agAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		try {
			bazraBalance = bazraBalanceRepository.findByaccount(bazraAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		if (withdraw.getAmount().compareTo(agAccountBalance.getBalance()) == 1) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("Agent has insufficient balance for your request"));
		}
		String transactioncode = "";
		for (int j = 0; j < 6; j++) {

			transactioncode = transactioncode + randomNumber.randomNumberGenerator(0, 9);
		}
		List<DebitTransaction> debitcurr = new ArrayList<DebitTransaction>();
		List<DebitTransaction> debitTransactions = debitTransactionRepository.findBycreateDate(today);
//		Account bazraAccount = accountRepository.findByType("ADMIN").get();

		for (int i = 0; i < debitTransactions.size(); i++) {
			if (debitTransactions.get(i).getAccount().getAccountNumber().equals(name)) {
				debitcurr.add(debitTransactions.get(i));

			}

		}
		BigDecimal balance = fromaccountBalance.getBalance();
		BigDecimal minbalance = withdraw.getAmount().add(commissionfee).add(transactionfee);
		if (debitcurr.isEmpty()) {

			if (balance.compareTo(minbalance) == 1) {
				daily = daily.add(withdraw.getAmount());
				if (dailyTransferLimit.compareTo(daily.negate().add(minbalance)) == 1) {
					
					fromaccountBalance.setBalance(balance.subtract(minbalance));
					frAccount.setDaily(daily.negate().add(withdraw.getAmount()));
					accountRepository.save(frAccount);
					agAccountBalance.setBalance(agAccountBalance.getBalance().add(withdraw.getAmount()));
					agAccount
							.setCommission(agAccount.getCommission().add(commissionfee.multiply(withdraw.getAmount())));
					accountRepository.save(agAccount);
					bazraBalance
							.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(withdraw.getAmount())));
					accountRepository.save(bazraAccount);
					AgentTransaction agentTransaction = new AgentTransaction();
					agentTransaction.setAgentAccount(agAccount);
					agentTransaction.setCustomerAccount(frAccount);
					agentTransaction.setAmount(withdraw.getAmount());
					agentTransaction.setCommission(commissionfee);
					agentTransaction.setTime(today);
					agentTransactionRepository.save(agentTransaction);
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(frAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(agAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(withdraw.getAmount());
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(withdraw.getAmount());
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);
					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(frAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(agAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);
					return ResponseEntity
							.ok(new WithdrawResponse(transaction.getTransactionAmount(), today,
									"Amount " + withdraw.getAmount()
											+ "$ has been debited from your account. Your current balance is "
											+ balance.subtract(withdraw.getAmount())));

				} else {
					return ResponseEntity.badRequest().body(new ResponseError("Daily Limit overpassed"));
				}
			} else {
				return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
			}

		} else {
			if (balance.compareTo(minbalance) == 1) {
				for (int i = 0; i < debitcurr.size(); i++) {

					daily = daily.add(debitcurr.get(i).getTransaction().getTransactionAmount());
					
				}
				if (dailyTransferLimit.compareTo(daily.negate().add(minbalance)) == 1) {
					
					fromaccountBalance.setBalance(balance.subtract(minbalance));
					
					frAccount.setDaily(daily.negate().add(withdraw.getAmount()));
					accountRepository.save(frAccount);
					agAccountBalance.setBalance(agAccountBalance.getBalance().add(withdraw.getAmount()));
					agAccount
							.setCommission(agAccount.getCommission().add(commissionfee.multiply(withdraw.getAmount())));
					accountRepository.save(agAccount);
					bazraBalance
							.setBalance(bazraBalance.getBalance().add(transactionfee.multiply(withdraw.getAmount())));
					accountRepository.save(bazraAccount);
					FullTransaction fullTransaction = new FullTransaction();
					fullTransaction.setFromAccountNumber(frAccount.getAccountNumber());
					fullTransaction.setToAccountNumber(agAccount.getAccountNumber());
					fullTransaction.setTransactionAmount(withdraw.getAmount());
					fullTransaction.setTransactionDateTime(today);
					fullTransaction.setTransactiontype(transactiontype.getTransactionType());
					fullTransactionRepository.save(fullTransaction);
					Transaction transaction = new Transaction();
					transaction.setCreateDate(LocalDate.now());
					transaction.setTransactionAmount(withdraw.getAmount());
					transaction.setTransactionCode(transactioncode);
					transaction.setTransactiontype(transactiontype);
					transactionRepository.save(transaction);
					Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setAccount(frAccount);
					debitTransaction.setCreateDate(LocalDate.now());
					debitTransaction.setTransaction(transaction2);
					debitTransactionRepository.save(debitTransaction);

					CreditTransaction creditTransaction = new CreditTransaction();
					creditTransaction.setAccount(agAccount);
					creditTransaction.setCreateDate(LocalDate.now());
					creditTransaction.setTransaction(transaction2);
					creditTransactionRepository.save(creditTransaction);

					return ResponseEntity
							.ok(new WithdrawResponse(transaction.getTransactionAmount(), today,
									"Amount " + withdraw.getAmount()
											+ "$ has been debited from your account. Your current balance is "
											+ balance.subtract(withdraw.getAmount())));
				} else {
					return ResponseEntity.badRequest()
							.body(new ResponseError("Daily Limit overpassed! Available transfer amount is "
									+ (dailyTransferLimit.subtract(frAccount.getDaily()))));
				}

			} else {
				return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
			}

		}

	}

	public ResponseEntity<?> Deposit(DepositRequest depositRequest, String name) {
		String agenttAccount = "+251" + name.substring(name.length() - 9);
		String tooAccount = "+251"
				+ depositRequest.getToAccountNumber().substring(depositRequest.getToAccountNumber().length() - 9);
		Optional<Account> optionfrom = accountRepository.findByAccountNumberEquals(agenttAccount);
		Optional<Account> optionto = accountRepository.findByAccountNumberEquals(tooAccount);
		AccountType accountType = accountTypeRepository.findByaccounttype("ADMIN").get();
		
		List<Account> optionAdmin = accountRepository.findByType(accountType);
		int x = randomNumber.randomNumberGenerator(0,optionAdmin.size()-1);
		Transactiontype transactiontype = transactionTypeRepository.findBytransactionType("DEPOSIT").get();
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		if (!optionfrom.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid agent account"));
		}
		if (!optionto.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid user account"));
		}
		if (optionAdmin.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No bazra account found"));
		}
		Optional<Settings> agentsettingOptional = settingRepository.findBysettingName("Agent Deposit Limit");
		if (!agentsettingOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("No setting 'Agent Deposit Limit' found"));
		}
		
		agentDepositLimit = agentsettingOptional.get().getValue();
		Account agAccount;
		Account toAccount;
		Account bazraAccount = optionAdmin.get(x);
		try {
			agAccount = optionfrom.get();
			if (!agAccount.getType().equals(accountTypeRepository.findByaccounttype("AGENT").get())) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid AGENT"));
			}

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
//		if (!agAccount.getType().getAccounttype().matches("AGENT")) {
//			return ResponseEntity.badRequest().body(new ResponseError("Not an agent account"));
//		}
		try {
			toAccount = optionto.get();
			if (toAccount.getType() != accountTypeRepository.findByaccounttype("USER").get()) {
				return ResponseEntity.badRequest().body(new ResponseError("Not a valid USER type account"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (toAccount == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (toAccount.isStatus()) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Blocked"));
		}

		if (depositRequest.getAmount() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
		}
		try {
			toaccountBalance = accountBalanceRepository.findByaccount(toAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		try {
			agAccountBalance = accountBalanceRepository.findByaccount(agAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		try {
			bazraBalance = bazraBalanceRepository.findByaccount(bazraAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		String transactioncode = "";
		for (int j = 0; j < 6; j++) {

			transactioncode = transactioncode + randomNumber.randomNumberGenerator(0, 9);
		}
		Levels toAccountLevel = userRepository.findByUsername(toAccount.getUser().getUsername()).get().getLevels();
		Optional<Settings> setting7Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_1)");
		if (!setting7Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_1)' found! "));
		}
		Optional<Settings> setting8Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_2)");
		if (!setting8Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_2)' found! "));
		}
		Optional<Settings> setting9Optional = settingRepository.findBysettingName("Balance Limit (LEVEL_3)");
		if (!setting9Optional.isPresent()) {
			return ResponseEntity.badRequest()
					.body(new ResponseError("No setting 'Balance Limit (LEVEL_3)' found! "));
		}
		if (toAccountLevel.equals(Levels.LEVEL_1)) {
			maxBalanceL1 = setting7Optional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_2)) {
			maxBalanceL1 = setting8Optional.get().getValue();
		} else if (toAccountLevel.equals(Levels.LEVEL_3)) {
			maxBalanceL1 = setting9Optional.get().getValue();
		}
		BigDecimal balance = agAccountBalance.getBalance();
		if (agentDepositLimit.compareTo(depositRequest.getAmount()) == -1) {
			return ResponseEntity.badRequest().body(new ResponseError("Deposit Limit overpassed"));
		}
		if (maxBalanceL1.compareTo(toaccountBalance.getBalance().add(depositRequest.getAmount())) == -1) {
			return ResponseEntity.badRequest().body(new ResponseError("User Account not able to accept"));
		}
		if (balance.compareTo(depositRequest.getAmount()) == 1) {
			agAccountBalance.setBalance(balance.subtract(depositRequest.getAmount()));
			accountRepository.save(agAccount);
			toaccountBalance.setBalance(toaccountBalance.getBalance().add(depositRequest.getAmount()));
			accountRepository.save(toAccount);
			accountBalanceRepository.save(agAccountBalance);
			accountBalanceRepository.save(toaccountBalance);
			AccountBalance accountBalance = accountBalanceRepository.findByaccount(toAccount).get();
			AgentTransaction agentTransaction = new AgentTransaction();
			agentTransaction.setAgentAccount(agAccount);
			agentTransaction.setCustomerAccount(toAccount);
			agentTransaction.setAmount(depositRequest.getAmount());
			agentTransaction.setCommission(commissionfee);
			agentTransaction.setTime(today);
			agentTransactionRepository.save(agentTransaction);
			FullTransaction fullTransaction = new FullTransaction();
			fullTransaction.setFromAccountNumber(agAccount.getAccountNumber());
			fullTransaction.setToAccountNumber(toAccount.getAccountNumber());
			fullTransaction.setTransactionAmount(depositRequest.getAmount());
			fullTransaction.setTransactionDateTime(today);
			fullTransaction.setTransactiontype(transactiontype.getTransactionType());
			fullTransactionRepository.save(fullTransaction);
			Transaction transaction = new Transaction();
			transaction.setCreateDate(LocalDate.now());
			transaction.setTransactionAmount(depositRequest.getAmount());
			transaction.setTransactionCode(transactioncode);
			transaction.setTransactiontype(transactiontype);
			transactionRepository.save(transaction);
			Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
			DebitTransaction debitTransaction = new DebitTransaction();
			debitTransaction.setAccount(agAccount);
			debitTransaction.setCreateDate(LocalDate.now());
			debitTransaction.setTransaction(transaction2);
			debitTransactionRepository.save(debitTransaction);

			CreditTransaction creditTransaction = new CreditTransaction();
			creditTransaction.setAccount(toAccount);
			creditTransaction.setCreateDate(LocalDate.now());
			creditTransaction.setTransaction(transaction2);
			creditTransactionRepository.save(creditTransaction);
			return ResponseEntity
					.ok(new DepositResponse(transaction.getTransactionAmount(), today,
							"Amount " + depositRequest.getAmount()
									+ "$ has been creadited to your account. Your current balance is "
									+ accountBalance.getBalance().add(depositRequest.getAmount())));
		}

		return ResponseEntity.badRequest().body(new ResponseError("Agent has Insufficient balance"));
	}

	public ResponseEntity<?> pay(DepositRequest depositRequest, String name) {
		String frommAccount = "+251" + name.substring(depositRequest.getToAccountNumber().length() - 9);
		String tooAccount = "+251"
				+ depositRequest.getToAccountNumber().substring(depositRequest.getToAccountNumber().length() - 9);
		Optional<Account> optionfrom = accountRepository.findByAccountNumberEquals(frommAccount);
		Optional<Account> optionto = accountRepository.findByAccountNumberEquals(tooAccount);
		AccountType accountType = accountTypeRepository.findByaccounttype("ADMIN").get();
		List<Account> optionAdmin = accountRepository.findByType(accountType);
		int x = randomNumber.randomNumberGenerator(0,optionAdmin.size()-1);
		Transactiontype transactiontype = transactionTypeRepository.findBytransactionType("PAY").get();
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		if (!optionfrom.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (!optionto.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid Merchant account"));
		}
		if (optionAdmin.isEmpty()) {
			return ResponseEntity.badRequest().body(new ResponseError("No valid admin account"));
		}
		Account frAccount = optionfrom.get();
		Account merAccount = optionto.get();
		Account bazraAccount = optionAdmin.get(x);
		try {
			merAccount = accountRepository.findByAccountNumberEquals(depositRequest.getToAccountNumber()).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (merAccount == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
		}
		if (!merAccount.getType().getAccounttype().matches("MERCHANT")) {
			return ResponseEntity.badRequest().body(new ResponseError("Not a merchant account"));
		}
		if (depositRequest.getAmount() == null) {
			return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
		}
		try {
			fromaccountBalance = accountBalanceRepository.findByaccount(frAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}

		try {
			agAccountBalance = accountBalanceRepository.findByaccount(merAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		try {
			bazraBalance = bazraBalanceRepository.findByaccount(bazraAccount).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account Balance Does not Exist"));
		}
		String transactioncode = "";
		for (int j = 0; j < 6; j++) {

			transactioncode = transactioncode + randomNumber.randomNumberGenerator(0, 9);
		}
		BigDecimal balance = fromaccountBalance.getBalance();
		if (balance.compareTo(depositRequest.getAmount()) == 1) {
			fromaccountBalance.setBalance(balance.subtract(depositRequest.getAmount()));
			accountRepository.save(frAccount);
			agAccountBalance.setBalance(agAccountBalance.getBalance().add(depositRequest.getAmount()));
			accountRepository.save(merAccount);
			accountBalanceRepository.save(fromaccountBalance);
			accountBalanceRepository.save(agAccountBalance);
			AccountBalance accountBalance = accountBalanceRepository.findByaccount(frAccount).get();
			
			FullTransaction fullTransaction = new FullTransaction();
			fullTransaction.setFromAccountNumber(frAccount.getAccountNumber());
			fullTransaction.setToAccountNumber(merAccount.getAccountNumber());
			fullTransaction.setTransactionAmount(depositRequest.getAmount());
			fullTransaction.setTransactionDateTime(today);
			fullTransaction.setTransactiontype(transactiontype.getTransactionType());
			fullTransactionRepository.save(fullTransaction);
			Transaction transaction = new Transaction();
			transaction.setCreateDate(LocalDate.now());
			transaction.setTransactionAmount(depositRequest.getAmount());
			transaction.setTransactionCode(transactioncode);
			transaction.setTransactiontype(transactiontype);
			transactionRepository.save(transaction);
			Transaction transaction2 = transactionRepository.findBytransactionCode(transactioncode).get();
			DebitTransaction debitTransaction = new DebitTransaction();
			debitTransaction.setAccount(frAccount);
			debitTransaction.setCreateDate(LocalDate.now());
			debitTransaction.setTransaction(transaction2);
			debitTransactionRepository.save(debitTransaction);

			CreditTransaction creditTransaction = new CreditTransaction();
			creditTransaction.setAccount(merAccount);
			creditTransaction.setCreateDate(LocalDate.now());
			creditTransaction.setTransaction(transaction2);
			creditTransactionRepository.save(creditTransaction);
			return ResponseEntity
					.ok(new DepositResponse(transaction.getTransactionAmount(), LocalDate.now(),
							"Amount " + depositRequest.getAmount()
									+ "$ has been debited from your account. Your current balance is "
									+ accountBalance.getBalance().add(depositRequest.getAmount())));
		}

		return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
	}

}
