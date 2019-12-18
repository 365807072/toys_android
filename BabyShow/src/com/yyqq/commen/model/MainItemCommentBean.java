package com.yyqq.commen.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 话题评论
 * */
public class MainItemCommentBean {
	private String review_id;
	private String img_id;
	private String demand;
	private String user_name;
	private String avatar;
	private String post_time;
	private String post_create_time;
	private String review_count;
	private String admire_count;
	private String is_admire;
	private String user_id;
	private String is_review;
	private ArrayList<Review> reviewData = new ArrayList<Review>();
	
	public String getIs_review() {
		return is_review;
	}
	public void setIs_review(String is_review) {
		this.is_review = is_review;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	private ArrayList<MainItemCommentImgBean> img = new ArrayList<MainItemCommentImgBean>();
	
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getReview_id() {
		return review_id;
	}
	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}
	public String getDemand() {
		return demand;
	}
	public void setDemand(String demand) {
		this.demand = demand;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getPost_time() {
		return post_time;
	}
	public void setPost_time(String post_time) {
		this.post_time = post_time;
	}
	public String getPost_create_time() {
		return post_create_time;
	}
	public void setPost_create_time(String post_create_time) {
		this.post_create_time = post_create_time;
	}
	public String getReview_count() {
		return review_count;
	}
	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}
	public String getAdmire_count() {
		return admire_count;
	}
	public void setAdmire_count(String admire_count) {
		this.admire_count = admire_count;
	}
	public String getIs_admire() {
		return is_admire;
	}
	public void setIs_admire(String is_admire) {
		this.is_admire = is_admire;
	}
	public ArrayList<MainItemCommentImgBean> getImg() {
		return img;
	}
	public void setImg(ArrayList<MainItemCommentImgBean> img) {
		this.img = img;
	}
	public ArrayList<Review> getReviewData() {
		return reviewData;
	}
	public void setReviewData(ArrayList<Review> reviewData) {
		this.reviewData = reviewData;
	}
	@Override
	public String toString() {
		return "MainItemCommentBean [review_id=" + review_id + ", img_id="
				+ img_id + ", demand=" + demand + ", user_name=" + user_name
				+ ", avatar=" + avatar + ", post_time=" + post_time
				+ ", post_create_time=" + post_create_time + ", review_count="
				+ review_count + ", admire_count=" + admire_count
				+ ", is_admire=" + is_admire + ", user_id=" + user_id
				+ ", img=" + img + "]";
	}
	
	public static void fromJson(JSONArray jsonaAll, ArrayList<MainItemCommentBean> commentList) {
		
		MainItemCommentBean bean;
		for(int j = 0 ; j < jsonaAll.length() ; j++){
			bean = new MainItemCommentBean();
			
			try {
				JSONObject json = jsonaAll.getJSONObject(j);
				bean.setImg_id(json.getString("img_id"));
				bean.setReview_id(json.getString("review_id"));
				bean.setDemand(json.getString("demand"));
				bean.setUser_name(json.getString("user_name"));
				bean.setAvatar(json.getString("avatar"));
				bean.setPost_time(json.getString("post_time"));
				bean.setPost_create_time(json.getString("post_create_time"));
				bean.setReview_count(json.getString("review_count"));
				bean.setAdmire_count(json.getString("admire_count"));
				bean.setIs_admire(json.getString("is_admire"));
				bean.setUser_id(json.getString("user_id"));
				
				if(json.has("is_review")){
					bean.setIs_review(json.getString("is_review"));
				}
				
				ArrayList<Review> commentData = new ArrayList<Review>();
				Review reviewBean = null;
				for(int k = 0 ; k < jsonaAll.getJSONObject(j).getJSONArray("getReviewReviewList").length() ; k++ ){
					reviewBean = new Review();
					reviewBean.fromJson(jsonaAll.getJSONObject(j).getJSONArray("getReviewReviewList").getJSONObject(k));
					commentData.add(reviewBean);
				}
				bean.setReviewData(commentData);

				// 获取图片集合
				JSONArray jsonArray = json.getJSONArray("img");
				MainItemCommentImgBean imageBean;
				ArrayList<MainItemCommentImgBean> imageBeanList = new ArrayList<MainItemCommentImgBean>();
				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						imageBean = new MainItemCommentImgBean();
						imageBean.setImg_thumb(jsonArray.getJSONObject(i).getString("img_thumb"));
						imageBeanList.add(imageBean);
					}
					bean.setImg(imageBeanList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			
			
			commentList.add(bean);
		}
	}
	
}
