package com.project.service;

import com.project.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountServiceImpl implements AccountService{

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private MongoTemplate mongoTemplate;

	private Query query;
	
	public AccountServiceImpl (MongoTemplate mongoTemplate){
		this.mongoTemplate=mongoTemplate;
	}
	@Override
	public void add(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		if(!mongoTemplate.collectionExists(Account.class)){
			mongoTemplate.createCollection(Account.class);
		}
		mongoTemplate.insert(account);
	}

	@Override
	public Account getAccount(String email) {
		query=new Query(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query, Account.class);
	}

	@Override
	public void update(Account account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Account account) {
		// TODO Auto-generated method stub
		
	}

}
