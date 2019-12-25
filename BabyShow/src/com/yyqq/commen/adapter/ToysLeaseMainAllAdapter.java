package com.yyqq.commen.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.h5.ToysLeaseHtmlInterstActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainAllActivity;
import com.yyqq.commen.model.ToysLeaseMainListBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class ToysLeaseMainAllAdapter extends BaseAdapter {

	private AbHttpUtil ab;
	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<ToysLeaseMainListBean> data = null;
	private int showType = 0;  // 0 = 待租 ，1 = 已租，-1 = 自己
	private int widthItem = 0;
	int numberIndex01;
	int numberIndex02;
	
	public ToysLeaseMainAllAdapter(Activity context, LayoutInflater inflater,
			MyApplication app, ArrayList<ToysLeaseMainListBean> data, int showType, AbHttpUtil ab, int widthItem) {
		super();
		this.ab = ab;
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.data = data;
		this.showType = showType;
		this.widthItem = widthItem;
	}

	@Override
	public int getCount() {
		if(data.size() != 1){
			if(data.size() == 0){
				return 0;
			}else{
				return data.size() % 2 > 0 ? data.size() / 2 + 1 : data.size() / 2;
			}
		}else{
			return 1;
		}
	}

	@Override
	public Object getItem(int numberIndex) {
		return data.get(numberIndex);
	}

	@Override
	public long getItemId(int numberIndex) {
		return numberIndex;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		if(showType == 0){
		
		numberIndex01 = position;
		if(position != 0){
			numberIndex01 += position;
		}else{
			numberIndex01 = position;
		}
		numberIndex02 = numberIndex01+1;
		
//		if(!numberIndex01 > data.size())
		
			convertView = inflater.inflate(R.layout.item_toys_all_list, null);
			ImageView lease_item_img = (ImageView) convertView.findViewById(R.id.lease_item_img);
			TextView lease_item_name = (TextView) convertView.findViewById(R.id.lease_item_name);
			TextView lease_item_monery = (TextView) convertView.findViewById(R.id.lease_item_monery);
			ImageView category_icon_url = (ImageView) convertView.findViewById(R.id.category_icon_url);
			TextView item_toys_all_company = (TextView) convertView.findViewById(R.id.item_toys_all_company);
			TextView item_toys_all_age = (TextView) convertView.findViewById(R.id.item_toys_all_age);
			LinearLayout item_lease_main_all = (LinearLayout) convertView.findViewById(R.id.item_lease_main_all);
			
			ImageView lease_item_img_02 = (ImageView) convertView.findViewById(R.id.lease_item_img_02);
			TextView lease_item_name_02 = (TextView) convertView.findViewById(R.id.lease_item_name_02);
			TextView lease_item_monery_02 = (TextView) convertView.findViewById(R.id.lease_item_monery_02);
			ImageView category_icon_url_02 = (ImageView) convertView.findViewById(R.id.category_icon_url_02);
			TextView item_toys_all_company_02 = (TextView) convertView.findViewById(R.id.item_toys_all_company_02);
			TextView item_toys_all_age_02 = (TextView) convertView.findViewById(R.id.item_toys_all_age_02);
			LinearLayout item_lease_main_all_02 = (LinearLayout) convertView.findViewById(R.id.item_lease_main_all_02);
			
			MyApplication.getInstance().display(data.get(numberIndex01).getImgUrl(), lease_item_img, R.drawable.def_image_toys);
			lease_item_name.setText(data.get(numberIndex01).getLeaseName());
			lease_item_monery.setText(data.get(numberIndex01).getLeaseMonery());
			item_toys_all_company.setText(data.get(numberIndex01).getCompany());
			item_toys_all_age.setText(data.get(numberIndex01).getAge());
			
			// 分类标签
			if(data.get(numberIndex01).getCategory_icon_url().equals("")){
				category_icon_url.setVisibility(View.GONE);
			}else{
				category_icon_url.setVisibility(View.VISIBLE);
				MyApplication.getInstance().display(data.get(numberIndex01).getCategory_icon_url(), category_icon_url, R.drawable.def_image_toys);
			}
			
			item_lease_main_all.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent in = new Intent(context, ToysLeaseDetailActivity.class);
//					if(position != 0){
//						in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position + 1).getBusiness_id());
//					}else{
						in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position*2).getBusiness_id());
//					}
					context.startActivity(in);
				}
			});
			
			
			if(numberIndex02 < data.size()){
				MyApplication.getInstance().display(data.get(numberIndex02).getImgUrl(), lease_item_img_02, R.drawable.def_image_toys);
				lease_item_name_02.setText(data.get(numberIndex02).getLeaseName());
				lease_item_monery_02.setText(data.get(numberIndex02).getLeaseMonery());
				item_toys_all_company_02.setText(data.get(numberIndex02).getCompany());
				item_toys_all_age_02.setText(data.get(numberIndex02).getAge());
				
				// 分类标签
				if(data.get(numberIndex02).getCategory_icon_url().equals("")){
					category_icon_url_02.setVisibility(View.GONE);
				}else{
					category_icon_url_02.setVisibility(View.VISIBLE);
					MyApplication.getInstance().display(data.get(numberIndex02).getCategory_icon_url(), category_icon_url_02, R.drawable.def_image_toys);
				}
				
				item_lease_main_all_02.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Intent in = new Intent(context, ToysLeaseDetailActivity.class);
//						if(position != 0){
//							in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position + 2).getBusiness_id());
//						}else{
							in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position*2+1).getBusiness_id());
