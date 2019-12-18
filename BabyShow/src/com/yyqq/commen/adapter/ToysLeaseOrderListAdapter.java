package com.yyqq.commen.adapter;

import java.util.ArrayList;

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
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderDetailActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainOrderActivity;
import com.yyqq.commen.model.ToysOrderItemBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 订单列表-（二级列表）
 * */
public class ToysLeaseOrderListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysOrderItemBean> data = null;
	private AbHttpUtil ab;
	private int comBiendIndex = -1;

	public ToysLeaseOrderListAdapter(Activity context, LayoutInflater inflater,
			ArrayList<ToysOrderItemBean> data, AbHttpUtil ab, int comBiendIndex) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
		this.ab = ab;
		this.comBiendIndex = comBiendIndex;
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
		convertView = inflater.inflate(R.layout.item_toys_order_list, null);
		ImageView item_toys_order_img = (ImageView) convertView.findViewById(R.id.item_toys_order_img);
		TextView item_toys_order_name = (TextView) convertView.findViewById(R.id.item_toys_order_name);
		TextView item_toys_order_type = (TextView) convertView.findViewById(R.id.item_toys_order_type);
		TextView item_toys_order_monery = (TextView) convertView.findViewById(R.id.item_toys_order_monery);
		ImageView category_icon_url = (ImageView) convertView.findViewById(R.id.category_icon_url);

		// 分类图标
		if(data.get(position).getCategory_icon_url().equals("")){
			category_icon_url.setVisibility(View.GONE);
		}else{
			category_icon_url.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(data.get(position).getCategory_icon_url(), category_icon_url, R.drawable.def_image);
		}
		
		// 会员卡
		if(data.get(position).getOrder_status().equals("2")){
			MyApplication.getInstance().display(data.get(position).getImg_thumb(), item_toys_order_img, R.drawable.def_image);
			item_toys_order_img.setScaleType(ScaleType.FIT_CENTER);
		}else{
			MyApplication.getInstance().display(data.get(position).getImg_thumb(), item_toys_order_img, R.drawable.def_image);
			item_toys_order_img.setScaleType(ScaleType.CENTER_INSIDE);
		}
		
		if(data.get(position).getIs_prize_status().equals("1")){
			item_toys_order_img.setScaleType(ScaleType.CENTER);
		}
		
		item_toys_order_name.setText(data.get(position).getBusiness_title());
		item_toys_order_type.setText(data.get(position).getStatus_name());
		item_toys_order_monery.setText(data.get(position).getSell_price());
		
		ImageView item_toys_order_delete = (ImageView) convertView.findViewById(R.id.item_toys_order_delete);
		ImageView item_toys_order_add = (ImageView) convertView.findViewById(R.id.item_toys_order_add);
		ImageView item_toys_order_pay = (ImageView) convertView.findViewById(R.id.item_toys_order_pay);
		
		if(data.get(position).getIs_reset().equals("1")){
			item_toys_order_add.setVisibility(View.VISIBLE);
		}else if(data.get(position).getIs_reset().equals("2")){
			item_toys_order_pay.setVisibility(View.VISIBLE);
		}
		
		if(data.get(position).getIs_del().equals("1")){
			item_toys_order_delete.setVisibility(View.VISIBLE);
		}
		
		item_toys_order_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(context);
				dialog.setTitle("自由环球租赁");
				dialog.setMessage("确定要删除订单吗？");
				dialog.setNegativeButton("再想想",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}

						});
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								deleteOrder(data.get(position).getOrder_id(), position);
							}
						});
				dialog.create().show();
			}
		});
		
		item_toys_order_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addOrder(data.get(position));
			}
		});
		
		item_toys_order_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				payOrder(data.get(position));
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.get(position).getIs_jump().equals("0")){
					Intent intent = new Intent(context, ToysLeaseOrderDetailActivity.class);
					intent.putExtra(ToysLeaseOrderDetailActivity.ORDER_ID_KEY, data.get(position).getOrder_id());
					context.startActivity(intent);
				}else if(data.get(position).getIs_jump().equals("1")){
					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position).getPost_url());
					context.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}

	/**
	 * 删除订单
	 * */
	private void deleteOrder(final String order_id, final int position){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("order_id", order_id);
		
		ab.get(ServerMutualConfig.DELETE_ORDER + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						data.remove(position);
						notifyDataSetChanged();
						
						// 这一批次全删除了，直接隐藏批次订单
						if(data.size() == 0 && comBiendIndex != -1){
							ToysLeaseMainOrderActivity.orderList.remove(comBiendIndex);
							ToysLeaseMainOrderActivity.adapter.notifyDataSetChanged();
						}
					}
				} catch (Exception e) {
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
	
	/**
	 * 在租一次
	 * */
	private void addOrder(ToysOrderItemBean bean){
		if(bean.getOrder_status().equals("2")){
			ToysLeaseOrderConfirmActivity.createNewOrder(context, ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, bean.getOrder_id());
		}else{
			Intent intent = new Intent(context, ToysLeaseOrderConfirmActivity.class);
			intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, bean.getOrder_id());
			context.startActivity(intent);
		}
	}
	
	/**
	 * 去支付
	 * */
	private void payOrder(ToysOrderItemBean bean){
		if(bean.getOrder_status().equals("2")){
			ToysLeaseOrderConfirmActivity.createNewOrder(context, ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, bean.getOrder_id());
		}else{
			Intent intent = new Intent(context, ToysLeaseOrderConfirmActivity.class);
			intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, bean.getOrder_id());
			context.startActivity(intent);
		}
	}
	
}