package com.yyqq.commen.model;

import org.json.JSONObject;

public class BabyItem {
	public String id = "";
	public String baby_name = "";
	public String baby_sex = "";
	public String born_date = "";
	public String age = "";
	public String avatar = "";
	public String idol_type = "";
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("id")) {
				id = json.getString("id");
			}
			if (json.has("baby_name")) {
				baby_name = json.getString("baby_name");
			}
			if (json.has("baby_sex")) {
				baby_sex = json.getString("baby_sex");
			}
			if (json.has("born_date")) {
				born_date = json.getString("born_date");
			}
			if (json.has("age")) {
				age = json.getString("age");
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			if (json.has("idol_type")) {
				idol_type = json.getString("idol_type");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
