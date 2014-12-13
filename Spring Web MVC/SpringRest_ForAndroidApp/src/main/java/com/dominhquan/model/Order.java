package com.dominhquan.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Order {
	@Id
	private String id;
	private String userOrderName;
	private String phone;
	private String address;
	private Double totalPrice;
	private String list_food;
	private Date createDate;
	private int status;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserOrderName() {
		return userOrderName;
	}
	public void setUserOrderName(String userOrderName) {
		this.userOrderName = userOrderName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getList_food() {
		return list_food;
	}
	public void setList_food(String list_food) {
		this.list_food = list_food;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
