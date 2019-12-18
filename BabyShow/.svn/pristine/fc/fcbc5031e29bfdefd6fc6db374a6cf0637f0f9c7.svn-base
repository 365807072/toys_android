package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GrowEditItem {
	public String tag_name = "";
	public String album_desc = "";

	public String img_id = "";
	public String img_title = "";
	public String img_description = "";
	public JSONObject json;
	public ArrayList<Image> ImageList = new ArrayList<Image>();

	public void fromJson(JSONObject json, String tag) {
		try {
			JSONArray json1_array = json.optJSONArray("img");
			for (int j = 0; j < json1_array.length(); j++) {
				Image mImage = new Image();
				mImage.fromJson(json1_array.optJSONObject(j));
				ImageList.add(mImage);
			}
			if (json.has("img_id")) {
				img_id = json.getInt("img_id") + "";
			}
			if (json.has("img_title")) {
				img_title = json.getString("img_title");
			}
			if (json.has("img_description")) {
				img_description = json.getString("img_description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Image {
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		public void fromJson(JSONObject json) {
			try {
				img_thumb = json.getString("img_thumb");
				img_thumb_width = json.getString("img_thumb_width");
				img_thumb_height = json.getString("img_thumb_height");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
