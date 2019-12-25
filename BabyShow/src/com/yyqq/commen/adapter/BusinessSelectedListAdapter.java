package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.BusinessSelectedListBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 精选商家列表adapter
 * */
public class BusinessSelectedListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<BusinessSelectedListBean> data = null;
	private int winWeight = 0;
	
	public BusinessSelectedListAdapter(Activity context, LayoutInflater inflater, MyApplication app, ArrayList<BusinessSelectedListBean> data, int winWeight) {
		super();
		this.winWeight = winWeight;
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
		convertView = inflater.inflate(R.layout.item_business_selected_list, null);
		
		ImageView item_business_selected_img = (ImageView) convertView.findViewById(R.id.item_business_selected_img);
//		LinearLayout.LayoutParams params = (LayoutParams) item_business_selected_img.getLayoutParams();
		int newH = (int) (winWeight * 0.4);
		item_business_selected_img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, newH));
		
		app.display(data.get(position).getBusiness_pic_new(), item_business_selected_img, R.drawable.def_image);
		
		return convertView;
	}

}
