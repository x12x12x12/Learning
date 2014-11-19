package com.server.service;

import com.server.model.Account;

public interface AccountService {
	void add(Account account);
	Account getAccount(String email);
	void update(Account account);
	void delete(Account account);
}
