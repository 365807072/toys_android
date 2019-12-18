package com.yyqq.code.postbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.commen.model.PostItem;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.MyAnimations;
import com.yyqq.commen.utils.VideoComperssUtils;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 添加话题(弃用于2016.08.24)
 * */
public class PostBarAddActivity extends Activity {
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private ImageView activity_selectimg_send;
	private EditText msg;
	private EditText post_title;
	public Activity context;
	public final String TAG = "PostBar_Show";
	public String imagepath = null;
	public static String msgString = "";
	public static String qunString = "";
	public static String titleString = "";
	private int babyShow = 1; // 标记是否同时发布到秀秀 跟帖的图片所属组，0代表秀秀，1代表帖子，2代表秀秀和帖子
	// 跟帖的标志
	private static String root_img_id = "";
	private static String user_id = "";
	private static String is_save = "";
	// private ImageView link;
	private static String group_name = "";
	private static String group_id = "";
	private static int post_class = 0;
	private static int is_group = 0;
	private ImageView shareWeixin, shareWeibo;
	private SharedPreferences shared;
	public static int maxSize = 6;
	private static boolean isSelectWechat = true;
	private static boolean isSelectSina;
	private PostItem item;
	private int Share = 1; // 表示分享
	Platform weibo;
	private NotificationManager notificationManager;
	private Notification notification;
//	private ImageView box1, box2, box3, box4;
//	private LinearLayout type1, type2, type3, type4, ly_type, ly_low;
//	private ImageView create_qun;
	private EditText qunName;
//	private TextView t1, t2, t3, t4;
	private String rep = "";
	
	private LinearLayout showshow_add_image;
	private LinearLayout showshow_add_video;
	public static String video_path = "";
	private ImageView showshow_add_video_thumbnail;
	private TextView post_add_cancel;
	private TextView post_add_sent;
	
	// 用于选择图片后返回时关闭多出的本Activity
	public static PostBarAddActivity postBarAddActivity; 
	private Timer videoTimer = new Timer();
	private Uri videoUri = null;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (TextUtils.isEmpty(getIntent().getStringExtra("group_id"))) {
				post_class = 0;
				is_group = 0;
				group_id = "";
				group_name = "";
				context.finish();
			} else {
				GroupRelevantUtils.CheckIntent(context, group_id);
				post_class = 0;
				is_group = 0;
				group_id = "";
				group_name = "";
				context.finish();
			}

		}
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.postbar_show);
		postBarAddActivity = this;
		if (!TextUtils.isEmpty(getIntent().getStringExtra("img_id"))) {
			root_img_id = getIntent().getStringExtra("img_id");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("user_id"))) {
			user_id = getIntent().getStringExtra("user_id");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("is_save"))) {
			is_save = getIntent().getStringExtra("is_save");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("group_id"))) {
			group_id = getIntent().getStringExtra("group_id");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("group_name"))) {
			group_name = getIntent().getStringExtra("group_name");
		}

		context = this;
		// 消息通知
		notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//		ShareSDK.initSDK(context);
