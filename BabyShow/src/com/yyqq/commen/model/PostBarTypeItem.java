package com.yyqq.commen.model;

import java.io.Serializable;

import org.json.JSONObject;

public class PostBarTypeItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String img = "";
	public String img_id = "";
	public String user_id = "";
	public String img_title = "";
	public String description = "";
	public String review_count = ""; // 参与人数
	public String post_count = ""; // 帖子数量/观看人数
	public long create_time;
	public long post_create_time;
	public String group_id = "";
	public String group_name = "";
	public String is_group = ""; // 0帖子、1群、2商家、3群中商家、4群中商家列表、5视频
	public String is_recommend = "";
	public String is_share = ""; // 是否参与了 1表示参与 0表示没参与
	public String is_top = ""; // 是否置顶
	public String is_notice = ""; // 是否为公告
	public String is_essence = ""; // 是否为精华
	public String distinguish = ""; // 帖子区分 1=公告 2=精华
	public String video_url = "";
	public String img_thumb_width = "";
	public String img_thumb_height = "";
	public String distance = "";
	public String order_count = "";
	public String market_price = "";
	public String babyshow_price = "";
	public boolean isSavePost = false;
	public boolean isFriends = false;
	public String group_tag_id = "";
	public String is_group_tag = "0";

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = null;
			if(json.has("img")){
				info = json.getJSONObject("img");
			}else{
				info = json;
			}
			if (info.has("isSavePost")) {
				isSavePost = info.getBoolean("isSavePost");
			}
			if (info.has("isFriends")) {
				isFriends = info.getBoolean("isFriends");
			}
			if (info.has("search_id")) {
				img_id = info.getString("search_id") + "";
				group_id = info.getString("search_id") + "";
			}
			if (info.has("search_word")) {
				group_name = info.getString("search_word") + "";
			}
			if (info.has("search_word")) {
				img_title = info.getString("search_word") + "";
			}
			if (info.has("img_description")) {
				description = info.getString("img_description") + "";
			}
			if (info.has("img_thumb")) {
				img = info.getString("img_thumb") + "";
			}
			if (info.has("distance")) {
				distance = info.getString("distance") + "";
			}
			if (info.has("order_count")) {
				order_count = info.getString("order_count") + "";
			}
			if (info.has("market_price")) {
				market_price = info.getString("market_price") + "";
			}
			if (info.has("babyshow_price")) {
				babyshow_price = info.getString("babyshow_price") + "";
			}
			if (info.has("img_id")) {
				img_id = info.getInt("img_id") + "";
			}
			if (info.has("user_id")) {
				user_id = info.getInt("user_id") + "";
			}
			if (info.has("description")) {
				description = info.getString("description");
			}
			if (info.has("img")) {
				img = info.getString("img");
			}
			if (info.has("post_count")) {
				post_count = info.getString("post_count") + "";
			}
			if (info.has("review_count")) {
				review_count = info.getString("review_count") + "";
			}
			if (info.has("create_time")) {
				create_time = info.getLong("create_time");
			}
			if (info.has("img_title")) {
				img_title = info.getString("img_title");
			}
			if (info.has("post_create_time")) {
				post_create_time = info.getLong("post_create_time");
			}
			if (info.has("group_id")) {
				group_id = info.getInt("group_id") + "";
			}
			if (info.has("group_name")) {
				group_name = info.getString("group_name");
			}
			if (info.has("is_group")) {
				is_group = info.getString("is_group");
			}
			if (info.has("is_recommend")) {
				is_recommend = info.getInt("is_recommend") + "";
			}
			if (info.has("is_share")) {
				is_share = info.getString("is_share") + "";
			}
			if (info.has("is_top")) {
				is_top = info.getInt("is_top") + "";
			}
			if (info.has("is_notice")) {
				is_notice = info.getInt("is_notice") + "";
			}
			if (info.has("is_essence")) {
				is_essence = info.getInt("is_essence") + "";
			}
			if (info.has("distinguish")) {
				distinguish = info.getString("distinguish");
			}
			if (info.has("video_url")) {
				video_url = info.getString("video_url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getImg() {
		return img;
	}

	public String getImg_thumb_width() {
		return img_thumb_width;
	}

	public void setImg_thumb_width(String img_thumb_width) {
		this.img_thumb_width = img_thumb_width;
	}

	public String getImg_thumb_height() {
		return img_thumb_height;
	}

	public void setImg_thumb_height(String img_thumb_height) {
		this.img_thumb_height = img_thumb_height;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg_id() {
		return img_id;
	}

	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getImg_title() {
		return img_title;
	}

	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}

	public String getPost_count() {
		return post_count;
	}

	public void setPost_count(String post_count) {
		this.post_count = post_count;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getPost_create_time() {
		return post_create_time;
	}

	public void setPost_create_time(long post_create_time) {
		this.post_create_time = post_create_time;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getIs_group() {
		return is_group;
	}

	public void setIs_group(String is_group) {
		this.is_group = is_group;
	}

	public String getIs_recommend() {
		return is_recommend;
	}

	public void setIs_recommend(String is_recommend) {
		this.is_recommend = is_recommend;
	}

	public String getIs_share() {
		return is_share;
	}

	public void setIs_share(String is_share) {
		this.is_share = is_share;
	}

	public String getIs_top() {
		return is_top;
	}

	public void setIs_top(String is_top) {
		this.is_top = is_top;
	}

	public String getIs_notice() {
		return is_notice;
	}

	public void setIs_notice(String is_notice) {
		this.is_notice = is_notice;
	}

	public String getIs_essence() {
		return is_essence;
	}

	public void setIs_essence(String is_essence) {
		this.is_essence = is_essence;
	}

	public String getDistinguish() {
		return distinguish;
	}

	public void setDistinguish(String distinguish) {
		this.distinguish = distinguish;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	

	public PostBarTypeItem() {
	}

	public PostBarTypeItem(String img, String img_id, String user_id,
			String img_title, String description, String review_count,
			String post_count, long create_time, long post_create_time,
			String group_id, String group_name, String is_group,
			String is_recommend, String is_share, String is_top,
			String is_notice, String is_essence, String distinguish,
			String video_url, JSONObject json) {
		super();
		this.img = img;
		this.img_id = img_id;
		this.user_id = user_id;
		this.img_title = img_title;
		this.description = description;
		this.review_count = review_count;
		this.post_count = post_count;
		this.create_time = create_time;
		this.post_create_time = post_create_time;
		this.group_id = group_id;
		this.group_name = group_name;
		this.is_group = is_group;
		this.is_recommend = is_recommend;
		this.is_share = is_share;
		this.is_top = is_top;
		this.is_notice = is_notice;
		this.is_essence = is_essence;
		this.distinguish = distinguish;
		this.video_url = video_url;
	}

	@Override
	public String toString() {
		return "PostBarTypeItem [img=" + img + ", img_id=" + img_id
				+ ", user_id=" + user_id + ", img_title=" + img_title
				+ ", description=" + description + ", review_count="
				+ review_count + ", post_count=" + post_count
				+ ", create_time=" + create_time + ", post_create_time="
				+ post_create_time + ", group_id=" + group_id + ", group_name="
				+ group_name + ", is_group=" + is_group + ", is_recommend="
				+ is_recommend + ", is_share=" + is_share + ", is_top="
				+ is_top + ", is_notice=" + is_notice + ", is_essence="
				+ is_essence + ", distinguish=" + distinguish + ", video_url="
				+ video_url + "]";
	}

}
