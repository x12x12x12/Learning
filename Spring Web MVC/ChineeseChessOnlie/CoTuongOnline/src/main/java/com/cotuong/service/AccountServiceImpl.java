package com.cotuong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cotuong.model.Account;

public class AccountServiceImpl implements AccountService{
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	private Query query;
	private Update update;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public AccountServiceImpl (MongoTemplate mongoTemplate){
		this.mongoTemplate=mongoTemplate;
	}
	@Override
	public void add(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		if(!mongoTemplate.collectionExists(Account.class)){
			mongoTemplate.createCollection(Account.class);
		}
		account.setPoint(10);
		account.setStatus(2);
		mongoTemplate.insert(account);
	}

	@Override
	public Account getAccount(String email) {
		query=new Query(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query, Account.class);
	}

	@Override
	public void update(Account account) {
//		update=new Update();
		
	}

	@Override
	public void delete(Account account) {
		
	}
	
	@Override
	public void activateAccount(String email){
		query=new Query(Criteria.where("email").is(email));
		update=new Update();
		update.set("status",0);
		mongoTemplate.updateFirst(query, update, Account.class);
	}
}
