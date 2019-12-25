package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.version_93.MainToysCategoryActivity;
import com.yyqq.commen.model.ToysMainClassBean;
import com.yyqq.commen.model.ToysMainClassItemBean;
import com.yyqq.framework.application.MyApplication;

public class ToysLeaseHomeMainApapter extends BaseAdapter {

	private Activity context;
	private LayoutInflater inflater;
	private ArrayList<ToysMainClassBean> data;
	
	public ToysLeaseHomeMainApapter(Activity context, LayoutInflater inflater,ArrayList<ToysMainClassBean> data) {
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
		
		if(data.get(position).getShow_type().equals("1")){ // 一张图
			convertView = inflater.inflate(R.layout.item_toys_main_img, null);
			ImageView item_hont_iv_01 = (ImageView) convertView.findViewById(R.id.item_hont_iv_01);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getBusiness_pic(), item_hont_iv_01, R.drawable.def_image_toys);
			
			DisplayMetrics DM = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(DM);
			int windowWidth = DM.widthPixels;
			int img01 = (int) (windowWidth * 0.35);
			LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) item_hont_iv_01.getLayoutParams();
			imgParams.height = img01;
			imgParams.width = windowWidth;
			item_hont_iv_01.setLayoutParams(imgParams);
			
			item_hont_iv_01.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(0));
				}
			});
		}else if(data.get(position).getShow_type().equals("2")){ // 三张并列
			convertView = inflater.inflate(R.layout.item_toys_main_more, null);
			final LinearLayout item_hont_click_all = (LinearLayout) convertView.findViewById(R.id.item_hont_click_all);
			TextView item_hont_tv_title = (TextView) convertView.findViewById(R.id.item_hont_tv_title);
			TextView item_hont_tv_title_more = (TextView) convertView.findViewById(R.id.item_hont_tv_title_more);
			TextView item_hont_tv_01 = (TextView) convertView.findViewById(R.id.item_hont_tv_01);
			TextView item_hont_tv_02 = (TextView) convertView.findViewById(R.id.item_hont_tv_02);
			TextView item_hont_tv_03 = (TextView) convertView.findViewById(R.id.item_hont_tv_03);
			TextView item_hont_pr_01 = (TextView) convertView.findViewById(R.id.item_hont_pr_01);
			TextView item_hont_pr_02 = (TextView) convertView.findViewById(R.id.item_hont_pr_02);
			TextView item_hont_pr_03 = (TextView) convertView.findViewById(R.id.item_hont_pr_03);
			TextView item_hont_pr_01_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_01_unit);
			TextView item_hont_pr_02_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_02_unit);
			TextView item_hont_pr_03_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_03_unit);
			final ImageView item_hont_iv_01 = (ImageView) convertView.findViewById(R.id.item_hont_iv_01);
			final ImageView item_hont_iv_02 = (ImageView) convertView.findViewById(R.id.item_hont_iv_02);
			final ImageView item_hont_iv_03 = (ImageView) convertView.findViewById(R.id.item_hont_iv_03);

			MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getBusiness_pic(), item_hont_iv_01, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(1).getBusiness_pic(), item_hont_iv_02, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(2).getBusiness_pic(), item_hont_iv_03, R.drawable.def_image_toys);
			
			item_hont_tv_title.setText(data.get(position).getClass_title());
			item_hont_tv_title_more.setText(data.get(position).getClass_more_title()+"  ");
			item_hont_tv_01.setText(data.get(position).getItemBean().get(0).getBusiness_title());
			item_hont_tv_02.setText(data.get(position).getItemBean().get(1).getBusiness_title());
			item_hont_tv_03.setText(data.get(position).getItemBean().get(2).getBusiness_title());
			item_hont_pr_01.setText(data.get(position).getItemBean().get(0).getSell_price());
			item_hont_pr_02.setText(data.get(position).getItemBean().get(1).getSell_price());
			item_hont_pr_03.setText(data.get(position).getItemBean().get(2).getSell_price());
			item_hont_pr_01_unit.setText(data.get(position).getItemBean().get(0).getUnit_name());
			item_hont_pr_02_unit.setText(data.get(position).getItemBean().get(1).getUnit_name());
			item_hont_pr_03_unit.setText(data.get(position).getItemBean().get(2).getUnit_name());
			
			final RelativeLayout item_hont_click_01 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_01);
			RelativeLayout item_hont_click_02 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_02);
			RelativeLayout item_hont_click_03 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_03);
			RelativeLayout item_hont_click_04 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_04);
			
			item_hont_click_01.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(data.get(position).getCategory_id().equals("0")){
						ToysLeaseMainTabActivity.tabHost.setCurrentTab(1);
					}else{
						Intent intent = new Intent(context, MainToysCategoryActivity.class);
						intent.putExtra(MainToysCategoryActivity.CATEGORY_ID_KEY, data.get(position).getCategory_id());
						context.startActivity(intent);
					}
				}
			});
			
			item_hont_click_02.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(0));
				}
			});
			
			item_hont_click_03.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(1));
				}
			});
			
			item_hont_click_04.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(2));
				}
			});
			
			DisplayMetrics DM = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(DM);
			int windowWidth = DM.widthPixels;
			int img01 = (int) (windowWidth * 0.25)  ;
			LayoutParams img01Params = (LayoutParams) item_hont_iv_01.getLayoutParams();
			img01Params.height = img01  ;
			img01Params.width = img01;
			item_hont_iv_01.setLayoutParams(img01Params);
			item_hont_iv_02.setLayoutParams(img01Params);
			item_hont_iv_03.setLayoutParams(img01Params);
			
			// 调整高度，修正图片拉伸