//						}
						context.startActivity(in);
					}
				});
			}else{
				item_lease_main_all_02.setVisibility(View.INVISIBLE);
			}
			
			LayoutParams imgParams = lease_item_img.getLayoutParams();
			imgParams.width = widthItem;
			imgParams.height = widthItem;
			lease_item_img.setLayoutParams(imgParams);
			lease_item_img_02.setLayoutParams(imgParams);
			
		return convertView;
	}
	

	/**
	 * 预约/取消预约提示框
	 * */
	public void changeIntereset(final Context context, final boolean isInterest, final ToysLeaseMainListBean bean){
		
		if(!MyApplication.getVisitor().equals("0")  || Config.getUser(context).uid.equals("")){
			Intent in = new Intent(context, WebLoginActivity.class);
			context.startActivity(in);
			return;
		}
		
		AlertDialog.Builder dialog = new Builder(context);
		dialog.setTitle("预约提示");
		if(isInterest){
			dialog.setMessage("确定预约这个宝贝么？");
		}else{
			dialog.setMessage("确定不预约了么？");
		}
		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isInterest){
							toInterest(context, bean);
						}else{
							cancelInterest(context, bean);
						}
					}});
		
		dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {}});
		dialog.create().show();
	}
	
	/**
	 * 预约玩具
	 * */
	private void toInterest(final Context con, final ToysLeaseMainListBean bean) {
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(con).uid);
		params.put("business_id", bean.getBusiness_id());
		ab.get(ServerMutualConfig.TOYS_TO_INTEREST + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						bean.setIs_cart("2");
						notifyDataSetChanged();
						AlertDialog.Builder dialog = new Builder(con);
						dialog.setTitle("预约提示");
						dialog.setMessage("预约成功！查看预约请到“订单-我的预约”");
						dialog.setNegativeButton("好的", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										
									}});
						dialog.setPositiveButton("去看看", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								con.startActivity(new Intent(con, ToysLeaseHtmlInterstActivity.class));
							}});
						dialog.create().show();
					}else{
						Toast.makeText(con, new JSONObject(content).getString("reMsg"), 2).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				Config.dismissProgress();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
		
	}
	
	
	/**
	 * 取消预约玩具
	 * */
	private void cancelInterest(final Context con, final ToysLeaseMainListBean bean) {
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(con).uid);
		params.put("business_id", bean.getBusiness_id());
		ab.get(ServerMutualConfig.TOYS_CANCEL_INTEREST + "&" + params.toString(),new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						bean.setIs_cart("1");
						notifyDataSetChanged();
						AlertDialog.Builder dialog = new Builder(con);
						dialog.setTitle("预约提示");
						dialog.setMessage("已取消！查看预约请到“订单-我的预约”");
						dialog.setNegativeButton("好的", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										
									}});
						dialog.setPositiveButton("去看看", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								con.startActivity(new Intent(con, ToysLeaseHtmlInterstActivity.class));
							}});
						dialog.create().show();
					}else{
						Toast.makeText(con, new JSONObject(content).getString("reMsg"), 2).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				Config.dismissProgress();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
		
	}
	
	

}
