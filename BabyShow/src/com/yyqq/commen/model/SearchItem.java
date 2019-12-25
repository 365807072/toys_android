package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchItem {
	public String id = "";
	public String user_id = "";
	public String nick_name = "";
	public String avatar = "";
	public String level_img = "";
	public String cate_id = "";
	public String is_pic = "";
	public String group_id = "";
	public String group_name = "";
	public String group_img = "";
	public String is_group = "";
	public String baby_id = "";
	public String baby_name = "";
	public String idol_type = "";
	// 商家搜索列表
	public String business_id = "";
	public String business_title = "";

	// 成长记录关注列表
	public String babys_idol_id = "";
	public String description = "";
	public long post_create_time;
	public String is_each_others = ""; // （是否同意关注 0 未操作 ；1同意；2取消）
	JSONObject json;
	
	public String search_id = "";
	public String username = "";
	public String img_thumb = "";

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			
			if (json.has("search_id")) {
				id = json.getString("search_id") + "";
			}
			
			if (json.has("search_word")) {
				nick_name = json.getString("search_word") + "";
			}
			
			if (json.has("img_thumb")) {
				avatar = json.getString("img_thumb") + "";
			}
			
			if (json.has("group_id")) {
				group_id = json.getString("group_id") + "";
			}
			
			if (json.has("search_id")) {
				id = json.getString("search_id") + "";
			}
			
			if (json.has("search_word")) {
				nick_name = json.getString("search_word") + "";
			}
			
			if (json.has("img_thumb")) {
				avatar = json.getString("img_thumb") + "";
			}
			
			if (json.has("group_id")) {
				group_id = json.getInt("group_id") + "";
			}
			
			if (json.has("search_id")) {
				id = json.getString("search_id") + "";
			}
			
			if (json.has("search_word")) {
				nick_name = json.getString("search_word") + "";
			}
			
			if (json.has("img_thumb")) {
				avatar = json.getString("img_thumb") + "";
			}
			
			if (json.has("id")) {
				id = json.getInt("id") + "";
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			if (json.has("level_img")) {
				level_img = json.getString("level_img");
			}
			if (json.has("cate_id")) {
				cate_id = json.getInt("cate_id") + "";
			}
			if (json.has("is_pic")) {
				is_pic = json.getInt("is_pic") + "";
			}
			if (json.has("group_name")) {
				group_name = json.getString("group_name");
			}
			if (json.has("group_img")) {
				group_img = json.getString("group_img");
			}
			if (json.has("is_group")) {
				is_group = json.getString("is_group") + "";
			}
			if (json.has("user_id")) {
				user_id = json.getString("user_id");
			}
			if (json.has("baby_id")) {
				baby_id = json.getString("baby_id");
			}
			if (json.has("babys_idol_id")) {
				babys_idol_id = json.getString("babys_idol_id");
			}
			if (json.has("description")) {
				description = json.getString("description");
			}
			if (json.has("post_create_time")) {
				post_create_time = json.getLong("post_create_time");
			}
			if (json.has("is_each_others")) {
				is_each_others = json.getString("is_each_others");
			}
			if (json.has("baby_name")) {
				baby_name = json.getString("baby_name");
			}
			if (json.has("idol_type")) {
				idol_type = json.getString("idol_type");
			}
			if (json.has("business_id")) {
				business_id = json.getString("business_id");
			}
			if (json.has("business_title")) {
				business_title = json.getString("business_title");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "SearchItem [id=" + id + ", user_id=" + user_id + ", nick_name="
				+ nick_name + ", avatar=" + avatar + ", level_img=" + level_img
				+ ", cate_id=" + cate_id + ", is_pic=" + is_pic + ", group_id="
				+ group_id + ", group_name=" + group_name + ", group_img="
				+ group_img + ", is_group=" + is_group + ", baby_id=" + baby_id
				+ ", baby_name=" + baby_name + ", idol_type=" + idol_type
				+ ", business_id=" + business_id + ", business_title="
				+ business_title + ", babys_idol_id=" + babys_idol_id
				+ ", description=" + description + ", post_create_time="
				+ post_create_time + ", is_each_others=" + is_each_others
				+ ", json=" + json + ", search_id=" + search_id + ", username="
				+ username + ", img_thumb=" + img_thumb + "]";
	}
	
	
}