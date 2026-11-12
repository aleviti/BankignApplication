package com.example.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Service;

//import org.apache.catalina.startup.ClassLoaderFactory.Repository;
//import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

import com.example.Dto.AccountDto;
import com.example.Dto.TransferRequest;
import com.example.Exception.InsufficientFoundsException;
import com.example.Model.Account;
import com.example.Model.Transaction;
import com.example.Model.TransactionType;
import com.example.Repository.AccountRepository;

import jakarta.validation.Valid;
@Service
public class AccountService {
	
	private Account account;
	private AccountRepository accountrepository;
	
	private final  java.util.concurrent.ExecutorService bankingExecutor=Executors.newFixedThreadPool(10, r-> {
		
	Thread t=new Thread(r);
	t.setName("Banking-worker-" +  t.getId());
		t.setDaemon(true);
		return t;
	});
	
	
	public AccountService(AccountRepository repository ) {this.accountrepository=repository;}
	
	
	
	public Account creaateAccount(String owner, BigDecimal intialBalance) {
		Account account= new Account(accountrepository.nextId(), owner , intialBalance);
		return accountrepository.save(account);
	}
	
	
	public List<Account> findAll1() {return accountrepository.findAll();}
	
	
	private Account requireAccount(Long id) throws AccountNotFoundException {
		
		Account account=accountrepository.findById(id);
	     if(account==null) {
	    	 throw new AccountNotFoundException();
	     }
	     return account;
	}
	
	public Future<Account> getBalanceAsync(Long id){
		Callable<Account>task= ()->{
			simulateLatency(150);
			return requireAccount(id);
		};
		return bankingExecutor.submit(task);
	}
	
	
	private void simulateLatency(int i) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private void simulateLatency(long mills) {
		try {
			Thread.sleep(mills);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Operation interrupted", e);
		}
	}
	public Future<Account> depositAsync (Long id, BigDecimal amount){
		Callable<Account> task= ()-> {
			Account account =requireAccount(id);
			
			ReentrantLock lock=account.getLock();
			lock.lock();
		try {
			simulateLatency(300);
			
			BigDecimal newBalance= account.getBalance().add(amount);
			account.setBalance(newBalance);
			accountrepository.addTransactions(id,
			new Transaction(id, TransactionType.DEPOSIT, amount, newBalance));
			return account;
		}finally {
			lock.unlock();
		}
		};
		return bankingExecutor.submit(task);
	}

	public Future<Account> withDrawAsync (Long id, BigDecimal amount){
		Callable<Account> task= ()-> {
			Account account =requireAccount(id);
			
			ReentrantLock lock=account.getLock();
			lock.lock();
		try {
			simulateLatency(300);
			if(account.getBalance().compareTo(amount)<0) {
				throw new InsufficientFoundsException(id, account.getBalance());
			}
			
			BigDecimal newBalance= account.getBalance().subtract(amount);
			account.setBalance(newBalance);
			accountrepository.addTransactions(id,
			new Transaction(id, TransactionType.DEPOSIT, amount, newBalance));
			return account;
		}finally {
			lock.unlock();
		}
		};
		return bankingExecutor.submit(task);
	}
	public Future<Account> transferAsync (Long fromId,Long toId, BigDecimal amount){
		Callable<Account> task= ()-> {
			if(fromId.equals(toId)) {
				throw new IllegalArgumentException("Cannot transfer to the same");
			}
			Account from=requireAccount(fromId);
			Account to= requireAccount(toId);
			
			Account first=fromId < toId? from : to;
		     Account second= fromId < toId? to: from;
			
			first.getLock().lock();
			try {
				second.getLock().lock();
			
			//ReentrantLock lock=account.getLock();
			//lock.lock();
		try {
			simulateLatency(400);
			if(from.getBalance().compareTo(amount)<0) {
				throw new InsufficientFoundsException(fromId, from.getBalance());
			}
			
			BigDecimal fromBalance= from.getBalance().subtract(amount);
			BigDecimal toBalance=to.getBalance().add(amount);
			from.setBalance(fromBalance);
			to.setBalance(toBalance);
			accountrepository.addTransactions(fromId,
			new Transaction(fromId, TransactionType.TRANSFER_OUT));
			accountrepository.addTransactions(toId,
					new Transaction(toId, TransactionType.TRANSFER_IN,amount, toBalance));
			return from;
		}finally {
			second.getLock().unlock();
		}
		}finally {
			first.getLock().unlock();
		}
		};
		return bankingExecutor.submit(task);
	}

	

public Future<List<Transaction>> getTransactionsAsync1(Long id){
	Callable<List<Transaction>> task=()->{
		requireAccount(id);
		simulateLatency(150);
		return accountrepository.getTransactions(id);
	};
	
	return bankingExecutor.submit(task);
		
}

public Map<String, Object> getStatement1(Long id, long timeoutMs)
		throws InterruptedException, ExecutionException, TimeoutException{
			Future<Account>accountFuture=getBalanceAsync(id);
			Future<List<Transaction>>txFuture=getTransactionsAsync(id);
			Account account=accountFuture.get(timeoutMs,TimeUnit.MILLISECONDS);
			List<Transaction>txs=txFuture.get(timeoutMs, TimeUnit.MILLISECONDS);
		 
		    		 
		    		 
	
		
		return Map.of(
				"account", account,
				"transaction", txs
	);
}


	public Account createAccount(String owner, BigDecimal intialBalance) {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Collection<AccountDto> findAll() {
		// TODO Auto-generated method stub
		return null;
	}


	public Object getStatment(Long id, long timeoutMs) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<List<Transaction>> getTransactionsAsync(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<Account> transferAsync(Long id, Object toAccountId, @Valid TransferRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<Account> withdrawAsync(Long id, Object toAccountId) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<Account> depositAsync(Long id, Object toAccountId, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<Account> withdrawAsync(Long id, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}
	public Future<Account> transferAsync(Long id, @Valid TransferRequest amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	


	
}
