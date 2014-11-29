package com.dominhquan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dominhquan.model.Item;
import com.mongodb.MongoException;

public class ItemServiceImpl implements ItemService{
	
	private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	
	@Autowired
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
		Item item=mongoTemplate.findAndRemove(query, Item.class);
		
	}

	@Override
	public void updateItem(Item item) throws MongoException{
		mongoTemplate.save(item);
		logger.info("Save item successfully, Details : "+ item.toString());
	}
	@Override
	public List<Item> getListItem(String restaurant) throws MongoException{
		query=new Query(Criteria.where("restaurant_name").regex(restaurant));
		return mongoTemplate.find(query, Item.class);
	}
	
	public List<Item> getListHotItem(String restaurant) throws MongoException{
		query=new Query(Criteria.where("restaurant_name").is(restaurant).and("status").is(1));
		query.with(new Sort(Sort.Direction.DESC,"updateDate"));
		return mongoTemplate.find(query, Item.class);
	}
	
	public int countItem() throws MongoException{
//		query=new Query(Criteria.where("_id").)
	}
}






