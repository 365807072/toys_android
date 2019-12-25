package com.yyqq.commen.model;

public class ToysAllSelect01ItemBean {

	private String each_title = "";
	private String each_val = "";
	private boolean isClick = false;

	public boolean isClick() {
		return isClick;
	}

	public void setClick(boolean isClick) {
		this.isClick = isClick;
	}

	public String getEach_title() {
		return each_title;
	}

	public void setEach_title(String each_title) {
		this.each_title = each_title;
	}

	public String getEach_val() {
		return each_val;
	}

	public void setEach_val(String each_val) {
		this.each_val = each_val;
	}

	@Override
	public String toString() {
		return "ToysAllSelect01ItemBean [each_title=" + each_title
				+ ", each_val=" + each_val + ", isClick=" + isClick + "]";
	}

}
