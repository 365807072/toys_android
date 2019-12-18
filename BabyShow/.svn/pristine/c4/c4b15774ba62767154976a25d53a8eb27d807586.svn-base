package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.MycardBean;
import com.yyqq.framework.application.MyApplication;

public class ToysMyCardAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<MycardBean> MycardBeanList;
	
	private DisplayMetrics metric;
	
	public ToysMyCardAdapter(Activity context, ArrayList<MycardBean> cardBeanList) {
		super();
		this.context = context;
		this.MycardBeanList = cardBeanList;
	}

	@Override
	public int getCount() {
		return MycardBeanList.size();
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
		convertView = inflater.inflate(R.layout.item_my_card_list, null);
		
		TextView item_search_hint_text = (TextView) convertView.findViewById(R.id.item_mycard_name);
		item_search_hint_text.setText(MycardBeanList.get(position).getCard_info());

		TextView item_search_hint_des = (TextView) convertView.findViewById(R.id.item_mycard_des);
		item_search_hint_des.setText(MycardBeanList.get(position).getStatus_name()+" "+MycardBeanList.get(position).getCard_id_title());
		
		final ImageView item_search_hint_img = (ImageView) convertView.findViewById(R.id.item_mycard_img);
//		item_search_hint_img.setImageBitmap(returnBitMap(MycardBeanList.get(position).getBusiness_pic()));
//		item_search_hint_img.setImageURL(MycardBeanList.get(position).getBusiness_pic());
		MyApplication.getInstance().display(MycardBeanList.get(position).getBusiness_pic(), item_search_hint_img, R.drawable.def_image);
		item_search_hint_img.setScaleType(ScaleType.FIT_XY);
		
		item_search_hint_img.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						metric = new DisplayMetrics();
						WindowManager mWindowManager  = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
						mWindowManager.getDefaultDisplay().getMetrics(metric);
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) item_search_hint_img.getLayoutParams();
						int newHeight = (int)(params.height * 0.5);
						params.height = 300;
						item_search_hint_img.setLayoutParams(params);
					}
				});
		
		return convertView;
	}
	
}