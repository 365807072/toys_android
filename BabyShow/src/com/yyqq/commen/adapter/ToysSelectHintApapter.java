package com.yyqq.commen.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.ToysAllSelect01Bean;

public class ToysSelectHintApapter extends BaseAdapter {

	private Activity context;
	private ToysAllSelect01Bean selectData01;
	
	public ToysSelectHintApapter(Activity context, ToysAllSelect01Bean selectData01) {
		super();
		this.context = context;
		this.selectData01 = selectData01;
	}

	@Override
	public int getCount() {
		return selectData01.getMain_val().size() == 0 ? 0 : selectData01.getMain_val().size();
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
		convertView = inflater.inflate(R.layout.item_toys_all_select, null);
		
		TextView item_search_hint_text = (TextView) convertView.findViewById(R.id.item_search_hint_text);
		item_search_hint_text.setText(selectData01.getMain_val().get(position).getEach_title());
		
		if(selectData01.getMain_val().get(position).isClick()){
			item_search_hint_text.setBackgroundResource(R.drawable.shape_text_down);
			item_search_hint_text.setTextColor(Color.parseColor("#ffffff"));
		}else{
			item_search_hint_text.setBackgroundResource(R.drawable.shape_text_up);
			item_search_hint_text.setTextColor(Color.parseColor("#666666"));
		}
		
		item_search_hint_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(position == 0){
					
					for(int i = 0 ; i < selectData01.getMain_val().size() ; i++){
						selectData01.getMain_val().get(i).setClick(false);
					}
					
					selectData01.getMain_val().get(0).setClick(true);
					
				}else{
					
					if(selectData01.getMain_val().get(position).isClick()){
						selectData01.getMain_val().get(position).setClick(false);
					}else{
						selectData01.getMain_val().get(position).setClick(true);
					}
					
					boolean haveClick = false;
					for(int i = 0 ; i < selectData01.getMain_val().size() ; i++){
						if(selectData01.getMain_val().get(i).isClick()){
							haveClick = true;
						}
					}
					
					if(haveClick){
						selectData01.getMain_val().get(0).setClick(false);
					}else{
						selectData01.getMain_val().get(0).setClick(true);
					}
					
				}
				
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
}