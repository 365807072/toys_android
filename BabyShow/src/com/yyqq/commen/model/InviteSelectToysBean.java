package com.yyqq.commen.model;

public class InviteSelectToysBean {

	private String prize_id = "";
	private String prize_title = "";
	private String prize_description = "";
	private String img = "";
	private String is_click = "";
	private String prize_number = "";

	public InviteSelectToysBean() {
	}

	public InviteSelectToysBean(String prize_id, String prize_title,
			String prize_description, String img, String is_click,
			String prize_number) {
		super();
		this.prize_id = prize_id;
		this.prize_title = prize_title;
		this.prize_description = prize_description;
		this.img = img;
		this.is_click = is_click;
		this.prize_number = prize_number;
	}

	public String getPrize_id() {
		return prize_id;
	}

	public void setPrize_id(String prize_id) {
		this.prize_id = prize_id;
	}

	public String getPrize_title() {
		return prize_title;
	}

	public void setPrize_title(String prize_title) {
		this.prize_title = prize_title;
	}

	public String getPrize_description() {
		return prize_description;
	}

	public void setPrize_description(String prize_description) {
		this.prize_description = prize_description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getIs_click() {
		return is_click;
	}

	public void setIs_click(String is_click) {
		this.is_click = is_click;
	}

	public String getPrize_number() {
		return prize_number;
	}

	public void setPrize_number(String prize_number) {
		this.prize_number = prize_number;
	}

	@Override
	public String toString() {
		return "InviteSelectToysBean [prize_id=" + prize_id + ", prize_title="
				+ prize_title + ", prize_description=" + prize_description
				+ ", img=" + img + ", is_click=" + is_click + ", prize_number="
				+ prize_number + "]";
	}

}
