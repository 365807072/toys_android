package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysLeaseDepositDetailBean;

/**
 * 押金详情
 * */
public class ToysLeaseDepositDetailAdapter extends BaseAdapter {

	private ArrayList<ToysLeaseDepositDetailBean> data;
	private Activity context;

	public ToysLeaseDepositDetailAdapter() {
	}

	public ToysLeaseDepositDetailAdapter(ArrayList<ToysLeaseDepositDetailBean> data,
			Activity context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size() == 0 ? 0 : data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_toys_deposit_detail, null);

		TextView toys_deposit_year = (TextView) convertView.findViewById(R.id.toys_deposit_year);
		TextView toys_deposit_hour = (TextView) convertView.findViewById(R.id.toys_deposit_hour);
		TextView toys_deposit_monery = (TextView) convertView.findViewById(R.id.toys_deposit_monery);
		TextView toys_deposit_detail = (TextView) convertView.findViewById(R.id.toys_deposit_detail);
		
		toys_deposit_year.setText(data.get(position).getYear());
		toys_deposit_hour.setText(data.get(position).getHour());
		toys_deposit_monery.setText(data.get(position).getMonery());
		toys_deposit_detail.setText(data.get(position).getDetail());
		
		return convertView;
	}

}
