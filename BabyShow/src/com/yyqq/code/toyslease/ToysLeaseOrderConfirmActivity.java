package com.yyqq.code.toyslease;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.commen.adapter.ToysLeaseBatteryListAdapter;
import com.yyqq.commen.adapter.ToysLeaseConfirmCartListAdapter;
import com.yyqq.commen.adapter.ToysLeaseConfirmToysListAdapter;
import com.yyqq.commen.model.ToysConfrimOrderCartListBean;
import com.yyqq.commen.model.ToysConfrimOrderToysListBean;
import com.yyqq.commen.model.ToysLeaseBatteryBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.MyListView;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.commen.view.SelectDeliveryView;
import com.yyqq.commen.view.SelectTimeView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 玩具世界-确认订单
 * */
@SuppressLint("NewApi")
public class ToysLeaseOrderConfirmActivity extends BaseActivity {

	private EditText lease_order_name;
	private EditText lease_order_phone;
	private TextView lease_order_address;
	private LinearLayout lease_order_zuqi;
	private TextView lease_order_hint01;
	private TextView lease_order_hint02;
	private TextView lease_order_time;
	private RecodeListView lease_order_list;
	private ScrollView lease_order_sc;
	private SelectTimeView lease_order_select;
	private RelativeLayout lease_order_time_select;
	private SelectDeliveryView lease_delivery_select;
	private TextView lease_order_time_no;
	private TextView lease_order_time_yes;
	private EditText lease_detail_addtext;
	private RelativeLayout lease_pay_all;
	private ImageView lease_pay_type_hint;
	private LinearLayout cart_ntf;
	private TextView toys_pay_hint_text;
	private MyListView lease_toys_list;
	private RelativeLayout pay_packet;
	private TextView account_hint_text;
	private LinearLayout lease_toys_list_hint;
	private TextView lease_toys_title;
	private TextView lease_toys_price;
	private RelativeLayout lease_order_delivery;
	private TextView lease_order_delivery_time;
	private TextView lease_order_hint03;
	private TextView lease_order_hint04;
	private Button toys_confirm_order_bt;
	private TextView lease_order_delivery_yes;
	private TextView lease_order_delivery_no;
	private RelativeLayout lease_order_delivery_select;
	private TextView shopping_car_hint_text;
	private TextView addr_hint_text;
	
//	public static String TOYS_ORDER_KEY = "toys_order_key";
	public static String CONFIRM_BUSINESS_ID = "business_id";
	public static String CONFIRM_ORDER_ID = "order_id";
	public static String CONFIRM_CARD_ID = "card_id";
	public static String CONFIRM_COMBINED_ID = "combined_id";
	
