package com.yyqq.commen.model;

public class ToysLeaseHomeHeadBean {

	private String imgUrl = "";
	private String id = "";
	private String is_jump = "";
	private String post_url = "";

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ToysLeaseHomeHeadBean [imgUrl=" + imgUrl + ", id=" + id
				+ ", is_jump=" + is_jump + ", post_url=" + post_url + "]";
	}

	public ToysLeaseHomeHeadBean() {
	}

	public ToysLeaseHomeHeadBean(String imgUrl, String id, String is_jump,
			String post_url) {
		super();
		this.imgUrl = imgUrl;
		this.id = id;
		this.is_jump = is_jump;
		this.post_url = post_url;
	}

}
