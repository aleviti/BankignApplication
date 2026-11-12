package com.example.Model;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

	
	
	private final Long id;
	private final String owner;
	
	private BigDecimal balance;
	
	public final transient ReentrantLock lock=new ReentrantLock();
	
	public Account (Long id, String owner, BigDecimal balance) {
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


	public ReentrantLock getLock() {
		// TODO Auto-generated method stub
		return lock;
	}


	public void setBalance(BigDecimal newBalance) {this.balance=balance;}
		// TODO Auto-generated method stub


		
	}



