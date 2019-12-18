package com.yyqq.code.toyslease.version_92;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.commen.adapter.ToysLeaseCartAdapter;
import com.yyqq.commen.adapter.ToysLeaseCartDeleteItemAdapter;
import com.yyqq.commen.model.ToysLeaseCartBean;
import com.yyqq.commen.model.ToysLeaseCartItemBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 玩具购物车
 * */
public class ToysLeaseMainShoppingCartActivity extends BaseActivity{
	
	private TextView shopping_car_hint_text;
	private ListView listView = null;
	private LinearLayout cart_ntf;
	private TextView lease_main_right;
	public static TextView cart_monery_hint;
	public static TextView cart_monery;
	public static ImageView cart_to_pay;
	private RelativeLayout cart_monery_all;
	private ImageView lease_back;
	public static RelativeLayout vip_hint;
	private RelativeLayout lease_main_delete_all;
	private RelativeLayout lease_main_all;
	private ListView lease_main_delete_list;
	private ImageView cart_to_delete;
	private TextView cart_to_delete_all_hint;
	public static ImageView cart_to_delete_all;
	private RelativeLayout no_toys_hint;

	public static ToysLeaseMainShoppingCartActivity toysLeaseMainShoppingCartActivity;
	public static boolean isShowBack = false;
	private boolean isIntent = false;
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private ToysLeaseCartAdapter cartAdapter = null;
	private ToysLeaseCartDeleteItemAdapter deleteAdapter = null;
	private ArrayList<ToysLeaseCartBean> data = new ArrayList<ToysLeaseCartBean>();
	private ArrayList<ToysLeaseCartItemBean> deleteData = new ArrayList<ToysLeaseCartItemBean>();
	public static Map<Integer,String> deleteToysIdList = new HashMap<Integer,String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_shopping_cart); 
	}

	@Override
	protected void initView() {
		inflater = LayoutInflater.from(this);
		no_toys_hint = (RelativeLayout) findViewById(R.id.no_toys_hint);
		cart_to_delete_all = (ImageView) findViewById(R.id.cart_to_delete_all);
		cart_to_delete_all_hint = (TextView) findViewById(R.id.cart_to_delete_all_hint);
		lease_main_delete_all = (RelativeLayout) findViewById(R.id.lease_main_delete_all);
		lease_main_all = (RelativeLayout) findViewById(R.id.lease_main_all);
		lease_main_delete_list = (ListView) findViewById(R.id.lease_main_delete_list);
		cart_to_delete = (ImageView) findViewById(R.id.cart_to_delete);
		vip_hint = (RelativeLayout) findViewById(R.id.vip_hint);
		lease_main_right = (TextView) findViewById(R.id.lease_main_right);
		shopping_car_hint_text = (TextView) findViewById(R.id.shopping_car_hint_text);
		listView = (ListView) findViewById(R.id.lease_main_list);
		cart_ntf = (LinearLayout) findViewById(R.id.cart_ntf);
		cart_monery_hint = (TextView) findViewById(R.id.cart_monery_hint);
		cart_monery = (TextView) findViewById(R.id.cart_monery);
		cart_to_pay = (ImageView) findViewById(R.id.cart_to_pay);
		cart_monery_all = (RelativeLayout) findViewById(R.id.cart_monery_all);
		lease_back = (ImageView) findViewById(R.id.lease_back);
		if(isShowBack){
			lease_back.setVisibility(View.VISIBLE);
			isShowBack = false;
		}else{
			lease_back.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(this);
		toysLeaseMainShoppingCartActivity = this;
		
		if(null == deleteAdapter){
			deleteAdapter = new ToysLeaseCartDeleteItemAdapter(this, inflater, deleteData);
		}
		lease_main_delete_list.setAdapter(deleteAdapter);
		
		if(null == cartAdapter){
			cartAdapter = new ToysLeaseCartAdapter(this, inflater, data);
		}
		
		listView.setAdapter(cartAdapter);
	}

	@Override
	protected void setListener() {
		
		no_toys_hint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(lease_back.isShown()){
					Intent intent = new Intent(ToysLeaseMainShoppingCartActivity.this, ToysLeaseMainTabActivity.class);
					intent.putExtra("tabid", 0);
					startActivity(intent);
				}else{
					ToysLeaseMainTabActivity.tabHost.setCurrentTab(0);
				}
			}
		});
		
		cart_to_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(deleteToysIdList.size() == 0){
					Toast.makeText(ToysLeaseMainShoppingCartActivity.this, "请勾选要删除的玩具", 3).show();
					return;
				}
				AlertDialog.Builder dialog = new Builder(ToysLeaseMainShoppingCartActivity.this);
				dialog.setTitle("自由环球租赁");
				dialog.setMessage("确定要删除这些玩具吗？");
				dialog.setNegativeButton("取消",
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
								deleteToys();
							}
						});
				dialog.create().show();
			}
		});
		
		cart_to_delete_all_hint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int deleteNumber = 0;
				for(int i = 0 ; i < deleteData.size() ; i++){
					if(deleteData.get(i).isDelete()){
						deleteNumber+=1;
					}
				}
				
				// 判断是否已经全选
				if(deleteNumber != deleteData.size()){
					for(int i = 0 ; i < deleteData.size() ; i++){
						deleteData.get(i).setDelete(true);
						deleteToysIdList.put(i, deleteData.get(i).getCart_id());
					}
					deleteAdapter.notifyDataSetChanged();
					cart_to_delete_all.setBackgroundResource(R.drawable.select_down);
				}else{
					for(int i = 0 ; i < deleteData.size() ; i++){
						deleteData.get(i).setDelete(false);
						deleteToysIdList.clear();
					}
					deleteAdapter.notifyDataSetChanged();
					cart_to_delete_all.setBackgroundResource(R.drawable.select_up);
				}
			}
		});
		
		cart_to_delete_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int deleteNumber = 0;
				for(int i = 0 ; i < deleteData.size() ; i++){
					if(deleteData.get(i).isDelete()){
						deleteNumber+=1;
					}
				}
				
				// 判断是否已经全选
				if(deleteNumber != deleteData.size()){
					for(int i = 0 ; i < deleteData.size() ; i++){
						deleteData.get(i).setDelete(true);
						deleteToysIdList.put(i, deleteData.get(i).getCart_id());
					}
					deleteAdapter.notifyDataSetChanged();
					cart_to_delete_all.setBackgroundResource(R.drawable.select_down);
				}else{
					for(int i = 0 ; i < deleteData.size() ; i++){
						deleteData.get(i).setDelete(false);
						deleteToysIdList.clear();
					}
					deleteAdapter.notifyDataSetChanged();
					cart_to_delete_all.setBackgroundResource(R.drawable.select_up);
				}
			}
		});
		
		vip_hint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vip_hint.setVisibility(View.GONE);
			}
		});
		
		lease_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseMainShoppingCartActivity.this.finish();
			}
		});
		
		lease_main_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(deleteData.size() < 1){
					Toast.makeText(ToysLeaseMainShoppingCartActivity.this, "您还没有可编辑的玩具", 3).show();
					return;
				}
				
				 if(lease_main_right.getText().toString().trim().equals("编辑")){
					 lease_main_right.setText("完成");
					 lease_main_delete_all.setVisibility(View.VISIBLE);
					 lease_main_all.setVisibility(View.GONE);
				 }else{
					 lease_main_right.setText("编辑");
					 lease_main_delete_all.setVisibility(View.GONE);
					 lease_main_all.setVisibility(View.VISIBLE);
				 }
			}
		});
	}
	
	/**
	 * 获取购物车数据
	 * */
	private void initCartData(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		ab.get(ServerMutualConfig.GET_CART_DATA + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						
						data.clear();
						
						// 顶部通知
						if(new JSONObject(content).getJSONObject("data1").getString("card_title").isEmpty()){
							cart_ntf.setVisibility(View.GONE);
						}else{
							cart_ntf.setVisibility(View.VISIBLE);
							shopping_car_hint_text.setText(new JSONObject(content).getJSONObject("data1").getString("card_title"));
						}
						
						// 底部金额、按钮
						changeCartBottomPage(ToysLeaseMainShoppingCartActivity.this, new JSONObject(content).getJSONObject("data1").getString("total_title"), new JSONObject(content).getJSONObject("data1").getString("total_price"), new JSONObject(content).getJSONObject("data1").getString("is_button"));
						
						// 中间玩具
						ToysLeaseCartBean cartBean = null;
						JSONArray cartJson = new JSONObject(content).getJSONArray("data");
						ArrayList<ToysLeaseCartItemBean> cartItemList = null;
						for(int i = 0 ; i < cartJson.length() ; i++ ){
							cartBean = new ToysLeaseCartBean();
							cartBean.setToys_title(cartJson.getJSONObject(i).getString("toys_title"));
							cartBean.setType(cartJson.getJSONObject(i).getString("type"));
							if(cartJson.getJSONObject(i).getString("cart_delete_status").equals("1")){
								cartBean.setDelete(true);
							}
							
							cartItemList = new ArrayList<ToysLeaseCartItemBean>();
							ToysLeaseCartItemBean cartItemBean = null;
							for(int j = 0 ; j < cartJson.getJSONObject(i).getJSONArray("toys_info").length(); j++ ){
								cartItemBean = new ToysLeaseCartItemBean();
								cartItemBean.setBusiness_id(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("business_id"));
								cartItemBean.setBusiness_title(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("business_title"));
								cartItemBean.setCart_id(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("cart_id"));
								cartItemBean.setCheck_state(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("check_state"));
								cartItemBean.setEvery_price(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("every_price"));
								cartItemBean.setImg_thumb(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("img_thumb"));
								cartItemBean.setIs_order(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("is_order"));
								cartItemBean.setDescription(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("description"));
								cartItemBean.setOrder_id(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("order_id"));
								cartItemBean.setSmall_img_thumb(cartJson.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("new_size_img_thumb"));
								cartItemList.add(cartItemBean);
							}
							cartBean.setToys_info(cartItemList);
							data.add(cartBean);
							
							if(cartBean.isDelete()){
								deleteData.clear();
								deleteData.addAll(cartItemList);
								deleteAdapter.notifyDataSetChanged();
							}
						}
						if(data.size() != 0){
							cartAdapter.notifyDataSetChanged();
							no_toys_hint.setVisibility(View.GONE);
						}else{
							ToysLeaseMainTabActivity.newMessage.setVisibility(View.GONE);
							lease_main_all.setVisibility(View.GONE);
							lease_main_delete_all.setVisibility(View.GONE);
							no_toys_hint.setVisibility(View.VISIBLE);
						}
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

	/**
	 * 修改购物车底部金额、按钮状态
	 * 
	 * */
	public static void changeCartBottomPage(final Context context, String hintText, String priceText, String PayBtType){
		
		cart_monery_hint.setText(hintText);
		cart_monery.setText(priceText);
		
		// “去结算”按钮状态
		if(PayBtType.equals("0")){
			cart_to_pay.setBackgroundResource(R.drawable.item_cart_pay_no);
			cart_to_pay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "请选择要结算的商品", 3).show();
				}
			});
		}else{
			cart_to_pay.setBackgroundResource(R.drawable.cart_to_pay);
			cart_to_pay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ToysLeaseOrderConfirmActivity.class);
					intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_CARD_ID, "1");
					context.startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * 删除玩具
	 * */
	private void deleteToys(){
		Config.showProgressDialog(this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		String cart_ids = "";
		Iterator iter = deleteToysIdList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry enter = (Map.Entry) iter.next();
			if(!cart_ids.isEmpty()){
				cart_ids+=",";
			}
			cart_ids+=enter.getValue();
		}
		params.put("cart_ids", cart_ids);
		ab.post(ServerMutualConfig.DELETE_TOYS, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						Toast.makeText(ToysLeaseMainShoppingCartActivity.this, "已删除", 3).show();
						initCartData();
					}else{
						Toast.makeText(ToysLeaseMainShoppingCartActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
			public void onFailure(int statusCode, String content, Throwable error) {
				super.onFailure(statusCode, content, error);
				Config.dismissProgress();
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// 修改底部栏文字颜色
		ToysLeaseMainTabActivity.iconText.get(0).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(1).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(2).setTextColor(Color.parseColor("#fe6363"));
		ToysLeaseMainTabActivity.iconText.get(3).setTextColor(Color.parseColor("#000000"));
		
		if(isIntent && !MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseMainShoppingCartActivity.this).uid.equals("")){
			if(null != ToysLeaseMainTabActivity.toysLeaseMainTabActivity){
				ToysLeaseMainTabActivity.toysLeaseMainTabActivity.finish();
			}
			Intent in = new Intent(ToysLeaseMainShoppingCartActivity.this, ToysLeaseMainTabActivity.class);
			startActivity(in);
			this.finish();
		}else if(!isIntent && !MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseMainShoppingCartActivity.this).uid.equals("")){
			isIntent = true;
			Intent in = new Intent(ToysLeaseMainShoppingCartActivity.this, WebLoginActivity.class);
			startActivity(in);
		}else{
			 cart_to_delete_all.setBackgroundResource(R.drawable.select_up);
			 lease_main_right.setText("编辑");
			 lease_main_delete_all.setVisibility(View.GONE);
			 lease_main_all.setVisibility(View.VISIBLE);
			// 网络情况
			if(Config.isConn(this)){
				ShoppingCartUtils.updateShoppingCartNumber(ToysLeaseMainShoppingCartActivity.this, true);
				initCartData();
			}else{
				listView.setVisibility(View.GONE);
				cart_monery_all.setVisibility(View.GONE);
				findViewById(R.id.net_error).setVisibility(View.VISIBLE);
				findViewById(R.id.net_error).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Settings.ACTION_SETTINGS ));
					}
				});
			}
		}
	}
}
