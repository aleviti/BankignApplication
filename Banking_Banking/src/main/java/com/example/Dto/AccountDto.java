package com.example.Dto;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class AccountDto {
	private final Long id;
	private final String owner;
	
	private BigDecimal balance;
	
	public final transient ReentrantLock lock=new ReentrantLock();
	
	public AccountDto (Long id, String owner, BigDecimal balance) {
		this.id=id;
		this.owner=owner;
		this.balance=balance;
	}
	
	
	public Long getId() {
		return this.id;
	}
	
	public String getowner() {
		return this.owner;
	}
	
	public BigDecimal getBalance() {
		return this.balance;
	}

}
