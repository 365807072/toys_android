package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.BusinessSelectedMoreBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 精选商家更多区域adapter
 * */
public class BusinessSelectedMoreAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<BusinessSelectedMoreBean> data = null;
	
	public BusinessSelectedMoreAdapter(Activity context, LayoutInflater inflater, MyApplication app, ArrayList<BusinessSelectedMoreBean> data) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() : 0 ;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_business_more_list, null);
		
		TextView bn_sc_more_name = (TextView) convertView.findViewById(R.id.bn_sc_more_name);
		bn_sc_more_name.setText(data.get(position).getCity_name());
		
		
		return convertView;
	}

}
