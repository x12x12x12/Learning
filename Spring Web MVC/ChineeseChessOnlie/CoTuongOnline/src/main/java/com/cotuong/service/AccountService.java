package com.cotuong.service;

import java.util.List;

import com.cotuong.model.Account;

public interface AccountService {
	void add(Account account);
	Account getAccount(String email);
	void update(Account account);
	void delete(Account account);
	void setStatusOnline(String email);
	void setStatusOffline(String email);
	void activateAccount(String email);
	List<Account> getListOnline();
}
