package com.yyqq.commen.model;

import org.json.JSONObject;

public class FriendItem {
	public String user_id;
	public String nick_name;
	public String avatar;
	public int relation;
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
				avatar = json.getString("avatar");
			}
			if (json.has("relation")) {
				relation = json.getInt("relation");
			}
			if (json.has("level_img")) {
				level_img = json.getString("level_img");
			}
		} catch (Exception e) {
		}
	}
}
