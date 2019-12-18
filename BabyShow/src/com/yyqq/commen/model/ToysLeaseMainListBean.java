package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ToysLeaseMainListBean {

	private String imgUrl = "";
	private String leaseName = "";
	private String userIconUrl = "";
	private String userName = "";
	private String supporType = "";
	private String leaseType = "";
	private String leaseMonery = "";
	private String leaseState = "";
	private String leaseStateName = "";
	private String postcreatetime = "";
	private String business_id = "";
	private String order_id = "";
	private String order_state = "";
	private String is_jump = "";
	private String post_url = "";
	private String is_cart = "";
	private String category_icon_url = "";
	private String age = "";
	private String company = "";

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLeaseName() {
		return leaseName;
	}

	public void setLeaseName(String leaseName) {
		this.leaseName = leaseName;
	}

	public String getUserIconUrl() {
		return userIconUrl;
	}

	public void setUserIconUrl(String userIconUrl) {
		this.userIconUrl = userIconUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSupporType() {
		return supporType;
	}

	public void setSupporType(String supporType) {
		this.supporType = supporType;
	}

	public String getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(String leaseType) {
		this.leaseType = leaseType;
	}

	public String getLeaseMonery() {
		return leaseMonery;
	}

	public void setLeaseMonery(String leaseMonery) {
		this.leaseMonery = leaseMonery;
	}

	public String getLeaseState() {
		return leaseState;
	}

	public void setLeaseState(String leaseState) {
		this.leaseState = leaseState;
	}

	public String getLeaseStateName() {
		return leaseStateName;
	}

	public void setLeaseStateName(String leaseStateName) {
		this.leaseStateName = leaseStateName;
	}

	public String getPostcreatetime() {
		return postcreatetime;
	}

	public void setPostcreatetime(String postcreatetime) {
		this.postcreatetime = postcreatetime;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getIs_jump() {
		return is_jump;
	}

	public void setIs_jump(String is_jump) {
		this.is_jump = is_jump;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getIs_cart() {
		return is_cart;
	}

	public void setIs_cart(String is_cart) {
		this.is_cart = is_cart;
	}

	public String getCategory_icon_url() {
		return category_icon_url;
	}

	public void setCategory_icon_url(String category_icon_url) {
		this.category_icon_url = category_icon_url;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ToysLeaseMainListBean [imgUrl=" + imgUrl + ", leaseName="
				+ leaseName + ", userIconUrl=" + userIconUrl + ", userName="
				+ userName + ", supporType=" + supporType + ", leaseType="
				+ leaseType + ", leaseMonery=" + leaseMonery + ", leaseState="
				+ leaseState + ", leaseStateName=" + leaseStateName
				+ ", postcreatetime=" + postcreatetime + ", business_id="
				+ business_id + ", order_id=" + order_id + ", order_state="
				+ order_state + ", is_jump=" + is_jump + ", post_url="
				+ post_url + ", is_cart=" + is_cart + ", category_icon_url="
				+ category_icon_url + ", age=" + age + ", company=" + company
				+ "]";
	}

	public ToysLeaseMainListBean fromJson(JSONObject json) {
		ToysLeaseMainListBean bean;
		bean = new ToysLeaseMainListBean();
		try {
			if (json.has("business_id")) {
				bean.setBusiness_id(json.getString("business_id"));
			}

			if (json.has("user_name")) {
				bean.setUserName(json.getString("user_name"));
			}

			if (json.has("business_title")) {
				bean.setLeaseName(json.getString("business_title"));
			}

			if (json.has("way")) {
				bean.setLeaseState(json.getString("way"));
			}

			if (json.has("img_thumb")) {
				bean.setImgUrl(json.getString("img_thumb"));
			}

			if (json.has("is_support")) {
				bean.setSupporType(json.getString("is_support"));
			}

			if (json.has("sell_price")) {
				bean.setLeaseMonery(json.getString("sell_price"));
			}

			if (json.has("post_create_time")) {
				bean.setPostcreatetime(json.getString("post_create_time"));
			}

			if (json.has("status")) {
				bean.setLeaseState(json.getString("status"));
			}

			if (json.has("status_name")) {
				bean.setOrder_state(json.getString("status_name"));
			}

			if (json.has("order_id")) {
				bean.setOrder_id(json.getString("order_id"));
			}

			if (json.has("avatar")) {
				bean.setUserIconUrl(json.getString("avatar"));
			}

			if (json.has("support_name")) {
				bean.setLeaseStateName(json.getString("support_name"));
			}

			if (json.has("way")) {
				bean.setLeaseType(json.getString("way"));
			}

			if (json.has("is_jump")) {
				bean.setIs_jump(json.getString("is_jump"));
			}

			if (json.has("post_url")) {
				bean.setPost_url(json.getString("post_url"));
			}

			if (json.has("is_order")) {
				bean.setIs_cart(json.getString("is_order"));
			}

			if (json.has("size_img_thumb")) {
				bean.setCategory_icon_url(json.getString("size_img_thumb"));
			}
			
			if (json.has("unit_name")) {
				bean.setCompany(json.getString("unit_name"));
			}
			
			if (json.has("age")) {
				bean.setAge(json.getString("age"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bean;
	}

}
