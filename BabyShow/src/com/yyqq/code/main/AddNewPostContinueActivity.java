package com.yyqq.code.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ab.http.AbRequestParams;
import com.ab.util.AbStrUtil;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.commen.model.PostItem;
import com.yyqq.commen.service.CrashHandlerUtils;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.MyAnimations;
import com.yyqq.commen.utils.VideoComperssUtils;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 继续发布新贴页
 * */
public class AddNewPostContinueActivity extends Activity {
	
	// 调用相册后返回标记
	public static final String AddNewContinueActivity_TAG = "AddNewContinueActivity";
	
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private RelativeLayout activity_selectimg_send;
	private TextView create;
	private EditText msg;
//	private EditText post_title;
	public Activity context;
	public final String TAG = "PublishedActivity";
	public String imagepath = null;
	public static String msgString = "";
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private int img_cate;
	private static boolean isSelectWechat = true;
	private static boolean isSelectSina;
	private PostItem item;
	private ImageView shareWeixin, shareWeibo;
	private static final int TAKE_PICTURE = 0x000000;
	private static final int IMAGE_CROP_RETURN = 4;
	private static String path = "";
	private static int imgNum;
	private String shearImg;
	private int Share = 1; // 表示分享
	Platform weibo;
	private NotificationManager notificationManager;
	private Notification notification;
	private String group_id = "";
	public static boolean thisFinish = false;
	public static AddNewPostContinueActivity showshowAddActivity;
	
	private RelativeLayout add_new_succese_hint;
	private Button add_new_succese_finish;
	private Button add_new_succese_add;
	private LinearLayout showshow_add_image;
	private LinearLayout showshow_add_video;
	private ImageView showshow_add_video_thumbnail;
	private TextView showshow_cancel;
//	private EditText qunName;
	private String video_path = "";
	private Timer videoTimer = new Timer();
	private Uri videoUri = null;
	private String root_img_id = "";
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			context.finish();
		}
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			path = savedInstanceState.getString("path");
			imgNum = savedInstanceState.getInt("imgNum");
		}
		
		Intent intent = getIntent();

		Config.setActivityState(this);
		showshowAddActivity = this;
		setContentView(R.layout.activity_add_new_continue);
		
