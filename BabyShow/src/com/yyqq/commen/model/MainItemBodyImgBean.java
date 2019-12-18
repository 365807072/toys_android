package com.yyqq.commen.model;

/**
 * 话题详情item的图片
 * 
 */
public class MainItemBodyImgBean {
	private String img_down = "";
	private String img = "";
	private String img_width = "";
	private String img_height = "";
	private String img_thumb = "";
	private String img_thumb_width = "";
	private String img_thumb_height = "";
	private String description = "";

	public MainItemBodyImgBean() {
		super();
	}

	public String getImg_down() {
		return img_down;
	}

	public void setImg_down(String img_down) {
		this.img_down = img_down;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg_width() {
		return img_width;
	}

	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}

	public String getImg_height() {
		return img_height;
	}

	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "MainItemBodyImgBean [img_down=" + img_down + ", img=" + img
				+ ", img_width=" + img_width + ", img_height=" + img_height
				+ ", img_thumb=" + img_thumb + ", img_thumb_width="
				+ img_thumb_width + ", img_thumb_height=" + img_thumb_height
				+ ", description=" + description + "]";
	}

}