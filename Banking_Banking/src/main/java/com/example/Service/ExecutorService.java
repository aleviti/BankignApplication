package com.example.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.example.Model.Account;
@Service
public class ExecutorService {

	public Future<Account> submit(Callable<Account> task) {
		// TODO Auto-generated method stub
		return null;
	}

}
