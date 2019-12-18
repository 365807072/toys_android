package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiaryItem {
	public String album_name = "";
	public String tag_type = "";

	public String album_id = "";
	public String album_description = "";
	public String tag_name = "";
	public String img_count = "";
	public String diary_cate = "";

	public String img_thumb = "";
	public String img_thumb_width = "";
	public String img_thumb_height = "";

	public ArrayList<String> imaStrings = new ArrayList<String>();
	public ArrayList<String> imaBig = new ArrayList<String>();
	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			this.json = json;
			if (json.has("album_name")) {
				album_name = json.getString("album_name");
			}
			if (json.has("tag_type")) {
				tag_type = json.getString("tag_type");
			}

			if (json.has("album_id")) {
				album_id = json.getString("album_id") + "";
			}
			if (json.has("album_description")) {
				album_description = json.getString("album_description");
			}
			if (json.has("tag_name")) {
				tag_name = json.getString("tag_name");
			}
			if (json.has("img_count")) {
				img_count = json.getString("img_count") + "";
			}
			if (json.has("diary_cate")) {
				diary_cate = json.getString("diary_cate");
			}
			JSONArray imgArray = json.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				img_thumb = imgArray.getJSONObject(i).getString("img_thumb");
				img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");

				imaStrings.add(img_thumb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
