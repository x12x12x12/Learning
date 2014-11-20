package com.cotuong.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cotuong.model.Account;
import com.cotuong.service.AccountServiceImpl;
import com.cotuong.service.MailService;

public class AppTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context=new ClassPathXmlApplicationContext("servlet-context.xml");
		AccountServiceImpl accountServiceImpl=(AccountServiceImpl) context.getBean("accountService");
		MailService mailService=(MailService) context.getBean("mailService");
		
		Account account=accountServiceImpl.getAccount("dominhquan.uit@gmail.com");
		if(account!=null){
			System.out.println(account.getEmail());
			System.out.println(account.getPassword());
			System.out.println(account.getName());
			System.out.println(account.getPoint());
			System.out.println(account.getStatus());
			System.out.println(account.getImg_url());
		}else{
			System.out.println("Fail");
		}
	}

}
