package com.yyqq.commen.model;

import org.json.JSONObject;

public class BabyDataItem {
	public String height;
	public String h50;
	public String h_mid;
	public String h950;
	public String h_posi;
	public String h_cal;
	public String weight;
	public String w50;
	public String w_mid;
	public String w950;
	public String w_posi;
	public String w_cal;
	public String remind_info;
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("height")) {
				height = json.getString("height");
			}
			if (json.has("h50")) {
				h50 = json.getString("h50");
			}
			if (json.has("h_mid")) {
				h_mid = json.getString("h_mid");
			}
			if (json.has("h950")) {
				h950 = json.getString("h950");
			}
			if (json.has("h_posi")) {
				h_posi = json.getString("h_posi");
			}
			if (json.has("h_cal")) {
				h_cal = json.getString("h_cal");
			}
			if (json.has("weight")) {
				weight = json.getString("weight");
			}
			if (json.has("w50")) {
				w50 = json.getString("w50");
			}
			if (json.has("w_mid")) {
				w_mid = json.getString("w_mid");
			}
			if (json.has("w950")) {
				w950 = json.getString("w950");
			}
			if (json.has("w_posi")) {
				w_posi = json.getString("w_posi");
			}
			if (json.has("w_cal")) {
				w_cal = json.getString("w_cal");
			}
			if (json.has("remind_info")) {
				remind_info = json.getString("remind_info");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
