package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysLeaseCartBean;
import com.yyqq.commen.view.MyListView;

public class ToysLeaseCartAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysLeaseCartBean> data = null;
	
	public ToysLeaseCartAdapter(Activity context, LayoutInflater inflater, ArrayList<ToysLeaseCartBean> data) {
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
		if(data.get(position).getType().equals("1")){ // 大标题
			return initCategoryView(data.get(position));
		}else{ // 内容主体
			return initMemberView(data.get(position));
		}
	}
	
	/**
	 * 初始化类别view
	 * */
	private View initCategoryView(ToysLeaseCartBean bean){
		View convertView = inflater.inflate(R.layout.item_cart_type01, null);
		TextView categoryName = (TextView) convertView.findViewById(R.id.item_cart_type);
		categoryName.setText(bean.getToys_title());
		return convertView;
	}
	
	/**
	 * 初始化玩具view
	 * */
	private View initMemberView(ToysLeaseCartBean bean){
		View convertView = inflater.inflate(R.layout.item_cart_type02, null);
		MyListView item_cart_list = (MyListView) convertView.findViewById(R.id.item_cart_list);
		ToysLeaseCartItemAdapter adapter = new ToysLeaseCartItemAdapter(context, inflater, bean.getToys_info());
		item_cart_list.setAdapter(adapter);
		return convertView;
	}
	
}
