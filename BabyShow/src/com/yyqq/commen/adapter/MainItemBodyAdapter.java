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

public class MainItemBodyAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<MainItemBodyImgBean> bodyList;
	private ArrayList<String> imgList;
	public int height = 0;
	private int textNumber = 0;
//	private int index = 0;
	
	public MainItemBodyAdapter(Activity context, LayoutInflater inflater,
			MyApplication app, ArrayList<MainItemBodyImgBean> bodyList, ArrayList<String> imgList) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.bodyList = bodyList;
		this.imgList = imgList;
	}

	@Override
	public int getCount() {
		return bodyList.size();
	}

	@Override
	public Object getItem(int position) {
		return bodyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_main_detial02_item, null);
		ImageView item_body_img = (ImageView) convertView.findViewById(R.id.item_body_img);
		TextView item_body_text = (TextView) convertView.findViewById(R.id.item_body_text);
		
		if(bodyList.get(position).getDescription().isEmpty()){
			item_body_img.setVisibility(View.VISIBLE);
			app.display(bodyList.get(position).getImg_thumb(), item_body_img, R.drawable.def_image);
			
			DisplayMetrics DM = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(DM);
			
			item_body_img.getLayoutParams().width = (int)(DM.widthPixels - Config.getSize("20"));
			float imgH = Integer.parseInt(bodyList.get(position).getImg_thumb_height());
			float imgW = Integer.parseInt(bodyList.get(position).getImg_thumb_width());
			float jj = (DM.widthPixels - Config.getSize("20")) / imgW;
			float jjj = imgH * jj;
			item_body_img.getLayoutParams().height = (int) jjj;
			
			item_body_text.setVisibility(View.GONE);
			item_body_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					intent.putExtra("imgList", imgList);
					intent.putExtra("isBuy", "isBuy");
					for(int i = 0 ; i < imgList.size() ; i++){
						if(bodyList.get(position).getImg_thumb().equals(imgList.get(i))){
							intent.putExtra("listIndex", i);
						}
					}
					context.startActivity(intent);
				}
			});
			
		}else{
			textNumber += 1;
			item_body_text.setVisibility(View.VISIBLE);
			SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, bodyList.get(position).getDescription());
			item_body_text.setText(spannableString);
			item_body_img.setVisibility(View.GONE);
		}
		return convertView;
	}

	
}