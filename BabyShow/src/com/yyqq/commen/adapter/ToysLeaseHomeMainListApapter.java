package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseCategoryActivity;
import com.yyqq.commen.model.ToysLeaseHomeMainBean;
import com.yyqq.framework.application.MyApplication;

public class ToysLeaseHomeMainListApapter extends BaseAdapter {

	private Activity context;
	private LayoutInflater inflater;
	private ArrayList<ToysLeaseHomeMainBean> data;
	
	public ToysLeaseHomeMainListApapter(Activity context, LayoutInflater inflater,ArrayList<ToysLeaseHomeMainBean> data) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size() == 0 ?  0 : data.size();
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
		convertView = inflater.inflate(R.layout.item_toys_lease_home_main, null);
		RelativeLayout item_toys_main_01 = (RelativeLayout) convertView.findViewById(R.id.item_toys_main_01);
		RelativeLayout item_toys_main_02 = (RelativeLayout) convertView.findViewById(R.id.item_toys_main_02);
		RelativeLayout item_toys_main_03 = (RelativeLayout) convertView.findViewById(R.id.item_toys_main_03);
		TextView item_toys_main_styleName = (TextView) convertView.findViewById(R.id.item_toys_main_styleName);
		TextView item_toys_main_more = (TextView) convertView.findViewById(R.id.item_toys_main_more);
		ImageView item_toys_main_img01 = (ImageView) convertView.findViewById(R.id.item_toys_main_img01);
		TextView item_toys_main_name01 = (TextView) convertView.findViewById(R.id.item_toys_main_name01);
		TextView item_toys_main_price01 = (TextView) convertView.findViewById(R.id.item_toys_main_price01);
		ImageView item_toys_main_img02 = (ImageView) convertView.findViewById(R.id.item_toys_main_img02);
		TextView item_toys_main_name02 = (TextView) convertView.findViewById(R.id.item_toys_main_name02);
		TextView item_toys_main_price02 = (TextView) convertView.findViewById(R.id.item_toys_main_price02);
		ImageView item_toys_main_img03 = (ImageView) convertView.findViewById(R.id.item_toys_main_img03);
		TextView item_toys_main_name03 = (TextView) convertView.findViewById(R.id.item_toys_main_name03);
		TextView item_toys_main_price03 = (TextView) convertView.findViewById(R.id.item_toys_main_price03);
		ImageView item_toys_main_imghint01 = (ImageView) convertView.findViewById(R.id.item_toys_main_imghint01);
		ImageView item_toys_main_imghint02 = (ImageView) convertView.findViewById(R.id.item_toys_main_imghint02);
		ImageView item_toys_main_imghint03 = (ImageView) convertView.findViewById(R.id.item_toys_main_imghint03);
		
		item_toys_main_styleName.setText(data.get(position).getCate_title());
		item_toys_main_more.setText(data.get(position).getMore_title());
		
		if(data.get(position).getItemBean().get(0).getIcon_url().isEmpty()){
			item_toys_main_imghint01.setVisibility(View.GONE);
		}else{
			item_toys_main_imghint01.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getIcon_url(), item_toys_main_imghint01, R.drawable.def_image);
		}
		MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getImg_thumb(), item_toys_main_img01, R.drawable.def_image);
		item_toys_main_price01.setText(data.get(position).getItemBean().get(0).getSell_price());
		item_toys_main_name01.setText(data.get(position).getItemBean().get(0).getBusiness_title());
		
		if(data.get(position).getItemBean().get(1).getIcon_url().isEmpty()){
			item_toys_main_imghint02.setVisibility(View.GONE);
		}else{
			item_toys_main_imghint02.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(1).getIcon_url(), item_toys_main_imghint02, R.drawable.def_image);
		}
		MyApplication.getInstance().display(data.get(position).getItemBean().get(1).getImg_thumb(), item_toys_main_img02, R.drawable.def_image);
		item_toys_main_price02.setText(data.get(position).getItemBean().get(1).getSell_price());
		item_toys_main_name02.setText(data.get(position).getItemBean().get(1).getBusiness_title());
		
		if(data.get(position).getItemBean().get(2).getIcon_url().isEmpty()){
			item_toys_main_imghint03.setVisibility(View.GONE);
		}else{
			item_toys_main_imghint03.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(2).getIcon_url(), item_toys_main_imghint03, R.drawable.def_image);
		}
		MyApplication.getInstance().display(data.get(position).getItemBean().get(2).getImg_thumb(), item_toys_main_img03, R.drawable.def_image);
		item_toys_main_price03.setText(data.get(position).getItemBean().get(2).getSell_price());
		item_toys_main_name03.setText(data.get(position).getItemBean().get(2).getBusiness_title());
		
		item_toys_main_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ToysLeaseCategoryActivity.class);
				intent.putExtra(ToysLeaseCategoryActivity.CATEGORY_ID_KEY, data.get(position).getCategory_id());
				context.startActivity(intent);
			}
		});
		
		item_toys_main_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.get(position).getItemBean().get(0).getType().equals("41")){ // 外链
					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position).getItemBean().get(0).getPost_url());
					context.startActivity(intent);
				}else{
					Intent intent = new Intent(context, ToysLeaseDetailActivity.class);
					intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position).getItemBean().get(0).getBusiness_id());
					context.startActivity(intent);
				}
			}
		});
		
		item_toys_main_02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.get(position).getItemBean().get(1).getType().equals("41")){ // 外链
					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position).getItemBean().get(1).getPost_url());
					context.startActivity(intent);
				}else{
					Intent intent = new Intent(context, ToysLeaseDetailActivity.class);
					intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position).getItemBean().get(1).getBusiness_id());
					context.startActivity(intent);
				}
			}
		});
		
		item_toys_main_03.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.get(position).getItemBean().get(2).getType().equals("41")){ // 外链
					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position).getItemBean().get(2).getPost_url());
					context.startActivity(intent);
				}else{
					Intent intent = new Intent(context, ToysLeaseDetailActivity.class);
					intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position).getItemBean().get(2).getBusiness_id());
					context.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}
}