//			item_hont_iv_01.getViewTreeObserver().addOnGlobalLayoutListener(  
//			
//				new OnGlobalLayoutListener() {  
//					@Override  
//					public void onGlobalLayout() {  
//						 int width  = item_hont_iv_01.getMeasuredWidth();
//						 LayoutParams changeParams = (LayoutParams) item_hont_iv_01.getLayoutParams();
//						 changeParams.height = width;
//						 changeParams.width = width;
//						 item_hont_iv_01.setLayoutParams(changeParams);
//						 item_hont_iv_02.setLayoutParams(changeParams);
//						 item_hont_iv_03.setLayoutParams(changeParams); 
//					}
//			});  
			
//			ViewTreeObserver vto = item_hont_iv_01.getViewTreeObserver(); 
//			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//			    @Override 
//			    public void onGlobalLayout() { 
//			        item_hont_iv_01.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//			       
//			    } 
//			});
        	
//        	LinearLayout.LayoutParams allParams = (LinearLayout.LayoutParams) item_hont_click_all.getLayoutParams();
//        	allParams.height = (int) (windowWidth * 0.4);
//        	allParams.width = windowWidth;
//        	item_hont_click_all.setLayoutParams(allParams);
			
		}else if(data.get(position).getShow_type().equals("3")){ // 一拖三
			convertView = inflater.inflate(R.layout.item_toys_main_hot, null);
			TextView item_hont_tv_title = (TextView) convertView.findViewById(R.id.item_hont_tv_title);
			TextView item_hont_tv_title_more = (TextView) convertView.findViewById(R.id.item_hont_tv_title_more);
			TextView item_hont_tv_01 = (TextView) convertView.findViewById(R.id.item_hont_tv_01);
			TextView item_hont_tv_02 = (TextView) convertView.findViewById(R.id.item_hont_tv_02);
			TextView item_hont_tv_03 = (TextView) convertView.findViewById(R.id.item_hont_tv_03);
			TextView item_hont_tv_04 = (TextView) convertView.findViewById(R.id.item_hont_tv_04);
			TextView item_hont_pr_01 = (TextView) convertView.findViewById(R.id.item_hont_pr_01);
			TextView item_hont_pr_02 = (TextView) convertView.findViewById(R.id.item_hont_pr_02);
			TextView item_hont_pr_03 = (TextView) convertView.findViewById(R.id.item_hont_pr_03);
			TextView item_hont_pr_04 = (TextView) convertView.findViewById(R.id.item_hont_pr_04);
			LinearLayout item_hont_all_right = (LinearLayout) convertView.findViewById(R.id.item_hont_all_right);
			TextView item_hont_pr_01_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_01_unit);
			TextView item_hont_pr_02_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_02_unit);
			TextView item_hont_pr_03_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_03_unit);
			TextView item_hont_pr_04_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_04_unit);
			
			item_hont_pr_01_unit.setText(data.get(position).getItemBean().get(0).getUnit_name());
			item_hont_pr_02_unit.setText(data.get(position).getItemBean().get(1).getUnit_name());
			item_hont_pr_03_unit.setText(data.get(position).getItemBean().get(2).getUnit_name());
			item_hont_pr_04_unit.setText(data.get(position).getItemBean().get(3).getUnit_name());
			
			final ImageView item_hont_iv_01 = (ImageView) convertView.findViewById(R.id.item_hont_iv_01);
			final ImageView item_hont_iv_02 = (ImageView) convertView.findViewById(R.id.item_hont_iv_02);
			final ImageView item_hont_iv_03 = (ImageView) convertView.findViewById(R.id.item_hont_iv_03);
			final ImageView item_hont_iv_04 = (ImageView) convertView.findViewById(R.id.item_hont_iv_04);
			
			MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getBusiness_pic(), item_hont_iv_01, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(1).getBusiness_pic(), item_hont_iv_02, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(2).getBusiness_pic(), item_hont_iv_03, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(3).getBusiness_pic(), item_hont_iv_04, R.drawable.def_image_toys);
			
			item_hont_tv_title.setText(data.get(position).getClass_title());
			item_hont_tv_title_more.setText(data.get(position).getClass_more_title()+"  ");
			
			RelativeLayout item_hont_click_01 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_01);
			RelativeLayout item_hont_click_02 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_02);
			RelativeLayout item_hont_click_03 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_03);
			RelativeLayout item_hont_click_04 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_04);
			RelativeLayout item_hont_click_05 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_05);
			
			item_hont_click_01.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(data.get(position).getCategory_id().equals("0")){
						ToysLeaseMainTabActivity.tabHost.setCurrentTab(1);
					}else{
						Intent intent = new Intent(context, MainToysCategoryActivity.class);
						intent.putExtra(MainToysCategoryActivity.CATEGORY_ID_KEY, data.get(position).getCategory_id());
						context.startActivity(intent);
					}
				}
			});
			
			item_hont_click_02.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(0));
				}
			});
			
			item_hont_click_03.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(1));
				}
			});
			
			item_hont_click_04.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(2));
				}
			});
			
			item_hont_click_05.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(3));
				}
			});
			
			DisplayMetrics DM = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(DM);
			int windowWidth = DM.widthPixels;
			
			int img01 = (int) (windowWidth * 0.35)  ;
			LayoutParams img01Params = (LayoutParams) item_hont_iv_01.getLayoutParams();
			img01Params.height = img01  ;
			img01Params.width = img01;
			item_hont_iv_01.setLayoutParams(img01Params);
			
			int img02 = (int) (windowWidth * 0.25)  ;
			LayoutParams img02Params = (LayoutParams) item_hont_iv_02.getLayoutParams();
			img02Params.height = img02  ;
			img02Params.width = img02;
			item_hont_iv_02.setLayoutParams(img02Params);
			
			int img03 = (int) (windowWidth * 0.25)  ;
			LayoutParams img03Params = (LayoutParams) item_hont_iv_03.getLayoutParams();
			img03Params.height = img03  ;
			img03Params.width = img03;
			item_hont_iv_03.setLayoutParams(img03Params);
			
			int img04 = (int) (windowWidth * 0.25)  ;
			LayoutParams img04Params = (LayoutParams) item_hont_iv_04.getLayoutParams();
			img04Params.height = img04  ;
			img04Params.width = img04;
			item_hont_iv_04.setLayoutParams(img04Params);
			
			// 调整高度，修正图片拉伸
