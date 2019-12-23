package com.yyqq.code.toyslease;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.business.BusinessSelectedListActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainOrderActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.code.toyslease.version_93.MainToysAllActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.commen.view.MyTabHost;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ToysLeaseMainTabActivity extends TabActivity {
	
	// 网络请求对象
	private static AbHttpUtil ab;
	public static String TAG = "MainTab";
	public static ToysLeaseMainTabActivity instance;
	public static String order_id = "";
	public static ToysLeaseMainTabActivity toysLeaseMainTabActivity = null;
	public static ArrayList<TextView> iconText = new ArrayList<TextView>();
	
	public static ImageView cartIcon = null;
	private RelativeLayout show_shared;
	private ImageView show_shared_cancel;
	private ImageView show_shared_show;
	public static boolean sharedCount = false; // 分享是否需要计数
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		ab = AbHttpUtil.getInstance(this);
		instance = this;
		toysLeaseMainTabActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_page);
		tabHost = (MyTabHost) findViewById(android.R.id.tabhost);
		setTabs();
		currentTabID = getIntent().getIntExtra("tabid", 0);
		
		if (currentTabID != 0) {
			tabHost.setCurrentTab(currentTabID);
		}
		
		// 需要进入订单详情
		if(getIntent().hasExtra("order_id")){
			order_id = getIntent().getStringExtra("order_id");
			tabHost.setCurrentTab(3);
		}
		
		// 清空默认筛选条件
		MainToysAllActivity.toys_age = "";
		MainToysAllActivity.toys_age_text = "";
		MainToysAllActivity.toys_brand = "";
		MainToysAllActivity.toys_brand_text = "";
		MainToysAllActivity.toys_type = "";
		MainToysAllActivity.toys_type_text = "";
		MainToysAllActivity.toys_rank  = "";
		MainToysAllActivity.toys_rank_text = "";
		
		show_shared_show = (ImageView) findViewById(R.id.show_shared_show);
		show_shared_cancel = (ImageView) findViewById(R.id.show_shared_cancel);
		show_shared = (RelativeLayout) findViewById(R.id.show_shared);

		// 获取弹窗信息
		getMainAlter();
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				newMessage.setVisibility(TextView.VISIBLE);
				break;
			}
		};
	};
	

	/** 记录当前分页ID */
	private int currentTabID = 0;

	private class TabHostTouch extends SimpleOnGestureListener {
		/** 滑动翻页所需距离 */
		private static final int ON_TOUCH_DISTANCE = 200;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() <= (-ON_TOUCH_DISTANCE)) {
				currentTabID = tabHost.getCurrentTab() - 1;
				if (currentTabID < 0) {
					currentTabID = tabHost.getTabCount() - 1;
				}
			} else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
				currentTabID = tabHost.getCurrentTab() + 1;
				if (currentTabID >= tabHost.getTabCount()) {
					currentTabID = 0;
				}
			}
			tabHost.setCurrentTab(currentTabID);
			return true;
		}
	}

	public void setTabs() {
		iconText.clear();
		addTab("toys_home", R.drawable.toys_home_select, "首页", MainToysAllActivity.class);
		addTab("toys_all", R.drawable.toys_all_select, "全部", MainToysAllActivity.class);
		addTab("toys_cart", R.drawable.toys_cart_select, "购物车", ToysLeaseMainShoppingCartActivity.class);
		addTab("toys_order", R.drawable.toys_order_select, "订单", ToysLeaseMainOrderActivity.class);
//		tabHost.setOpenAnimation(true);
		tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals("toys_home")){
					finish();
				}
			}
		});
	}

	public static MyTabHost tabHost;

	public void addTab(String tag, int drawableID, String name, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec(tag);
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.main_pag_tab, getTabWidget(), false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		TextView icon_name = (TextView) tabIndicator.findViewById(R.id.icon_name);
		icon.setBackgroundDrawable(getResources().getDrawable(drawableID));
		icon_name.setText(name);
		iconText.add(icon_name);
		// icon_name.setShadowLayer(1F, 1F, 0F, R.color.yinying);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		if (name.equals("购物车")) {
			cartIcon = icon;
			newMessage = (TextView) tabIndicator.findViewById(R.id.title);
		}
		tabHost.addTab(spec);
	}

	public static TextView newMessage;
	public OnTouchListener UploadTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			}
			return true;
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if(ShoppingCartUtils.getCartNumber(this) != 0){
			ToysLeaseMainTabActivity.newMessage.setVisibility(View.VISIBLE);
			ToysLeaseMainTabActivity.newMessage.setText(ShoppingCartUtils.getCartNumber(this)+"");
			// 更新购物车数量角标
		}
		ShoppingCartUtils.updateShoppingCartNumber(instance, true);
	}
	
	@Override
	protected void onStart() {
		// 显示悬浮窗  
//		Intent intent = new Intent(ToysLeaseMainTabActivity.this, InviteStartService.class);    
//		startService(intent);    
		super.onStart();
	}
	
    @Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		// 销毁悬浮窗  
