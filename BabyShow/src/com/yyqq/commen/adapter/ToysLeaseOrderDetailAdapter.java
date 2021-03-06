package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysLeaseOrderDetailBean;
import com.yyqq.framework.application.MyApplication;

public class ToysLeaseOrderDetailAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private ArrayList<ToysLeaseOrderDetailBean> bodyList;
	
	public ToysLeaseOrderDetailAdapter(LayoutInflater inflater, ArrayList<ToysLeaseOrderDetailBean> bodyList) {
		super();
		this.inflater = inflater;
		this.bodyList = bodyList;
	}

	@Override
	public int getCount() {
		return bodyList.size();
	}

	@Override
	public Object getItem(int position) {
		return bodyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(bodyList.get(position).getStyle().equals("3")){
			convertView = inflater.inflate(R.layout.item_lease_order_detail03, null);
			TextView lease_order_type = (TextView) convertView.findViewById(R.id.lease_order_type);
			lease_order_type.setText(bodyList.get(position).getType());
		}else{
			if(position % 2 != 0){
				convertView = inflater.inflate(R.layout.item_lease_order_detail01, null);
				ImageView lease_order_img = (ImageView) convertView.findViewById(R.id.lease_order_img);
				TextView lease_order_type = (TextView) convertView.findViewById(R.id.lease_order_type);
				TextView lease_order_time = (TextView) convertView.findViewById(R.id.lease_order_time);
				
				MyApplication.getInstance().display(bodyList.get(position).getImgUrl(), lease_order_img, R.drawable.def_image);
				lease_order_type.setText(bodyList.get(position).getType());
				lease_order_time.setText(bodyList.get(position).getTime());
			}else{
				convertView = inflater.inflate(R.layout.item_lease_order_detail02, null);
				ImageView lease_order_img = (ImageView) convertView.findViewById(R.id.lease_order_img);
				TextView lease_order_type = (TextView) convertView.findViewById(R.id.lease_order_type);
				TextView lease_order_time = (TextView) convertView.findViewById(R.id.lease_order_time);
				
				MyApplication.getInstance().display(bodyList.get(position).getImgUrl(), lease_order_img, R.drawable.def_image);
				lease_order_type.setText(bodyList.get(position).getType());
				lease_order_time.setText(bodyList.get(position).getTime());
			}
		}
		return convertView;
	}

	
}
