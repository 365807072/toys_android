package com.yyqq.commen.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class MainItemHintBean {

	private String img_id = "";
	private String is_group = "";
	private String img_title = "";
	private String user_id = "";
	private String video_img = "";
	private String video_url = "";
	private String width = "";
	private String height = "";
	
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getIs_group() {
		return is_group;
	}
	public void setIs_group(String is_group) {
		this.is_group = is_group;
	}
	public String getImg_title() {
		return img_title;
	}
	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getVideo_img() {
		return video_img;
	}
	public void setVideo_img(String video_img) {
		this.video_img = video_img;
	}
	
	@Override
	public String toString() {
		return "MainItemHintBean [img_id=" + img_id + ", is_group=" + is_group
				+ ", img_title=" + img_title + ", user_id=" + user_id
				+ ", video_img=" + video_img + ", video_url=" + video_url
				+ ", width=" + width + ", height=" + height + "]";
	}
	
	public static MainItemHintBean fromJson(JSONObject jsonObj, ArrayList<MainItemHintBean> hintList){
		MainItemHintBean bean = new MainItemHintBean();
		try {
			bean.setImg_id(jsonObj.getString("img_id"));
			bean.setImg_title(jsonObj.getString("img_title"));
			bean.setIs_group(jsonObj.getString("is_group"));
			bean.setUser_id(jsonObj.getString("user_id"));
			bean.setVideo_url(jsonObj.getString("video_url"));
			bean.setVideo_img(jsonObj.getString("img_thumb"));
			bean.setWidth(jsonObj.getInt("img_thumb_width")+"");
			bean.setHeight(jsonObj.getInt("img_thumb_height")+"");
			hintList.add(bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
