package com.yyqq.commen.model;

import java.util.ArrayList;

public class ToysOrderListBean {

	private String combined_order_id = "";
	private String post_create_time = "";
	private String is_del = "";
	private String is_reset = "";
	private String order_state = "";
	private String toys_title = "";
	private ArrayList<ToysOrderItemBean> itemList = new ArrayList<ToysOrderItemBean>();

	public ToysOrderListBean() {
	}

	public ToysOrderListBean(String combined_order_id, String post_create_time,
			String is_del, String is_reset, String order_state,
			String toys_title, ArrayList<ToysOrderItemBean> itemList) {
		super();
		this.combined_order_id = combined_order_id;
		this.post_create_time = post_create_time;
		this.is_del = is_del;
		this.is_reset = is_reset;
		this.order_state = order_state;
		this.toys_title = toys_title;
		this.itemList = itemList;
	}

	public String getCombined_order_id() {
		return combined_order_id;
	}

	public void setCombined_order_id(String combined_order_id) {
		this.combined_order_id = combined_order_id;
	}

	public String getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}

	public String getIs_del() {
		return is_del;
	}

	public void setIs_del(String is_del) {
		this.is_del = is_del;
	}

	public String getIs_reset() {
		return is_reset;
	}

	public void setIs_reset(String is_reset) {
		this.is_reset = is_reset;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getToys_title() {
		return toys_title;
	}

	public void setToys_title(String toys_title) {
		this.toys_title = toys_title;
	}

	public ArrayList<ToysOrderItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ToysOrderItemBean> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "ToysOrderListBean [combined_order_id=" + combined_order_id
				+ ", post_create_time=" + post_create_time + ", is_del="
				+ is_del + ", is_reset=" + is_reset + ", order_state="
				+ order_state + ", toys_title=" + toys_title + ", itemList="
				+ itemList + "]";
	}

}
