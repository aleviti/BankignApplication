package com.example.Exception;

import java.math.BigDecimal;


public class InsufficientFoundsException extends RuntimeException {
public InsufficientFoundsException (Long id) {super("Insufficient Founds Exception:" +id);
	

}

public InsufficientFoundsException(Long id, BigDecimal balance) {
	// TODO Auto-generated constructor stub
	
	
	
}
}