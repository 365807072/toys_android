package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;

@SuppressWarnings("deprecation")
public class SearchHint02Apapter extends BaseAdapter {

	private Activity context;
	private int windowWidth;
	private ArrayList<String> headData;
	
	public SearchHint02Apapter(Activity context, ArrayList<String> headData) {
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
		convertView = inflater.inflate(R.layout.item_search_listviewcontext, null);
		
		TextView item_search_hint_text = (TextView) convertView.findViewById(R.id.text_context);
		item_search_hint_text.setText(headData.get(position));
		
		TextView text_number = (TextView) convertView.findViewById(R.id.text_number);
		text_number.setText(position + 1 +"");
		
		if(position == 0){
			text_number.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textview_to_shape_01));
		}else if(position == 1){
			text_number.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textview_to_shape_02));
		}else if(position == 2){
			text_number.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textview_to_shape_03));
		}else{
			text_number.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textview_to_shape));
		}
		
		return convertView;
	}
}