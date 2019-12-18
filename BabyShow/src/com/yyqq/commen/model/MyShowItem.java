package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyShowItem {
	public String img_id = "";
	public String user_id = "";
	public String user_name = "";
	public String avatar = "";
	public String description = "";
	public String admire_count = "";
	public String review_count = "";
	public long create_time;
	public String img_cate = "";
	public String post_url = "";
	public String post_url_title = "";
	public String post_url_image = "";
	public String is_admire = "";
	public String level_img = "";
	public String cate_id = "";
	public String cate_name = "";

	public String img_down = "";
	public String img = "";
	public String img_width = "";
	public String img_height = "";
	public String img_thumb = "";
	public String img_thumb_width = "";
	public String img_thumb_height = "";

	public String imageThumb = "";
	public ArrayList<String> imaStrings = new ArrayList<String>();
	public ArrayList<String> imaBig = new ArrayList<String>();
	public ArrayList<String> imaWid = new ArrayList<String>();
	public ArrayList<String> imaHed = new ArrayList<String>();

	public boolean isZan = false;
	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;
			if (info.has("img_id")) {
				img_id = info.getInt("img_id") + "";
			}
			if (info.has("user_id")) {
				user_id = info.getInt("user_id") + "";
			}
			if (info.has("user_name")) {
				user_name = info.getString("user_name");
			}
			if (info.has("avatar")) {
				avatar = info.getString("avatar");
			}
			if (info.has("description")) {
				description = info.getString("description");
			}
			if (info.has("admire_count")) {
				admire_count = info.getInt("admire_count") + "";
			}
			if (info.has("review_count")) {
				review_count = info.getString("review_count") + "";
			}
			if (info.has("create_time")) {
				create_time = info.getLong("create_time");
			}
			if (info.has("img_cate")) {
				img_cate = info.getString("img_cate");
			}
			if (info.has("post_url")) {
				post_url = info.getString("post_url");
			}
			if (info.has("post_url_title")) {
				post_url_title = info.getString("post_url_title");
			}
			if (info.has("post_url_image")) {
				post_url_image = info.getString("post_url_image");
			}
			if (info.has("is_admire")) {
				is_admire = info.getString("is_admire") + "";
			}
			if (info.has("level_img")) {
				level_img = info.getString("level_img");
			}
			if (info.has("cate_id")) {
				cate_id = info.getInt("cate_id") + "";
			}
			if (info.has("cate_name")) {
				cate_name = info.getString("cate_name");
			}

			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				img_down = imgArray.getJSONObject(i).getString("img_down");
				img = imgArray.getJSONObject(i).getString("img");
				img_width = imgArray.getJSONObject(i).getString("img_width");
				img_height = imgArray.getJSONObject(i).getString("img_height");
				img_thumb = imgArray.getJSONObject(i).getString("img_thumb");
				img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");

				imaStrings.add(img_thumb);
				imaBig.add(img);
				imaWid.add(img_width);
				imaHed.add(img_height);
			}

			if (info.getInt("is_admire") == 0) {
				isZan = false;
			} else {
				isZan = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
