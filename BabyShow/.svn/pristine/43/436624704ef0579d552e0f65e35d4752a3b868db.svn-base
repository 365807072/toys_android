package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyqq.babyshow.R;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.model.MainItemCommentBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;

public class MainItemCommentPhotoAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	public MainItemCommentBean commentBean = null;
	private DisplayMetrics DM;

	public MainItemCommentPhotoAdapter(Activity context, MainItemCommentBean commentBean, MyApplication app) {
		super();
		this.context = context;
		this.app = app;
		this.commentBean = commentBean;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DM = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(DM);
	}

	@Override
	public int getCount() {
		return this.commentBean.getImg().size() > 4 ? 4 : this.commentBean.getImg().size();
	}

	@Override
	public Object getItem(int position) {
		return this.commentBean.getImg().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_main_item_comment, null);
		ImageView main_item_comment_img = (ImageView) convertView.findViewById(R.id.main_item_comment_img);
		app.display(commentBean.getImg().get(position).getImg_thumb(), main_item_comment_img, R.drawable.deft_color);
		
		float hi = (DM.widthPixels - Config.getSize("100")) / 4;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int)hi,(int)hi);
//		Log.e("fanfan", DM.widthPixels + "----" + hi);
		main_item_comment_img.setLayoutParams(param);
		
		if(commentBean.getImg().size() != 0){
			
			final ArrayList<String> imgList = new ArrayList<String>();
			for(int i = 0 ; i < commentBean.getImg().size() ; i++){
				imgList.add(commentBean.getImg().get(i).getImg_thumb());
			}
			
			main_item_comment_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					intent.putExtra("imgList", imgList);
					intent.putExtra("isBuy", "isBuy");
					context.startActivity(intent);
				}
			});
		}
		
		
		return convertView;
	}
}