//		weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		MyAnimations.initOffset(this);
//		isZhineng = getIntent().getBooleanExtra("iszhineng", false);
		imagepath = getIntent().getStringExtra("path");
		Init();
	}

	@Override
	protected void onResume() {
		context = this;
		msg.setText(msgString);
		qunName.setText(qunString);
		post_title.setText(titleString);
		if(adapter.getCount() > 1){
			findViewById(R.id.showshow_add_image_hint).setVisibility(View.GONE);
			findViewById(R.id.showshow_add_video).setVisibility(View.GONE);
			noScrollgridview.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
		super.onResume();
		MobclickAgent.onResume(this);
	}

	
	@Override
	protected void onPause() {
		msgString = msg.getText().toString().trim();
		titleString = post_title.getText().toString().trim();
		qunString = qunName.getText().toString().trim();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isSelectWechat)
			shareWeixin.setBackgroundResource(R.drawable.show_wxshare_sel);
		else
			shareWeixin.setBackgroundResource(R.drawable.show_wxshare);
		if (isSelectSina)
			shareWeibo.setBackgroundResource(R.drawable.show_wbshare_sel);
		else
			shareWeibo.setBackgroundResource(R.drawable.show_wbshare);
	}

	public void Init() {
		
		showshow_add_video_thumbnail = (ImageView) findViewById(R.id.showshow_add_video_thumbnail);
		
		// 分享到微博微信
		shareWeixin = (ImageView) findViewById(R.id.shareWeixin);
		shareWeibo = (ImageView) findViewById(R.id.shareWeibo);
		if (weibo.isClientValid()) {
			isSelectSina = true;
			shareWeibo.setBackgroundResource(R.drawable.show_wbshare_sel);
		}
		shareWeixin.setOnClickListener(weixinClick);
		shareWeibo.setOnClickListener(weiboClick);

		// 帖子类别
//		ly_type = (LinearLayout) findViewById(R.id.ly_type);

//		type1 = (LinearLayout) findViewById(R.id.type1);
//		t1 = (TextView) findViewById(R.id.t1);
//		box1 = (ImageView) findViewById(R.id.box1);
//
//		type2 = (LinearLayout) findViewById(R.id.type2);
//		t2 = (TextView) findViewById(R.id.t2);
//		box2 = (ImageView) findViewById(R.id.box2);
//
//		type3 = (LinearLayout) findViewById(R.id.type3);
//		t3 = (TextView) findViewById(R.id.t3);
//		box3 = (ImageView) findViewById(R.id.box3);
//
//		type4 = (LinearLayout) findViewById(R.id.type4);
//		t4 = (TextView) findViewById(R.id.t4);
//		box4 = (ImageView) findViewById(R.id.box4);

//		type1.setOnClickListener(type1Click);
//		type2.setOnClickListener(type2Click);
//		type3.setOnClickListener(type3Click);
//		type4.setOnClickListener(type4Click);

//		ly_low = (LinearLayout) findViewById(R.id.ly_low);
//		create_qun = (ImageView) findViewById(R.id.create_qun);
		qunName = (EditText) findViewById(R.id.qun_name);
		qunName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (qunName.length() > 1) {
//					create_qun.setBackgroundResource(R.drawable.qun_biao);
					is_group = 1;
				} else {
//					create_qun.setBackgroundResource(R.drawable.qun_hui);
					is_group = 0;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				if (qunName.length() > 1) {
//					create_qun.setBackgroundResource(R.drawable.qun_biao);
					is_group = 1;
				} else {
//					create_qun.setBackgroundResource(R.drawable.qun_hui);
					is_group = 0;
				}
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (qunName.length() > 1) {
//					create_qun.setBackgroundResource(R.drawable.qun_biao);
					is_group = 1;
				} else {
//					create_qun.setBackgroundResource(R.drawable.qun_hui);
					is_group = 0;
				}
			}

		});

		if (!TextUtils.isEmpty(getIntent().getStringExtra("group_id"))) {
//			ly_type.setVisibility(View.GONE);
//			ly_low.setVisibility(View.GONE);
		}

		// 链接
		// link = (ImageView) findViewById(R.id.link);
		// if (post_url.length() > 1)
		// link.setBackgroundResource(R.drawable.postbar_add_link_sel);
		// link.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// showDig();
		// }
		// });

		post_title = (EditText) findViewById(R.id.post_title);
		msg = (EditText) findViewById(R.id.msg);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		adapter = new GridAdapter(this);
		// adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == 0) {
					if (arg2 == 4) {
						Toast.makeText(context, "最多4张", 1).show();
					} else {
						msgString = msg.getText().toString().trim();
						new PopupWindowsPicture(PostBarAddActivity.this,
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
		
		post_add_sent = (TextView) findViewById(R.id.post_add_sent);
		post_add_sent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (msg.getText().toString().trim().isEmpty() && post_title.getText().toString().trim().isEmpty() && adapter.getCount() == 0 && qunName.getText().toString().trim().isEmpty()) {
					Toast.makeText(context, "什么都没有，完善一下吧", 0).show();
				}else{
					Send();
				}
			}
		});

		activity_selectimg_send = (ImageView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (ly_low.getVisibility() == View.VISIBLE) {
					if (msg.getText().toString().trim().isEmpty() && post_title.getText().toString().trim().isEmpty() && adapter.getCount() == 0 && qunName.getText().toString().trim().isEmpty()) {
						Toast.makeText(context, "什么都没有，完善一下吧", 0).show();
					} else {
						selectInputMethod();
						new PopupWindows(context, activity_selectimg_send);
					}
//				} else {
//					if ("".equals(msg.getText().toString().trim())) {
//						Toast.makeText(context, "亲，描述不可以不写哦", 0).show();
//					} else {
//						selectInputMethod();
//						new PopupWindows(context, activity_selectimg_send);
//					}
//				}

			}
		});
		
		post_add_cancel = (TextView) findViewById(R.id.post_add_cancel);
		post_add_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostBarAddActivity.this.finish();
			}
		});
		
		showshow_add_image = (LinearLayout) findViewById(R.id.showshow_add_image);
		showshow_add_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findViewById(R.id.showshow_add_image_hint).setVisibility(View.GONE);
				showshow_add_video.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.VISIBLE);
				msgString = msg.getText().toString().trim();
				new PopupWindowsPicture(PostBarAddActivity.this,
						noScrollgridview);
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getApplicationContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						msg.getWindowToken(), 0);
			}
		});
		
		showshow_add_video = (LinearLayout) findViewById(R.id.showshow_add_video);
		showshow_add_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showshow_add_image.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.GONE);
				VideoComperssUtils.intentLocationVideo(PostBarAddActivity.this);
			}
		});


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
					shareWeibo
							.setBackgroundResource(R.drawable.show_wbshare_sel);
				}
				isSelectSina = true;
			} else {
				shareWeibo.setBackgroundResource(R.drawable.show_wbshare);
				isSelectSina = false;
			}
		}
	};

	public View.OnClickListener type1Click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			post_class = 2;
