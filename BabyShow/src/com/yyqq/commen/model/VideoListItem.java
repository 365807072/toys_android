package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoListItem {
	public String img_id = "";
	public String create_time = "";
	public String post_create_time = "";
	public String video_url = "";
	public String img = "";

	public void fromJson(JSONObject json) throws JSONException {
		if (json.has("img_id")) {
			img_id = json.getString("img_id");
		}
		if (json.has("create_time")) {
			create_time = json.getString("create_time");
		}
		if (json.has("post_create_time")) {
			post_create_time = json.getString("post_create_time");
		}
		if (json.has("video_url")) {
			video_url = json.getString("video_url");
		}
		if (json.has("img")) {
			img = json.getString("img");
		}
	}
}
