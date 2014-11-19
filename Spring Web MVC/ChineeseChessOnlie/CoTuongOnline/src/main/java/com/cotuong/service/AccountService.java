package com.cotuong.service;

import com.cotuong.model.Account;

public interface AccountService {
	void add(Account account);
	Account getAccount(String email);
	void update(Account account);
	void delete(Account account);
}
