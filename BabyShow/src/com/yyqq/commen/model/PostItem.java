package com.yyqq.commen.model;

import org.json.JSONObject;

public class PostItem {
	public String img_id = "";
	public String is_post = "";
	public String user_id = "";

	public void fromJson(JSONObject json) {
			if (json.has("img_id")) {
				img_id = json.optString("img_id","");
			}
			if (json.has("user_id")) {
				user_id = json.optString("user_id","") + "";
			}
	}

}