//			item_hont_iv_01.getViewTreeObserver().addOnGlobalLayoutListener(  
//					
//					new OnGlobalLayoutListener() {  
//						@Override  
//						public void onGlobalLayout() {  
//							 int width  = item_hont_iv_01.getMeasuredWidth();
//							 LayoutParams changeParams = (LayoutParams) item_hont_iv_01.getLayoutParams();
//							 changeParams.height = width;
//							 changeParams.width = width;
//							 item_hont_iv_01.setLayoutParams(changeParams);
//						}
//			});  
			
//			ViewTreeObserver vto = item_hont_iv_01.getViewTreeObserver(); 
//			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//			    @Override 
//			    public void onGlobalLayout() { 
//			    	item_hont_iv_01.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//			        int width  = item_hont_iv_01.getMeasuredWidth();
//			        LayoutParams changeParams = (LayoutParams) item_hont_iv_01.getLayoutParams();
//			        changeParams.height = width;
//			        changeParams.width = width;
//					item_hont_iv_01.setLayoutParams(changeParams);
//			    } 
//			});
			
			// 调整高度，修正图片拉伸
//			ViewTreeObserver vto02 = item_hont_iv_02.getViewTreeObserver(); 
//			vto02.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//			    @Override 
//			    public void onGlobalLayout() { 
//			    	item_hont_iv_02.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//			        int width  = item_hont_iv_02.getMeasuredWidth();
//			        LayoutParams changeParams = (LayoutParams) item_hont_iv_02.getLayoutParams();
//			        changeParams.height = width;
//			        changeParams.width = width;
//					item_hont_iv_02.setLayoutParams(changeParams);
//			    } 
//			});
//			
//			// 调整高度，修正图片拉伸
//			ViewTreeObserver vto03 = item_hont_iv_03.getViewTreeObserver(); 
//			vto03.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//				@Override 
//				public void onGlobalLayout() { 
//					item_hont_iv_03.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//					int width  = item_hont_iv_03.getMeasuredWidth();
//					LayoutParams changeParams = (LayoutParams) item_hont_iv_03.getLayoutParams();
//					changeParams.height = width;
//					changeParams.width = width;
//					item_hont_iv_03.setLayoutParams(changeParams);
//				} 
//			});
//			
//			// 调整高度，修正图片拉伸
//			ViewTreeObserver vto04 = item_hont_iv_04.getViewTreeObserver(); 
//			vto04.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//				@Override 
//				public void onGlobalLayout() { 
//					item_hont_iv_04.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//					int width  = item_hont_iv_04.getMeasuredWidth();
//					LayoutParams changeParams = (LayoutParams) item_hont_iv_04.getLayoutParams();
//					changeParams.height = width;
//					changeParams.width = width;
//					item_hont_iv_04.setLayoutParams(changeParams);
//				} 
//			});
			