//		Intent intent = new Intent(ToysLeaseMainTabActivity.this, InviteStartService.class);    
//		stopService(intent);   
	}

    
	/**
	 * 获取弹窗信息
	 * */
	private void getMainAlter() {
		
		// 判断网络是否正常
		if(!Config.isConn(this)){
			return;
		}
		
		final AbRequestParams paramsHot = new AbRequestParams();
		paramsHot.put("login_user_id", Config.getUser(this).uid);
		ab.get(ServerMutualConfig.GET_TOYS_MAIN_ALERT + "&" + paramsHot.toString(), new AbStringHttpResponseListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (!content.isEmpty()) {
					try {
						final JSONObject json = new JSONObject(content);
						
						// 不展示弹窗
						if(json.getJSONObject("data").getString("is_show").equals("0")){
							show_shared.setVisibility(View.GONE);
							return;
						}else{
							if(StartActivity.toysAlertShow){
								show_shared.setVisibility(View.VISIBLE);
							}
							StartActivity.toysAlertShow = false;
						}
						
						MyApplication.getInstance().display(json.getJSONObject("data").getString("img"), show_shared_show, R.drawable.def_image);
					
						show_shared.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								show_shared.setVisibility(View.GONE);
							}
						});
						
						show_shared_cancel.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								show_shared.setVisibility(View.GONE);
							}
						});
						
						show_shared_show.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								show_shared.setVisibility(View.GONE);
								try {
									imagesClick(ToysLeaseMainTabActivity.this, json.getJSONObject("data").getString("type"), json.getJSONObject("data").getString("post_url"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(ToysLeaseMainTabActivity.this, "没有更多了", 3).show();
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
	 * 首页弹窗跳转
	 * */
	private void imagesClick(Context context, String type, String value) {
		
		Intent intent = new Intent();
		
		if("2".equals(type)) { // 帖子
			intent.setClass(context, MainItemDetialActivity.class);
			intent.putExtra("img_id", value);
		} else if ("3".equals(type)) { // 视频
			// 暂不支持			
		}else if("4".equals(type)){ // 精选商家列表
			intent = new Intent(context, BusinessSelectedListActivity.class);
			intent.putExtra(BusinessSelectedListActivity.CITY_ID, value);
			intent.putExtra(BusinessSelectedListActivity.CITY_NAME, "精选商家");
		}else if("22".equals(type)){ // 商家详情
			intent.setClass(context, BusinessDetailActivity.class);
			intent.putExtra("business_id", value);
			intent.putExtra("business_name", "精选商家");
		}else if("41".equals(type)){ // 外链
			intent = new Intent(context, MainItemDetialWebActivity.class);
			intent.putExtra(MainItemDetialWebActivity.LINK_URL, value);
		}else if("7".equals(type)){ // 玩具详情
			intent = new Intent(context, ToysLeaseDetailActivity.class);
			intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, value);
		}else if("32".equals(type)){ // 群
			GroupRelevantUtils.CheckIntent(context, value);
		}else if("51".equals(type)){ // 玩具世界首页
			intent = new Intent(context, ToysLeaseMainTabActivity.class);
		}else if("11".equals(type)){ // 邀请好友
			intent = new Intent(context, InviteFriendActivity.class);
		}
		
		startActivity(intent);
		
	}
}
