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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class ToysLeaseAllAdapter extends BaseAdapter {

	private AbHttpUtil ab;
	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<ToysLeaseMainListBean> data = null;
	private int showType = 0;  // 0 = 待租 ，1 = 已租，-1 = 自己
	
	public ToysLeaseAllAdapter(Activity context, LayoutInflater inflater,
			MyApplication app, ArrayList<ToysLeaseMainListBean> data, int showType, AbHttpUtil ab) {
		super();
		this.ab = ab;
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.data = data;
		this.showType = showType;
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() : 0;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
//		if(showType == 0){
			convertView = inflater.inflate(R.layout.item_lease_main, null);
			final RelativeLayout item_lease_main_all = (RelativeLayout) convertView.findViewById(R.id.item_lease_main_all);
			final ImageView lease_item_img = (ImageView) convertView.findViewById(R.id.lease_item_img);
			TextView lease_item_name = (TextView) convertView.findViewById(R.id.lease_item_name);
			ImageView lease_user_icon = (ImageView) convertView.findViewById(R.id.lease_user_icon);
			TextView lease_user_name = (TextView) convertView.findViewById(R.id.lease_user_name);
			TextView lease_item_support = (TextView) convertView.findViewById(R.id.lease_item_suppor);
//			ImageView lease_item_type = (ImageView) convertView.findViewById(R.id.lease_item_type);
			TextView lease_item_monery = (TextView) convertView.findViewById(R.id.lease_item_monery);
			TextView lease_item_state = (TextView) convertView.findViewById(R.id.lease_item_state);
			final ImageView lease_item_add = (ImageView) convertView.findViewById(R.id.lease_item_add);
			ImageView category_icon_url = (ImageView) convertView.findViewById(R.id.category_icon_url);
			
			// 分类标签
			if(data.get(position).getCategory_icon_url().isEmpty()){
				category_icon_url.setVisibility(View.GONE);
			}else{
				category_icon_url.setVisibility(View.VISIBLE);
//				category_icon_url.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.list_icon_02));
				MyApplication.getInstance().display(data.get(position).getCategory_icon_url(), category_icon_url, R.drawable.def_image);
			}
			
			MyApplication.getInstance().display(data.get(position).getImgUrl(), lease_item_img, R.drawable.def_image);
			lease_item_name.setText(data.get(position).getLeaseName());
			MyApplication.getInstance().display(data.get(position).getUserIconUrl(), lease_user_icon, R.drawable.def_head);
			lease_user_name.setText(data.get(position).getUserName());
			lease_item_support.setText(data.get(position).getLeaseStateName());
			
			lease_item_monery.setText(data.get(position).getLeaseMonery());
			
//			if(data.get(position).getLeaseType().equals("1")){
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_zu));
//			}else{
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_mai));
//			}
			
			
			if(data.get(position).getIs_cart().equals("0")){ // 可租
				lease_item_add.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.add_to_cart));
				lease_item_add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ShoppingCartUtils.addToysToCart(context, data.get(position).getBusiness_id(), lease_item_add, ToysLeaseMainAllActivity.toys_lease_main_all, lease_item_img);
					}
				});
			}else if(data.get(position).getIs_cart().equals("1")){ // 可预约
				lease_item_add.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.add_to_interst));
				lease_item_add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeIntereset(context, true, data.get(position));
					}
				});
			}else if(data.get(position).getIs_cart().equals("2")){ // 取消预约
				lease_item_add.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.add_to_interst_no));
				lease_item_add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeIntereset(context, false, data.get(position));
					}
				});
			}else{ // 其他（隐藏）
				lease_item_add.setVisibility(View.GONE);
			}
			
			
//		}else if(showType == 1){
//			convertView = inflater.inflate(R.layout.item_lease_main, null);
//			ImageView lease_item_img = (ImageView) convertView.findViewById(R.id.lease_item_img);
//			TextView lease_item_name = (TextView) convertView.findViewById(R.id.lease_item_name);
//			ImageView lease_user_icon = (ImageView) convertView.findViewById(R.id.lease_user_icon);
//			TextView lease_user_name = (TextView) convertView.findViewById(R.id.lease_user_name);
//			TextView lease_item_support = (TextView) convertView.findViewById(R.id.lease_item_suppor);
//			ImageView lease_item_type = (ImageView) convertView.findViewById(R.id.lease_item_type);
//			TextView lease_item_monery = (TextView) convertView.findViewById(R.id.lease_item_monery);
//			TextView lease_item_state = (TextView) convertView.findViewById(R.id.lease_item_state);
//			
//			MyApplication.getInstance().display(data.get(position).getImgUrl(), lease_item_img, R.drawable.def_image);
//			lease_item_name.setText(data.get(position).getLeaseName());
//			MyApplication.getInstance().display(data.get(position).getUserIconUrl(), lease_user_icon, R.drawable.def_head);
//			lease_user_name.setText(data.get(position).getUserName());
//			lease_item_support.setText(data.get(position).getLeaseStateName());
//			
//			if(data.get(position).getLeaseType().equals("1")){
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_zu));
//			}else{
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_mai));
//			}
//			
//			lease_item_monery.setText(data.get(position).getLeaseMonery());
//		}else if(showType == -1){
//			convertView = inflater.inflate(R.layout.item_lease_main, null);
//			ImageView lease_item_img = (ImageView) convertView.findViewById(R.id.lease_item_img);
//			TextView lease_item_name = (TextView) convertView.findViewById(R.id.lease_item_name);
//			ImageView lease_user_icon = (ImageView) convertView.findViewById(R.id.lease_user_icon);
//			TextView lease_user_name = (TextView) convertView.findViewById(R.id.lease_user_name);
//			ImageView lease_item_type = (ImageView) convertView.findViewById(R.id.lease_item_type);
//			TextView lease_item_monery = (TextView) convertView.findViewById(R.id.lease_item_monery);
//			TextView lease_item_state = (TextView) convertView.findViewById(R.id.lease_item_state);
//			
//			MyApplication.getInstance().display(data.get(position).getImgUrl(), lease_item_img, R.drawable.def_image);
//			lease_item_name.setText(data.get(position).getLeaseName());
//			MyApplication.getInstance().display(data.get(position).getUserIconUrl(), lease_user_icon, R.drawable.def_head);
//			lease_user_name.setText(data.get(position).getUserName());
//			
//			if(data.get(position).getLeaseType().equals("1")){
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_zu));
//			}else{
//				lease_item_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_mai));
//			}
//			
//			lease_item_monery.setText(data.get(position).getLeaseMonery());
//			lease_item_state.setText(data.get(position).getOrder_state());
//		}
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
