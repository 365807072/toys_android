package com.yyqq.commen.model;

import java.util.ArrayList;

/**
 * 购物车
 * */
public class ToysLeaseCartBean {

	private String toys_title = "";
	private String type = "";
	private boolean isDelete = false;
	private ArrayList<ToysLeaseCartItemBean> toys_info = new ArrayList<ToysLeaseCartItemBean>();

	public ToysLeaseCartBean(){}
	
	public ToysLeaseCartBean(String toys_title, String type, boolean isDelete,
			ArrayList<ToysLeaseCartItemBean> toys_info) {
		super();
		this.toys_title = toys_title;
		this.type = type;
		this.isDelete = isDelete;
		this.toys_info = toys_info;
	}

	public String getToys_title() {
		return toys_title;
	}

	public void setToys_title(String toys_title) {
		this.toys_title = toys_title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public ArrayList<ToysLeaseCartItemBean> getToys_info() {
		return toys_info;
	}

	public void setToys_info(ArrayList<ToysLeaseCartItemBean> toys_info) {
		this.toys_info = toys_info;
	}

	@Override
	public String toString() {
		return "ToysLeaseCartBean [toys_title=" + toys_title + ", type=" + type
				+ ", isDelete=" + isDelete + ", toys_info=" + toys_info + "]";
	}

}
