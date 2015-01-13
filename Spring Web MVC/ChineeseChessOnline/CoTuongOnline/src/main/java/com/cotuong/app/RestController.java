package com.cotuong.app;

//import java.util.Date;
//import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
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
			String validate=passwordEncoder.encode(account.getEmail());
			try {
				mailService.sendMail("appgame.cotuong@gmail.com",account.getEmail(), "Account Activation Code","Your code :"+validate 
												+" Link active : http://localhost:8080/active?ref="+account.getEmail());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		account.setPassword("");
		return account;
	}
	
	@RequestMapping(value=AppRestUri.active_account,method=RequestMethod.POST)
	public @ResponseBody Account activeAccount(@RequestBody Account account){
		Account check=accountService.getAccount(account.getEmail());
		if(check!=null){
			if(passwordEncoder.matches(account.getEmail(),account.getPassword())){
				accountService.activateAccount(account.getEmail());	
				account.setPassword("ok");
				return account;
			}
		}
		account.setPassword("fail");
		return account;
	}
	
	@RequestMapping(value=AppRestUri.player_Online,method=RequestMethod.GET)
	public @ResponseBody List<Account> listOnline(){
		List<Account> list =accountService.getListOnline();
		return list;
	}

}
