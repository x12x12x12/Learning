package com.dominhquan.app;

import java.util.*;

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
	private ItemService itemService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountService accountService;
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
	public @ResponseBody List<Order> getListOrderByRestaurantCode(@PathVariable("code") String code){
		List<Order> list=itemService.getListOrderByRestaurantCode(code);
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
	
	@RequestMapping(value=AppRestUri.create_order,method=RequestMethod.POST)
	public @ResponseBody String createOrder(@RequestBody Order order){
		String id_order="";
		if(order.getPhone().equalsIgnoreCase("") || order.getAddress().equalsIgnoreCase("") || order.getList_food().equalsIgnoreCase("")){
			return "fail";
		}else{
			try {
				HashMap<String,String> data_send=new HashMap<String, String>();
				String[] array_food=order.getList_food().split(",");
				for(String food : array_food){
					String[] food_data=food.split("-");
					if(!data_send.containsKey(food_data[0])){
						data_send.put(food_data[0],food);
					}else{
						food=data_send.get(food_data[0])+","+food;
						data_send.put(food_data[0],food);
					}
				}
				for(Map.Entry<String,String> entry : data_send.entrySet()){
//					System.out.println("Key :"+entry.getKey()+",Value :"+entry.getValue());
					order.setRestaurant_code(entry.getKey());
					order.setList_food(entry.getValue());
					id_order+=itemService.createOrder(order)+",";
				}
			}catch (Exception ex){
				order.setId("fail");
			}
		}
		return id_order;
	}

	@RequestMapping(value=AppRestUri.update_order,method=RequestMethod.POST)
	public @ResponseBody String updateOrder(@RequestBody Order order){
		String result="fail";
		try{
			itemService.updateOrder(order);
		}catch (Exception ex){
			return result;
		}
		return result;
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

	@RequestMapping(value=AppRestUri.check_account,method=RequestMethod.POST)
	public @ResponseBody Account checkAccount(@RequestBody Account account){
		Account check=accountService.getAccount(account.getEmail());
		if(check!=null){
			account.setEmail("fail");
		}else{
			accountService.add(account);
//			mailService.sendMail("appgame.cotuong@gmail.com",account.getEmail(), "Spring Mail Subject","https://www.facebook.com/");
		}
		account.setPassword("");
		return account;
	}

	@RequestMapping(value=AppRestUri.import_test_data,method=RequestMethod.GET)
	public void import_food_data(){
//		String [][] food_data=new String[][]{
//				{"813817187-food01","Chả cá","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food02","Bánh bao","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food03","Bánh ướt","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food04","Bánh mì","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food05","Bánh tầm","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food06","Bánh giò","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food07","Bánh canh","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food08","Bún riêu","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food09","Bún cá","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food10","Bún cua","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food11","Bún mắm","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food12","Bún đậu","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food13","Bún thịt nướng","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food14","Bánh xèo ","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food15","Chả giò","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food16","Chả chiên","New World","100","1","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food17","Bánh tráng trộn","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food18","Cơm tấm","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food19","Bún bò huế","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//				{"813817187-food20","Bún chả","New World","100","0","http://i.imgur.com/w93vQqOb.jpg","http://i.imgur.com/w93vQqOb.jpg"},
//		};
//		for(String[] row :food_data){
//			int length=row.length;
//			for (int i=0;i<length-1;i++){
//				Item item=new Item();
//				item.setId(row[0]);
//				item.setName(row[1]);
//				item.setRestaurant_name(row[2]);
//				item.setPrice(Double.parseDouble(row[3]));
//				item.setStatus(Integer.parseInt(row[4]));
//				item.setImg_url(row[5]);
//				item.setImg_ico(row[6]);
//				item.setCreateDate(new Date());
//				item.setUpdateDate(new Date());
//				itemService.createItem(item);
//			}
//		}
	}
	
}
