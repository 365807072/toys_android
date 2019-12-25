package com.yyqq.commen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OperationThemeItem {
	public String img_id;
	public String user_id;
	public String user_name;
	public String review_count;
	public String avatar;
	public String avatar_origin;
	public String img_down;
	public String img;
	public String img_width;
	public String img_height;
	public String img_thumb;
	public String img_thumb_width;
	public String img_thumb_height;
	public String share_img_id;
	public String description;
	public String admire_count;
	public String total_count;
	public String create_time;
	public String share_content;
	public String is_admire;
	public String is_activity;
	JSONObject json;
	JSONArray jsonList;

	public void fromJson(JSONObject js) throws JSONException {
		this.json = js.getJSONObject("img");
		jsonList = js.getJSONArray("reviews");

		img_id = json.getInt("img_id") + "";
		user_id = json.getInt("user_id") + "";
		user_name = json.getString("user_name");
		review_count = json.getInt("review_count") + "";
		avatar = json.getString("avatar");
		avatar_origin = json.getString("avatar_origin");
		img_down = json.getString("img_down");
		img = json.getString("img");
		img_width = json.getInt("img_width") + "";
		img_height = json.getInt("img_height") + "";
		img_thumb = json.getString("img_thumb");
		img_thumb_width = json.getInt("img_thumb_width") + "";
		img_thumb_height = json.getInt("img_thumb_height") + "";
		share_img_id = json.getInt("share_img_id") + "";
		description = json.getString("description");
		admire_count = json.getInt("admire_count") + "";
		total_count = json.getInt("total_count") + "";
		create_time = json.getString("create_time");
		share_content = json.getString("share_content");
		is_admire = json.getInt("is_admire") + "";
		is_activity = json.getInt("is_activity") + "";

	}

	@Override
	public String toString() {
		return super.toString();
	}

}
