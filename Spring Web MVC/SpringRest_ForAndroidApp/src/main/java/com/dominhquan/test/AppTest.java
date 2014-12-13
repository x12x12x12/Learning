package com.dominhquan.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dominhquan.model.Account;
import com.dominhquan.service.AccountServiceImpl;
import com.dominhquan.service.MailService;

public class AppTest {
	
	public static void main(String args[]){

            ApplicationContext context= new ClassPathXmlApplicationContext("templates/servlet-context.xml");
            try {
                    context = new ClassPathXmlApplicationContext("servlet-context.xml");
            } catch (BeansException e) {
                    e.printStackTrace();
            }
            AccountServiceImpl accountServiceImpl=(AccountServiceImpl) context.getBean("accountService");
//		ItemServiceImpl itemServiceImpl= (ItemServiceImpl) context.getBean("itemService");
		MailService mailService=(MailService) context.getBean("mailService");

		/**
		 *
		 */
//		mailService.sendMail("dominhquan.uit@gmail.com","appgame.cotuong@gmail.com", "Spring Mail Subject","Spring Mail Data");
//		mailService.sendMail("appgame.cotuong@gmail.com","dominhquan.uit@gmail.com", "Spring Mail Subject","https://www.facebook.com/");
		  /**
		   * Account
		   */
		Account account=new Account();
		account.setEmail("11520616@gmail.com");
		account.setName("Lotteria");
		account.setPassword("123456");
		accountServiceImpl.add(account);
		mailService.sendActivationEmail(account, "appgame.cotuong@gmail.com");
//
//		account=accountServiceImpl.getAccount("dominhquan.uit@gmail.com");
//		if(account!=null){
//			System.out.println(account.getPassword());
//		}else{
//			System.out.println("Fail");
//		}
			/**
			 * Item
			 */
//		Item item=itemServiceImpl.getItem("food1");
//		item.setStatus(1);
//		item.setCreateDate(new Date());
//		item.setUpdateDate(new Date());
//		itemServiceImpl.updateItem(item);
//		for(int i=1;i<21;i++){
//			Item item=new Item();
//			item.setId("food"+i);
//			item.setName("Food-"+i);
//			item.setRestaurant_name("Restaurant-"+i);
//			item.setCreateDate(new Date());
//			item.setUpdateDate(new Date());
//			item.setPrice(new Double(125.50*i));
//			itemServiceImpl.createItem(item);
//		}
//		List<Item> list=itemServiceImpl.getListItem("Restaurant");
//		for (Item item : list) {
//			System.out.println(item.toString());
//		}
        String [][] food_data=new String[][]{
                {"food01","Chả cá","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food02","Bánh bao","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food03","Bánh ướt","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food04","Bánh mì","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food05","Bánh tầm","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food06","Bánh giò","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food07","Bánh canh","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food08","Bún riêu","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food09","Bún cá","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food10","Bún cua","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food11","Bún mắm","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food12","Bún đậu","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food13","Bún thịt nướng","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food14","Bánh xèo ","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food15","Chả giò","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food16","Chả chiên","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food17","Bánh tráng trộn","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food18","Cơm tấm","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food19","Bún bò huế","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
                {"food20","Bún chả","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
        };
//        for(String[] row :food_data){
//            int length=row.length;
//            for (int i=0;i<length-1;i++){
//                Item item=new Item();
//                item.setId(row[0]);
//                item.setName(row[1]);
//                item.setRestaurant_name(row[2]);
//                item.setPrice(Double.parseDouble(row[3]));
//                item.setStatus(Integer.parseInt(row[4]));
//                item.setImg_url(row[5]);
//                item.setImg_ico(row[6]);
//                item.setCreateDate(new Date());
//                item.setUpdateDate(new Date());
//                itemServiceImpl.createItem(item);
//            }
//        }
	}
}
