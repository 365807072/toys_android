package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BusinessItem {
	public String business_title; // 商家名称
	public String business_description; // 商家简介
	public String grade1, grade3; // 评分等级
	public String business_package1, business_package2, business_package3,
			business_package4;
	public String business_activity_num1, business_activity_num2,
			business_activity_num3, business_activity_num4; // 商家活动次数
	public String business_activity_time1, business_activity_time2,
			business_activity_time3, business_activity_time4; // 商家活动时间
	public String business_market_price1, business_market_price2,
			business_market_price3, business_market_price4; // 市场价格
	public String business_babyshow_price1, business_babyshow_price2,
			business_babyshow_price3, business_babyshow_price4; // 秀秀价格
	public String business_free_state1, business_free_state2,
			business_free_state3, business_free_state4;
	public String business_location; // 商家地址
	public String work_time; // 使用时间
	public String business_contact; // 商家联系电话
	public String user_role; // 是否是商家
	public String user_name; // 评论的用户
	public String user_time; // 评论时间
	public String user_msg; // 评论内容
	public String lat; // 经度
	public String log; // 维度
	public String tencent_lat; // 腾讯经度
	public String tencent_log; // 腾讯维度
	public List<Img> listImg;
	public ArrayList<String> imaBig = new ArrayList<String>();
	public ArrayList<String> imaWid = new ArrayList<String>();
	public ArrayList<String> imaHed = new ArrayList<String>();

	public JSONObject json;

	public class Img {
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb;
		public String img_thumb_width;
		public String img_thumb_height;
	}

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("business_title")) {
				business_title = json.getString("business_title");
			}
			if (json.has("business_description")) {
				business_description = json.getString("business_description");
			}
			if (json.has("grade1")) {
				grade1 = json.getString("grade1");
			}
			if (json.has("grade3")) {
				grade3 = json.getString("grade3");
			}
			if (json.has("business_activity_num1")) {
				business_activity_num1 = json
						.getString("business_activity_num1");
			}
			if (json.has("business_activity_num2")) {
				business_activity_num2 = json
						.getString("business_activity_num2");
			}
			if (json.has("business_activity_num3")) {
				business_activity_num3 = json
						.getString("business_activity_num3");
			}
			if (json.has("business_activity_num4")) {
				business_activity_num4 = json
						.getString("business_activity_num4");
			}
			if (json.has("business_activity_time1")) {
				business_activity_time1 = json
						.getString("business_activity_time1");
			}
			if (json.has("business_activity_time2")) {
				business_activity_time2 = json
						.getString("business_activity_time2");
			}
			if (json.has("business_activity_time3")) {
				business_activity_time3 = json
						.getString("business_activity_time3");
			}
			if (json.has("business_activity_time4")) {
				business_activity_time4 = json
						.getString("business_activity_time4");
			}
			if (json.has("business_market_price1")) {
				business_market_price1 = json
						.getString("business_market_price1");
			}
			if (json.has("business_market_price2")) {
				business_market_price2 = json
						.getString("business_market_price2");
			}
			if (json.has("business_market_price3")) {
				business_market_price3 = json
						.getString("business_market_price3");
			}
			if (json.has("business_market_price4")) {
				business_market_price4 = json
						.getString("business_market_price4");
			}
			if (json.has("business_babyshow_price1")) {
				business_babyshow_price1 = json
						.getString("business_babyshow_price1");
			}
			if (json.has("business_babyshow_price2")) {
				business_babyshow_price2 = json
						.getString("business_babyshow_price2");
			}
			if (json.has("business_babyshow_price3")) {
				business_babyshow_price3 = json
						.getString("business_babyshow_price3");
			}
			if (json.has("business_babyshow_price4")) {
				business_babyshow_price4 = json
						.getString("business_babyshow_price4");
			}
			if (json.has("business_location")) {
				business_location = json.getString("business_location");
			}
			if (json.has("work_time")) {
				work_time = json.getString("work_time");
			}
			if (json.has("business_contact")) {
				business_contact = json.getString("business_contact");
			}
			if (json.has("business_package1")) {
				business_package1 = json.getString("business_package1");
			}
			if (json.has("business_package2")) {
				business_package2 = json.getString("business_package2");
			}
			if (json.has("business_package3")) {
				business_package3 = json.getString("business_package3");
			}
			if (json.has("business_package4")) {
				business_package4 = json.getString("business_package4");
			}
			if (json.has("business_free_state1")) {
				business_free_state1 = json.getString("business_free_state1");
			}
			if (json.has("business_free_state2")) {
				business_free_state2 = json.getString("business_free_state2");
			}
			if (json.has("business_free_state3")) {
				business_free_state3 = json.getString("business_free_state3");
			}
			if (json.has("business_free_state4")) {
				business_free_state4 = json.getString("business_free_state4");
			}
			if (json.has("user_role")) {
				user_role = json.getString("user_role");
			}
			if (json.has("user_name")) {
				user_name = json.getString("user_name");
			}
			if (json.has("user_time")) {
				user_time = json.getString("user_time");
			}
			if (json.has("user_msg")) {
				user_msg = json.getString("user_msg");
			}
			if (json.has("lat")) {
				lat = json.getString("lat");
			}
			if (json.has("log")) {
				log = json.getString("log");
			}
			if (json.has("tencent_lat")) {
				tencent_lat = json.getString("tencent_lat");
			}
			if (json.has("tencent_log")) {
				tencent_log = json.getString("tencent_log");
			}

			JSONArray imgArray = json.getJSONArray("img");
			listImg = new ArrayList<BusinessItem.Img>();
			for (int i = 0; i < imgArray.length(); i++) {
				Img arg0 = new Img();
				arg0.img = imgArray.getJSONObject(i).getString("img");
				arg0.img_width = imgArray.getJSONObject(i).getString(
						"img_width");
				arg0.img_height = imgArray.getJSONObject(i).getString(
						"img_height");
				arg0.img_thumb = imgArray.getJSONObject(i).getString(
						"img_thumb");
				arg0.img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				arg0.img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");
				listImg.add(arg0);
				imaBig.add(arg0.img);
				imaWid.add(arg0.img_width);
				imaHed.add(arg0.img_height);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