//			int all = (int) (windowWidth * 0.4);
//			android.widget.LinearLayout.LayoutParams allParams = (android.widget.LinearLayout.LayoutParams) item_hont_click_02.getLayoutParams();
//			allParams.width = all;
//			allParams.height = allParams.height;
//			item_hont_click_02.setLayoutParams(allParams);
			
			item_hont_tv_01.setText(data.get(position).getItemBean().get(0).getBusiness_title());
			item_hont_tv_02.setText(data.get(position).getItemBean().get(1).getBusiness_title());
			item_hont_tv_03.setText(data.get(position).getItemBean().get(2).getBusiness_title());
			item_hont_tv_04.setText(data.get(position).getItemBean().get(3).getBusiness_title());
			
			item_hont_pr_01.setText(data.get(position).getItemBean().get(0).getSell_price());
			item_hont_pr_02.setText(data.get(position).getItemBean().get(1).getSell_price());
			item_hont_pr_03.setText(data.get(position).getItemBean().get(2).getSell_price());
			item_hont_pr_04.setText(data.get(position).getItemBean().get(3).getSell_price());
			
			LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) item_hont_all_right.getLayoutParams();
			rightParams.width = rightParams.width;
			rightParams.height = (int) (windowWidth * 0.65)  ;
			item_hont_all_right.setLayoutParams(rightParams);
			
		}else if(data.get(position).getShow_type().equals("4")){ // 四张两行
			convertView = inflater.inflate(R.layout.item_toys_main_new, null);
			TextView item_hont_tv_title = (TextView) convertView.findViewById(R.id.item_hont_tv_title);
			TextView item_hont_tv_title_more = (TextView) convertView.findViewById(R.id.item_hont_tv_title_more);
			TextView item_hont_tv_01 = (TextView) convertView.findViewById(R.id.item_hont_tv_01);
			TextView item_hont_tv_02 = (TextView) convertView.findViewById(R.id.item_hont_tv_02);
			TextView item_hont_tv_03 = (TextView) convertView.findViewById(R.id.item_hont_tv_03);
			TextView item_hont_tv_04 = (TextView) convertView.findViewById(R.id.item_hont_tv_04);
			TextView item_hont_pr_01 = (TextView) convertView.findViewById(R.id.item_hont_pr_01);
			TextView item_hont_pr_02 = (TextView) convertView.findViewById(R.id.item_hont_pr_02);
			TextView item_hont_pr_03 = (TextView) convertView.findViewById(R.id.item_hont_pr_03);
			TextView item_hont_pr_04 = (TextView) convertView.findViewById(R.id.item_hont_pr_04);
			final ImageView item_hont_iv_01 = (ImageView) convertView.findViewById(R.id.item_hont_iv_01);
			final ImageView item_hont_iv_02 = (ImageView) convertView.findViewById(R.id.item_hont_iv_02);
			final ImageView item_hont_iv_03 = (ImageView) convertView.findViewById(R.id.item_hont_iv_03);
			final ImageView item_hont_iv_04 = (ImageView) convertView.findViewById(R.id.item_hont_iv_04);
			TextView item_hont_pr_01_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_01_unit);
			TextView item_hont_pr_02_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_02_unit);
			TextView item_hont_pr_03_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_03_unit);
			TextView item_hont_pr_04_unit = (TextView) convertView.findViewById(R.id.item_hont_pr_04_unit);
			
			item_hont_pr_01_unit.setText(data.get(position).getItemBean().get(0).getUnit_name());
			item_hont_pr_02_unit.setText(data.get(position).getItemBean().get(1).getUnit_name());
			item_hont_pr_03_unit.setText(data.get(position).getItemBean().get(2).getUnit_name());
			item_hont_pr_04_unit.setText(data.get(position).getItemBean().get(3).getUnit_name());
			MyApplication.getInstance().display(data.get(position).getItemBean().get(0).getBusiness_pic(), item_hont_iv_01, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(1).getBusiness_pic(), item_hont_iv_02, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(2).getBusiness_pic(), item_hont_iv_03, R.drawable.def_image_toys);
			MyApplication.getInstance().display(data.get(position).getItemBean().get(3).getBusiness_pic(), item_hont_iv_04, R.drawable.def_image_toys);
			
			item_hont_tv_title.setText(data.get(position).getClass_title());
			item_hont_tv_title_more.setText(data.get(position).getClass_more_title()+"  ");
			
			item_hont_pr_01.setText(data.get(position).getItemBean().get(0).getSell_price());
			item_hont_pr_02.setText(data.get(position).getItemBean().get(1).getSell_price());
			item_hont_pr_03.setText(data.get(position).getItemBean().get(2).getSell_price());
			item_hont_pr_04.setText(data.get(position).getItemBean().get(3).getSell_price());
			
			final RelativeLayout item_hont_click_02 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_02);
			RelativeLayout item_hont_click_01 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_01);
			RelativeLayout item_hont_click_03 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_03);
			RelativeLayout item_hont_click_04 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_04);
			RelativeLayout item_hont_click_05 = (RelativeLayout) convertView.findViewById(R.id.item_hont_click_05);
			
			item_hont_click_01.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(data.get(position).getCategory_id().equals("0")){
						ToysLeaseMainTabActivity.tabHost.setCurrentTab(1);
					}else{
						Intent intent = new Intent(context, MainToysCategoryActivity.class);
						intent.putExtra(MainToysCategoryActivity.CATEGORY_ID_KEY, data.get(position).getCategory_id());
						context.startActivity(intent);
					}
				}
			});
			
			item_hont_click_02.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(0));
				}
			});
			
			item_hont_click_03.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(1));
				}
			});
			
			item_hont_click_04.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(2));
				}
			});
			
			item_hont_click_05.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentConsole(data.get(position).getItemBean().get(3));
				}
			});
			
			
			DisplayMetrics DM = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(DM);
			int windowWidth = DM.widthPixels;
			int newH = (int) (windowWidth * 0.3);
			