//			box1.setBackgroundResource(R.drawable.check_box_sel);
//			box2.setBackgroundResource(R.drawable.check_box);
//			box3.setBackgroundResource(R.drawable.check_box);
//			box4.setBackgroundResource(R.drawable.check_box);
//
//			t1.setTextColor(Color.parseColor("#ff5d5d"));
//			t2.setTextColor(Color.parseColor("#A3A3A3"));
//			t3.setTextColor(Color.parseColor("#A3A3A3"));
//			t4.setTextColor(Color.parseColor("#A3A3A3"));
		}
	};

	public View.OnClickListener type2Click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			post_class = 3;
//			box1.setBackgroundResource(R.drawable.check_box);
//			box2.setBackgroundResource(R.drawable.check_box_sel);
//			box3.setBackgroundResource(R.drawable.check_box);
//			box4.setBackgroundResource(R.drawable.check_box);
//
//			t1.setTextColor(Color.parseColor("#A3A3A3"));
//			t2.setTextColor(Color.parseColor("#ff5d5d"));
//			t3.setTextColor(Color.parseColor("#A3A3A3"));
//			t4.setTextColor(Color.parseColor("#A3A3A3"));
		}
	};

	public View.OnClickListener type3Click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			post_class = 4;
//			box1.setBackgroundResource(R.drawable.check_box);
//			box2.setBackgroundResource(R.drawable.check_box);
//			box3.setBackgroundResource(R.drawable.check_box_sel);
//			box4.setBackgroundResource(R.drawable.check_box);
//
//			t1.setTextColor(Color.parseColor("#A3A3A3"));
//			t2.setTextColor(Color.parseColor("#A3A3A3"));
//			t3.setTextColor(Color.parseColor("#ff5d5d"));
//			t4.setTextColor(Color.parseColor("#A3A3A3"));
		}
	};

	public View.OnClickListener type4Click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			post_class = 1;
