package com.example.Exception;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<?> handleNotFound(AccountNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
	.body(Map.of("error", ex.getMessage()));
	}
	
	
	@ExceptionHandler(InsufficientFoundsException.class)
	public ResponseEntity<?> handleNotFunds(InsufficientFoundsException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	.body(Map.of("error", ex.getMessage()));
	}

	
	@ExceptionHandler(TimeoutException.class)
	public ResponseEntity<?> handleTimeoutException(TimeoutException ex){
		return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
	.body(Map.of("error", "The operation timed out waiting on"));
	}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleInsufficentNotFunds(IllegalArgumentException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	.body(Map.of("error", ex.getMessage()));
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception ex){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	.body(Map.of("error", ex.getMessage()));
	}
	
	
	
	
}
