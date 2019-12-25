package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 精选商家列表bean
 * */
public class BusinessSelectedListBean {

	private String post_create_time = "";
	private String business_id = "";
	private String business_pic_new = "";
	private String is_last = "";
	private String post_url = "";
	
	public BusinessSelectedListBean() {
	}

	

	public BusinessSelectedListBean(String post_create_time,
			String business_id, String business_pic_new, String is_last,
			String post_url) {
		super();
		this.post_create_time = post_create_time;
		this.business_id = business_id;
		this.business_pic_new = business_pic_new;
		this.is_last = is_last;
		this.post_url = post_url;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getBusiness_pic_new() {
		return business_pic_new;
	}

	public void setBusiness_pic_new(String business_pic_new) {
		this.business_pic_new = business_pic_new;
	}

	public String getIs_last() {
		return is_last;
	}

	public void setIs_last(String is_last) {
		this.is_last = is_last;
	}

	@Override
	public String toString() {
		return "BusinessSelectedListBean [post_create_time=" + post_create_time
				+ ", business_id=" + business_id + ", business_pic_new="
				+ business_pic_new + ", is_last=" + is_last + ", post_url="
				+ post_url + "]";
	}

	// 解析字符串
	public BusinessSelectedListBean fromJson(JSONObject jb) {
		BusinessSelectedListBean bean = new BusinessSelectedListBean();
		try {
			if (jb.has("business_id")) {
				bean.setBusiness_id(jb.getString("business_id"));
			}
			if (jb.has("business_pic_new")) {
				bean.setBusiness_pic_new(jb.getString("business_pic_new"));
			}
			if (jb.has("post_create_time")) {
				bean.setPost_create_time(jb.getString("post_create_time"));
			}
			if (jb.has("is_last")) {
				bean.setIs_last(jb.getString("is_last"));
			}	
			if (jb.has("post_url")) {
				bean.setPost_url(jb.getString("post_url"));
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
