package com.yyqq.commen.model;

import org.json.JSONObject;

public class MobileItem {
	public String hidden_mobile;

	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("hidden_mobile")) {
				hidden_mobile = json.getString("hidden_mobile");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
