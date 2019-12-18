package com.yyqq.commen.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 原点item的bean
 * */
public class MainListItemBean implements Serializable{

	private String id = "";
	private String type = "";
	private String img_title = "";
	private String review_count = "";
	private String post_count = "";
	private String style = "";
	private String img_style = "";
	private String img_count = "";
	private String jump = "";
	private String video_url = "";
	private String user_name = "";
	private String user_id = "";
	private String image_url = "";
	private String tag_id = "";
	private String post_create_time = "";
	private boolean isPlay = false;
	private String img_thumb = "";
	private String img_thumb_width = "";
	private String img_thumb_height = "";
	private String img_ids = "";
	private String img_description = "";
	private String hot_time_title = "";
	private String show_time = "";
	private String post_url = "";
	private String is_link = "";
	private String business_id = "";
	private String group_class_title = "";
	private String cate_id;
	private boolean isEssence = false;
	private String color = "";
	private boolean groupType = false;
	private ArrayList<MainItemImageBean> img = new ArrayList<MainItemImageBean>();
	
	public boolean isGroupType() {
		return groupType;
	}

	public void setGroupType(boolean groupType) {
		this.groupType = groupType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isEssence() {
		return isEssence;
	}

	public void setEssence(boolean isEssence) {
		this.isEssence = isEssence;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getShow_time() {
		return show_time;
	}

	public void setShow_time(String show_time) {
		this.show_time = show_time;
	}

	public String getShow_Time(){
		return show_time;
	}
	
	public void setShow_Time(String show_time){
		this.show_time = show_time;
	}
	
	public String getHot_time_title(){
		return hot_time_title;
	}
	
	public void setHot_time_title(String hot_time_title){
		this.hot_time_title = hot_time_title;
	}
	
	public String getImg_description() {
		return img_description;
	}

	public void setImg_description(String img_description) {
		this.img_description = img_description;
	}
	
	public String getImg_ids() {
		return img_ids;
	}

	public void setImg_ids(String img_ids) {
		this.img_ids = img_ids;
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

	public String getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public ArrayList<MainItemImageBean> getImg() {
		return img;
	}
	
	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getIs_link() {
		return is_link;
	}

	public void setIs_link(String is_link) {
		this.is_link = is_link;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getGroup_class_title() {
		return group_class_title;
	}

	public void setGroup_class_title(String group_class_title) {
		this.group_class_title = group_class_title;
	}
	
	public MainListItemBean() {
		img.clear();
	}

	public String getCate_id() {
		return cate_id;
	}

	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}

	public String getId() {
		return id;
	}

	public String getImg_style() {
		return img_style;
	}

	public void setImg_style(String img_style) {
		this.img_style = img_style;
	}

	public String getImg_count() {
		return img_count;
	}

	public void setImg_count(String img_count) {
		this.img_count = img_count;
	}

	public String getJump() {
		return jump;
	}

	public void setJump(String jump) {
		this.jump = jump;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImg_title() {
		return img_title;
	}

	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}

	public String getPost_count() {
		return post_count;
	}

	public void setPost_count(String post_count) {
		this.post_count = post_count;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public ArrayList<MainItemImageBean> getImgList() {
		return img;
	}

	public void setImg(ArrayList<MainItemImageBean> img) {
		this.img = img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

	@Override
	public String toString() {
		return "MainListItemBean [id=" + id + ", type=" + type + ", img_title="
				+ img_title + ", review_count=" + review_count
				+ ", post_count=" + post_count + ", style=" + style
				+ ", img_style=" + img_style + ", img_count=" + img_count
				+ ", jump=" + jump + ", video_url=" + video_url
				+ ", user_name=" + user_name + ", user_id=" + user_id
				+ ", image_url=" + image_url + ", tag_id=" + tag_id
				+ ", post_create_time=" + post_create_time + ", isPlay="
				+ isPlay + ", img_thumb=" + img_thumb + ", img_thumb_width="
				+ img_thumb_width + ", img_thumb_height=" + img_thumb_height
				+ ", img_ids=" + img_ids + ", img_description="
				+ img_description + ", hot_time_title=" + hot_time_title
				+ ", show_time=" + show_time + ", post_url=" + post_url
				+ ", is_link=" + is_link + ", business_id=" + business_id
				+ ", group_class_title=" + group_class_title + ", cate_id="
				+ cate_id + ", img=" + img + "]";
	}

	public MainListItemBean fromJson(JSONObject json) {
		MainListItemBean bean;
		bean = new MainListItemBean();
		try {
			bean.setImg_title(json.getString("img_title"));
			bean.setStyle(json.getInt("style")+"");
			bean.setPost_count(json.getString("post_count"));
			bean.setImg_style(json.getInt("img_style")+"");
			bean.setType(json.getString("type"));
			bean.setUser_name(json.getString("user_name"));
			
			if(json.has("color")){
				bean.setColor(json.getString("color"));
			}
			
			if(json.has("essence_state")){
				if(json.getString("essence_state").equals("1")){
					bean.setEssence(true);
				}else{
					bean.setEssence(false);
				}
			}
			
			if(json.has("user_id")){
				bean.setUser_id(json.getString("user_id")+"");
			}
			
			if(json.has("post_url")){
				bean.setPost_url(json.getString("post_url")+"");
			}

			if(json.has("is_link")){
				bean.setIs_link(json.getString("is_link")+"");
			}

			if(json.has("group_class_title")){
				bean.setGroup_class_title(json.getString("group_class_title")+"");
			}
			
			if(json.has("show_post_create_time")){
				bean.setShow_Time(json.getString("show_post_create_time")+"");
			}
			
			if(json.has("hot_time_title")){
				bean.setHot_time_title(json.getString("hot_time_title")+"");
			}
			
			if(json.has("img_description")){
				bean.setImg_description(json.getString("img_description")+"");
			}
			
			if(json.has("tag_id")){
				bean.setTag_id(json.getInt("tag_id")+"");
			}
			
			if(json.has("img_ids")){
				bean.setImg_ids(json.getString("img_ids")+"");
			}
			
			if(json.has("video_url")){
				bean.setVideo_url(json.getString("video_url"));
			}
			
			if(json.has("post_create_time")){
				bean.setPost_create_time(json.getString("post_create_time"));
			}
			
			if(json.has("img_thumb_width")){
				bean.setImg_thumb_width(json.getString("img_thumb_width"));
			}
			
			if(json.has("img_thumb_height")){
				bean.setImg_thumb_height(json.getString("img_thumb_height"));
			}
			
			if(json.has("img_thumb")){
				bean.setImg_thumb(json.getString("img_thumb"));
			}

			// 获取图片集合
			JSONArray jsonArray = json.getJSONArray("img");
			MainItemImageBean imageBean;
			ArrayList<MainItemImageBean> imageBeanList = new ArrayList<MainItemImageBean>();
			if (jsonArray.length() != 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					imageBean = new MainItemImageBean();
					if(jsonArray.getJSONObject(i).has("user_id")){
						imageBean.setUser_id(jsonArray.getJSONObject(i).getString("user_id"));
					}
					if(jsonArray.getJSONObject(i).has("img_id")){
						imageBean.setImg_id(jsonArray.getJSONObject(i).getString("img_id"));
					}
					
					if(null == bean.getImg_thumb() || bean.getImg_thumb().isEmpty()){
						if(jsonArray.getJSONObject(i).has("img_thumb")){
							bean.setImg_thumb(jsonArray.getJSONObject(i).getString("img_thumb"));
							imageBean.setImg_thumb(jsonArray.getJSONObject(i).getString("img_thumb"));
						}
					}else{
						imageBean.setImg_thumb(jsonArray.getJSONObject(i).getString("img_thumb"));
					}
//					imageBean.setType(jsonArray.getJSONObject(i).getString(
//							"type"));
//					imageBean.setCurrent_price(jsonArray.getJSONObject(i)
//							.getString("current_price"));
//					imageBean.setOriginal_price(jsonArray.getJSONObject(i)
//							.getString("original_price"));
					
					if(null == bean.getImg_thumb_height() || bean.getImg_thumb_height().isEmpty()){
						if(jsonArray.getJSONObject(i).has("img_thumb_height")){
							imageBean.setImg_thumb_height(jsonArray.getJSONObject(i).getString("img_thumb_height"));
						}
					}else{
						imageBean.setImg_thumb_width(bean.getImg_thumb_width());
					}
					
					if(null == bean.getImg_thumb_width() || bean.getImg_thumb_width().isEmpty()){
						if(jsonArray.getJSONObject(i).has("img_thumb_width")){
							imageBean.setImg_thumb_width(jsonArray.getJSONObject(i).getString("img_thumb_width"));
						}
					}else{
						imageBean.setImg_thumb_width(bean.getImg_thumb_width());
					}
					
					if(null == bean.getPost_create_time() || bean.getPost_create_time().isEmpty()){
						if(jsonArray.getJSONObject(i).has("post_create_time")){
							imageBean.setPost_create_time(jsonArray.getJSONObject(i).getString("post_create_time"));
						}
					}else{
						imageBean.setPost_create_time(bean.getPost_create_time());
					}
					
//					imageBean.setTitle(jsonArray.getJSONObject(i).getString(
//							"title")); 
//					imageBean.setPost_url(jsonArray.getJSONObject(i).getString(
//							"post_url"));
					imageBeanList.add(imageBean);
				}
				bean.setImg(imageBeanList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bean;
	}

}
