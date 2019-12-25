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
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.commen.model.ToysOrderListBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.MyListView;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 订单列表-（一级列表）
 * */
public class ToysLeaseOrderListAdapter02 extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<ToysOrderListBean> data = null;
	private AbHttpUtil ab;

	public ToysLeaseOrderListAdapter02(Activity context, LayoutInflater inflater,
			ArrayList<ToysOrderListBean> data, AbHttpUtil ab) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
		this.ab = ab;
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
		convertView = inflater.inflate(R.layout.item_toys_order_list02, null);
		TextView toys_order_id = (TextView) convertView.findViewById(R.id.order_id);
		ImageView toys_order_vip = (ImageView) convertView.findViewById(R.id.order_vip);
		TextView toys_order_term = (TextView) convertView.findViewById(R.id.order_term);
		MyListView item_order_list_all_order = (MyListView) convertView.findViewById(R.id.item_order_list_all_order);
		ToysLeaseOrderListAdapter adapter = new ToysLeaseOrderListAdapter(context, inflater, data.get(position).getItemList(), ab, position);
		item_order_list_all_order.setAdapter(adapter);
		
		toys_order_id.setText("订单批号：" + data.get(position).getCombined_order_id());
		toys_order_term.setText(data.get(position).getToys_title());
		
		ImageView item_toys_order_delete = (ImageView) convertView.findViewById(R.id.item_toys_order_delete);
		ImageView item_toys_order_add = (ImageView) convertView.findViewById(R.id.item_toys_order_add);
		ImageView item_toys_order_pay = (ImageView) convertView.findViewById(R.id.item_toys_order_pay);
//		ImageView item_toys_order_change = (ImageView) convertView.findViewById(R.id.item_toys_order_change); 
//		ImageView item_toys_order_cancle = (ImageView) convertView.findViewById(R.id.item_toys_order_change); 
//		ImageView item_toys_order_set = (ImageView) convertView.findViewById(R.id.item_toys_order_change); 
			
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
				if(!((Activity) context).isFinishing())
				{
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
							deleteOrder(data.get(position).getCombined_order_id(), position);
						}
					});
					dialog.create().show();
				}else{
					deleteOrder(data.get(position).getCombined_order_id(), position);
				}
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
		
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(data.get(position).getIs_jump().equals("0")){
//					Intent intent = new Intent(context, ToysLeaseOrderDetailActivity.class);
//					intent.putExtra(ToysLeaseOrderDetailActivity.ORDER_ID_KEY, data.get(position).getO);
//					context.startActivity(intent);
//				}else if(data.get(position).getIs_jump().equals("1")){
//					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
//					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position).getPost_url());
//					context.startActivity(intent);
//				}
//			}
//		});
//		}
		
		return convertView;
	}

	/**
	 * 删除订单
	 * */
	private void deleteOrder(final String order_id, final int position){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("combined_order_id", order_id);
		
		ab.get(ServerMutualConfig.DELETE_ORDER + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						data.remove(position);
						notifyDataSetChanged();
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
	private void addOrder(ToysOrderListBean bean){
		if(bean.getCombined_order_id().equals("2")){
			ToysLeaseOrderConfirmActivity.createNewOrder(context, ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, bean.getCombined_order_id());
		}else{
			Intent intent = new Intent(context, ToysLeaseOrderConfirmActivity.class);
			intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, bean.getCombined_order_id());
			context.startActivity(intent);
		}
	}
	
	/**
	 * 去支付
	 * */
	private void payOrder(ToysOrderListBean bean){
		if(bean.getOrder_state().equals("2")){
			ToysLeaseOrderConfirmActivity.createNewOrder(context, ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, bean.getCombined_order_id());
		}else{
			Intent intent = new Intent(context, ToysLeaseOrderConfirmActivity.class);
			intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, bean.getCombined_order_id());
			context.startActivity(intent);
		}
	}
	
}
