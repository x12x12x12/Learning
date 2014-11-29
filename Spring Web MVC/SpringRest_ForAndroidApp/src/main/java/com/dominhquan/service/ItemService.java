package com.dominhquan.service;

import java.util.List;

import com.dominhquan.model.Item;

public interface ItemService {
	void createItem(Item item);
	Item getItem(String id);
	Item removeItem(String id);
	void updateItem(Item item);
	List<Item> getListItem(String restaurant);
}
