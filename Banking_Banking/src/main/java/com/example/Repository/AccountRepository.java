package com.example.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.Model.Account;
import com.example.Model.Transaction;

@Repository
public class AccountRepository{
	
	
	
	private final Map<Long, Account>accounts=new ConcurrentHashMap<>();
	private final Map<Long , List<Transaction>> transactions=new ConcurrentHashMap<>();
	private final AtomicLong idGenerator=new AtomicLong(1);
	
	public AccountRepository() {
		Account a1=new Account(idGenerator.getAndIncrement(), "Alice", new BigDecimal("100000"));
		Account a2=new Account(idGenerator.getAndIncrement(), "Bob", new BigDecimal ("100000"));
		save(a1);
		save(a2);
	}
	
	
	
	public Account save (Account account) {
		accounts.put(account.getId(), account);
		transactions.putIfAbsent(account.getId(), new CopyOnWriteArrayList<>());
		return account;
	}
	
	public Account  findById(long id) {return accounts.get(id);}
	public List<Account> findAll(){return List.copyOf(accounts.values());}
	public long nextId() {return idGenerator.getAndIncrement();}
	public void addTransactions(Long accountId, Transaction transaction) {
		transactions.computeIfAbsent(accountId,  k -> new  CopyOnWriteArrayList<>());
	}
	
	
	public List<Transaction> getTransactions(Long accountId){
		return List.copyOf(transactions.getOrDefault(accountId, List.of()));
	
	}
}