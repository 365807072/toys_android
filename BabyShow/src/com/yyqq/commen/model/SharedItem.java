package com.yyqq.commen.model;

import org.json.JSONObject;

public class SharedItem {
	public String user_id;
	public String nick_name;
	public String head;
	public int isShare;
	public String level_img = "";

	public void fromJson(JSONObject json) {
		try {
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("avatar")) {
				head = json.getString("avatar");
			}
			if (json.has("is_share")) {
				isShare = json.getInt("is_share");
			}
			if (json.has("level_img")) {
				level_img = json.getString("level_img");
			}
		} catch (Exception e) {
		}
	}
}