//			box1.setBackgroundResource(R.drawable.check_box);
//			box2.setBackgroundResource(R.drawable.check_box);
//			box3.setBackgroundResource(R.drawable.check_box);
//			box4.setBackgroundResource(R.drawable.check_box_sel);
//
//			t1.setTextColor(Color.parseColor("#A3A3A3"));
//			t2.setTextColor(Color.parseColor("#A3A3A3"));
//			t3.setTextColor(Color.parseColor("#A3A3A3"));
//			t4.setTextColor(Color.parseColor("#ff5d5d"));
		}
	};

	/* 选择图片的pop */
	public class PopupWindowsPicture extends PopupWindow {
		public PopupWindowsPicture(Context mContext, View parent) {
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
					if (isFallBit()) {
						photo();
					} else {
						Toast.makeText(context, "已达到上限", 0).show();
					}
					dismiss();
				}
			});
			/**
			 * 从手机相册里选
			 */
			Photo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isFallBit()) {
						Intent intent = new Intent(PostBarAddActivity.this,
								PhoneAlbumActivity.class);
						if ("gen".equals(getIntent().getStringExtra("gen"))) {
							intent.putExtra("gen", "gen");
						}
						intent.putExtra("tag", "isPostBar");
						intent.putExtra("group_id", group_id);
						intent.putExtra(Config.SELECTNAME, maxSize);
						startActivity(intent);
					} else {
						Toast.makeText(context, "已达到上限", 1).show();
					}
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
			 * 从秀秀相册里选
			 */
			album.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (isFallBit()) {
						Intent intent = new Intent(PostBarAddActivity.this,
								PhotoManager.class);
						if ("gen".equals(getIntent().getStringExtra("gen"))) {
							intent.putExtra("gen", "gen");
						}
						intent.putExtra("isAlbum", "isAlbum");
						intent.putExtra("tag", "isPostBar");
						intent.putExtra("group_id",
								getIntent().getStringExtra("group_id"));
						Config.SELECTEDIMAGECOUNT = maxSize;
						startActivity(intent);
					} else {
						Toast.makeText(context, "已达到上限", 1).show();
					}
					dismiss();
				}
			});
		}
	}

	// private void showDig() {
	// final EditText edittext = new EditText(context);
	// edittext.setText(post_url);
	// new AlertDialog.Builder(context).setTitle("请输入")
	// .setIcon(android.R.drawable.ic_dialog_info).setView(edittext)
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// post_url = edittext.getText().toString();
	// if (post_url.length() > 1)
	// link.setBackgroundResource(R.drawable.postbar_add_link_sel);
	// }
	// }).setNegativeButton("取消", null).show();
	// }

	private boolean isFallBit() {

		if (BimpUtil.drr.size() >= maxSize) {
			return false;
		}
		return true;
	}

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
//								Config.dismissProgress();
								super.onFailure(t, strMsg);
							}

							@Override
							public void onSuccess(String t) {
								Config.dismissProgress();
							}
						});
			} else if(msg.what == 1){
				Config.showProgressDialog(context, false, null);
				FinalHttp fh = new FinalHttp();
				// Log.e("fanfan", rep + params.toString());
				fh.post(rep, params, new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						Config.dismissProgress();
						super.onFailure(t, strMsg);
//						Toast.makeText(context, strMsg, 1).show();
					}

					@Override
					public void onSuccess(String t) {
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(t);
							item = new PostItem();
							item.fromJson(json.getJSONObject("data"));
							if (isSelectWechat) {
								shareWechat(
										titleString,
										shearImg,
										msgString,
										BimpUtil.drr.size() == 0 ? "" : BimpUtil.drr
												.get(0));
							}
							if (isSelectSina) {
								shareSinaWeibo(titleString, shearImg,
										msgString, BimpUtil.drr.size() == 0 ? ""
												: BimpUtil.drr.get(0));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
//						notificationManager.cancel(3);
//						notification = new Notification(R.drawable.icon,
//								"您的新话题已发布成功！", System.currentTimeMillis());
//						notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//						notification.setLatestEventInfo(context, "自由环球租赁",
//								"您的新话题已发布成功！", null);
//						notificationManager.notify(4, notification);
//						notificationManager.cancel(4);
						Toast.makeText(context, "发布成功", 0).show();
						post_class = 0;
						is_group = 0;
						group_id = "";
						super.onSuccess(t);
						BimpUtil.drr.clear();
						BimpUtil.time.clear();
						BimpUtil.bmp.clear();
						PhotoEdit.imgUris = "";
						isSelectWechat = true;
						isSelectSina = false;
//						Intent intent = new Intent();
//						if (!TextUtils.isEmpty(getIntent().getStringExtra("group_id"))) {
//							intent.setClass(context, GroupMainActivity.class);
//							intent.putExtra("group_id", group_id);
//							intent.putExtra("group_name", group_name);
//						} 
//						startActivity(intent);
						context.finish();
					}
				});
			}else if(msg.what == 2){
				
				VideoComperssUtils.postuploadIcon = true;
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
				PostBarAddActivity.this.finish();
			}else if(msg.what == 3){
				/**
				 * 压缩完成，开始上传
				 * */
				notification = new Notification(R.drawable.icon,"您的新话题正在发布", System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				notification.setLatestEventInfo(context, "自由环球租赁","您的新话题正在发布", null);
				notificationManager.notify(2, notification);
			
				String outFilePath = "";
				if(1 < Integer.parseInt(VideoComperssUtils.VIDEO_FILE_SIZE)){
					videoTask.cancel();
					VideoComperssUtils.stopComperssService(PostBarAddActivity.this);
					outFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + VideoComperssUtils.VIDEO_FILE_PATH +"/"+VideoComperssUtils.VIDEO_FILE_NAME+ VideoComperssUtils.VIDEO_FILE_SUFFIX;
				}else{
					outFilePath = video_path;
				}
				
				videoParams.put("video_url", new File(outFilePath));
				videoParams.put("image_url", VideoComperssUtils.getVideoThumbnailFile(video_path));
				VideoComperssUtils.setVideoParams(videoParams);
				VideoComperssUtils.videoUpload(PostBarAddActivity.this, rep,notificationManager, "您的新话题已发布成功！");
				PostBarAddActivity.this.finish();
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
	private String shearImg;

	@SuppressLint("NewApi")
	public void Send() {
		msgString = msg.getText().toString().trim();
		titleString = post_title.getText().toString().trim();
		qunString = qunName.getText().toString().trim();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_title", titleString);
		params.put("img_desc", msgString);
		
		videoParams.put("login_user_id", Config.getUser(context).uid);
		videoParams.put("img_title", titleString);
		videoParams.put("img_desc", msgString);

		File mFile = null;
		int filecount = 0;
		if (BimpUtil.drr.size() > 0) {
			for (int i = 0; i < BimpUtil.drr.size(); i++) {
				if (!BimpUtil.drr.get(i).startsWith("http")) {
					filecount++;
					if (BimpUtil.drr.size() == 1) {
						mFile = BitmapCache.getimageyang(BimpUtil.drr.get(i),
								"img_" + i + ".jpg", "dan");
					} else {
						mFile = BitmapCache.getimageyang(BimpUtil.drr.get(i),
								"img_" + i + ".jpg", "duo");
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

		params.put("file_count", filecount + "");
		params.put("root_img_id", root_img_id);
		params.put("type", babyShow + "");
		
		videoParams.put("file_count", filecount + "");
		videoParams.put("root_img_id", root_img_id);
		videoParams.put("type", babyShow + "");
		
		if (!TextUtils.isEmpty(getIntent().getStringExtra("group_id"))) {
			rep = ServerMutualConfig.PublicGroupPost;
			params.put("group_id", group_id);
			videoParams.put("group_id", group_id);
		} else {
			rep = ServerMutualConfig.PublicPost;
			params.put("is_group", is_group + ""); // 默认是0 ，1是圈子
			videoParams.put("is_group", is_group + ""); // 默认是0 ，1是圈子
			if (1 == is_group) {
				params.put("group_name", qunString); // 默认是0 ，1是圈子
				videoParams.put("group_name", qunString); // 默认是0 ，1是圈子
			}
			params.put("post_class", post_class + "");
			videoParams.put("post_class", post_class + "");
		}
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

//	public void uploadImage(int i) {
//		final int num = i + 1;
//		final FinalHttp fh = new FinalHttp();
//		final AjaxParams params = new AjaxParams();
//		params.put("user_id", Config.getUser(context).uid);
//		params.put("is_android", "1");
//		params.put("file_count", 1 + "");
//		params.put("album_id", getIntent().getStringExtra("album_id"));
//		String name = Bimp.drr.get(i).substring(
//				Bimp.drr.get(i).lastIndexOf("/") + 1, Bimp.drr.get(i).length());
//		JSONObject json = new JSONObject();
//		try {
//			json.put(name, Bimp.time.get(i));
//			params.put("img", json.toString());
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			params.put("img1", new File(Bimp.drr.get(i)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (!isZhineng) {
//			fh.post(ServerMutualConfig.UpImgs, params,
//					new AjaxCallBack<String>() {
//						@Override
//						public void onFailure(Throwable t, String strMsg) {
//							super.onFailure(t, strMsg);
//							Config.dismissProgress();
//							Config.showFiledToast(context);
//						}
//
//						@Override
//						public void onSuccess(String t) {
//							super.onSuccess(t);
//							JSONObject json;
//							try {
//								json = new JSONObject(t);
//								if (num < Bimp.drr.size()) {
//									uploadImage(num);
//								} else {
//									Config.dismissProgress();
//									Toast.makeText(context,
//											json.getString("reMsg"),
//											Toast.LENGTH_SHORT).show();
//									Bimp.drr.clear();
//									Bimp.time.clear();
//									Bimp.bmp.clear();
//									PhotoEdit.imgUris = "";
//									Bimp.max = 0;
//									finish();
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//		} else {
//			fh.post(ServerMutualConfig.ImportImg, params,
//					new AjaxCallBack<String>() {
//						@Override
//						public void onFailure(Throwable t, String strMsg) {
//							super.onFailure(t, strMsg);
//							Config.dismissProgress();
//							Config.showFiledToast(context);
//						}
//
//						@Override
//						public void onSuccess(String t) {
//							super.onSuccess(t);
//							JSONObject json;
//							try {
//								json = new JSONObject(t);
//								if (num < Bimp.drr.size()) {
//									uploadImage(num);
//								} else {
//									Config.dismissProgress();
//									Toast.makeText(context,
//											json.getString("reMsg"),
//											Toast.LENGTH_SHORT).show();
//									Bimp.drr.clear();
//									Bimp.time.clear();
//									Bimp.bmp.clear();
//									PhotoEdit.imgUris = "";
//									Bimp.max = 0;
//									finish();
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//		}
//	}

//	public boolean isZhineng = false;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// FileUtils.deleteDir();
		post_class = 0;
		is_group = 0;
		group_id = "";
		msgString = "";
		titleString = "";
		qunString = "";
		BimpUtil.drr.clear();
		BimpUtil.time.clear();
		BimpUtil.bmp.clear();
		PhotoEdit.imgUris = "";
		BimpUtil.max = 0;
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
						getResources(), R.drawable.postbar_add_picture));
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

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BimpUtil.max == BimpUtil.drr.size()) {
							// Message message = new Message();
							// message.what = 1;
							// handler.sendMessage(message);
							context.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									adapter.notifyDataSetChanged();
								}
							});
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

	/* 发布热点的pop */
	public class PopupWindows extends PopupWindow {
		RelativeLayout pop;

		public PopupWindows(final Context mContext, View parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.post_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			pop = (RelativeLayout) view.findViewById(R.id.pop);

			// 发帖
			ImageView sendPost = (ImageView) view.findViewById(R.id.postSend);
			sendPost.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.a_shang_xia));
			sendPost.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							try {
								Send();
//							} catch (Exception e) {
//								e.printStackTrace();
//								runUi(mContext, "网络出了故障，请重新上传");
//								Intent intent = new Intent();
//								intent.setClass(context, PostBarActivity.class);
//								startActivity(intent);
//							}
//						}
//					}).start();
					
					dismiss();
//					notification = new Notification(R.drawable.icon, "您的新话题正在发布",
//							System.currentTimeMillis());
//					notification.flags |= Notification.FLAG_ONGOING_EVENT;
//					notification.setLatestEventInfo(context, "自由环球租赁", "您的新话题正在发布",
//							null);
//					notificationManager.notify(4, notification);
					// Toast.makeText(context,
					// "相片上传中\n可能需要2-3分钟\n完成后有提示\n先去其他页面看看吧", 0).show();
					// Config.showProgressDialog(context, false, null);
				}
			});
			// 同时发布到秀秀
			ImageView xiuxiu = (ImageView) view.findViewById(R.id.xiuxiu);
			xiuxiu.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.a_shang_xia));
			xiuxiu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO 同时发布到秀秀操作
					babyShow = 2;
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							try {
								Send();
//							} catch (Exception e) {
//								runUi(mContext, "网络出了故障");
//								e.printStackTrace();
//								Intent intent = new Intent();
//								intent.setClass(context, PostBarActivity.class);
//								startActivity(intent);
//							}
//						}
//					}).start();
					dismiss();
//					notification = new Notification(R.drawable.icon, "您的新话题正在发布",
//							System.currentTimeMillis());
//					notification.flags |= Notification.FLAG_ONGOING_EVENT;
//					notification.setLatestEventInfo(context, "自由环球租赁", "您的新话题正在发布",
//							null);
//					notificationManager.notify(4, notification);
					// Toast.makeText(context,
					// "相片上传中\n可能需要2-3分钟\n完成后有提示\n先去其他页面看看吧", 0).show();
//					Intent intent = new Intent();
//					intent.setClass(context, PostBarActivity.class);
//					startActivity(intent);
					// Config.showProgressDialog(context, false, null);
				}
			});
			// 取消
			TextView cancle = (TextView) view.findViewById(R.id.textView1);
			cancle.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.a_shang_xia));
			cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					pop.setVisibility(View.GONE);
					pop.startAnimation(AnimationUtils.loadAnimation(mContext,
							R.anim.a_xia_shang));
					dismiss();
				}
			});
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private static final int IMAGE_CROP_RETURN = 4;
	private String path = "";
	private static int imgNum;

	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imgNum++;
		path = "temp_image_" + imgNum + ".jpg";
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/" + path);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
	}

	public void selectInputMethod() {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(msg.getWindowToken(), 0);
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
			if ("gen".equals(getIntent().getStringExtra("gen"))) {
				intent2.putExtra("gen", "gen");
			}
			intent2.putExtra("tag", "isPostBar");
			intent2.putExtra("path", Config.ImageFile + path);
			intent2.putExtra("group_id", getIntent().getStringExtra("group_id"));
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

	private void shareSinaWeibo(String title, String shearUrlImg, String msg,
			String path) {
		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		sp.setTitle(title);
		sp.setText("晒晒我家宝宝最新萌照|最美的宝宝成长记录 "
				+ "http://www.meimei.yihaoss.top/share_post.php?img_id="
				+ item.img_id + "&user_id=" + item.user_id);
		if (TextUtils.isEmpty(path) || path.startsWith("http")) {
			sp.setImageUrl(shearUrlImg);
		} else if (!TextUtils.isEmpty(path) || !path.startsWith("http")) {
			sp.setImagePath(path);
		} else if (path.startsWith("http") && !TextUtils.isEmpty(path)) {
			sp.setImagePath(path);
		} else {
			sp.setImageUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
					+ item.img_id + "&user_id=" + item.user_id);
		}
		sp.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	private void shareWechat(String title, String shearUrlImg, String msg,
			String path) {
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
			sp.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
		}
		sp.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
		sp.setUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
				+ item.img_id + "&user_id=" + item.user_id);
//		Platform weixin = ShareSDK.getPlatform(context, WechatMoments.NAME);
//		weixin.setPlatformActionListener(paListener); // 设置分享事件回调
//		// 执行图文分享
//		weixin.share(sp);
	}

	private PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
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
		}

	};

	private void runUi(final Context context, final String str) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, str, 1).show();
			}
		});
	}
}
