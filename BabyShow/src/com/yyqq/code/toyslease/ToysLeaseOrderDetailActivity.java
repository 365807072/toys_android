package com.yyqq.code.toyslease;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.business.BusinessSelectedListActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainAllActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainOrderActivity;
import com.yyqq.code.toyslease.version_93.MainToysAllActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.adapter.ToysLeaseOrderDetailAdapter;
import com.yyqq.commen.adapter.ToysLeaseOrderInfoDetailAdapter;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.ToysLeaseOrderDetailBean;
import com.yyqq.commen.model.ToysLeaseOrderInfoBean;
import com.yyqq.commen.share.GroupSharedUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 我的玩具订单详情
 * 
 * */
public class ToysLeaseOrderDetailActivity extends BaseActivity implements OnPullDownListener{
	
	public static final String ORDER_ID_KEY = "order_id_key";
	private AbHttpUtil ab;
	private ToysLeaseOrderDetailAdapter adapter;
	private String business_id = "";
	private boolean showServer = false;
	public static ToysLeaseOrderDetailActivity toysLeaseOrderDetailActivity = null;
	private ArrayList<ToysLeaseOrderInfoBean> bodyList = new ArrayList<ToysLeaseOrderInfoBean>();
	private ToysLeaseOrderInfoDetailAdapter infoAdapter;
	private ArrayList<ToysLeaseOrderDetailBean> list = new ArrayList<ToysLeaseOrderDetailBean>();
	private MainListItemBean sharedBean;
	private int source;
	private String order_id = "";
	
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
	private RelativeLayout show_shared_01;
	private ImageView show_shared_cancel_01;
	private ImageView show_shared_show_01;
	private ImageView show_shared_all_01;
	private RelativeLayout show_shared;
	private ImageView show_shared_cancel;
	private ImageView show_shared_show;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_order_detail);
	}
	
	@Override
	protected void initView() {
		inflater = LayoutInflater.from(ToysLeaseOrderDetailActivity.this);
		show_shared_show = (ImageView) findViewById(R.id.show_shared_show);
		show_shared_cancel = (ImageView) findViewById(R.id.show_shared_cancel);
		show_shared = (RelativeLayout) findViewById(R.id.show_shared);
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
		show_shared_show_01 = (ImageView) findViewById(R.id.show_shared_show_01);
		show_shared_cancel_01 = (ImageView) findViewById(R.id.show_shared_cancel_01);
		show_shared_01 = (RelativeLayout) findViewById(R.id.show_shared_01);
		show_shared_all_01 = (ImageView) findViewById(R.id.show_shared_all_01);
	}

	@Override
	protected void initData() {
		ToysLeaseMainOrderActivity.NEED_UPDATE = false;
		toysLeaseOrderDetailActivity = this;
		ab = AbHttpUtil.getInstance(ToysLeaseOrderDetailActivity.this);
		pullDownView.setOnPullDownListener(this);
		pullDownView.enableAutoFetchMore(false, 1);// 设置不可以自动获取更多 滑到最后一个不会自动获取
		pullDownView.setShowHeader();// 显示并且可以使用头部刷新
		pullDownView.setHideFooter();
		pullDownView.setShowHeaderLayout(mRelativeLayout);
		
		
		order_id = getIntent().getStringExtra(ORDER_ID_KEY);
		
		if(null == infoAdapter){
			infoAdapter = new ToysLeaseOrderInfoDetailAdapter(ToysLeaseOrderDetailActivity.this, inflater, bodyList);
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
		
		show_shared_show_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show_shared_01.setVisibility(View.GONE);
				IntentConsole(sharedBean);
			}
		});
		
		show_shared_all_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentConsole(sharedBean);
			}
		});
		
		show_shared_cancel_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show_shared_01.setVisibility(View.GONE);
			}
		});
		
		show_shared_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show_shared.setVisibility(View.GONE);
			}
		});
		
		show_shared.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show_shared.setVisibility(View.GONE);
			}
		});
		
		phone01_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
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
				Intent in = new Intent(ToysLeaseOrderDetailActivity.this, ToysLeaseFeedbackActivity.class);
				in.putExtra(ToysLeaseFeedbackActivity.ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
				startActivity(in);
			}
		});
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseOrderDetailActivity.this.finish();
			}
		});
		
		lease_order_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
				dialog.setTitle("自由环球租赁");
				dialog.setMessage("选好您要换的玩具后，确定更换需要到购物车里完成");
				dialog.setNegativeButton("去选玩具", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 显示返回按钮
						MainToysAllActivity.isShowBack = true;
						// 清空默认筛选条件
						MainToysAllActivity.toys_age = "";
						MainToysAllActivity.toys_age_text = "";
						MainToysAllActivity.toys_brand = "";
						MainToysAllActivity.toys_brand_text = "";
						MainToysAllActivity.toys_type = "";
						MainToysAllActivity.toys_type_text = "";
						MainToysAllActivity.toys_rank  = "";
						MainToysAllActivity.toys_rank_text = "";
						Intent intent = new Intent(ToysLeaseOrderDetailActivity.this, MainToysAllActivity.class);
//						intent.putExtra(ToysLeaseMainAllActivity.IS_SHOW_CARD, true);
						startActivity(intent);
					}});
				
				dialog.setPositiveButton("先不换",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {}});
				dialog.create().show();
			}
		});

	}
	
	@Override
	public void onRefresh() {
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
		params.put("order_id", getIntent().getStringExtra(ORDER_ID_KEY));
		
		ab.get(ServerMutualConfig.GET_ORDER_DETAIL + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						
						// 判断是否使用web页面
						if(!new JSONObject(content).getJSONObject("data").getString("post_url").equals("")){
							lease_end_center_webview.setVisibility(View.VISIBLE);
							lease_end_center.setVisibility(View.GONE);
							MyWebviewUtils.initWebView(ToysLeaseOrderDetailActivity.this, toyslease_webview, null);
							toyslease_webview.loadUrl(new JSONObject(content).getJSONObject("data").getString("post_url")+"?login_user_id="+Config.getUser(ToysLeaseOrderDetailActivity.this).uid+"&order_id="+getIntent().getStringExtra(ORDER_ID_KEY));
						}else{
							lease_end_center_webview.setVisibility(View.GONE);
							lease_end_center.setVisibility(View.VISIBLE);
						}
						
						// 客服相关
						if(new JSONObject(content).getJSONObject("data").getString("postman_mobile").equals("")){
							phone02.setVisibility(View.GONE);
						}else{
							phone02.setVisibility(View.VISIBLE);
							phone02_number.setText(new JSONObject(content).getJSONObject("data").getString("postman_mobile"));
							phone02_call.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
									dialog.setTitle("自由环球租赁");
									dialog.setMessage("确定要联系配送小哥吗？");
									dialog.setNegativeButton("拨号", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent intent = new Intent(Intent.ACTION_CALL);
											intent.setData(Uri.parse("tel:" + phone02_number.getText().toString().trim()));
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
						}
						// 订单信息
						bodyList.clear();
						ToysLeaseOrderInfoBean infobean = null;
						for(int i = 0 ; i < new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").length() ; i++){
							infobean = new ToysLeaseOrderInfoBean();
							infobean.setHint(new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").getJSONObject(i).getString("order_title"));
							infobean.setNumber(new JSONObject(content).getJSONObject("data").getJSONArray("orderinfo").getJSONObject(i).getString("order_value"));
							bodyList.add(infobean);
						}
						infoAdapter.notifyDataSetChanged();
						
						// 玩具信息
						MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("img_thumb"), lease_order_img, R.drawable.def_image);
						lease_order_title.setText(new JSONObject(content).getJSONObject("data").getString("business_title"));
						toys_order_info_type.setText(new JSONObject(content).getJSONObject("data").getString("status_name"));
						toys_order_info_price.setText(new JSONObject(content).getJSONObject("data").getString("every_sell_price"));
						lease_order_endtime.setText(new JSONObject(content).getJSONObject("data").getString("order_post_time"));
						lease_order_type.setText(new JSONObject(content).getJSONObject("data").getString("status_name"));
						business_id = new JSONObject(content).getJSONObject("data").getString("business_id");
						// 分类图标
						if(new JSONObject(content).getJSONObject("data").has("new_size_img_thumb") && !new JSONObject(content).getJSONObject("data").getString("new_size_img_thumb").equals("")){
							category_icon_url.setVisibility(View.VISIBLE);
							MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("new_size_img_thumb"), category_icon_url, R.drawable.def_image);
						}else{
							category_icon_url.setVisibility(View.GONE);
						}
						
						// 是否可更换玩具
						if(new JSONObject(content).getJSONObject("data").getString("is_change").equals("0")){
							lease_order_change.setVisibility(View.VISIBLE);
						}else{
							lease_order_change.setVisibility(View.GONE);
						}
						
						if(new JSONObject(content).getJSONObject("data").getString("order_status").equals("0")){
							
							source = 3;
							
							// 提示分享
							if(ToysLeasePayConfirmActivity.showShared){
								getSharedInfo();
							}else{
								show_shared.setVisibility(View.GONE);
							}
							
							// 物流信息
							list.clear();
							ToysLeaseOrderDetailBean bean = null;
							for(int i = 0 ; i < new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").length() ; i++ ){
								bean = new ToysLeaseOrderDetailBean();
								bean.setImgUrl(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("img_thumb"));
								bean.setTime(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("listing_create_time"));
								bean.setType(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("listing_status_name"));
								bean.setStyle(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(i).getString("style"));
								list.add(bean);
							}
							adapter.notifyDataSetChanged();
						}else if(new JSONObject(content).getJSONObject("data").getString("order_status").equals("2")){
							
							source = 1;
							
							// 会员卡详情
							list.clear();
							adapter.notifyDataSetChanged();
							DisplayMetrics DM = new DisplayMetrics();
							ToysLeaseOrderDetailActivity.this.getWindowManager().getDefaultDisplay().getMetrics(DM);
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toys_order_info_card.getLayoutParams();
							int oldwidth = params.width; // 原来的宽度
							int oldheight = params.height;
							
							params.width = DM.widthPixels;
							params.height = oldheight+(DM.widthPixels-oldwidth);
							
							toys_order_info_card.setLayoutParams(params);
							toys_order_info_card.setVisibility(View.VISIBLE);
							if(!new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(0).getString("img_thumb").equals("")){
								MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getJSONArray("order_listing").getJSONObject(0).getString("img_thumb"), toys_order_info_card, R.drawable.def_image);
							}
							if(new JSONObject(content).getJSONObject("data").getString("is_refund").equals("0")){
								lease_order_type_all.setVisibility(View.GONE);
							}
							
						}
						
						// 根据订单信息，获取提示分享信息
						getActivityInfo();
						
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
												ToysLeaseOrderConfirmActivity.createNewOrder(ToysLeaseOrderDetailActivity.this, ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
											}else{
												Intent intent = new Intent(ToysLeaseOrderDetailActivity.this, ToysLeaseOrderConfirmActivity.class);
												intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_ORDER_ID, getIntent().getStringExtra(ORDER_ID_KEY));
												startActivity(intent);
											}
										}else{ // 其它
											checkServer(refund_status, new JSONObject(content).getJSONObject("data").getString("refund_des"));
										}
									} catch (JSONException e) {
										e.printStackTrace();
									} 
								}
							});
						}else{
							lease_order_cancel.setVisibility(View.GONE);
						}
						
					}else{
						Toast.makeText(ToysLeaseOrderDetailActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
		Config.showProgressDialog(ToysLeaseOrderDetailActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
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
						Toast.makeText(ToysLeaseOrderDetailActivity.this, "取消成功", 2).show();
					}else{
						Toast.makeText(ToysLeaseOrderDetailActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
						AlertDialog.Builder dialog = new Builder(ToysLeaseOrderDetailActivity.this);
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
	 * 获取分享信息
	 * */
	private void getSharedInfo(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		
		ab.get(ServerMutualConfig.GET_SHARED_INFO + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						if(new JSONObject(content).getJSONObject("data").getString("display_status").equals("1")){
							MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("icon_img"), show_shared_show, R.drawable.def_image_toys);
							show_shared.setVisibility(View.VISIBLE);
							ToysLeasePayConfirmActivity.showShared = false;
							
							show_shared_show.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									show_shared.setVisibility(View.GONE);
									ToysLeasePayConfirmActivity.showShared = false;
									
									try {
										GroupSharedUtils.groupShareBean bean = new GroupSharedUtils.groupShareBean();
										bean.setContext(ToysLeaseOrderDetailActivity.this);
										bean.setMore(true);
										bean.setShare_img(new JSONObject(content).getJSONObject("data").getString("window_img"));
										bean.setShare_text(new JSONObject(content).getJSONObject("data").getString("second_title"));
										bean.setShare_title(new JSONObject(content).getJSONObject("data").getString("main_title"));
										bean.setShare_url(new JSONObject(content).getJSONObject("data").getString("post_url"));
										GroupSharedUtils.SharedGroup(bean, new JSONObject(content).getJSONObject("data").getString("activity_post_url_new"));
										
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
							
						}else{
							show_shared.setVisibility(View.GONE);
							ToysLeasePayConfirmActivity.showShared = false;
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
				Config.dismissProgress();
			}
		});
	}
	
	/**
	 * 获取分享信息
	 * */
	private void getActivityInfo(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("source", source+"");
		params.put("order_id", getIntent().getStringExtra(ORDER_ID_KEY));	
		
		ab.get(ServerMutualConfig.GET_DROER_DETAIL_ACTIVITY + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						
						// 弹窗
						if(new JSONObject(content).getJSONObject("data").getString("is_show_window").equals("1")){
							show_shared_01.setVisibility(View.VISIBLE);
							MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("window_img"), show_shared_show_01, R.drawable.def_image_toys);							
						}else{
							show_shared_01.setVisibility(View.GONE);
						}
						
						// 浮标
						if(new JSONObject(content).getJSONObject("data").getString("is_show_float").equals("1")){
							show_shared_all_01.setVisibility(View.VISIBLE);
							MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("float_img"), show_shared_all_01, R.drawable.def_image_toys);							
						}else{
							show_shared_all_01.setVisibility(View.GONE);
						}
						
						sharedBean = new MainListItemBean();
						sharedBean.setType(new JSONObject(content).getJSONObject("data").getString("type"));
						sharedBean.setId(new JSONObject(content).getJSONObject("data").getString("post_url"));
						sharedBean.setImage_url(new JSONObject(content).getJSONObject("data").getString("share_img"));
						sharedBean.setImg_description(new JSONObject(content).getJSONObject("data").getString("second_title"));
						sharedBean.setImg_title(new JSONObject(content).getJSONObject("data").getString("main_title"));
						sharedBean.setPost_url(new JSONObject(content).getJSONObject("data").getString("post_url"));
						
						// 是否分享小程序
						if(new JSONObject(content).getJSONObject("data").getString("is_wechat").equals("1")){ 
							sharedBean.setVideo_url(new JSONObject(content).getJSONObject("data").getString("wechat_post_url"));
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
				Config.dismissProgress();
				Toast.makeText(ToysLeaseOrderDetailActivity.this, content, 5).show();
			}
		});
	}
	
	/**
	 * 跳转管理
	 * 1. 被点击的View
	 * 2. 跳转至页面
	 * */
	public void IntentConsole(final MainListItemBean bean){
		if(bean.getType().equals("52")){
			GroupSharedUtils.groupShareBean shareBean = new GroupSharedUtils.groupShareBean();
			shareBean.setMore(true);
			shareBean.setContext(ToysLeaseOrderDetailActivity.this);
			shareBean.setShare_img(bean.getImage_url());
			shareBean.setShare_text(bean.getImg_description());
			shareBean.setShare_title(bean.getImg_title());
			shareBean.setShare_url(bean.getPost_url()+"?"+"order_id="+order_id+"&login_user_id="+Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
			
			if(!bean.getVideo_url().equals("")){
				shareBean.setWxPath(bean.getVideo_url()+"?"+"order_id="+order_id+"&login_user_id="+Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
			}
			GroupSharedUtils.SharedGroup(shareBean);
		}else{
			Intent intent = null;
			switch (Integer.parseInt(bean.getType())) {
			case 2:
				// 帖子详情
				intent = new Intent(ToysLeaseOrderDetailActivity.this, MainItemDetialActivity.class);
				intent.putExtra("user_id", Config.getUser(ToysLeaseOrderDetailActivity.this).uid);
				intent.putExtra("img_id", bean.getPost_url());
				break;
				
			case 41:
				// 外链
				intent = new Intent(ToysLeaseOrderDetailActivity.this, MainItemDetialWebActivity.class);
				intent.putExtra(MainItemDetialWebActivity.LINK_URL, bean.getPost_url());
				break;
				
			case 7:
				// 玩具详情
				intent = new Intent(ToysLeaseOrderDetailActivity.this, ToysLeaseDetailActivity.class);
				intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, bean.getPost_url());
				break;
				
			case 11:
				// 邀请详情
				intent = new Intent(ToysLeaseOrderDetailActivity.this, InviteFriendActivity.class);
				break;
			}
			if(null != intent){
				ToysLeaseOrderDetailActivity.this.startActivity(intent);
			}
		}
	}

}
