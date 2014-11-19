package com.cotuong.app;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cotuong.uri.AppRestUri;
import com.cotuong.model.Account;
import com.cotuong.service.AccountService;
import com.cotuong.service.MailService;


@Controller
public class RestController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	private MailService mailService;

	@RequestMapping(value=AppRestUri.check_account,method=RequestMethod.POST)
	public @ResponseBody Account checkAccount(@RequestBody Account account){
		Account check=accountService.getAccount(account.getEmail());
		if(check!=null){
			account.setEmail("fail");
		}else{
			accountService.add(account);
			mailService.sendMail("appgame.cotuong@gmail.com",account.getEmail(), "Spring Mail Subject","https://www.facebook.com/");
		}
		account.setPassword("");
		return account;
	}
	
	
}
