package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostBarItem {
	public String img_id = "";
	public String user_id = "";
	public String user_name = "";

	public String img_title = "";
	public String description = "";
	public String is_recommend = "";
	public String admire_count = "";
	public String review_count = "";
	public long create_time;
	public long post_create_time;
	public String is_admire = "";
	public String is_save = "";
	public String level_img = "";
	public String video_url = "";
	public ArrayList<Review> reviews = new ArrayList<PostBarItem.Review>();
	public ArrayList<Image> ImageList = new ArrayList<Image>();

	public class Image {
		public String img_down = "";
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		public void fromJson(JSONObject json) {
			try {
				img_down = json.getString("img_down");
				img = json.getString("img");
				img_width = json.getString("img_width");
				img_height = json.getString("img_height");
				img_thumb = json.getString("img_thumb");
				img_thumb_width = json.getString("img_thumb_width");
				img_thumb_height = json.getString("img_thumb_height");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class Review {
		public String id = "";
		public String name = "";
		public String review = "";

		public void fromJson(JSONObject json) {
			try {
				id = json.getString("id");
				name = json.getString("user_name");
				review = json.getString("demand");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isZan = false;
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;
			if (info.has("video_url")) {
				video_url = info.getString("video_url");
			}
			if (info.has("img_id")) {
				img_id = info.getInt("img_id") + "";
			}
			if (info.has("user_id")) {
				user_id = info.getInt("user_id") + "";
			}
			if (info.has("user_name")) {
				user_name = info.getString("user_name");
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
			if (info.has("is_admire")) {
				is_admire = info.getString("is_admire") + "";
			}
			if (info.has("img_title")) {
				img_title = info.getString("img_title");
			}
			if (info.has("is_recommend")) {
				is_recommend = info.getString("is_recommend") + "";
			}
			if (info.has("post_create_time")) {
				post_create_time = info.getLong("post_create_time");
			}
			if (info.has("is_save")) {
				is_save = info.getString("is_save") + "";
			}
			if (info.has("level_img")) {
				level_img = info.getString("level_img");
			}

			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				Image mImage = new Image();
				mImage.fromJson(imgArray.optJSONObject(i));
				ImageList.add(mImage);
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
