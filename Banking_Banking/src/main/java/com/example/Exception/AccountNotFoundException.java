package com.example.Exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(Long id) {super("Account not found:" +id);

}
}
