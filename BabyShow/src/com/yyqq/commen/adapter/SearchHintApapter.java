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

@SuppressWarnings("deprecation")
public class SearchHintApapter extends BaseAdapter {

	private Activity context;
	private int windowWidth;
	private ArrayList<String> headData;
	
	public SearchHintApapter(Activity context, ArrayList<String> headData) {
		super();
		this.context = context;
		this.headData = headData;
	}

	@Override
	public int getCount() {
		return headData.size() == 0 ? 0 : headData.size();// 实现循环显示
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
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_search_hint, null);
		
		TextView item_search_hint_text = (TextView) convertView.findViewById(R.id.item_search_hint_text);
		item_search_hint_text.setText(headData.get(position));
		
		return convertView;
	}
}