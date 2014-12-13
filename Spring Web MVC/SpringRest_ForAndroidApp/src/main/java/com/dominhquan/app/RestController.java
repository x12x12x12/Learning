package com.dominhquan.app;

import java.util.Date;
import java.util.List;

import com.dominhquan.model.Order;
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

import com.dominhquan.model.Account;
import com.dominhquan.model.Item;
import com.dominhquan.service.AccountService;
import com.dominhquan.service.ItemService;
import com.dominhquan.service.MailService;
import com.dominhquan.uri.AppRestUri;


@Controller
public class RestController {
	
	private static final Logger logger = LoggerFactory.getLogger(RestController.class);
	@Autowired
	ItemService itemService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AccountService accountService;
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value=AppRestUri.get_item,method=RequestMethod.GET)
	public @ResponseBody Item getItem(@PathVariable("id") String id){
		Item item=itemService.getItem(id);
		return item;
	}
	
	@RequestMapping(value=AppRestUri.get_all_restaurant,method=RequestMethod.GET)
	public @ResponseBody List<Account> getListRestaurant(){
		List<Account> list=itemService.getListRestaurants();
		return list;

	}

	@RequestMapping(value=AppRestUri.get_all_items_in_restaurant,method=RequestMethod.GET)
	public @ResponseBody List<Item> getListRestaurant(@PathVariable("name") String name){
		List<Item> list=itemService.getListItem(name);
		return list;

	}
	@RequestMapping(value=AppRestUri.get_all_orders_in_restaurant,method=RequestMethod.GET)
	public @ResponseBody List<Order> getListOrderInRestaurant(@PathVariable("name") String name){
		List<Order> list=itemService.getListOrderByRestaurant(name);
		return list;
	}

	@RequestMapping(value=AppRestUri.get_all_orders_by_user,method=RequestMethod.GET)
	public @ResponseBody List<Order> getListOrderByUser(@PathVariable("name") String name){
		List<Order> list=itemService.getListOrderByUser(name);
		return list;
	}

	@RequestMapping(value=AppRestUri.get_all_hot_items,method=RequestMethod.GET)
	public @ResponseBody List<Item> getListItemByRestaurant(){
		List<Item> list=itemService.getListHotItem();
		return list;
	}
	
	@RequestMapping(value=AppRestUri.order_menu,method=RequestMethod.POST)
	public @ResponseBody Order orderItem(@RequestBody Order order){
		order.setStatus(0);
		return order;
	}
	
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
	
	@RequestMapping(value=AppRestUri.update_item,method=RequestMethod.POST)
	public @ResponseBody Item updateItem(@RequestBody Item item){
		logger.info("Update item :" + item.getId());
		item.setUpdateDate(new Date());
		try{
			if(item.getId()!=null && item.getName()!=null &&item.getPrice()!=null && item.getImg_url()!=null){
				if(item.getImg_ico()==null){
					item.setImg_ico("");
				}
				itemService.updateItem(item);
			}
		}catch (Exception ex){
			item.setId("fail");
		}
		return item;
	}
	
	@RequestMapping(value=AppRestUri.create_item,method=RequestMethod.POST)
	public @ResponseBody Item createItem(@RequestBody Item item){
		logger.info("Create item :"+item.getId());
		item.setCreateDate(new Date());
		item.setUpdateDate(new Date());
		try{
			if(item.getId()!=null && item.getName()!=null &&item.getPrice()!=null && item.getImg_url()!=null){
				if(item.getImg_ico()==null){
					item.setImg_ico("");
				}
				itemService.createItem(item);
			}
		}catch(Exception ex){
			item.setId("fail");
		}
		return item;
	}
	
	@RequestMapping(value=AppRestUri.delete_item,method=RequestMethod.POST)
	public @ResponseBody Item deleteItem(@PathVariable("id") String id){
		logger.info("User request to delete item :"+ id);
		Item item=new Item();
			try{
				item=itemService.removeItem(id);
				if(item.getName()!=null){
					logger.info("Delete item " + item.getName() + " successful");
				}
			}catch(Exception ex){
				item.setId("fail");
			}
		return item;
	}
	
	
	
}
