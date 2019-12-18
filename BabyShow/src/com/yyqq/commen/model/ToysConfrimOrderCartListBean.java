package com.yyqq.commen.model;

/**
 * 玩具世界，确认订单，会员卡列表bean
 * 
 * */
public class ToysConfrimOrderCartListBean {

	private String card_id = "";
	private String card_title = "";
	private String is_choose = "";
	private String selected_state = "";

	public ToysConfrimOrderCartListBean() {
		super();
	}

	public ToysConfrimOrderCartListBean(String card_id, String card_title,
			String is_choose, String selected_state) {
		super();
		this.card_id = card_id;
		this.card_title = card_title;
		this.is_choose = is_choose;
		this.selected_state = selected_state;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_title() {
		return card_title;
	}

	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}

	public String getIs_choose() {
		return is_choose;
	}

	public void setIs_choose(String is_choose) {
		this.is_choose = is_choose;
	}

	public String getSelected_state() {
		return selected_state;
	}

	public void setSelected_state(String selected_state) {
		this.selected_state = selected_state;
	}

	@Override
	public String toString() {
		return "ToysConfrimOrderCartListBean [card_id=" + card_id
				+ ", card_title=" + card_title + ", is_choose=" + is_choose
				+ ", selected_state=" + selected_state + "]";
	}

}
