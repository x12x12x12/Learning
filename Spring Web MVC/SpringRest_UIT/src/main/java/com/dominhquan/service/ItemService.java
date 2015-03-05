package com.dominhquan.service;

import java.util.List;

import com.dominhquan.model.Account;
import com.dominhquan.model.Item;
import com.dominhquan.model.Order;

public interface ItemService {
	void createItem(Item item);
	void updateItem(Item item);
	void updateOrder(Order order);
	String createOrder(Order order);
	Item getItem(String id);
	Item removeItem(String id);
	List<Item> getAllItem();
	List<Item> getListItem(String restaurant);
	List<Account> getListRestaurants();
	List<Item> getListHotItem();
	List<Order> getListOrderByRestaurantCode(String code);
	List<Order> getListOrderByUser(String userName);
	int countItem();
	Double getMoneyForItem(String id,int quantity);
}
