package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HotImgList {
	public String user_name = "";
	public String album_description = "";
	public String avatar = "";
	public List<Img> imgList = new ArrayList<Img>();

	public class Img {
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";
	}

	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			if (json.has("user_name")) {
				user_name = json.getString("user_name");
			}
			if (json.has("album_description")) {
				album_description = json.getString("album_description");
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			JSONArray imgArray = json.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				Img img = new Img();
				img.img_thumb = imgArray.getJSONObject(i).getString(
						"img_thumb");
				img.img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				img.img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");
				imgList.add(img);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}