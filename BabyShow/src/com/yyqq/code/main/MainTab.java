package com.yyqq.code.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbStrUtil;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.business.BusinessList;
import com.yyqq.code.business.BusinessSelectedListActivity;
import com.yyqq.code.business.UserOderList;
import com.yyqq.code.group.SuperGroupActivity;
import com.yyqq.code.grow.GrowActivity;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderDetailActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.service.SystemService;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GPSLocationUtils;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.MyTabHost;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class MainTab extends TabActivity {
	public static String TAG = "MainTab";
	public static MainTab instance;
	private FrameLayout frameLayout;
	private static int IMAGE_CAMERA_RESULT = 1;
	private static int IMAGE_IMAGE_RESULT = 2;
	private static int IMAGE_CROP_RESULT = 3;
	private static int IMAGE_CROP_RETURN = 4;
	public static boolean imgBoolean = false;
	private HomeKeyEventBroadCastReceiver receiver;
	private AlertDialog.Builder dialog;
	public static ArrayList<TextView> iconText = new ArrayList<TextView>();
	
	private RelativeLayout show_shared;
	private ImageView show_shared_cancel;
	private ImageView show_shared_show;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		
		Config.setActivityState(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_page);
		instance = this;
//		initAddXiuxiuDialog();
		
		// 启动消息推送接收服务
//		startService(new Intent(this, SystemService.class));
		
		tabHost = (MyTabHost) findViewById(android.R.id.tabhost);
		setTabs();
		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		frameLayout = tabHost.getTabContentView();
		currentTabID = getIntent().getIntExtra("tabid", 0);
		if (currentTabID != 0) {
			tabHost.setCurrentTab(currentTabID);
		}
		
		// 检查网络状况
		if (!Config.isConn(instance)) {
			this.showDialog(instance);
		}
		
		/**
		 * 通过消息推送打开本页面
		 * 
		 * */
		if(getIntent().hasExtra("intent_Info")){
			try {
				IntentConsole(MainTab.this, new JSONObject(getIntent().getStringExtra("intent_Info")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		show_shared_show = (ImageView) findViewById(R.id.show_shared_show);
		show_shared_cancel = (ImageView) findViewById(R.id.show_shared_cancel);
		show_shared = (RelativeLayout) findViewById(R.id.show_shared);
		
		// 获取首页弹窗
		getMainAlter();
	}

	@Override
	protected void onStart() {
		super.onStart();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				BimpUtil.drr.clear();
				BimpUtil.time.clear();
				BimpUtil.bmp.clear();
				BimpUtil.max = 0;
				PhotoEdit.imgUris = "";
			}
		});
	}

	class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
		static final String SYSTEM_REASON = "reason";
		static final String SYSTEM_HOME_KEY = "homekey";// home key
		static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				newMessage.setVisibility(TextView.GONE);
				break;
			}
		};
	};

	public void showNew() {
		Message msg2 = handler.obtainMessage();
		msg2.what = 1;
		msg2.sendToTarget();
		SharedPreferences sp = instance.getSharedPreferences(
				"babyshow_system_count", Context.MODE_PRIVATE);
		newMessage.setText(sp.getInt("notice", 0) + "");
	}

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return Config.onKeyDown(keyCode, event, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void setTabs() {
		iconText.clear();
		addTab("myshow", R.drawable.main_home_select, "首页", GoodLife.class);
//		addTab("myshow", R.drawable.main_home_select, "首页", BusinessSelectedListActivity.class);
//		addTab("play", R.drawable.main_play_select, "学与玩", BusinessList.class);
		addTab("add", R.drawable.main_tab_toys, "玩具世界", MainToToysMainActivity.class);
		addTab("photo", R.drawable.main_record_select, "记录", GrowActivity.class);
		addTab("index", R.drawable.main_my_select, "我的", PersonalCenterActivity.class);
		tabHost.setOpenAnimation(true);
	}

	public static MyTabHost tabHost;

	public void addTab(String tag, int drawableID, String name, Class<?> c) {
		Intent intent = new Intent(this, c);
		intent.putExtra("visitor", getIntent().getStringExtra("visitor"));
		intent.putExtra("toMyShow", getIntent().getIntExtra("toMyShow", -1));
		intent.putExtra("lat", GPSLocationUtils.getLatitude(instance));
		intent.putExtra("lon", GPSLocationUtils.getLongitude(instance));
		TabHost.TabSpec spec = tabHost.newTabSpec(tag);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.main_pag_tab, getTabWidget(), false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		TextView icon_name = (TextView) tabIndicator
				.findViewById(R.id.icon_name);
		icon.setBackgroundDrawable(getResources().getDrawable(drawableID));
		icon_name.setText(name);
		iconText.add(icon_name);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		if (tag.equals("index")) {
			newMessage = (TextView) tabIndicator.findViewById(R.id.title);
		}
		tabHost.addTab(spec);
	}

	TextView newMessage;
//	public OnTouchListener UploadTouch = new OnTouchListener() {
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			}
//			return true;
//		}
//	};

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		SharedPreferences sp = instance.getSharedPreferences(
				"babyshow_system_count", Context.MODE_PRIVATE);
		if (sp.getInt("notice", 0) > 0) {
			showNew();
		} else {
			newMessage.setVisibility(View.GONE);
		}
		if (SystemService.intances != null
				&& SystemService.intances.mNotificationManager != null) {
			SystemService.intances.mNotificationManager.cancelAll();
		}
		if (imgBoolean) {
			Config.deleteFile(Config.ImageFile + "temp_image.jpg");
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, IMAGE_IMAGE_RESULT);
			imgBoolean = false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == IMAGE_IMAGE_RESULT) {
				Uri uri = intent.getData();
				String currentFilePath = getPath(uri);
				if (!AbStrUtil.isEmpty(currentFilePath)) {
					Intent intent2 = new Intent();
					intent2.setClass(MainTab.this, CropImage.class);
					intent2.putExtra("path", currentFilePath);
					startActivityForResult(intent2, IMAGE_CROP_RETURN);
				}
			} else if (requestCode == IMAGE_CAMERA_RESULT) {
				Intent intent2 = new Intent();
				intent2.setClass(MainTab.this, CropImage.class);
				intent2.putExtra("path", Config.ImageFile + "temp_image.jpg");
				startActivityForResult(intent2, IMAGE_CROP_RETURN);
			}
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	private void showDialog(final Activity context) {
		dialog = new Builder(context);
		dialog.setTitle("网络连接失败提示：");
		dialog.setMessage("· 尚未连接互联网或网络不稳定\n· 请检查是否有安全软件禁止访问网络");
		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});
		dialog.create().show();
	}