//		// 收集错误
//		CrashHandlerUtils crashHandler = CrashHandlerUtils.getInstance();    
//		crashHandler.init(this);
		
		context = this;
		// 消息通知
		notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		MobSDK.init(context);
		weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		MyAnimations.initOffset(this);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		isZhineng = getIntent().getBooleanExtra("iszhineng", false);
		imagepath = getIntent().getStringExtra("path");
		if(getIntent().hasExtra("group_id")){
			group_id = getIntent().getStringExtra("group_id");
		}
		root_img_id = intent.getStringExtra("root_img_id");

		if(!TextUtils.isEmpty(group_id)){
			findViewById(R.id.showshow_add_video).setVisibility(View.GONE);
			TextView showshow_center_text = (TextView) findViewById(R.id.showshow_center_text);
			showshow_center_text.setText("参与");
		}
		
		// Log.e("fanfan", group_id + "555");
		Init();
	}

	@Override
	protected void onResume() {
		
		if(!MyApplication.getVisitor().equals("0")){ // 没有登录
			Intent in = new Intent(AddNewPostContinueActivity.this, WebLoginActivity.class);
			startActivity(in);
			this.finish();
		}
		
		if(thisFinish){
			AddNewPostContinueActivity.this.finish();
			thisFinish = false;
		}
		adapter.notifyDataSetChanged();

		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		msgString = msg.getText().toString().trim();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		shareWeixin.setVisibility(View.GONE);
		shareWeibo.setVisibility(View.GONE);
//		if (isSelectWechat)
//			shareWeixin.setBackgroundResource(R.drawable.show_wxshare_sel);
//		else
//			shareWeixin.setBackgroundResource(R.drawable.show_wxshare);
//		if (isSelectSina)
//			shareWeibo.setBackgroundResource(R.drawable.show_wbshare_sel);
//		else
//			shareWeibo.setBackgroundResource(R.drawable.show_wbshare);
	}

	public void Init() {
		
		add_new_succese_hint = (RelativeLayout) findViewById(R.id.add_new_succese_hint);
		add_new_succese_finish = (Button) findViewById(R.id.add_new_succese_finish);
		add_new_succese_add = (Button) findViewById(R.id.add_new_succese_add);
		showshow_cancel = (TextView) findViewById(R.id.showshow_cancel);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		showshow_add_image = (LinearLayout) findViewById(R.id.showshow_add_image);
		showshow_add_video = (LinearLayout) findViewById(R.id.showshow_add_video);
		showshow_add_video_thumbnail = (ImageView) findViewById(R.id.showshow_add_video_thumbnail);
		
		// 完成
		add_new_succese_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(AddCheckActivity.isToMain){
					AddCheckActivity.isToMain = false;
//					GoodLife.TO_NEWEST = true;
					Intent intent = new Intent(AddNewPostContinueActivity.this, MainTab.class);
					intent.putExtra("tabid", 0);
					startActivity(intent);
				}
				context.finish();
			}
		});
		
		// 继续
		add_new_succese_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddNewPostContinueActivity.this, AddNewPostContinueActivity.class);
				intent.putExtra("root_img_id", root_img_id);
				intent.putExtra("group_id", group_id);
				context.startActivity(intent);
				context.finish();
			}
		});
		
		// 
		showshow_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddNewPostContinueActivity.this.finish();
			}
		});
		
		showshow_add_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				MyApplication.image_or_video = 0;
				findViewById(R.id.showshow_add_image_hint).setVisibility(View.GONE);
				showshow_add_video.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.VISIBLE);
				msgString = msg.getText().toString().trim();
				new PopupWindows(AddNewPostContinueActivity.this, noScrollgridview);
				InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(msg.getWindowToken(), 0);
			}
		});
		
		showshow_add_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				MyApplication.image_or_video = 1;
				showshow_add_image.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.GONE);
				VideoComperssUtils.intentLocationVideo(AddNewPostContinueActivity.this);
			}
		});
		
		// 分享到微博微信
		shareWeixin = (ImageView) findViewById(R.id.shareWeixin);
		shareWeibo = (ImageView) findViewById(R.id.shareWeibo);
		if (weibo.isClientValid()){
			isSelectSina = true;
			shareWeibo.setBackgroundResource(R.drawable.show_wbshare_sel);
		}
		shareWeixin.setOnClickListener(weixinClick);
		shareWeibo.setOnClickListener(weiboClick);

		msg = (EditText) findViewById(R.id.msg);
		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == 0) {
					if (arg2 == 10) {
						Toast.makeText(context, "最多9张", 1).show();
					} else {
						msgString = msg.getText().toString().trim();
						new PopupWindows(AddNewPostContinueActivity.this,
								noScrollgridview);
						InputMethodManager inputMethodManager = (InputMethodManager) context
								.getApplicationContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(
								msg.getWindowToken(), 0);
					}

				}
			}
		});
		activity_selectimg_send = (RelativeLayout) findViewById(R.id.activity_selectimg_send);
		create = (TextView) findViewById(R.id.create);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(msg.getText().toString().trim().isEmpty() && adapter.getCount() == 1) {
					Toast.makeText(context, "什么都没有，完善一下吧", 0).show();
					return;
				}
				
				InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(msg.getWindowToken(), 0);
				
//				if (!TextUtils.isEmpty(group_id)) {
					notification = new Notification(R.drawable.icon, "正在上传",System.currentTimeMillis());
					notification.flags |= Notification.FLAG_ONGOING_EVENT;
					notification.setLatestEventInfo(context, "自由环球租赁", "正在上传", null);
					notificationManager.notify(2, notification);
					img_cate = 0;
					Config.showProgressDialog(context, false, null);
					new Thread(new Runnable() {

						@Override
						public void run() {
							Send();
						}
					}).start();