//			// 初始化文字间距
//			LayoutParams imgParmams = (LayoutParams) item_hont_tv_01.getLayoutParams();
//			imgParmams.rightMargin = (int) (windowWidth * 0.2);
//			item_hont_tv_01.setLayoutParams(imgParmams);
//			item_hont_tv_02.setLayoutParams(imgParmams);
//			item_hont_tv_03.setLayoutParams(imgParmams);
//			item_hont_tv_04.setLayoutParams(imgParmams);
			
        	// 初始化图片高度
        	RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) item_hont_iv_01.getLayoutParams();
        	imgParams.height = newH;
        	imgParams.width = newH;
        	item_hont_iv_01.setLayoutParams(imgParams);
        	item_hont_iv_02.setLayoutParams(imgParams);
        	item_hont_iv_03.setLayoutParams(imgParams);
        	item_hont_iv_04.setLayoutParams(imgParams);
            
        	// 初始化列表高度
        	android.widget.LinearLayout.LayoutParams allParmms = (android.widget.LinearLayout.LayoutParams) item_hont_click_02.getLayoutParams();
        	allParmms.width = (int) (windowWidth * 0.5);
        	allParmms.height = (int) (windowWidth * 0.3)  ;
        	item_hont_click_02.setLayoutParams(allParmms);
        	item_hont_click_03.setLayoutParams(allParmms);
        	item_hont_click_04.setLayoutParams(allParmms);
        	item_hont_click_05.setLayoutParams(allParmms);
        	
        	item_hont_tv_01.setText(data.get(position).getItemBean().get(0).getBusiness_title());
			item_hont_tv_02.setText(data.get(position).getItemBean().get(1).getBusiness_title());
			item_hont_tv_03.setText(data.get(position).getItemBean().get(2).getBusiness_title());
			item_hont_tv_04.setText(data.get(position).getItemBean().get(3).getBusiness_title());
        	