//	private void initAddXiuxiuDialog() {
//		add_xiuxiu_dialog = (RelativeLayout) findViewById(R.id.add_xiuxiu_dialog);
//		tab_add_cancel = (RelativeLayout) findViewById(R.id.tab_add_cancel);
//		tab_add_xiuxiu = (ImageView) findViewById(R.id.tab_add_xiuxiu);
//		tab_add_huati = (ImageView) findViewById(R.id.tab_add_huati);
//
//		add_xiuxiu_dialog.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cancelAddDialog();
//			}
//		});
//
//		// 取消添加
//		tab_add_cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cancelAddDialog();
//			}
//		});
//
//		// 添加秀秀
//		tab_add_xiuxiu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cancelAddDialog();
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(400);
//							instance.runOnUiThread(new Runnable() {
//
//								@Override
//								public void run() {
//									startActivity(new Intent(MainTab.this,
//											AddNewPostActivity.class));
//								}
//							});
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}).start();
//			}
//		});
//
//		// 添加话题
//		tab_add_huati.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cancelAddDialog();
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(400);
//							instance.runOnUiThread(new Runnable() {
//
//								@Override
//								public void run() {
//									startActivity(new Intent(MainTab.this,
//											AddNewPostActivity.class));
//								}
//							});
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}).start();
//			}
//		});
//	}
//
//	// 取消添加秀秀/话题提示框
//	public static void cancelAddDialog() {
//
//		tab_add_cancel.setVisibility(View.INVISIBLE);
//		ImageViewAnimationUtil animation01 = new ImageViewAnimationUtil(400, 600);
//		animation01.downToView(tab_add_xiuxiu);
//
//		ImageViewAnimationUtil animation02 = new ImageViewAnimationUtil(400, 600);
//		animation02.downToView(tab_add_huati);
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(400);
//					instance.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							tab_add_xiuxiu.setVisibility(View.GONE);
//							tab_add_huati.setVisibility(View.GONE);
//							add_xiuxiu_dialog.setVisibility(View.GONE);
//						}
//					});
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//
//	}
//
//	// 显示添加秀秀/话题提示框
//	public static void showAddDialog() {
//		add_xiuxiu_dialog.setVisibility(View.VISIBLE);
//		tab_add_xiuxiu.setVisibility(View.VISIBLE);
//		tab_add_huati.setVisibility(View.VISIBLE);
//
//		ImageViewAnimationUtil animation01 = new ImageViewAnimationUtil(400,
//				600);
//		animation01.upToView(tab_add_xiuxiu);
//
//		ImageViewAnimationUtil animation02 = new ImageViewAnimationUtil(400,
//				600);
//		animation02.upToView(tab_add_huati);
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(500);
//					instance.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							MainTab.tab_add_cancel.setVisibility(View.VISIBLE);
//						}
//					});
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
	
	/**
	 * 通过消息通送进入后，分发页面
	 * 
	 * */
	private void IntentConsole(Context context, JSONObject json){
		Intent toIntent = null;
		try {
			if(Integer.parseInt(json.getString("index")) == 7){
				toIntent = new Intent(context, SuperGroupActivity.class);
				toIntent.putExtra(SuperGroupActivity.GROUP_ID, json.getString("group_id"));
				toIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(toIntent);
			}else{
				switch(Integer.parseInt(json.getString("index"))){
				case 1:
					toIntent = new Intent(context, MainItemDetialActivity.class);
					toIntent.putExtra("img_id", json.getString("img_id"));
					break;
				case 2:
					toIntent = new Intent(context, VideoDetialActivity.class);
					PostBarTypeItem postBean = new PostBarTypeItem();
					postBean.setImg_id(json.getString("img_id"));
					postBean.setUser_id(json.getString("user_id"));
					postBean.setVideo_url(json.getString("video_url"));
					postBean.setImg(json.getString("img_url"));
					postBean.setImg_thumb_width(json.getString("img_w"));
					postBean.setImg_thumb_height(json.getString("img_h"));
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, postBean);
					toIntent.putExtras(bun);
					break;
				case 3:
					toIntent = new Intent(context, PostBarActivity.class);
					toIntent.putExtra("img_title", json.getString("title"));
					toIntent.putExtra("tag_id", json.getString("tag_id"));
					toIntent.putExtra("img_ids", json.getString("img_ids"));
					toIntent.putExtra("type", json.getString("type"));
					break;
				case 4:
					toIntent = new Intent(context, MainTab.class);
					toIntent.putExtra("tabid", 1);
					break;
				case 5:
					toIntent = new Intent(context, BusinessDetailActivity.class);
					toIntent.putExtra("business_id", json.getString("business_id"));
					break;
				case 6:
					toIntent = new Intent(context, UserOderList.class);
					break;
				case 8:
					toIntent = new Intent(context, MainItemDetialWebActivity.class);
					toIntent.putExtra(MainItemDetialWebActivity.LINK_URL, json.getString("web_url"));
					break;
				case 9:
					toIntent = new Intent(context, ToysLeaseMainTabActivity.class);
					break;
				case 10:
					toIntent = new Intent(context, ToysLeaseDetailActivity.class);
					toIntent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, json.getString("img_id"));
					break;
				case 11:
					toIntent = new Intent(context, ToysLeaseOrderDetailActivity.class);
					toIntent.putExtra(ToysLeaseOrderDetailActivity.ORDER_ID_KEY, json.getString("img_id"));
					break;
				};
				if(null != toIntent){
					MyApplication.isFromPush = true;
					toIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					context.startActivity(toIntent);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		AbHttpUtil.getInstance(this).get(ServerMutualConfig.GET_MAIN_ALERT + "&" + paramsHot.toString(), new AbStringHttpResponseListener() {
			
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
							if(StartActivity.mainAlertShow){
								show_shared.setVisibility(View.VISIBLE);
							}
							StartActivity.mainAlertShow = false;
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
									imagesClick(MainTab.this, json.getJSONObject("data").getString("type"), json.getJSONObject("data").getString("post_url"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MainTab.this, "没有更多了", 3).show();
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