//				} else {
//					if (!areButtonsShowing) {
//						MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
//						create.startAnimation(MyAnimations.getRotateAnimation(0, -360, 300));
//					} else {
//						MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
//						create.startAnimation(MyAnimations.getRotateAnimation(-360, 0, 3));
//					}
//					areButtonsShowing = !areButtonsShowing;
//				}
			}
		});

		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {

			composerButtonsWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener() {
						@SuppressLint("NewApi")
						@Override
						public void onClick(View arg0) {
							
							MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
							create.startAnimation(MyAnimations.getRotateAnimation(-360, 0, 3));
							
							if(video_path.isEmpty()){
								notification = new Notification(R.drawable.icon, "您的新贴正在发布", System.currentTimeMillis());
								notification.flags |= Notification.FLAG_ONGOING_EVENT;
								notification.setLatestEventInfo(context, "自由环球租赁", "您的新贴正在发布", null);
								notificationManager.notify(2, notification);
							}
							
							switch (arg0.getId()) {
							case R.id.open_permission:
								img_cate = 0;
								Config.showProgressDialog(context, false, null);
								new Thread(new Runnable() {

									@Override
									public void run() {
										Send();
									}
								}).start();
								
								break;
							case R.id.friend_permession:
								img_cate = 1;
								 Config.showProgressDialog(context, false, null);
								new Thread(new Runnable() {

									@Override
									public void run() {
										Send();
									}
								}).start();
								
								break;
							// case R.id.share_permession:
							// img_cate = 2;
							// // Config.showProgressDialog(context, false,
							// // null);
							// new Thread(new Runnable() {
							//
							// @Override
							// public void run() {
							// Send();
							// }
							// }).start();
							//
							// break;

							default:
								break;
							}
						}
					});
		}

		activity_selectimg_send.startAnimation(MyAnimations.getRotateAnimation(
				0, 360, 200));
	}

	public View.OnClickListener weixinClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isSelectWechat) {
				shareWeixin.setBackgroundResource(R.drawable.show_wxshare_sel);
				isSelectWechat = true;
			} else {
				shareWeixin.setBackgroundResource(R.drawable.show_wxshare);
				isSelectWechat = false;
			}
		}
	};

	public View.OnClickListener weiboClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isSelectSina) {
				if (weibo.isClientValid()) {
					shareWeibo
							.setBackgroundResource(R.drawable.show_wbshare_sel);
				} else {
					Share = 0;
					weibo.setPlatformActionListener(paListener);
					weibo.authorize();
					shareWeibo.setBackgroundResource(R.drawable.show_wbshare);
					// 移除授权
					// weibo.removeAccount();
				}
				isSelectSina = true;
			} else {
				shareWeibo.setBackgroundResource(R.drawable.show_wbshare);
				isSelectSina = false;
			}
		}
	};

	private Handler handler = new Handler() {

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 123) {
				FinalHttp fh = new FinalHttp();
				AjaxParams param = new AjaxParams();
				param.put("user_id", Config.getUser(context).uid);
				param.put("user_type", "1");
				param.put("uid", (String) msg.obj);
				fh.post(ServerMutualConfig.CheckWeibo, param,
						new AjaxCallBack<String>() {

							@Override
							public void onFailure(Throwable t, String strMsg) {
								Config.dismissProgress();
								super.onFailure(t, strMsg);
							}

							@Override
							public void onSuccess(String t) {
								Config.dismissProgress();
							}
						});
			} else if (msg.what == 1) {
				FinalHttp fh = new FinalHttp();
				
				String url = ServerMutualConfig.ADD_NEW_POSTBAR;
				
				if(!group_id.isEmpty()){
					url = ServerMutualConfig.ADD_NEW_POSTBAR_IN_QUN;
				}

				fh.post(url, params, new AjaxCallBack<String>() {

							@Override
							public void onFailure(Throwable t, String strMsg) {
								Config.dismissProgress();
								super.onFailure(t, strMsg);
								Toast.makeText(context, "请求失败，请重试", 1).show();
							}

							@Override
							public void onSuccess(String t) {
								Config.dismissProgress();
								super.onSuccess(t);
								try {
									JSONObject json = new JSONObject(t);
									item = new PostItem();
									item.fromJson(json.getJSONObject("data"));
									root_img_id = json.getJSONObject("data").getString("img_id");
//									if (isSelectWechat) {
//										shareWechat(shearImg, msgString, 	BimpUtil.drr.get(0));
//									}
//									if (isSelectSina) {
//										shareSinaWeibo(shearImg, msgString, BimpUtil.drr.get(0));
//									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								notificationManager.cancel(2);
								notification = new Notification(R.drawable.icon, "您的新贴已发布成功！", System.currentTimeMillis());
								notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
								notification.setLatestEventInfo(context, "自由环球租赁", "您的新贴已发布成功！", null);
								notificationManager.notify(3, notification);
								notificationManager.cancel(3);
								PhotoEdit.imgUris = "";
								add_new_succese_hint.setVisibility(View.VISIBLE);
							}

						});
			}else if(msg.what == 2){
				
				VideoComperssUtils.uploadIcon = true;
				
				NotificationManager notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.icon,"您的视频正在处理", System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				notification.setLatestEventInfo(context, "自由环球租赁","您的视频正在处理", null);
				notificationManager.notify(4, notification);
				notificationManager.cancel(4);
				/**
				 * 启动压缩服务并监听压缩进度
				 * */
				if(!video_path.isEmpty()){
					VideoComperssUtils.startComperssService(context, video_path);
					
					videoTimer.schedule(videoTask, 5000, 3000); // 5s后执行task,经过5s再次执行  
				}
				AddNewPostContinueActivity.this.finish();
			}else if(msg.what == 3){
				/**
				 * 压缩完成，开始上传
				 * */
				notification = new Notification(R.drawable.icon,"您的新贴正在发布", System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				notification.setLatestEventInfo(context, "自由环球租赁","您的新贴正在发布", null);
				notificationManager.notify(2, notification);
				
				String outFilePath = "";
				if(1 < Integer.parseInt(VideoComperssUtils.VIDEO_FILE_SIZE)){
					videoTask.cancel();
					VideoComperssUtils.stopComperssService(AddNewPostContinueActivity.this);
					outFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + VideoComperssUtils.VIDEO_FILE_PATH +"/"+VideoComperssUtils.VIDEO_FILE_NAME+ VideoComperssUtils.VIDEO_FILE_SUFFIX;
				}else{
					outFilePath = video_path;
				}
				
				videoParams.put("video_url", new File(outFilePath));
				videoParams.put("image_url", VideoComperssUtils.getVideoThumbnailFile(video_path));
				VideoComperssUtils.setVideoParams(videoParams);
				
				String url = ServerMutualConfig.ADD_NEW_POSTBAR;
				
				if(!group_id.isEmpty()){
					url = ServerMutualConfig.ADD_NEW_POSTBAR_IN_QUN;
				}
				
				VideoComperssUtils.videoUpload(AddNewPostContinueActivity.this, url, notificationManager, "您的新贴已发布成功！");
				AddNewPostContinueActivity.this.finish();
			}
			super.handleMessage(msg);
		}
	};
	
	TimerTask videoTask = new TimerTask() {  
		  
        @Override  
        public void run() {  
        	if(VideoComperssUtils.comperssServiceComplete){
        		VideoComperssUtils.comperssServiceComplete = false;
        		Message message = new Message();  
        		message.what = 3;  
        		handler.sendMessage(message);  
        	}
        }  
    };  
    
	AjaxParams params = new AjaxParams();
	AbRequestParams videoParams = new AbRequestParams();

	@SuppressLint("NewApi")
	public void Send() {
		if (!TextUtils.isEmpty(msgString) && BimpUtil.drr.size() == 0 && PhotoEdit.imgUris == null) {
			handler.sendEmptyMessage(0);
		} else {
			
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("img_title", "");
			params.put("img_desc", msg.getText().toString().trim());
			params.put("group_name", "");
//			params.put("group_type", img_cate+"");
			params.put("root_img_id", root_img_id);
			
			videoParams.put("login_user_id", Config.getUser(context).uid);
			videoParams.put("img_title", "");
			videoParams.put("img_desc", msg.getText().toString().trim());
			videoParams.put("group_name", "");
//			videoParams.put("group_type", img_cate+"");
			videoParams.put("root_img_id", root_img_id);
			
			params.put("is_group", "0");
			videoParams.put("is_group", "0");
			
			params.put("user_id", Config.getUser(context).uid);
			videoParams.put("user_id", Config.getUser(context).uid);
			File mFile = null;
			int filecount = 0;
			if (BimpUtil.drr.size() > 0) {
				for (int i = 0; i < BimpUtil.drr.size(); i++) {
					if (!BimpUtil.drr.get(i).startsWith("http")) {
						filecount++;
						if (BimpUtil.drr.size() == 1) {
							mFile = BitmapCache.getimageyang(
									BimpUtil.drr.get(i), "img_" + i + ".jpg",
									"dan");
						} else {
							mFile = BitmapCache.getimageyang(
									BimpUtil.drr.get(i), "img_" + i + ".jpg",
									"duo");
						}
						try {
							params.put("img" + (i + 1), mFile);
							videoParams.put("img" + (i + 1), mFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
			msgString = msg.getText().toString().trim();

			if (!TextUtils.isEmpty(group_id)) {
				params.put("group_id", group_id + "");
				videoParams.put("group_id", group_id + "");
			}
			params.put("file_count", filecount + "");
			params.put("img_desc", msgString);
			params.put("is_forward", "");
			params.put("img_cate", String.valueOf(img_cate));
			
			videoParams.put("file_count", "1");
			videoParams.put("img_desc", msgString);
			videoParams.put("is_forward", "");
			videoParams.put("img_cate", String.valueOf(img_cate));
			videoParams.put("is_video", "1");
			
			StringBuilder imgUri = null;
			if (!TextUtils.isEmpty(PhotoEdit.imgUris)) {
				imgUri = new StringBuilder(PhotoEdit.imgUris);
				imgUri.deleteCharAt(imgUri.length() - 1);
				imgUri.append("]");
				params.put("img_urls", imgUri.toString());
				videoParams.put("img_urls", imgUri.toString());
			}
			if (isSelectWechat || isSelectSina) {
				StringBuilder shearUrlImg = null;
				if (!TextUtils.isEmpty(imgUri)) {
					String str = imgUri.delete(0, 2).toString();
					if (str.contains(",")) {
						shearUrlImg = new StringBuilder(str.split(",")[0]);
					} else {
						shearUrlImg = new StringBuilder(str);
						shearUrlImg.deleteCharAt(shearUrlImg.length() - 1);
					}
					shearUrlImg.deleteCharAt(shearUrlImg.length() - 1);
				}
				shearImg = null != shearUrlImg ? shearUrlImg.toString() : "";
			}
			
			/**
			 * 1 表示上传图片
			 * 2 表示上传视频
			 * */
			if(video_path.isEmpty()){
				handler.sendEmptyMessage(1);
			}else{
				if(1 < Integer.parseInt(VideoComperssUtils.VIDEO_FILE_SIZE)){
					handler.sendEmptyMessage(2);
				}else{
					handler.sendEmptyMessage(3);
				}
			}
		}

	}

	public boolean isZhineng = false;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// FileUtils.deleteDir();
		msgString = "";
		BimpUtil.drr.clear();
		BimpUtil.time.clear();
		BimpUtil.bmp.clear();
		BimpUtil.max = 0;
		PhotoEdit.imgUris = "";
	}

	// @SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (BimpUtil.drr.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			noScrollgridview.setVisibility(View.VISIBLE);
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
			} else {
				if (BimpUtil.drr.get(position - 1).startsWith("http")) {
					MyApplication.getInstance().display(
							BimpUtil.drr.get(position - 1), holder.image,
							R.drawable.def_image);
				} else {
					String path = BimpUtil.drr.get(position - 1);
					try {
						Bitmap bm = BitmapCache.revitionImageSize(null, path);
						holder.image.setImageBitmap(bm);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BimpUtil.max == BimpUtil.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = BimpUtil.drr.get(BimpUtil.max);
								Bitmap bm = BitmapCache.revitionImageSize(null,
										path);
								BimpUtil.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								BimpUtil.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			Button album, Photo, camera, cancel;
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_1));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			album = (Button) view.findViewById(R.id.item_popupwindows_album);
			/**
			 * 拍摄一张
			 */
			camera.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			/**
			 * 从手机相册里选
			 */
			Photo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(AddNewPostContinueActivity.this,
							PhoneAlbumActivity.class);
					intent.putExtra("tag", AddNewContinueActivity_TAG);
					intent.putExtra(Config.SELECTNAME, 9);
					startActivity(intent);
					dismiss();
				}
			});
			/**
			 * 取消
			 */
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			/**
			 * 从贴相册里选
			 */
			album.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(AddNewPostContinueActivity.this,
							PhotoManager.class);
					intent.putExtra("isAlbum", "isAlbum");
					intent.putExtra("tag", AddNewContinueActivity_TAG);
					Config.SELECTEDIMAGECOUNT = 9;
					startActivity(intent);
					dismiss();
				}
			});
		}
	}

	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imgNum++;
		path = "temp_image_" + imgNum + ".jpg";
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/" + path);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("path", path);
		outState.putInt("imgNum", imgNum);
	}

	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			Intent intent2 = new Intent();
			intent2.setClass(context, CropImage.class);
			intent2.putExtra("tag", AddNewContinueActivity_TAG);
			intent2.putExtra("path", Config.ImageFile + path);
			startActivityForResult(intent2, IMAGE_CROP_RETURN);
			break;
		case IMAGE_CROP_RETURN:
			break;
		case VideoComperssUtils.REQUEST_CODE_INPUT_VIDEO_PATH:
			
			if(null == data){
				return;
			}
			
			video_path = VideoComperssUtils.getVideoPath(context, data);
			
			if(video_path.isEmpty()){
				return;
			}
			
			// 视频时长
			int videoSecond = 0; 
			// 视频时长
			double videoSize = 0.00;
			
			if(!VideoComperssUtils.getVideoDuration(video_path).isEmpty()){
				videoSecond = Integer.parseInt(VideoComperssUtils.getVideoDuration(video_path));
			};
			
			videoSize = VideoComperssUtils.getFileOrFilesSize(video_path, VideoComperssUtils.SIZETYPE_MB);
			
			if(200 < videoSize){ // 大于200M
				Toast.makeText(context, "视频文件过大", 0).show();
				return;
			}else if(60000*3 < videoSecond){ // 时长超过3分钟
				Toast.makeText(context, "视频文件过大", 0).show();
				return;
			}else if(100 < videoSize && 200 > videoSize){ // 100M-200M之间压缩至10M
				VideoComperssUtils.VIDEO_FILE_SIZE = "10";
			}else if(50 < videoSize && 100 > videoSize){ // 50M-100M之间压缩至8M
				VideoComperssUtils.VIDEO_FILE_SIZE = "8";
			}else if(20 < videoSize && 50 > videoSize){ // 20M-50M之间压缩至5M
				VideoComperssUtils.VIDEO_FILE_SIZE = "5";
			}else if(5 < videoSize && 20 > videoSize && videoSecond > (30*1000)){ // 5M-20M并且时长大于30秒
				VideoComperssUtils.VIDEO_FILE_SIZE = "-1";
			}else if(5 < videoSize && 20 > videoSize && videoSecond <= (30*1000)){// 5M-20M并且时长小于30秒
				VideoComperssUtils.VIDEO_FILE_SIZE = "5";
			}else if(0 < videoSize && 5 > videoSize){
				VideoComperssUtils.VIDEO_FILE_SIZE = "-1";
			}
			
			// 视频文件后缀
			if(video_path.contains(".")){
				String[] str = video_path.split(".");
				if(str.length != 0){
					VideoComperssUtils.VIDEO_FILE_SUFFIX = str[str.length-1];
				}
			}
			
			// 隐藏分享
			shareWeixin.setVisibility(View.GONE);
			shareWeibo.setVisibility(View.GONE);
			isSelectWechat = false;
			isSelectSina = false;
			
			// 隐藏图片列表
			showshow_add_image.setVisibility(View.GONE);
			findViewById(R.id.showshow_add_video_view).setVisibility(View.VISIBLE);
			findViewById(R.id.showshow_add_video_hint).setVisibility(View.GONE);
			showshow_add_video_thumbnail.setVisibility(View.VISIBLE);
			showshow_add_video_thumbnail.setImageBitmap(VideoComperssUtils.getVideoThumbnailBitmap(VideoComperssUtils.getVideoPath(context, data)));
			break;
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

	@SuppressLint("NewApi")
	private void shareSinaWeibo(String shearUrlImg, String msg, String path) {
		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		String url = "";
		if(video_path.isEmpty()){
			url = "www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id;
		}else{
			url = "www.meimei.yihaoss.top/fenxiang/index.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id;
		}
		sp.setText("晒晒我家宝宝最新萌照|最美的宝宝成长记录  " + url);
		if (TextUtils.isEmpty(path) || path.startsWith("http")) {
			sp.setImageUrl(shearUrlImg);
		} else if (!TextUtils.isEmpty(path) || !path.startsWith("http")) {
			sp.setImagePath(path);
		} else if (path.startsWith("http") && !TextUtils.isEmpty(path)) {
			sp.setImagePath(path);
		} else {
			if(video_path.isEmpty()){
				sp.setImageUrl("www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
			}else{
				sp.setImageUrl("www.meimei.yihaoss.top/fenxiang/index.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
			}
		}
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	@SuppressLint("NewApi")
	private void shareWechat(String shearUrlImg, String msg, String path) {
		cn.sharesdk.wechat.moments.WechatMoments.ShareParams sp = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_TEXT);
		sp.setTitle("晒晒我家宝宝最新萌照|最美的宝宝成长记录");
		sp.setText(msg);
		sp.setShareType(Platform.SHARE_WEBPAGE);
		if (TextUtils.isEmpty(path) || path.startsWith("http")) {
			sp.setImageUrl(shearUrlImg);
		} else if (!TextUtils.isEmpty(path) || !path.startsWith("http")) {
			sp.setImagePath(path);
		} else if (path.startsWith("http") && !TextUtils.isEmpty(path)) {
			sp.setImagePath(path);
		} else {
			if(video_path.isEmpty()){
				sp.setImageUrl("www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
			}else{
				sp.setImageUrl("www.meimei.yihaoss.top/fenxiang/index.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
			}
		}
		
		if(video_path.isEmpty()){
			sp.setUrl("www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
		}else{
			sp.setUrl("www.meimei.yihaoss.top/fenxiang/index.html?" + "img_id=" + item.img_id + "&user_id=" + item.user_id);
		}
		Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
		weixin.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weixin.share(sp);	}

	private PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// android.util.Log.e("fff +onError: ", arg0.toString());

		}

		@Override
		public void onComplete(final Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			if (Share == 0) {
				Message mes = new Message();
				mes.obj = arg0.getDb().getUserId();
				mes.what = 123;
				handler.sendMessage(mes);
			} else {
				Toast.makeText(context, "分享成功", 0).show();
			}
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// android.util.Log.e("fff +onCancel: ", arg0.toString());
		}

	};
	
	/**
	 * 提示是否确认上传视频
	 * */
	private void showHintDialog(final int requestCode, final int resultCode, final Intent data){
		Builder builder = new Builder(context);
		builder.setMessage("确定秀秀这段视频吗？");
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						VideoComperssUtils.startComperssService(context, requestCode, resultCode, data);
					}
				});
		builder.setNeutralButton("换一个", null);
		builder.create().show();
	}
	
}