	public static boolean UPDATE_ADDR = false;
	public static String ADDR_ID = ""; // 分区id
	public static String ADDR_DETAIL = ""; // 详细地址
	public static String DIS_PROMPT = ""; // 配送时长
	public static String USER_NAME = "";
	public static String USER_PHONE = "";
	public static String USER_ADDTEXT = "";
	
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	public static String order_id = "";
	private String min = "";
	private String max = "";
	public static ToysLeaseOrderConfirmActivity toysLeaseOrderConfirmActivity = null;
	private String source = "0";
	private String is_balance = "0";
	private MyListView lease_toys_vip;
	private MyListView lease_toys_need;
	// 会员卡列表
	private ArrayList<ToysConfrimOrderCartListBean> cartList = new ArrayList<ToysConfrimOrderCartListBean>();
	// 玩具列表
	private ArrayList<ToysConfrimOrderToysListBean> toysList = new ArrayList<ToysConfrimOrderToysListBean>();
	// 电池选择列表
	private ArrayList<ToysLeaseBatteryBean> batteryList = new ArrayList<ToysLeaseBatteryBean>();
	// 租期列表
	private ArrayList<String> termList = new ArrayList<String>();
	// 选中的租期
	private int termIndex = 0;
	// 送货时间
	private ArrayList<String> deliveryList = new ArrayList<String>();
	// 选中的送货时间
	private int deliveryIndex = 0;
	// 批次订单号
	private String combined_order_id = "";
	// 是否提示购买会员卡
	private boolean hintCart = false;
	// 电池adapter
	private ToysLeaseBatteryListAdapter btrAdapter;
	// 购卡提示语
	private String hintText = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_order);
	}

	@Override
	protected void initView() {
		addr_hint_text = (TextView) findViewById(R.id.addr_hint_text);
		shopping_car_hint_text = (TextView) findViewById(R.id.shopping_car_hint_text);
		toys_confirm_order_bt = (Button) findViewById(R.id.toys_confirm_order_bt);
		lease_order_hint04 = (TextView) findViewById(R.id.lease_order_hint04);
		lease_order_hint03 = (TextView) findViewById(R.id.lease_order_hint03);
		lease_order_delivery = (RelativeLayout) findViewById(R.id.lease_order_delivery);
		lease_order_delivery_time = (TextView) findViewById(R.id.lease_order_delivery_time);
		lease_toys_list_hint = (LinearLayout) findViewById(R.id.lease_toys_list_hint);
		lease_toys_title = (TextView) findViewById(R.id.lease_toys_title);
		lease_toys_price = (TextView) findViewById(R.id.lease_toys_price);
		lease_toys_vip = (MyListView) findViewById(R.id.lease_toys_vip);
		lease_toys_list = (MyListView) findViewById(R.id.lease_toys_list);
		cart_ntf = (LinearLayout) findViewById(R.id.cart_ntf);
		toys_pay_hint_text = (TextView) findViewById(R.id.toys_pay_hint_text);
		lease_delivery_select = (SelectDeliveryView) findViewById(R.id.lease_delivery_select);
		lease_order_delivery_select = (RelativeLayout) findViewById(R.id.lease_order_delivery_select);
		lease_order_delivery_yes = (TextView) findViewById(R.id.lease_order_delivery_yes);
		lease_order_delivery_no = (TextView) findViewById(R.id.lease_order_delivery_no);
		account_hint_text = (TextView) findViewById(R.id.account_hint_text);
		pay_packet = (RelativeLayout) findViewById(R.id.pay_packet);
		lease_pay_all = (RelativeLayout) findViewById(R.id.lease_pay_all);
		lease_pay_type_hint = (ImageView) findViewById(R.id.lease_pay_type_hint);
		lease_order_time_select = (RelativeLayout) findViewById(R.id.lease_order_time_select);
		lease_order_select = (SelectTimeView) findViewById(R.id.lease_order_select);
		lease_order_time_no = (TextView) findViewById(R.id.lease_order_time_no);
		lease_order_time_yes = (TextView) findViewById(R.id.lease_order_time_yes);
		lease_detail_addtext = (EditText) findViewById(R.id.lease_detail_addtext);
		lease_order_name = (EditText) findViewById(R.id.lease_order_name);
		lease_order_phone = (EditText) findViewById(R.id.lease_order_phone);
		lease_order_address = (TextView) findViewById(R.id.lease_order_address);
		lease_order_zuqi = (LinearLayout) findViewById(R.id.lease_order_zuqi);
		lease_order_hint01 = (TextView) findViewById(R.id.lease_order_hint01);
		lease_order_hint02 = (TextView) findViewById(R.id.lease_order_hint02);
		lease_order_time = (TextView) findViewById(R.id.lease_order_time);
		lease_order_list = (RecodeListView) findViewById(R.id.lease_order_list);
		lease_toys_need = (MyListView) findViewById(R.id.lease_toys_need);
		lease_order_sc = (ScrollView) findViewById(R.id.lease_order_sc);
		lease_order_list.setFocusable(false); // 消除listview焦点
		lease_order_sc.smoothScrollTo(0,20); // scrollView置顶
		lease_order_sc.requestFocus();
	}

	@Override
	protected void initData() {
		toysLeaseOrderConfirmActivity = this;
		ab = AbHttpUtil.getInstance(ToysLeaseOrderConfirmActivity.this);
		inflater = LayoutInflater.from(ToysLeaseOrderConfirmActivity.this);
		
		// 创建订单
		createNewOrder();
	}
	
	@Override
	protected void setListener() {
		
		lease_toys_need.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editBatteryType(batteryList.get(position), position);
			}
		});
		
		lease_toys_vip.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editCartType(cartList.get(position));
			}
		});

		lease_order_delivery_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_delivery_select.setVisibility(View.GONE);
				lease_order_delivery_time.setText(deliveryList.get(deliveryIndex));
			}
		});
		
		lease_order_delivery_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_delivery_select.setVisibility(View.GONE);
			}
		});
		
		toys_confirm_order_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(lease_order_phone.getText().toString().trim().isEmpty()){
					Toast.makeText(v.getContext(), "请填写您的手机号", 3).show();
				}else{
					if(!hintCart){
						submitUserInfo();
					}else{
//						AlertDialog.Builder dialog = new Builder(ToysLeaseOrderConfirmActivity.this);
//						dialog.setTitle("温馨提示：");
//						dialog.setMessage(hintText);
//						dialog.setNegativeButton("去购卡",
//								new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								Intent in = new Intent(ToysLeaseOrderConfirmActivity.this, ToysLeaseDetailActivity.class);
//								in.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, "1629");
//								startActivity(in);
//							}
//							
//						});
//						dialog.setPositiveButton("直接租",
//								new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
								submitUserInfo();
//							}
//						});
//						dialog.create().show();
					}
				}
			}
		});
		
		lease_order_delivery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_delivery_select.setVisibility(View.VISIBLE);
			}
		});
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseOrderConfirmActivity.this.finish();
			}
		});
		
		lease_order_time_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_time_select.setVisibility(View.GONE);
				lease_order_time.setText(termList.get(termIndex)+"天");
			}
		});
		
		lease_order_time_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_time_select.setVisibility(View.GONE);
			}
		});
		
		lease_order_time_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_time_select.setVisibility(View.GONE);
			}
		});
		
		lease_order_zuqi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lease_order_time_select.setVisibility(View.VISIBLE);
			}
		});
		
		lease_order_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!lease_order_name.equals("")){
					USER_NAME = lease_order_name.getText().toString().trim();
				}
				
				if(!lease_order_phone.equals("")){
					USER_PHONE = lease_order_phone.getText().toString().trim();
				}
				
				if(!lease_detail_addtext.equals("")){
					USER_ADDTEXT = lease_detail_addtext.getText().toString().trim();
				}
				
				if(null != ToysLeaseAddressSelectActivity.toysLeaseAddressSelectActivity){
					ToysLeaseAddressSelectActivity.toysLeaseAddressSelectActivity.finish();
				}
				startActivity(new Intent(ToysLeaseOrderConfirmActivity.this, ToysLeaseAddressSelectActivity.class));
			}
		});
		
	}
	
	/**
	 * 生成新订单
	 * 
	 * 注：向接口传入“business_id”代表是立即租下（单件），不传代表购物车结算（批量）
	 * */
	private void createNewOrder(){
		Config.showProgressDialog(ToysLeaseOrderConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderConfirmActivity.this).uid);
		if(getIntent().hasExtra(CONFIRM_BUSINESS_ID)){
			params.put("business_id", getIntent().getStringExtra(CONFIRM_BUSINESS_ID));
			params.put("source", "0");
		}
		if(getIntent().hasExtra(CONFIRM_CARD_ID)){
			params.put("source", "1");
		}
		if(getIntent().hasExtra(CONFIRM_ORDER_ID)){
			params.put("order_id", getIntent().getStringExtra(CONFIRM_ORDER_ID));
			params.put("source", "2");
		}
		if(getIntent().hasExtra(CONFIRM_COMBINED_ID)){
			params.put("combined_order_id", getIntent().getStringExtra(CONFIRM_COMBINED_ID));
			params.put("source", "3");
		}
		ab.get(ServerMutualConfig.CREATE_NEW_ORDER + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						JSONObject jsonCon = new JSONObject(content).getJSONObject("data");
						
						// 初始化收货信息记录
						lease_order_name.setText(jsonCon.getString("user_name"));
						lease_order_phone.setText(jsonCon.getString("mobile"));
						lease_order_address.setText(jsonCon.getString("address"));
						if(!jsonCon.getString("prompt_title").isEmpty()){
							cart_ntf.setVisibility(View.VISIBLE);
							shopping_car_hint_text.setText(jsonCon.getString("prompt_title"));
						}else{
							cart_ntf.setVisibility(View.GONE);
						}
						
						// 获取批次订单号
						combined_order_id = jsonCon.getString("combined_order_id");
						
						// 初始化会员卡和玩具订单列表
						initCartAndToysView(jsonCon);
						
						// 初始化租期选择列表
						initTermView(jsonCon);
						
						// 初始化收货时间选择列表
						initDeliveryView(jsonCon);
						
					}else{
						Toast.makeText(ToysLeaseOrderConfirmActivity.this, new JSONObject(content).getString("reMsg"), Toast.LENGTH_SHORT).show();
						lease_pay_all.setVisibility(View.GONE);
						lease_pay_type_hint.setVisibility(View.VISIBLE);
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
				lease_pay_all.setVisibility(View.GONE);
				lease_pay_type_hint.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/**
	 * 提交个人信息
	 * */
	private void submitUserInfo(){
		Config.showProgressDialog(ToysLeaseOrderConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderConfirmActivity.this).uid);
		params.put("combined_order_id", combined_order_id);
		if(lease_order_zuqi.isShown()){ // 普通用户需要上传租赁天数
			params.put("rent_day", termList.get(termIndex));
		}else{
			params.remove("rent_day");
		}
		params.put("user_name", lease_order_name.getText().toString().trim());
		params.put("mobile", lease_order_phone.getText().toString().trim());
		params.put("address", lease_order_address.getText().toString().trim());
		params.put("addtext", lease_detail_addtext.getText().toString().trim());
		params.put("delivery_time", deliveryList.get(deliveryIndex));
		params.put("city_id",ADDR_ID);
		ab.post(ServerMutualConfig.SUBMIT_USER_INFO, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						Intent intent = new Intent(ToysLeaseOrderConfirmActivity.this, ToysLeasePayConfirmActivity.class);
						intent.putExtra(ToysLeasePayConfirmActivity.ORDER_ID_KEY, combined_order_id);
						startActivity(intent);
					}else{
						Toast.makeText(ToysLeaseOrderConfirmActivity.this, new JSONObject(content).getString("reMsg"), Toast.LENGTH_SHORT).show();
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
	 * 修改会员卡是否启用的状态
	 * */
	private void editCartType(ToysConfrimOrderCartListBean bean){
		Config.showProgressDialog(ToysLeaseOrderConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderConfirmActivity.this).uid);
		params.put("combined_order_id", combined_order_id);
		params.put("card_id", bean.getCard_id());
		if(bean.getSelected_state().equals("1")){
			params.put("selected_state", "0");
		}else{
			params.put("selected_state", "1");	
		}
		
		ab.post(ServerMutualConfig.EDIT_CART_TYPE , params, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						
						// 初始化租期列表+玩具列表
						initCartAndToysView(new JSONObject(content).getJSONObject("data"));
						
						// 初始化租期选择列表
						initTermView(new JSONObject(content).getJSONObject("data"));
						
//						// 初始化会员卡和玩具订单列表
//						initCartAndToysView(jsonCon);
						
					}else{
						Toast.makeText(ToysLeaseOrderConfirmActivity.this, new JSONObject(content).getString("reMsg"), Toast.LENGTH_SHORT).show();
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
	 * 修改是否购买电池标记
	 * */
	private void editBatteryType(ToysLeaseBatteryBean bean, final int index){
		Config.showProgressDialog(ToysLeaseOrderConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderConfirmActivity.this).uid);
		params.put("order_id", bean.getOrder_id());
		
		ab.post(ServerMutualConfig.EDIT_BATTERY_TYPE , params, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						batteryList.get(index).setOrder_id(new JSONObject(content).getJSONObject("data").getString("order_id"));
						batteryList.get(index).setBattery_title(new JSONObject(content).getJSONObject("data").getString("battery_title"));
						batteryList.get(index).setIs_battery(new JSONObject(content).getJSONObject("data").getString("is_battery"));
						btrAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(ToysLeaseOrderConfirmActivity.this, new JSONObject(content).getString("reMsg"), Toast.LENGTH_SHORT).show();
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
	 * 初始化会员卡+玩具列表
	 * */
	private void initCartAndToysView(JSONObject jsonCon){
		try{
			
			// 修改批次订单号
			combined_order_id = jsonCon.getString("combined_order_id");
			
			// 初始化会员卡列表
			if(jsonCon.getJSONArray("card_info").length() != 0){
				hintCart = false;
				ToysConfrimOrderCartListBean cartBean = null;
				cartList.clear();
				for(int i = 0 ; i < jsonCon.getJSONArray("card_info").length() ; i++){
					cartBean = new ToysConfrimOrderCartListBean();
					cartBean.setCard_id(jsonCon.getJSONArray("card_info").getJSONObject(i).getString("card_id"));
					cartBean.setCard_title(jsonCon.getJSONArray("card_info").getJSONObject(i).getString("card_title_new"));
					cartBean.setIs_choose(jsonCon.getJSONArray("card_info").getJSONObject(i).getString("is_choose"));
					cartBean.setSelected_state(jsonCon.getJSONArray("card_info").getJSONObject(i).getString("selected_state"));
					cartList.add(cartBean);
				}
				ToysLeaseConfirmCartListAdapter adapter = new ToysLeaseConfirmCartListAdapter(ToysLeaseOrderConfirmActivity.this, inflater, cartList);
				lease_toys_vip.setAdapter(adapter);
			}else{
				hintCart = true;
				lease_toys_vip.setVisibility(View.GONE); // 隐藏会员卡列表
			}
			
			// 初始化玩具列表
			if(jsonCon.getJSONArray("toys_info").length() != 0){
				if(jsonCon.has("no_card_title") && !jsonCon.getString("no_card_title").equals("")){
					hintText = jsonCon.getString("no_card_title");
				}
				lease_toys_title.setText(jsonCon.getString("toys_title"));
				lease_toys_price.setText(jsonCon.getString("total_price"));
				ToysConfrimOrderToysListBean toysBean = null;
				toysList.clear();
				for(int i = 0 ; i < jsonCon.getJSONArray("toys_info").length() ; i++){
					toysBean = new ToysConfrimOrderToysListBean();
					toysBean.setBusiness_id(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("business_id"));
					toysBean.setBusiness_title(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("business_title"));
					toysBean.setEvery_sell_price(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("every_sell_price"));
					toysBean.setImg_thumb(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("img_thumb"));
					toysBean.setMarket_price(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("market_price"));
					toysBean.setMember_price(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("order_card_info"));
					toysBean.setOrder_id(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("order_id"));
					toysBean.setCategory_icon_url(jsonCon.getJSONArray("toys_info").getJSONObject(i).getString("new_size_img_thumb"));
					toysList.add(toysBean);
				}
				ToysLeaseConfirmToysListAdapter adapter = new ToysLeaseConfirmToysListAdapter(ToysLeaseOrderConfirmActivity.this, inflater, toysList);
				lease_toys_list.setAdapter(adapter);
				
				// 初始化电池列表
				if(jsonCon.getString("battery_state").equals("1")){
					batteryList.clear();
					ToysLeaseBatteryBean btr = null;
					for(int i = 0 ; i < jsonCon.getJSONArray("battery_info").length() ; i++){
						btr = new ToysLeaseBatteryBean();
						btr.setBattery_title(jsonCon.getJSONArray("battery_info").getJSONObject(i).getString("battery_title"));
						btr.setOrder_id(jsonCon.getJSONArray("battery_info").getJSONObject(i).getString("order_id"));
						btr.setIs_battery(jsonCon.getJSONArray("battery_info").getJSONObject(i).getString("is_battery"));
						batteryList.add(btr);
					}
					btrAdapter = new ToysLeaseBatteryListAdapter(ToysLeaseOrderConfirmActivity.this, inflater, batteryList);
					lease_toys_need.setAdapter(btrAdapter);
				}else{
					lease_toys_need.setVisibility(View.GONE);
				}
				
			}else{
				lease_toys_list.setVisibility(View.GONE); // 隐藏玩具列表
				lease_toys_list_hint.setVisibility(View.GONE); // 隐藏玩具title
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化租期选择列表
	 * */
	private void initTermView(JSONObject jsonCon){
		try{
			if(jsonCon.getJSONObject("rent_info").getString("is_rent").equals("1")){
				lease_order_zuqi.setVisibility(View.VISIBLE);
				min = jsonCon.getJSONObject("rent_info").getString("rent_day");
				max = jsonCon.getJSONObject("rent_info").getString("more_rent_day");
				if(!min.trim().isEmpty() && !max.trim().isEmpty()){
					if(Integer.parseInt(min) != Integer.parseInt(max)){
						termList.clear();
						for(int i = Integer.parseInt(min) ; i <= Integer.parseInt(max) ; i++ ){
							termList.add(i+"");
						}
						lease_order_select.lists(termList).fontSize(35).showCount(5).selectTip("天").select(0).listener(new SelectTimeView.OnSelectTimeViewItemSelectListener() {
							
							@Override
							public void onItemSelect(int index) {
								termIndex = index;
							}
						}).build();
						
						lease_order_delivery_select.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								lease_order_delivery_select.setVisibility(View.GONE);
							}
						});
						
					}else{
						termList.add(min);
					}
				}
				lease_order_hint01.setText(jsonCon.getJSONObject("rent_info").getString("rent_day_des"));
				lease_order_hint02.setText(jsonCon.getJSONObject("rent_info").getString("rent_end_time"));
				lease_order_time.setText(termList.get(termIndex)+"天");
			}else{
				lease_order_zuqi.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化收货时间列表
	 * */
	private void initDeliveryView(JSONObject jsonCon){
		try{
			lease_order_hint03.setText(jsonCon.getString("delivery_title"));
			lease_order_hint04.setText(jsonCon.getString("delivery_des"));
			lease_order_delivery_time.setText(jsonCon.getString("defalut_delivery_time"));
			
			ADDR_ID = jsonCon.getString("city_id");
			lease_order_address.setText(jsonCon.getString("address"));
			addr_hint_text.setText(jsonCon.getString("dis_prompt"));
			if(!jsonCon.getString("dis_prompt").equals("")){
				addr_hint_text.setVisibility(View.VISIBLE);
			}else{
				addr_hint_text.setVisibility(View.GONE);
			}
			
			
			deliveryList.clear();
			for(int i = 0 ; i < jsonCon.getJSONArray("delivery_info").length() ; i++){
				deliveryList.add(jsonCon.getJSONArray("delivery_info").getJSONObject(i).getString("delivery_time"));
			}
			lease_delivery_select.lists(deliveryList).fontSize(35).showCount(5).selectTip("").select(0).listener(new SelectDeliveryView.OnSelectTimeViewItemSelectListener() {
				
				@Override
				public void onItemSelect(int index) {
					deliveryIndex = index;
				}
			}).build();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建订单并支付
	 * */
	public static void createNewOrder(final Context context, String key, String value){
		
		// 未登录
		if(!MyApplication.getVisitor().equals("0")  || Config.getUser(context).uid.equals("")){
			Intent intent = new Intent(context, WebLoginActivity.class);
			context.startActivity(intent);
			return;
		}
		
		Config.showProgressDialog(context, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		if(key.equals(CONFIRM_BUSINESS_ID)){
			params.put("business_id", value);
			params.put("source", "0");
		}
		if(key.equals(CONFIRM_CARD_ID)){
			params.put("source", "1");
		}
		if(key.equals(CONFIRM_ORDER_ID)){
			params.put("order_id", value);
			params.put("source", "2");
		}
		if(key.equals(CONFIRM_COMBINED_ID)){
			params.put("combined_order_id", value);
			params.put("source", "3");
		}
		AbHttpUtil.getInstance(context).get(ServerMutualConfig.CREATE_NEW_ORDER + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						JSONObject jsonCon = new JSONObject(content).getJSONObject("data");
						String combined_order_id = jsonCon.getString("combined_order_id");
						if(!combined_order_id.isEmpty()){
							Intent intent = new Intent(context, ToysLeasePayConfirmActivity.class);
							intent.putExtra(ToysLeasePayConfirmActivity.ORDER_ID_KEY, combined_order_id);
							context.startActivity(intent);
						}else{
							Toast.makeText(context, "请再次重试！", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(context, new JSONObject(content).getString("reMsg"), Toast.LENGTH_SHORT).show();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if(UPDATE_ADDR){
			UPDATE_ADDR = false;

			if(!ADDR_DETAIL.equals("")){
				lease_order_address.setText(ADDR_DETAIL);
			}
			
			if(!USER_NAME.equals("")){
				lease_order_name.setText(USER_NAME);
			}
			
			if(!USER_PHONE.equals("")){
				lease_order_phone.setText(USER_PHONE);
			}
			
			if(!USER_ADDTEXT.equals("")){
				lease_detail_addtext.setText(USER_ADDTEXT);
			}
			
			if(!DIS_PROMPT.equals("")){
				addr_hint_text.setText(DIS_PROMPT);
				addr_hint_text.setVisibility(View.VISIBLE);
			}else{
				addr_hint_text.setVisibility(View.GONE);
			}
			
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onResume(this);
	}
}
