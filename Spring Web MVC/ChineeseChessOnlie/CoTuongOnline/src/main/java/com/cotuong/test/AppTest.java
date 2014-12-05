package com.cotuong.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cotuong.model.Account;
import com.cotuong.service.AccountServiceImpl;
import com.cotuong.service.MailService;

@SuppressWarnings("unused")
public class AppTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context=new ClassPathXmlApplicationContext("servlet-context.xml");
		AccountServiceImpl accountServiceImpl=(AccountServiceImpl) context.getBean("accountService");
//		MailService mailService=(MailService) context.getBean("mailService");
		
		
//		Account account=new Account();
//		account.setEmail("dominhquan.uit@gmail.com");
//		account.setName("Đỗ Minh Quân");
//		mailService.sendActivationEmail(account, "appgame.cotuong@gmail.com");
		
		/**
		 * Add custom data
		 */
//		for(int i=0;i<=20;i++){
//			Account account=new Account();
//			account.setName("Mr_"+i);
//			account.setEmail("mr_"+i+"_@gmail.com");
//			account.setPassword("123456");
//			accountServiceImpl.add(account);
//		}
		
		/**
		 * List Player Online
		 */
		List<Account> list=accountServiceImpl.getListOnline();
		for (Account account : list) {
			System.out.println(account.getName()+"-"+account.getEmail()+"-"+account.getPoint());
		}
//		accountServiceImpl.setStatusOffline("dominhquan.uit@gmail.com");
//		accountServiceImpl.setStatusOnline("dominhquan.uit@gmail.com");
	}

}
