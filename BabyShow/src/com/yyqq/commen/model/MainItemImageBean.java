package com.yyqq.commen.model;

import java.io.Serializable;

public class MainItemImageBean implements Serializable{
	private String user_id;
	private String img_id;
	private String img_thumb;
	private String img_thumb_width;
	private String img_thumb_height;
	private String type;
	private String current_price;
	private String original_price;
	private String post_create_time;
	private String title;
	private String post_url;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getImg_id() {
		return img_id;
	}

	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public MainItemImageBean() {
		super();
	}
	
	public String getImg_thumb_width() {
		return img_thumb_width;
	}

	public void setImg_thumb_width(String img_thumb_width) {
		this.img_thumb_width = img_thumb_width;
	}

	public String getImg_thumb_height() {
		return img_thumb_height;
	}

	public void setImg_thumb_height(String img_thumb_height) {
		this.img_thumb_height = img_thumb_height;
	}

	@Override
	public String toString() {
		return "MainItemImageBean [user_id=" + user_id + ", img_id=" + img_id
				+ ", img_thumb=" + img_thumb + ", img_thumb_width="
				+ img_thumb_width + ", img_thumb_height=" + img_thumb_height
				+ ", type=" + type + ", current_price=" + current_price
				+ ", original_price=" + original_price + ", post_create_time="
				+ post_create_time + ", title=" + title + ", post_url="
				+ post_url + "]";
	}

}