package com.yyqq.commen.model;

public class ToysLeaseOrderInfoBean {

	private String hint;
	private String number;

	public ToysLeaseOrderInfoBean() {
		super();
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "ToysLeaseOrderBean [hint=" + hint + ", number=" + number + "]";
	}

}
