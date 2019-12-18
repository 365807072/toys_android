package com.yyqq.code.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.HeadListItem;
import com.yyqq.commen.model.SpecialItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class SpecialActivity extends Activity implements OnPullDownListener {

	private final String TAG = "fanfna_SpecialActivity";
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private Activity context;
	private ArrayList<SpecialItem> data = new ArrayList<SpecialItem>();
	private ArrayList<SpecialItem> data1 = new ArrayList<SpecialItem>();
	private ArrayList<HeadListItem> headData = new ArrayList<HeadListItem>();
	private MyApplication app;
	private String fileName = "SpecialActivity.txt";
	private int windowWidth;
	/** 头部view **/
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private FrameLayout ly1, ly2, ly3;
	private ImageView ig1, ig2, ig3;
	private TextView tv1, tv2, tv3;;
	private LinearLayout right;
	
	private TextView toShow, toSpecial;
	private ImageView back;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.myspecial);
		MobSDK.init(context);
		init();
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void init() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		app = (MyApplication) context.getApplication();
		inflater = LayoutInflater.from(context);
		/** 头部的view **/
		initHeadbar();

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(backClick);
		toShow = (TextView) findViewById(R.id.toShow);
		toSpecial = (TextView) findViewById(R.id.toSpecial);
		toShow.setOnClickListener(showClick);
		toSpecial.setOnClickListener(specialClick);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (adapter == null) {
			adapter = new MyAdapter();
		}
		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		if (!Config.isConn(context)) {
			try {
				getlistData(Config.read(context, fileName));
			} catch (Exception e) {
			}
		}
		onRefresh();
	}

	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.special_head, null);
		/**
		 * 热门主题
		 */
		// 计算左右两边宽度
		int hi = (windowWidth - Config.getSize("15")) / 5;
		int leftWidth = hi * 3;
		int rightWidth = hi * 2;

		// 右边整体布局宽高
		right = (LinearLayout) mRelativeLayout.findViewById(R.id.right);
		right.getLayoutParams().height = leftWidth;
		right.getLayoutParams().width = rightWidth;
		// 左边整体布局宽高
		ly1 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly1);
		ly1.getLayoutParams().height = leftWidth;
		ly1.getLayoutParams().width = leftWidth;
		// 右边图片高度
		ly2 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly2);
		ly2.getLayoutParams().height = -1;
		ly3 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly3);
		ly3.getLayoutParams().height = -1;

		ig1 = (ImageView) mRelativeLayout.findViewById(R.id.ig1);
		ig2 = (ImageView) mRelativeLayout.findViewById(R.id.ig2);
		ig3 = (ImageView) mRelativeLayout.findViewById(R.id.ig3);

		ig1.setScaleType(ScaleType.CENTER_CROP);
		ig2.setScaleType(ScaleType.CENTER_CROP);
		ig3.setScaleType(ScaleType.CENTER_CROP);

		tv1 = (TextView) mRelativeLayout.findViewById(R.id.tv1);
		tv2 = (TextView) mRelativeLayout.findViewById(R.id.tv2);
		tv3 = (TextView) mRelativeLayout.findViewById(R.id.tv3);
	}

	private void getValue() {
		app.display(data1.get(0).listImg.get(0).img, ig1, R.drawable.def_image);
		app.display(data1.get(1).listImg.get(0).img, ig2, R.drawable.def_image);
		app.display(data1.get(2).listImg.get(0).img, ig3, R.drawable.def_image);
		ig1.setOnClickListener(igClick1);
		ig2.setOnClickListener(igClick2);
		ig3.setOnClickListener(igClick3);
		tv1.setText(data1.get(0).cate_name);
		tv2.setText(data1.get(1).cate_name);
		tv3.setText(data1.get(2).cate_name);
	}

	public OnClickListener igClick1 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailList.class);
			intent.putExtra("cate_id", data1.get(0).cate_id);
			intent.putExtra("cate_name", data1.get(0).cate_name);
			startActivity(intent);
		}
	};
	public OnClickListener igClick2 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailList.class);
			intent.putExtra("cate_id", data1.get(1).cate_id);
			intent.putExtra("cate_name", data1.get(1).cate_name);
			startActivity(intent);
		}
	};
	public OnClickListener igClick3 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailList.class);
			intent.putExtra("cate_id", data1.get(2).cate_id);
			intent.putExtra("cate_name", data1.get(2).cate_name);
			startActivity(intent);
		}
	};

	private void getRemenList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// Log.e("fanfan",
		// ServerMutualConfig.ShowPlazaList + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.ShowPlazaList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data1.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								SpecialItem item = new SpecialItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), content);
								data1.add(item);
							}
							getValue();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onRefresh() {
		getRemenList();
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// Log.e("fanfan",
		// ServerMutualConfig.SpecialListV2 + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.SpecialListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						Config.save(context, content, fileName);
						getlistData(content);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
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
		if (data.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("last_id", data.get(data.size() - 1).rank);
		// Log.e("fanfan_fan",
		// ServerMutualConfig.SpecialListV2 + "&" + params.toString());
		ab.get(ServerMutualConfig.SpecialListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								SpecialItem item = new SpecialItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), content);
								data.add(item);
							}
							adapter.notifyDataSetChanged();
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

	class ViewHolder {
		TextView theme_name;
		TextView theme_renshu;
		LinearLayout theme_img;
		ImageView img1, img2, img3, img4;
	}

	@SuppressLint("ResourceAsColor")
	public class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.myspecial_item, null);
			holder = new ViewHolder();
			holder.theme_name = (TextView) convertView
					.findViewById(R.id.theme_name);
			holder.theme_renshu = (TextView) convertView
					.findViewById(R.id.theme_renshu);
			holder.theme_img = (LinearLayout) convertView
					.findViewById(R.id.theme_img);
			holder.img1 = new ImageView(context);
			holder.img2 = new ImageView(context);
			holder.img3 = new ImageView(context);
			holder.img4 = new ImageView(context);
			int hi = (windowWidth - Config.getSize("19")) / 4;
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(hi,
					hi);
			((MarginLayoutParams) param).rightMargin = Config.getSize("3");
			holder.theme_img.addView(holder.img1, param);
			holder.theme_img.addView(holder.img2, param);
			holder.theme_img.addView(holder.img3, param);
			holder.theme_img.addView(holder.img4, param);
			convertView.setTag(holder);

			final SpecialItem item = data.get(position);
			// 主题名称
			holder.theme_name.setText(item.cate_name);
			// 参与人数
			holder.theme_renshu.setText("共" + item.renshu + "人参与");
			// 图片
			app.display(item.listImg.get(0).img, holder.img1,
					R.drawable.deft_color);
			app.display(item.listImg.get(1).img, holder.img2,
					R.drawable.deft_color);
			app.display(item.listImg.get(2).img, holder.img3,
					R.drawable.deft_color);
			app.display(item.listImg.get(3).img, holder.img4,
					R.drawable.deft_color);
			holder.img1.setScaleType(ScaleType.CENTER_CROP);
			holder.img2.setScaleType(ScaleType.CENTER_CROP);
			holder.img3.setScaleType(ScaleType.CENTER_CROP);
			holder.img4.setScaleType(ScaleType.CENTER_CROP);
			// 点击事件
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, SpecialDetailList.class);
					intent.putExtra("cate_id", item.cate_id);
					intent.putExtra("cate_name", item.cate_name);
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	// private void makeScaleType(String value) {
	// if (value.equalsIgnoreCase("fitxy")) {// 不按比例缩放图片，目标是把图片塞满整个View
	// this.setScaleType(ScaleType.FIT_XY);
	// } else if (value.equalsIgnoreCase("fitcenter")) {//
	// 图片按比例扩大/缩小到View的宽度，居中显示
	// this.setScaleType(ScaleType.FIT_CENTER);
	// } else if (value.equalsIgnoreCase("center")) {//
	// 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
	// this.setScaleType(ScaleType.CENTER);
	// } else if (value.equalsIgnoreCase("centercrop")) {//
	// 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
	// this.setScaleType(ScaleType.CENTER_CROP);
	// } else if (value.equalsIgnoreCase("centerinside")) {//
	// 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
	// this.setScaleType(ScaleType.CENTER_INSIDE);
	// } else if (value.equalsIgnoreCase("fitstart")) {//
	// 图片按比例扩大/缩小到View的宽度，置于顶部
	// this.setScaleType(ScaleType.FIT_START);
	// } else if (value.equalsIgnoreCase("fitend")) {// 图片按比例扩大/缩小到View的宽度，置于底部
	// this.setScaleType(ScaleType.FIT_END);
	// }
	// }

	private void getlistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			for (int i = 0; i < json.getJSONArray("data").length(); i++) {
				SpecialItem item = new SpecialItem();
				item.fromJson(json.getJSONArray("data").getJSONObject(i),
						content);
				data.add(item);
			}
			if (data.isEmpty()) {
				mPullDownView.setVisibility(View.GONE);
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			context.finish();
		}
	};
	
	public OnClickListener showClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, ShowShowMainActivity.class);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};
	public OnClickListener specialClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialActivity.class);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};
}