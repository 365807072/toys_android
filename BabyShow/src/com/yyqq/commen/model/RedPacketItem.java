package com.yyqq.commen.model;

import org.json.JSONObject;

public class RedPacketItem {
	
	public String packet_id;
	public String packet_price; // 红包价格
	public String packet_type; // 红包类型
	public String packet_msg; // 红包描述
	public String expiration; // 过期时间
	public String ExpiryDate; // 有效期
	public String mainTitle;
	public long post_create_time;
	
	public String check_packet;
	public String order_id;
	public String packet_description;
	

	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("packet_id")) {
				packet_id = json.getString("packet_id");
			}
			if (json.has("packet_price")) {
				packet_price = json.getString("packet_price");
			}
			if (json.has("packet_type")) {
				packet_type = json.getString("packet_type");
			}
			if (json.has("packet_msg")) {
				packet_msg = json.getString("packet_msg");
			}
			if (json.has("expiration")) {
				expiration = json.getString("expiration");
			}
			if (json.has("ExpiryDate")) {
				ExpiryDate = json.getString("ExpiryDate");
			}
			if (json.has("post_create_time")) {
				post_create_time = json.getLong("post_create_time");
			}
			if (json.has("check_packet")) {
				check_packet = json.getString("check_packet") + "";
			}
			if (json.has("order_id")) {
				order_id = json.getString("order_id") + "";
			}
			if (json.has("packet_description")) {
				packet_description = json.getString("packet_description");
			}
			if (json.has("packet_title")) {
				mainTitle = json.getString("packet_title");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
