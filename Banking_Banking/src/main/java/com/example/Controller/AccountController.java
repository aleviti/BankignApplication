package com.example.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.AccountDto;
import com.example.Dto.AmountRequest;
import com.example.Dto.TransferRequest;
import com.example.Model.Account;
import com.example.Model.Transaction;
import com.example.Service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController{
	
	private static final long TIMEOUT_MS= 5000;
	private final AccountService accountService;
	
	
	public AccountController (AccountService accountService) {this.accountService=accountService;}

	@PostMapping
	public ResponseEntity<AccountDto> createAccount(@RequestParam String owner, @RequestParam(defaultValue="0") BigDecimal intialBalance){
	
	
	Account account= accountService.createAccount(owner, intialBalance);
return ResponseEntity.status(HttpStatus.CREATED).body(toDto(account));	
	
	
}
	
	
	@GetMapping
	public List<AccountDto> listAccounts(){
		return accountService.findAll().stream()./*map(this::toDto)*/collect(Collectors.toList());
	}
	
	
	@GetMapping("/{id}/transfer")
	
	public Callable<ResponseEntity<AccountDto>> transfer(@PathVariable Long id, @Valid @RequestBody TransferRequest request){
		return ()->{
			Future<Account> future=accountService.transferAsync(id, request.getToAccountId(), request);
			Account account=resolve(future);
			return ResponseEntity.ok(toDto(account));
		};
	}
	
@GetMapping("/{id}")
	
	public Callable<ResponseEntity<AccountDto>> getAccount(@PathVariable Long id){
		return ()->{
			Future<Account> future=accountService.getBalanceAsync(id);
			
			
			
			//(id, request.getToAccountId(), request);
			Account account=resolve(future);
			return ResponseEntity.ok(toDto(account));
		};
	}


		
		
		
		@PostMapping("/{id}/deposit")

		public Callable<ResponseEntity<AccountDto>> deposit(@PathVariable Long id, @Valid @RequestBody AmountRequest request  ){
	return ()->{
		Future<Account> future=accountService.depositAsync(id, request.getToAccountId(),request.getAmount());
		
		
		
		//(id, request.getToAccountId(), request);
		Account account=resolve(future);
		return ResponseEntity.ok(toDto(account));
	};
		}
	@PostMapping("/{id}/withdraw")

	public Callable<ResponseEntity<AccountDto>> withdraw(@PathVariable Long id, @Valid @RequestBody  AmountRequest amount ){
return ()->{
	Future<Account> future=accountService.withdrawAsync(id,amount.getAmount());
	
	
	
	//(id, request.getToAccountId(), request);
	Account account=resolve(future);
	return ResponseEntity.ok(toDto(account));
};

			
}
@GetMapping("/{id}/Transfer")

public Callable<ResponseEntity<AccountDto>> transfer1(@PathVariable Long id, @Valid @RequestBody  TransferRequest request){
	
	return ()->{
		Future<Account> future=accountService.transferAsync(id,request.getAmount());
		
		
		
		//(id, request.getToAccountId(), request);
		Account account=resolve(future);
		return ResponseEntity.ok(toDto(account));
};
}

	@GetMapping("/{id}/transactions")

	public Callable<ResponseEntity<List<Transaction>>> transaction(@PathVariable Long id){
return ()->{
	Future<List<Transaction>> future=accountService.getTransactionsAsync(id);
	try {
		return  ResponseEntity.ok(future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS));
	}catch(ExecutionException e) {
	 throw unwrap(e);	
	} catch(TimeoutException e) {
		future.cancel(true);
		throw e;
	}
};
		
		
}


@GetMapping("/{id}/statment")

public Callable<ResponseEntity<Map<String, Object>>> statement(@PathVariable Long id){

		//	Account account=resolve(future);
			//return ResponseEntity.ok(toDto(account));
		//};
	//}
	
	return ()-> ResponseEntity.ok(accountService.getStatement1(id, TIMEOUT_MS));
}
	private Account resolve(Future<Account> future) throws Exception {
		try {
			return future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
		}catch(ExecutionException e) {
		 throw unwrap(e);	
		} catch(TimeoutException e) {
			future.cancel(true);
			throw e;
		}
	}
		private Exception unwrap(ExecutionException e) {
			Throwable cause= e.getCause();
			if( cause instanceof RuntimeException re) {
				return re;
			}
			return e;
		}
		
		private AccountDto toDto(Account account) {
			return new AccountDto(account.getId(), account.getowner(),account.getBalance());
	};
	
}