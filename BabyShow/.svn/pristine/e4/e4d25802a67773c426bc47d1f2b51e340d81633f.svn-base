package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.model.MainItemBodyImgBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.framework.application.MyApplication;

public class GroupDetialCatagoryAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<String> cateGoryList;

	public GroupDetialCatagoryAdapter(Activity context,
			LayoutInflater inflater, ArrayList<String> cateGoryList) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.cateGoryList = cateGoryList;
	}

	@Override
	public int getCount() {
		return cateGoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return cateGoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_group_category, null);

		TextView item_group_catagory = (TextView) convertView.findViewById(R.id.item_group_catagory);
		item_group_catagory.setText(cateGoryList.get(position));
		
		return convertView;
	}

}
