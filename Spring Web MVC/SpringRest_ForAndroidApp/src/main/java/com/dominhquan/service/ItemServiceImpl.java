package com.dominhquan.service;

import java.util.Date;
import java.util.List;

import com.dominhquan.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dominhquan.model.Item;
import com.dominhquan.model.Order;
import com.mongodb.MongoException;

import org.springframework.data.mongodb.core.query.Update;

public class ItemServiceImpl implements ItemService{
	
	private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	
	private MongoTemplate mongoTemplate;

	private Query query;
	
	public ItemServiceImpl (MongoTemplate mongoTemplate){
		this.mongoTemplate=mongoTemplate;
	}

	@Override
	public void createItem(Item item) throws MongoException{
		if(!mongoTemplate.collectionExists(Item.class)){
			mongoTemplate.createCollection(Item.class);
		}
		mongoTemplate.insert(item);
		logger.info("Save item successfully, Details : "+ item);
	}

	@Override
	public Item getItem(String id) throws MongoException{
		query=new Query(Criteria.where("_id").is(id));
		return mongoTemplate.findOne(query, Item.class);
	}
	
	@Override
	public Item removeItem(String id) throws MongoException{
		query=new Query(Criteria.where("_id").is(id));
		return mongoTemplate.findAndRemove(query, Item.class);
	}

	@Override
	public void updateItem(Item item) throws MongoException{
		query=new Query();
		query.addCriteria(Criteria.where("_id").is(item.getId()));
		Update update=new Update();
		update.set("name",item.getName());
		update.set("price",item.getPrice());
		update.set("updateDate",new Date());
		update.set("status",item.getStatus());
		update.set("img_url",item.getImg_url());
		update.set("img_ico",item.getImg_ico());
		mongoTemplate.updateFirst(query,update,Item.class);
		logger.info("Save item successfully, Details : "+ item.toString());
	}

	@Override
	public void updateOrder(Order order) throws MongoException{
		query=new Query();
		query.addCriteria(Criteria.where("_id").is(order.getId()));
		Update update=new Update();
		update.set("status",order.getStatus());
		mongoTemplate.updateFirst(query,update,Order.class);
	}

	@Override
	public List<Item> getListItem(String restaurant) throws MongoException{
		query=new Query(Criteria.where("restaurant_name").regex(restaurant));
		return mongoTemplate.find(query, Item.class);
	}

	@Override
	public List<Item> getListHotItem() throws MongoException{
		query=new Query(Criteria.where("status").is(0));
		query.with(new Sort(Sort.Direction.DESC,"updateDate"));
		return mongoTemplate.find(query, Item.class);
	}

	@Override
	public List<Account> getListRestaurants() throws MongoException{
		query=new Query();
		query.fields().include("name").include("img_url");
		return mongoTemplate.find(query, Account.class);
	}

	@Override
	public List<Order> getListOrderByRestaurantCode(String code) throws MongoException{
		query=new Query(Criteria.where("restaurant_code").is(code));
		return mongoTemplate.find(query, Order.class);
	}

	@Override
	public List<Order> getListOrderByUser(String userName) throws MongoException{
		query=new Query(Criteria.where("userOrderName").is(userName));
		return mongoTemplate.find(query, Order.class);
	}

	@Override
	public String createOrder(Order order) throws MongoException{
		// 0 : done, 1: pending, 2:abort
		String id_hashCode=order.getRestaurant_code()+order.getPhone()+new Date().toString();
		order.setId(Integer.toString(Math.abs(id_hashCode.hashCode())));
		order.setStatus(1);
		order.setCreateDate(new Date());
		if(!mongoTemplate.collectionExists(Order.class)){
			mongoTemplate.createCollection(Order.class);
		}
		mongoTemplate.insert(order);
		return order.getId();
	}

	@Override
	public int countItem() throws MongoException{
//		query=new Query(Criteria.where("_id").)
		return 0;
	}

	@Override
	public Double getMoneyForItem(String id, int quantity) {
		Item item=getItem(id);
		return item.getPrice()*quantity;
	}

	@Override
	public List<Item> getAllItem() throws MongoException{
		query=new Query();
		return mongoTemplate.find(query, Item.class);
	}
}






