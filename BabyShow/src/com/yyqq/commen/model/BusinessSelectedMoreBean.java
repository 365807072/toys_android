package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 精选商家列表bean
 * */
public class BusinessSelectedMoreBean {

	private String city_id = "";
	private String city_name = "";
	private String title_center = "";
	private String status = "";
	private String title = "";

	public BusinessSelectedMoreBean() {
	}

	public BusinessSelectedMoreBean(String city_id, String city_name,
			String title_center, String status, String title) {
		super();
		this.city_id = city_id;
		this.city_name = city_name;
		this.title_center = title_center;
		this.status = status;
		this.title = title;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getTitle_center() {
		return title_center;
	}

	public void setTitle_center(String title_center) {
		this.title_center = title_center;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "BusinessSelectedMoreBean [city_id=" + city_id + ", city_name="
				+ city_name + ", title_center=" + title_center + ", status="
				+ status + ", title=" + title + "]";
	}

	// 解析字符串
	public BusinessSelectedMoreBean fromJson(JSONObject jb) {
		BusinessSelectedMoreBean bean = new BusinessSelectedMoreBean();
		try {
			if (jb.has("city_id")) {
				bean.setCity_id(jb.getString("city_id"));
			}
			if (jb.has("city_name")) {
				bean.setCity_name(jb.getString("city_name"));
			}
			if (jb.has("title_center")) {
				bean.setTitle_center(jb.getString("title_center"));
			}
			if (jb.has("status")) {
				bean.setStatus(jb.getString("status"));
			}
			if (jb.has("title")) {
				bean.setTitle(jb.getString("title"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
