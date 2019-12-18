package com.yyqq.code.toyslease.h5;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
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
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDepositActivity;
import com.yyqq.code.toyslease.ToysLeaseFeedbackActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.commen.adapter.ToysLeaseOrderDetailAdapter;
import com.yyqq.commen.adapter.ToysLeaseOrderInfoDetailAdapter;
import com.yyqq.commen.model.ToysLeaseOrderDetailBean;
import com.yyqq.commen.model.ToysLeaseOrderInfoBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 我的会员卡订单详情
 * 
 * */
public class ToysLeaseHtmlOrderCardDetailActivity extends BaseActivity implements OnPullDownListener{
	
	public static final String ORDER_ID_KEY = "order_id_key";
	private AbHttpUtil ab;
	private ToysLeaseOrderDetailAdapter adapter;
	private String business_id = "";
	private boolean showServer = false;
	public static ToysLeaseHtmlOrderCardDetailActivity toysLeaseOrderDetailActivity = null;
	private ArrayList<ToysLeaseOrderInfoBean> bodyList = new ArrayList<ToysLeaseOrderInfoBean>();
	private ToysLeaseOrderInfoDetailAdapter infoAdapter;
	private ArrayList<ToysLeaseOrderDetailBean> list = new ArrayList<ToysLeaseOrderDetailBean>();
	
	private LayoutInflater inflater;
	private PullDownView pullDownView;
	private ListView listView;
	private LinearLayout mRelativeLayout;
//	private TextView lease_order_top;
	private RecodeListView lease_order_list;
	private TextView lease_order_endtime;
	private TextView lease_order_type;
	private TextView lease_order_cancel;
	private ImageView lease_order_img;
	private TextView lease_order_title;
	private TextView toys_order_info_type;
	private TextView toys_order_info_price;
	private ImageView toys_order_info_card;
	private RelativeLayout lease_order_type_all;
	private TextView lease_order_change;
	private ImageView category_icon_url;
	private TextView toys_feedback;
	private RelativeLayout phone01;
	private RelativeLayout phone02;
	private TextView phone01_number;
	private ImageView phone01_call;
	private TextView phone02_number;
	private ImageView phone02_call;
	private RelativeLayout lease_end_center_webview;
	private RelativeLayout lease_end_center;
	private WebView toyslease_webview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_order_detail);
	}
	
	@Override
	protected void initView() {
		inflater = LayoutInflater.from(ToysLeaseHtmlOrderCardDetailActivity.this);
		lease_end_center_webview = (RelativeLayout) findViewById(R.id.lease_end_center_webview);
		lease_end_center = (RelativeLayout) findViewById(R.id.lease_end_center);
		toyslease_webview = (WebView) findViewById(R.id.toyslease_webview);
		mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.item_lease_order_detailtop, null);
		lease_order_list = (RecodeListView) mRelativeLayout.findViewById(R.id.lease_order_list);
		category_icon_url = (ImageView) mRelativeLayout.findViewById(R.id.category_icon_url);
		lease_order_list.setFocusable(false); // 消除listview焦点
		lease_order_img = (ImageView) mRelativeLayout.findViewById(R.id.lease_order_img);
		lease_order_title = (TextView) mRelativeLayout.findViewById(R.id.lease_order_title);
		toys_order_info_type = (TextView) mRelativeLayout.findViewById(R.id.toys_order_info_type);
		toys_order_info_price = (TextView) mRelativeLayout.findViewById(R.id.toys_order_info_price);
		toys_order_info_card = (ImageView) mRelativeLayout.findViewById(R.id.toys_order_info_card);
		lease_order_change = (TextView) findViewById(R.id.lease_order_change);
		pullDownView = (PullDownView) findViewById(R.id.list);
		listView = pullDownView.getListView();
		lease_order_endtime = (TextView) findViewById(R.id.lease_order_endtime);
		lease_order_type = (TextView) findViewById(R.id.lease_order_type);
		lease_order_cancel = (TextView) findViewById(R.id.lease_order_cancel);
		lease_order_cancel.setVisibility(View.GONE);
		lease_order_type_all = (RelativeLayout) findViewById(R.id.lease_order_type_all);
		toys_feedback = (TextView) findViewById(R.id.toys_feedback);
		phone01 = (RelativeLayout) findViewById(R.id.phone01);
		phone02 = (RelativeLayout) findViewById(R.id.phone02);
		phone01_number = (TextView) findViewById(R.id.phone01_number);
		phone02_number = (TextView) findViewById(R.id.phone02_number);
		phone01_call = (ImageView) findViewById(R.id.phone_01_call);
		phone02_call = (ImageView) findViewById(R.id.phone02_call);
		lease_end_center_webview.setVisibility(View.VISIBLE);
		lease_end_center.setVisibility(View.GONE);
	}

	@Override
	protected void initData() {
		
		lease_end_center_webview.setVisibility(View.VISIBLE);
		lease_end_center.setVisibility(View.GONE);
		
		String url = "http://www.meimei.yihaoss.top/H5/toy_count/order_cout.html?login_user_id="+Config.getUser(ToysLeaseHtmlOrderCardDetailActivity.this).uid+"&order_id="+getIntent().getStringExtra(ORDER_ID_KEY);
		MyWebviewUtils.initWebView(ToysLeaseHtmlOrderCardDetailActivity.this, toyslease_webview, null);
		toyslease_webview.loadUrl(url);

		toysLeaseOrderDetailActivity = this;
		ab = AbHttpUtil.getInstance(ToysLeaseHtmlOrderCardDetailActivity.this);
		pullDownView.setOnPullDownListener(this);
		pullDownView.enableAutoFetchMore(false, 1);// 设置不可以自动获取更多 滑到最后一个不会自动获取
		pullDownView.setShowHeader();// 显示并且可以使用头部刷新
		pullDownView.setHideFooter();
		pullDownView.setShowHeaderLayout(mRelativeLayout);
		
		if(null == infoAdapter){
			infoAdapter = new ToysLeaseOrderInfoDetailAdapter(ToysLeaseHtmlOrderCardDetailActivity.this, inflater, bodyList);
		}
		lease_order_list.setAdapter(infoAdapter);
		
		if(null == adapter){
			adapter = new ToysLeaseOrderDetailAdapter(inflater, list);
		}
		listView.setAdapter(adapter);
		listView.setDivider(null);
		
		onRefresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void setListener() {
		
		phone01_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(ToysLeaseHtmlOrderCardDetailActivity.this);
				dialog.setTitle("自由环球租赁");
				dialog.setMessage("确定要联系客服吗？");
				dialog.setNegativeButton("拨号", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:" + "18515394818"));
						startActivity(intent);
					}});
				
				dialog.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
							}});
				dialog.create().show();
			}
		});
		
		toys_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, ToysLeaseFeedbackActivity.class);
				in.putExtra(ToysLeaseFeedbackActivity.ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
				startActivity(in);
			}
		});
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseHtmlOrderCardDetailActivity.this.finish();
			}
		});
		
		lease_order_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
