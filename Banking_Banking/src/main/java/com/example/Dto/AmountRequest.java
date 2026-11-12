package com.example.Dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AmountRequest {

	
	
	@NotNull
	@DecimalMin(value ="0.01", message ="amount must be  greater be greater than zero")
	private BigDecimal amount;
	
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	public void setAmount(BigDecimal amount) {this.amount=amount;}

	public Object getToAccountId() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
