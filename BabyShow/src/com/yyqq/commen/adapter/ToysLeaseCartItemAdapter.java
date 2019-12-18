package com.yyqq.commen.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.commen.model.ToysLeaseCartItemBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 购物车玩具item
 * */
public class ToysLeaseCartItemAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysLeaseCartItemBean> data = null;
	
	public ToysLeaseCartItemAdapter(Activity context, LayoutInflater inflater, ArrayList<ToysLeaseCartItemBean> data) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
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
		convertView = inflater.inflate(R.layout.item_cart_type03, null);
		final View all = convertView;
		ImageView item_cart_select = (ImageView) convertView.findViewById(R.id.item_cart_select);
		ImageView item_cart_img = (ImageView) convertView.findViewById(R.id.item_cart_img);
		TextView item_cart_name = (TextView) convertView.findViewById(R.id.item_cart_name);
		TextView item_cart_price = (TextView) convertView.findViewById(R.id.item_cart_price);
		TextView item_toys_order_hint = (TextView) convertView.findViewById(R.id.item_toys_order_hint);
		ImageView item_toys_type_icon = (ImageView) convertView.findViewById(R.id.item_toys_type_icon);
		RelativeLayout item_cart_select_parent = (RelativeLayout) convertView.findViewById(R.id.item_cart_select_parent);
		
		MyApplication.getInstance().display(data.get(position).getImg_thumb(), item_cart_img, R.drawable.def_image);
		item_cart_name.setText(data.get(position).getBusiness_title());
		item_cart_price.setText(data.get(position).getEvery_price());
		item_toys_order_hint.setText(data.get(position).getDescription());
		
		// 是否显示小图标
		if(!data.get(position).getSmall_img_thumb().isEmpty()){
			MyApplication.getInstance().display(data.get(position).getSmall_img_thumb(), item_toys_type_icon, R.drawable.def_image);
			item_toys_type_icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ToysLeaseMainShoppingCartActivity.vip_hint.setVisibility(View.VISIBLE);
				}
			});
		}else{
			item_toys_type_icon.setVisibility(View.GONE);
		}
		
		// 是否被选中
		if(data.get(position).getCheck_state().equals("1")){
			item_cart_select.setBackgroundResource(R.drawable.select_down);
		}else{
			item_cart_select.setBackgroundResource(R.drawable.select_up);
		}
		
		// 是否可选
		if(data.get(position).getIs_order().equals("0")){
			item_cart_select_parent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					updateCartType(data.get(position), position, all);
				}
			});
		}else{
			item_cart_select_parent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder dialog = new Builder(context);
					dialog.setMessage(data.get(position).getDescription());
					dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
						
					});
					dialog.create().show();
				}
			});
		}
			
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ToysLeaseDetailActivity.class);
				intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position).getBusiness_id());
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	/**
	 * 修改购物车状态
	 * */
	private void updateCartType(final ToysLeaseCartItemBean bean, final int position, final View convertView){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("cart_id", bean.getCart_id());
		params.put("order_id", bean.getOrder_id());
		AbHttpUtil.getInstance(context).get(ServerMutualConfig.UPDATE_CART_TYPE + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						data.get(position).setBusiness_id(new JSONObject(content).getJSONObject("data").getString("business_id"));
						data.get(position).setBusiness_title(new JSONObject(content).getJSONObject("data").getString("business_title"));
						data.get(position).setCart_id(new JSONObject(content).getJSONObject("data").getString("cart_id"));
						data.get(position).setCheck_state(new JSONObject(content).getJSONObject("data").getString("check_state"));
						data.get(position).setDescription(new JSONObject(content).getJSONObject("data").getString("description"));
						data.get(position).setEvery_price(new JSONObject(content).getJSONObject("data").getString("every_price"));
						data.get(position).setImg_thumb(new JSONObject(content).getJSONObject("data").getString("img_thumb"));
						data.get(position).setIs_order(new JSONObject(content).getJSONObject("data").getString("is_order"));
						data.get(position).setOrder_id(new JSONObject(content).getJSONObject("data").getString("order_id"));
						data.get(position).setSmall_img_thumb(new JSONObject(content).getJSONObject("data").getString("new_size_img_thumb"));
						data.get(position).setClose_order_des(new JSONObject(content).getJSONObject("data").getString("close_order_des"));
						
						ImageView item_cart_select = (ImageView) convertView.findViewById(R.id.item_cart_select);
						TextView item_cart_name = (TextView) convertView.findViewById(R.id.item_cart_name);
						TextView item_cart_price = (TextView) convertView.findViewById(R.id.item_cart_price);
						TextView item_toys_order_hint = (TextView) convertView.findViewById(R.id.item_toys_order_hint);
						ImageView item_toys_type_icon = (ImageView) convertView.findViewById(R.id.item_toys_type_icon);
						
						item_cart_name.setText(data.get(position).getBusiness_title());
						item_cart_price.setText(data.get(position).getEvery_price());
						item_toys_order_hint.setText(data.get(position).getDescription());
						
						if(!data.get(position).getClose_order_des().isEmpty()){
							AlertDialog.Builder dialog = new Builder(context);
							dialog.setMessage(data.get(position).getClose_order_des());
							dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
								
							});
							dialog.create().show();
						}
						
						// 是否显示小图标
						if(!data.get(position).getSmall_img_thumb().isEmpty()){
							MyApplication.getInstance().display(data.get(position).getSmall_img_thumb(), item_toys_type_icon, R.drawable.def_image);
						}else{
							item_toys_type_icon.setVisibility(View.GONE);
						}
						
						// 是否被选中
						if(data.get(position).getCheck_state().equals("1")){
							item_cart_select.setBackgroundResource(R.drawable.select_down);
						}else{
							item_cart_select.setBackgroundResource(R.drawable.select_up);
						}
						
						// 更新购物车底部总计金额
						ToysLeaseMainShoppingCartActivity.changeCartBottomPage(context, new JSONObject(content).getJSONObject("data").getString("total_title"), new JSONObject(content).getJSONObject("data").getString("total_price"), new JSONObject(content).getJSONObject("data").getString("is_button"));
					}else{
						Toast.makeText(context, new JSONObject(content).getString("reMsg"), 3).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}
	
}
