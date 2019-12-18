package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysAllSelect01Bean;
import com.yyqq.commen.view.MyGridView;

public class ToysSelectHintScreenApapter extends BaseAdapter {

	private Activity context;
	private ArrayList<ToysAllSelect01Bean> selectData01;
	
	public ToysSelectHintScreenApapter(Activity context, ArrayList<ToysAllSelect01Bean> selectData01) {
		super();
		this.context = context;
		this.selectData01 = selectData01;
	}

	@Override
	public int getCount() {
		return selectData01.size() == 0 ? 0 : selectData01.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_toys_all_screen, null);
		
		TextView item_toys_all_sort_name = (TextView) convertView.findViewById(R.id.item_toys_all_sort_name);
		MyGridView lease_select_list_02 = (MyGridView) convertView.findViewById(R.id.lease_select_list_02);
		
		ToysAllSelect01Bean itemBean = new ToysAllSelect01Bean();
		itemBean.setMain_title(selectData01.get(position).getMain_title());
		itemBean.setMain_val(selectData01.get(position).getMain_val());
		
		ToysSelectHint02Apapter selectAdapter02 = new ToysSelectHint02Apapter(context, itemBean);
		lease_select_list_02.setAdapter(selectAdapter02);
		
		item_toys_all_sort_name.setText(itemBean.getMain_title());
		
		return convertView;
	}
}