package com.yyqq.code.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.http.AbHttpUtil;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.R.drawable;
import com.yyqq.babyshow.R.id;
import com.yyqq.babyshow.R.layout;
import com.yyqq.commen.adapter.MyAdapter;
import com.yyqq.commen.utils.CleanDataUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;

public class InitActivity extends Activity {
	private AbHttpUtil abhttp;
	private Activity context;
	private MyApplication myMyApplication;

	private ViewPager mPager;
	private int[] imgs = new int[] { R.drawable.viewpager_1 };
	private View view;
	private List<View> mList;
	private AlertDialog.Builder dialog;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.main);
		context = this;
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.setDebug(Log.isDebug);
		init();
		// initPoints();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void init() {
		view = findViewById(R.id.view);
		mPager = (ViewPager) findViewById(R.id.viewPager);
		mList = new ArrayList<View>();
		LinearLayout linearLayout;
		for (int n = 0; n < imgs.length; n++) {
			linearLayout = new LinearLayout(this);
			linearLayout.setGravity(Gravity.CENTER_VERTICAL);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView img1 = new ImageView(this);
			ImageView img2 = new ImageView(this);
			// img1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			linearLayout.addView(img2);
			linearLayout.addView(img1);
			// img.setScaleType(ScaleType.FIT_XY);
			// img2.setImageResource(des[n]);
			img1.setImageResource(imgs[n]);
			img1.setPadding(0, 5, 0, 0);
			mList.add(linearLayout);
		}

		MyAdapter adapter = new MyAdapter(mList);
		mPager.setAdapter(adapter);
		if (mList.size() > 0) {
			View view = mList.get(mList.size() - 1);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(!Config.isConn(InitActivity.this)){
						InitActivity.this.showDialog(InitActivity.this);
					}else{
						Intent intent = new Intent(InitActivity.this, MainTab.class);
						startActivity(intent);
						InitActivity.this.finish();
					}
				}
			});
		}
	}
	
	// public void initPoints() {
	// myPoints = new ImageView[mList.size()];
	// mGroup = (LinearLayout) findViewById(R.id.viewGroup);
	// /**
	// * 添加小圆点图片
	// */
	// for (int i = 0; i < mList.size(); i++) {
	// mPointImg = new ImageView(getApplicationContext());
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// params.setMargins(7, 0, 7, 0);
	// mPointImg.setLayoutParams(params);
	// myPoints[i] = mPointImg;
	// if (i == 0) {
	// myPoints[i].setBackgroundResource(R.drawable.dian_hong);
	// } else {
	// myPoints[i].setBackgroundResource(R.drawable.dian_hui);
	// }
	// mGroup.addView(myPoints[i]);
	// }
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return Config.onKeyDown(keyCode, event, this);
	}

	OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// for (int i = 0; i < myPoints.length; i++) {
			// myPoints[arg0].setBackgroundResource(R.drawable.dian_hong);
			// // 不是当前选中的page，其小圆点设置为未选中的状态
			// if (arg0 != i) {
			// myPoints[i].setBackgroundResource(R.drawable.dian_hui);
			// }
			// }
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// myPoints[arg0].setBackgroundResource(R.drawable.umeng_socialize_follow_off);
		}
	};

	private void showDialog(final Activity context){
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
}
