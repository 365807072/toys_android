package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Baby {
	public String name = "";
	public String birthDay = "";
	public String sex;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("baby_birth", birthDay);
			json.put("baby_name", name);
			json.put("sex", sex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