//				dialog.setTitle("自由环球租赁");
//				dialog.setMessage("选好您要换的玩具后，确定更换需要到购物车里完成");
//				dialog.setNegativeButton("去选玩具", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(ToysLeaseOrderDetailActivity.this, ToysLeaseMainAllActivity.class);
//						intent.putExtra(ToysLeaseMainAllActivity.IS_SHOW_CARD, true);
//						startActivity(intent);
//					}});
//				
//				dialog.setPositiveButton("先不换",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {}});
//				dialog.create().show();
			}
		});

	}
	
	@Override
	public void onRefresh() {
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseHtmlOrderCardDetailActivity.this).uid);
		params.put("order_id", getIntent().getStringExtra(ORDER_ID_KEY));
		
		ab.get(ServerMutualConfig.GET_ORDER_DETAIL + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						
//						// 判断是否使用web页面
//						if(!new JSONObject(content).getJSONObject("data").getString("post_url").equals("")){
//							lease_end_center_webview.setVisibility(View.VISIBLE);
//							lease_end_center.setVisibility(View.GONE);
//							MyWebviewUtils.initWebView(ToysLeaseOrderCardDetailActivity.this, toyslease_webview, null);
//							toyslease_webview.loadUrl(new JSONObject(content).getJSONObject("data").getString("post_url")+"?login_user_id="+Config.getUser(ToysLeaseOrderCardDetailActivity.this).uid+"&order_id="+getIntent().getStringExtra(ORDER_ID_KEY));
//						}else{
//						}
//							lease_end_center_webview.setVisibility(View.VISIBLE);
//							lease_end_center.setVisibility(View.GONE);
						
//						// 客服相关
//						if(new JSONObject(content).getJSONObject("data").getString("postman_mobile").equals("")){
//							phone02.setVisibility(View.GONE);
//						}else{
//							phone02.setVisibility(View.VISIBLE);
//							phone02_number.setText(new JSONObject(content).getJSONObject("data").getString("postman_mobile"));
//							phone02_call.setOnClickListener(new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									AlertDialog.Builder dialog = new Builder(ToysLeaseHtmlOrderCardDetailActivity.this);
//									dialog.setTitle("自由环球租赁");
//									dialog.setMessage("确定要联系配送小哥吗？");
//									dialog.setNegativeButton("拨号", new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//											Intent intent = new Intent(Intent.ACTION_CALL);
//											intent.setData(Uri.parse("tel:" + phone02_number.getText().toString().trim()));
//											startActivity(intent);
//										}});
//									
//									dialog.setPositiveButton("取消",
//											new DialogInterface.OnClickListener() {
//
//												@Override
//												public void onClick(DialogInterface arg0, int arg1) {
//													
//												}});
//									dialog.create().show();
//								}
//							});
//						}
//						
//						// 订单信息
//						bodyList.clear();
//						ToysLeaseOrderInfoBean infobean = null;
//						for(int i = 0 ; i < new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").length() ; i++){
//							infobean = new ToysLeaseOrderInfoBean();
//							infobean.setHint(new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").getJSONObject(i).getString("order_title"));
//							infobean.setNumber(new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").getJSONObject(i).getString("order_value"));
//							bodyList.add(infobean);
//						}
//						infoAdapter.notifyDataSetChanged();
//						
//						// 玩具信息
//						MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("img_thumb"), lease_order_img, R.drawable.def_image);
//						lease_order_title.setText(new JSONObject(content).getJSONObject("data").getString("business_title"));
//						toys_order_info_type.setText(new JSONObject(content).getJSONObject("data").getString("status_name"));
//						toys_order_info_price.setText(new JSONObject(content).getJSONObject("data").getString("every_sell_price"));
//						lease_order_endtime.setText(new JSONObject(content).getJSONObject("data").getString("order_post_time"));
//						lease_order_type.setText(new JSONObject(content).getJSONObject("data").getString("status_name"));
//						business_id = new JSONObject(content).getJSONObject("data").getString("business_id");
						
						// 分类图标
						if(new JSONObject(content).getJSONObject("data").has("size_img_thumb") && !new JSONObject(content).getJSONObject("data").getString("size_img_thumb").equals("")){
							category_icon_url.setVisibility(View.VISIBLE);
							MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("size_img_thumb"), category_icon_url, R.drawable.def_image);
						}else{
							category_icon_url.setVisibility(View.GONE);
						}
						
						// 是否可更换玩具
						if(new JSONObject(content).getJSONObject("data").getString("is_change").equals("0")){
							lease_order_change.setVisibility(View.GONE);
						}else{
							lease_order_change.setVisibility(View.GONE);
						}
						
						if(new JSONObject(content).getJSONObject("data").getString("order_status").equals("0")){
							// 物流信息
//							list.clear();
//							ToysLeaseOrderDetailBean bean = null;
//							for(int i = 0 ; i < new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").length() ; i++ ){
//								bean = new ToysLeaseOrderDetailBean();
//								bean.setImgUrl(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("img_thumb"));
//								bean.setTime(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("listing_create_time"));
//								bean.setType(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("listing_status_name"));
//								bean.setStyle(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("style"));
//								list.add(bean);
//							}
//							adapter.notifyDataSetChanged();
						}else if(new JSONObject(content).getJSONObject("data").getString("order_status").equals("2")){
							// 会员卡详情
//							list.clear();
//							adapter.notifyDataSetChanged();
//							DisplayMetrics DM = new DisplayMetrics();
//							ToysLeaseHtmlOrderCardDetailActivity.this.getWindowManager().getDefaultDisplay().getMetrics(DM);
//							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toys_order_info_card.getLayoutParams();
//							int oldwidth = params.width; // 原来的宽度
//							int oldheight = params.height;
//							
//							params.width = DM.widthPixels;
//							params.height = oldheight+(DM.widthPixels-oldwidth);
//							
//							toys_order_info_card.setLayoutParams(params);
//							toys_order_info_card.setVisibility(View.VISIBLE);
//							if(!new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(0).getString("img_thumb").equals("")){
//								MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(0).getString("img_thumb"), toys_order_info_card, R.drawable.def_image);
//							}
							if(new JSONObject(content).getJSONObject("data").getString("is_refund").equals("0")){
								lease_order_type_all.setVisibility(View.GONE);
							}
							
						}
						
						if(new JSONObject(content).getJSONObject("data").getString("is_refund").equals("1")){
							lease_order_cancel.setVisibility(View.VISIBLE);
							lease_order_cancel.setText(new JSONObject(content).getJSONObject("data").getString("refund_name"));
							lease_order_cancel.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									try {
										String refund_status = new JSONObject(content).getJSONObject("data").getString("refund_status");
										if(refund_status.equals("1")){ // 去支付
											if(new JSONObject(content).getJSONObject("data").getString("order_status").equals("2")){
												ToysLeaseOrderConfirmActivity.createNewOrder(ToysLeaseHtmlOrderCardDetailActivity.this, ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
											}else{
												Intent intent = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, ToysLeaseOrderConfirmActivity.class);
												intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
												startActivity(intent);
											}
										}else{ // 其它
											checkServer(refund_status, new JSONObject(content).getJSONObject("data").getString("refund_des"));
										}
//										else if(refund_status.equals("11")){ // 退押金
//											AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
//											dialog.setTitle("自由环球租赁");
//											dialog.setMessage("确定要退押金吗？");
//											dialog.setNegativeButton("取消",
//													new DialogInterface.OnClickListener() {
//							
//														@Override
//														public void onClick(DialogInterface dialog,
//																int which) {
//														}
//							
//													});
//											dialog.setPositiveButton("确定",
//													new DialogInterface.OnClickListener() {
//							
//														@Override
//														public void onClick(DialogInterface arg0, int arg1) {
//															cancelOrder("11");
//														}
//													});
//											dialog.create().show();
//										}
									} catch (JSONException e) {
										e.printStackTrace();
									} 
								}
							});
						}else{
							lease_order_cancel.setVisibility(View.GONE);
						}
						
					}else{
						Toast.makeText(ToysLeaseHtmlOrderCardDetailActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				pullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
				pullDownView.notifyDidMore();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 取消订单或退押金
	 * */
	private void cancelOrder(String type){
		Config.showProgressDialog(ToysLeaseHtmlOrderCardDetailActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseHtmlOrderCardDetailActivity.this).uid);
		params.put("order_id", getIntent().getStringExtra(ORDER_ID_KEY));
		params.put("source", "0");
		params.put("refund_status", type);
		ab.get(ServerMutualConfig.CANCEL_ORDER + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						onRefresh();
						Toast.makeText(ToysLeaseHtmlOrderCardDetailActivity.this, "取消成功", 2).show();
					}else{
						Toast.makeText(ToysLeaseHtmlOrderCardDetailActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
	 * 查询是否需要收取服务费
	 * */
	private void checkServer(final String refund_status, String refund_des){
//		Config.showProgressDialog(this, false, null);
//		final AbRequestParams params = new AbRequestParams();
//		params.put("login_user_id", Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
//		params.put("order_id", getIntent().getStringExtra(ORDER_ID_KEY));
//		ab.get(ServerMutualConfig.CHECK_SERVER + "&" + params.toString(),new AbStringHttpResponseListener() {
//			
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				super.onSuccess(statusCode, content);
//				try {
//					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						AlertDialog.Builder dialog = new Builder(ToysLeaseHtmlOrderCardDetailActivity.this);
						dialog.setTitle("自由环球租赁");
						dialog.setMessage(refund_des);
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
										cancelOrder(refund_status);
									}
								});
						dialog.create().show();
//					}else{
//						Toast.makeText(ToysLeaseOrderDetailActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			@Override
//			public void onFinish() {
//				super.onFinish();
//				Config.dismissProgress();
//			}
//			
//			@Override
//			public void onFailure(int statusCode, String content,
//					Throwable error) {
//				super.onFailure(statusCode, content, error);
//			}
//		});
	}
	
	/**
	 * JS回调的原生方法
	 * */
	public class JSCallback{
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToInterestPage(){
			Intent intent = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, ToysLeaseHtmlInterstActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToLoginPage(){
			Intent intent = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, WebLoginActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 获取用户ID
		 * */
		@JavascriptInterface
		public String callMethodGetLoginUserId(){
			return Config.getUser(ToysLeaseHtmlOrderCardDetailActivity.this).uid;
		}
		
		/**
		 * 判断用户是否已经登录
		 * */
		@JavascriptInterface
		public String callMethodIsLogin(){
			if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseHtmlOrderCardDetailActivity.this).uid.equals("")){
				return "0"; // 未登录
			}else{
				return "1"; // 已登陆
			}
		}
		
		/**
		 * 进入玩具详情
		 * */
		@JavascriptInterface
		public void callMethodToToysDetail(String toys_id){
			if(null == toys_id || toys_id.equals("") || toys_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlOrderCardDetailActivity.this, "找不到这个玩具哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, ToysLeaseHtmlDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlDetailActivity.TOYS_DETAIL_KEY, toys_id);
			startActivity(intent);
		}
		
	
		/**
		 * JS调用的提示Toast方法
		 * */
		@JavascriptInterface
		public void showToast(String toastStr) {
			Toast.makeText(ToysLeaseHtmlOrderCardDetailActivity.this, toastStr, Toast.LENGTH_LONG).show();
		}
		
		/**
		 * JS调用的跳转邀请页
		 * */
		@JavascriptInterface
		public void callMethodToInvite() {
			Intent intent = new Intent(ToysLeaseHtmlOrderCardDetailActivity.this, InviteFriendActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 退出浏览器
		 * */
		@JavascriptInterface
		public void callMethodFinish(){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					ToysLeaseHtmlOrderCardDetailActivity.this.finish();
				}
			});
		}
		 
	 }

}
