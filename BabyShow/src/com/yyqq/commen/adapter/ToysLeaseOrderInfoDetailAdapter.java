package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysLeaseOrderInfoBean;

public class ToysLeaseOrderInfoDetailAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysLeaseOrderInfoBean> bodyList;
	
	public ToysLeaseOrderInfoDetailAdapter(Activity context, LayoutInflater inflater, ArrayList<ToysLeaseOrderInfoBean> bodyList) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.bodyList = bodyList;
	}

	@Override
	public int getCount() {
		return bodyList.size() == 0 ? 0 : bodyList.size();
	}

	@Override
	public Object getItem(int position) {
		return bodyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_lease_order_info_detail, null);
		TextView item_lease_hint = (TextView) convertView.findViewById(R.id.item_lease_hint);
		TextView item_lease_number = (TextView) convertView.findViewById(R.id.item_lease_number);
		item_lease_hint.setText(bodyList.get(position).getHint());
		item_lease_number.setText(bodyList.get(position).getNumber());
		return convertView;
	}

	
}
