package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ZanItem {
	public String user_name = "";
	public String id = "";
	public String head = "";
	public long time;
	public String user_id = "";

	public void fromJson(JSONObject json) {
		try {
			user_name = json.getString("user_name");
			id = json.getInt("id") + "";
			head = json.getString("avatar");
			user_id = json.getInt("user_id") + "";
			time = json.getLong("admire_time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
