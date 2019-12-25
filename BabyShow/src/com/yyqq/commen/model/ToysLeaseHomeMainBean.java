package com.yyqq.commen.model;

import java.util.ArrayList;

public class ToysLeaseHomeMainBean {

	private String cate_title = "";
	private String category_id = "";
	private String more_title = "";
	private String type = "";
	private String style = "";
	private String post_create_time = "";
	private ArrayList<ToysLeaseHomeMainItemBean> itemBean = new ArrayList<ToysLeaseHomeMainItemBean>();

	public String getCate_title() {
		return cate_title;
	}

	public void setCate_title(String cate_title) {
		this.cate_title = cate_title;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getMore_title() {
		return more_title;
	}

	public void setMore_title(String more_title) {
		this.more_title = more_title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}

	public ArrayList<ToysLeaseHomeMainItemBean> getItemBean() {
		return itemBean;
	}

	public void setItemBean(ArrayList<ToysLeaseHomeMainItemBean> itemBean) {
		this.itemBean = itemBean;
	}

	public ToysLeaseHomeMainBean() {
	}

	public ToysLeaseHomeMainBean(String cate_title, String category_id,
			String more_title, String type, String style,
			String post_create_time,
			ArrayList<ToysLeaseHomeMainItemBean> itemBean) {
		super();
		this.cate_title = cate_title;
		this.category_id = category_id;
		this.more_title = more_title;
		this.type = type;
		this.style = style;
		this.post_create_time = post_create_time;
		this.itemBean = itemBean;
	}

	@Override
	public String toString() {
		return "ToysLeaseHomeMainBean [cate_title=" + cate_title
				+ ", category_id=" + category_id + ", more_title=" + more_title
				+ ", type=" + type + ", style=" + style + ", post_create_time="
				+ post_create_time + ", itemBean=" + itemBean + "]";
	}

}