//        	RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) item_hont_tv_01.getLayoutParams();
//        	int oldWidth = textParams.width;
//        	int oldHeight = textParams.height;
//        	textParams.width = oldWidth - newH;
//        	textParams.width = oldHeight;
//        	item_hont_tv_01.setLayoutParams(textParams);
//        	item_hont_tv_02.setLayoutParams(textParams);
//        	item_hont_tv_03.setLayoutParams(textParams);
//        	item_hont_tv_04.setLayoutParams(textParams);
//        	item_hont_tv_01.setText(data.get(position).getItemBean().get(0).getBusiness_title());
//			item_hont_tv_02.setText(data.get(position).getItemBean().get(1).getBusiness_title());
//			item_hont_tv_03.setText(data.get(position).getItemBean().get(2).getBusiness_title());
//			item_hont_tv_04.setText(data.get(position).getItemBean().get(3).getBusiness_title());
        	
        	// 调整高度，修正图片拉伸
//        	item_hont_iv_01.getViewTreeObserver().addOnGlobalLayoutListener(  
//        			
//    				new OnGlobalLayoutListener() {  
//    					@Override  
//    					public void onGlobalLayout() {  
//    						 int width  = item_hont_iv_01.getMeasuredWidth();
//    						 LayoutParams changeParams = (LayoutParams) item_hont_iv_01.getLayoutParams();
//    						 changeParams.height = width-10;
//    						 changeParams.width = width-10;
//    						 item_hont_iv_01.setLayoutParams(changeParams);
//    						 item_hont_iv_02.setLayoutParams(changeParams);
//    						 item_hont_iv_03.setLayoutParams(changeParams); 
//    						 item_hont_iv_04.setLayoutParams(changeParams);
//    					}
//    			});  
//        	ViewTreeObserver vto01 = item_hont_iv_01.getViewTreeObserver(); 
//        	vto01.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//        		@Override 
//        		public void onGlobalLayout() { 
//        			item_hont_iv_01.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//        			int width  = item_hont_iv_01.getMeasuredWidth();
//        			LayoutParams changeParams = (LayoutParams) item_hont_iv_01.getLayoutParams();
//        			changeParams.height = width;
//        			changeParams.width = width;
//        			item_hont_iv_01.setLayoutParams(changeParams);
//        			item_hont_iv_02.setLayoutParams(changeParams);
//        			item_hont_iv_03.setLayoutParams(changeParams);
//        			item_hont_iv_04.setLayoutParams(changeParams);
//        		} 
//        	});
		}
		
		return convertView;
	}
	
	/**
	 * 详情/外链
	 * */ 
	public void IntentConsole(ToysMainClassItemBean bean){
		if(bean.getSource().equals("1")){ // 玩具详情
			Intent in = new Intent(context, ToysLeaseDetailActivity.class);
			in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, bean.getBusiness_id());
			context.startActivity(in);
		}else if(bean.getSource().equals("3")){// 外链
			Intent in = new Intent(context, MainItemDetialWebActivity.class);
			in.putExtra(MainItemDetialWebActivity.LINK_URL, bean.getWeb_link());
			context.startActivity(in);
		}else if(bean.getSource().equals("2")){// 列表
			if(bean.getBusiness_id().equals("0")){
				ToysLeaseMainTabActivity.tabHost.setCurrentTab(1);
			}else{
				Intent in = new Intent(context, MainToysCategoryActivity.class);
				in.putExtra(MainToysCategoryActivity.CATEGORY_ID_KEY, bean.getBusiness_id());
				context.startActivity(in);
			}
		}
		
	}
	
}