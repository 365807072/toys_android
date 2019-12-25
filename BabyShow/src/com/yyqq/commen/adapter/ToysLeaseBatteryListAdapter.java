package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysLeaseBatteryBean;

/**
 * 玩具世界支付页面-电池选择列表
 * */
public class ToysLeaseBatteryListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysLeaseBatteryBean> data = null;
	
	public ToysLeaseBatteryListAdapter(Activity context, LayoutInflater inflater, ArrayList<ToysLeaseBatteryBean> data) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() : 0;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_toys_confirm_cart, null);
		ImageView item_con_cart_icon = (ImageView) convertView.findViewById(R.id.item_con_cart_icon);
		TextView item_con_cart_text = (TextView) convertView.findViewById(R.id.item_con_cart_text);
		
		item_con_cart_text.setText(data.get(position).getBattery_title());
		
		if(data.get(position).getIs_battery().equals("0")){
			item_con_cart_icon.setBackgroundResource(R.drawable.select_up);
		}else{
			item_con_cart_icon.setBackgroundResource(R.drawable.select_down);
			item_con_cart_text.setTextColor(Color.parseColor("#fc6262"));
		}
		
		return convertView;
	}
}
