package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.version_93.MainToysHomeActivity;
import com.yyqq.commen.model.ToysLeaseHomeHeadBean;
import com.yyqq.framework.application.MyApplication;

@SuppressWarnings("deprecation")
public class ToysLeaseHomeHeadListApapter extends BaseAdapter {

	private Activity context;
	private int windowWidth;
	private ArrayList<ToysLeaseHomeHeadBean> headData;
	private LayoutInflater inflater;
	
	public ToysLeaseHomeHeadListApapter(Activity context, ArrayList<ToysLeaseHomeHeadBean> headData, int windowWidth, LayoutInflater inflater) {
		super();
		this.context = context;
		this.headData = headData;
		this.windowWidth = windowWidth;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return headData.size() == 0 ? 0 : headData.size();
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
		convertView = inflater.inflate(R.layout.item_home_head_list, null);
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.item_home_head_img);
		MyApplication.getInstance().display(headData.get(position).getImgUrl(), imageView, R.drawable.def_image);
		
		return convertView;
	}
}