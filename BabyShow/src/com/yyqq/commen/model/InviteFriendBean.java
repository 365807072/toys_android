package com.yyqq.commen.model;

/**
 * 已经邀请的好友历史列表
 * 
 * */
public class InviteFriendBean {

	private String invitation_id = "";
	private String user_id = "";
	private String avatar = "";
	private String nick_name = "";
	private String status = "";
	private String status_title = "";
	private String inv_description = "";
	private String inv_button = "";

	public InviteFriendBean() {
	}

	public InviteFriendBean(String invitation_id, String user_id,
			String avatar, String nick_name, String status,
			String status_title, String inv_description, String inv_button) {
		super();
		this.invitation_id = invitation_id;
		this.user_id = user_id;
		this.avatar = avatar;
		this.nick_name = nick_name;
		this.status = status;
		this.status_title = status_title;
		this.inv_description = inv_description;
		this.inv_button = inv_button;
	}

	public String getInvitation_id() {
		return invitation_id;
	}

	public void setInvitation_id(String invitation_id) {
		this.invitation_id = invitation_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_title() {
		return status_title;
	}

	public void setStatus_title(String status_title) {
		this.status_title = status_title;
	}

	public String getInv_description() {
		return inv_description;
	}

	public void setInv_description(String inv_description) {
		this.inv_description = inv_description;
	}

	public String getInv_button() {
		return inv_button;
	}

	public void setInv_button(String inv_button) {
		this.inv_button = inv_button;
	}

	@Override
	public String toString() {
		return "InviteFriendBean [invitation_id=" + invitation_id
				+ ", user_id=" + user_id + ", avatar=" + avatar
				+ ", nick_name=" + nick_name + ", status=" + status
				+ ", status_title=" + status_title + ", inv_description="
				+ inv_description + ", inv_button=" + inv_button + "]";
	}

}
