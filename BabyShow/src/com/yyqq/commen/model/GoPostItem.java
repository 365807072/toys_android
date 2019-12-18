package com.yyqq.commen.model;

import org.json.JSONObject;

public class GoPostItem {
	public String img_id = "";
	public String is_save = "";
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("img_id")) {
				img_id = json.getString("img_id");
			}
			if (json.has("is_save")) {
				is_save = json.getString("is_save");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
