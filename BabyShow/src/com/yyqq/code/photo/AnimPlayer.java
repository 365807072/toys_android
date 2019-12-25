package com.yyqq.code.photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.service.AnimPlayerService;
import com.yyqq.commen.view.NotifiableViewFlipper;
import com.yyqq.commen.view.NotifiableViewFlipper.OnFlipListener;
import com.yyqq.framework.application.MyApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class AnimPlayer extends Activity {

	private NotifiableViewFlipper flipper;
	private Intent intent;
	private int random;
	public ArrayList<String> data = null;
	boolean autoflag = true;
	private String TAG = "AnimPlayer";
	private ImageView replay;
	public ArrayList<String> ImaWid = null;
	public ArrayList<String> ImaHed = null;
	private int screenWidth, screenHeigh;
	private RelativeLayout layout;
	private HomeKeyEventBroadCastReceiver receiver;
	private final int mId = 0x100;
	Map<Integer, Boolean> mMap = new HashMap<Integer, Boolean>();
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.anim_player);
		receiver = new HomeKeyEventBroadCastReceiver();
		registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeigh = dm.heightPixels;
		initiaView();
		data = getIntent().getStringArrayListExtra("imgList");
		ImaWid = getIntent().getStringArrayListExtra("imaWid");
		ImaHed = getIntent().getStringArrayListExtra("imaHed");
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				flipper.addView(addImageById(i, ImaWid.get(i), ImaHed.get(i)));
			}
		}
		// timer.schedule(task, 5000, 5000); // 延时1000ms后执行，1000ms执行一次
		if (autoflag) {
			play();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}

	public void play() {
		replay.setVisibility(View.GONE);
		flipper.setAutoStart(autoflag);
		flipper.startFlipping();
		autoflag = false;
		String i = "";
		intent.putExtra("music", (int) (Math.random() * 6 + 1));
		startService(intent);
	}

	public void stop() {
		flipper.stopFlipping();
		autoflag = true;
		stopService(intent);
		replay.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化参数
	 */

	private void initiaView() {
		layout = (RelativeLayout) findViewById(R.id.layout);
		flipper = (NotifiableViewFlipper) findViewById(R.id.flipper);
		replay = (ImageView) findViewById(R.id.replay);
		flipper.setOnFlipListener(adFlipListener);
		replay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				flipper.showNext();
				play();
			}
		});
		intent = new Intent(this, AnimPlayerService.class);
	}

	public View addImageById(int id, String imaWid2, String imaHed2) {

		final ImageView iv = new ImageView(this);
		iv.setId(mId + id);
		MyApplication.getInstance().display(data.get(id), iv,
				R.drawable.def_image, listener);
		int newW;
		int newH;
		int width = Integer.valueOf(imaWid2);
		int height = Integer.valueOf(imaHed2);

		if (width == height) {
			newW = screenHeigh;
			newH = screenHeigh;
		} else if (width > height) { // 横图
			newW = (int) (screenHeigh * 1.3); // 宽=高*1.3
			newH = screenHeigh;
		} else { // 竖图
			newW = (int) (screenHeigh * 0.75); // 宽=高*0.75
			newH = screenHeigh;
		}
		iv.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				newW, newH);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		iv.setLayoutParams(params);

		RelativeLayout layout = new RelativeLayout(this);
		layout.setGravity(Gravity.CENTER);
		layout.addView(iv);
		layout.setId(mId + id);

		return layout;
	}

	@SuppressLint("NewApi")
	final Handler handler = new Handler() {
		/*
		 * fade_in,fade_out 直接变成下一张 in_right_left 从右向左进 out_left_right 从左向右出
		 * scale_translate 从左上角向中间，放大显示 slide_up_in 从下往上推进 push_left_in 从右往左进
		 * anim1 从中间向左下角移动，不完全消失 anim2 在中间顺时针旋转 anim3 从中间由小变大再恢复正常 anim4
		 * 从中间往下，还不完全消失 umeng_socialize_slide_in_from_bottom 从下往中间，很快
		 * zoom_exit从中间缩小向左上角退出 hyperspace_in 透明渐变出现
		 */

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				random = (int) (Math.random() * 7 + 1);
				int id = (flipper.getChildAt(flipper.getDisplayedChild()))
						.getId();
				// Log.e("yyf", "getid :" + id);
				// Log.e("yyf", "random :" + random);
				if (mMap.get(id) == null || mMap.get(id)) {
					// Config.showProgressDialog(AnimPlayer.this, false, null);
				} else {
					// Config.dismissProgress();
				}
				switch (random) {
				}

				break;
			}
			if (flipper.getDisplayedChild() + 2 > data.size() && !autoflag) {
				stop();

			}
			super.handleMessage(msg);
		}
	};

	class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
		static final String SYSTEM_REASON = "reason";
		static final String SYSTEM_HOME_KEY = "homekey";// home key
		static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String reason = intent.getStringExtra(SYSTEM_REASON);
			if (reason.equals(SYSTEM_HOME_KEY)) {
				AnimPlayer.this.finish();
			} else if (reason.equals(SYSTEM_RECENT_APPS)) {
				AnimPlayer.this.finish();
			}
		}
	}

	ImageLoadingListener listener = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// Log.e("yyyImageLoadingListener", "onLoadingStarted" + arg0);
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			mMap.put(arg1.getId(), false);
			// Log.e("yyyImageLoadingListener", "onLoadingFailed" + arg0);
		}

		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			// Log.e("yyf", "save " + arg1.getId());
			mMap.put(arg1.getId(), true);
			// Log.e("yyyImageLoadingListener", "onLoadingComplete" + arg0);
		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// Log.e("yyyImageLoadingListener", "onLoadingCancelled" + arg0);
		}
	};
	/**
	 * 轮番广告切换监听器，更新进度标记锚点的显示
	 */
	private OnFlipListener adFlipListener = new OnFlipListener() {

		@Override
		public void onShowPrevious(NotifiableViewFlipper flipper) {
			random = (int) (Math.random() * 7 + 1);
			setAimtion(random);
		}

		@Override
		public void onShowNext(NotifiableViewFlipper flipper) {
			random = (int) (Math.random() * 7 + 1);
			setAimtion(random);
		}
	};

	private void setAimtion(int key) {
		int id = (flipper.getChildAt(flipper.getDisplayedChild())).getId();
		// Log.e("yyf", "getid :" + id);
		// Log.e("yyf", "random :" + random);
		if (mMap.get(id) == null || !mMap.get(id)) {
			flipper.isShow = false;
			// Config.showProgressDialog(AnimPlayer.this, false, null);
		} else {
			flipper.isShow = true;
			// Config.dismissProgress();
		}
		switch (key) {
		case 1:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.in_right_left));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.out_left_right));

			break;
		case 2:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.hyperspace_in));
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.back_scale));
			break;
		case 3:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.push_up_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.push_up_out));
			break;
		case 4:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.fade));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.hold));
			break;
		case 5:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.appear));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.disappear));
			break;
		case 6:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.rotate_center));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.out_to_left));
			break;
		case 7:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.zoom_enter));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this, R.anim.zoom_exit));
			break;
		case 8:
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this,
					R.anim.umeng_socialize_slide_in_from_bottom));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					AnimPlayer.this,
					R.anim.umeng_socialize_slide_out_from_bottom));
			break;
		default:
			break;
		}
	}
}
