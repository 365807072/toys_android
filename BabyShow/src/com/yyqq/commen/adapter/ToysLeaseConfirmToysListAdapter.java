package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysConfrimOrderToysListBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 玩具世界支付页面-玩具列表
 * */
public class ToysLeaseConfirmToysListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysConfrimOrderToysListBean> data = null;
	
	public ToysLeaseConfirmToysListAdapter(Activity context, LayoutInflater inflater, ArrayList<ToysConfrimOrderToysListBean> data) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_toys_pay_list, null);
		ImageView lease_toys_pay_img = (ImageView) convertView.findViewById(R.id.lease_toys_pay_img);
		TextView lease_toys_pay_title = (TextView) convertView.findViewById(R.id.lease_toys_pay_title);
		TextView lease_toys_pay_price = (TextView) convertView.findViewById(R.id.lease_toys_pay_price);
		TextView lease_toys_pay_monery = (TextView) convertView.findViewById(R.id.lease_toys_pay_monery);
		TextView lease_toys_pay_hint = (TextView) convertView.findViewById(R.id.lease_toys_pay_hint);
		ImageView category_icon_url = (ImageView) convertView.findViewById(R.id.category_icon_url);
		
		// 分类图标
		if(data.get(position).getCategory_icon_url().isEmpty()){
			category_icon_url.setVisibility(View.GONE);
		}else{
			category_icon_url.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(data.get(position).getCategory_icon_url(), category_icon_url, R.drawable.def_image);
		}
		
		MyApplication.getInstance().display(data.get(position).getImg_thumb(), lease_toys_pay_img, R.drawable.def_image);
		lease_toys_pay_title.setText(data.get(position).getBusiness_title());
		lease_toys_pay_price.setText(data.get(position).getMarket_price());
		lease_toys_pay_monery.setText(data.get(position).getEvery_sell_price());
		lease_toys_pay_hint.setText(data.get(position).getMember_price());
		
		return convertView;
	}

}
