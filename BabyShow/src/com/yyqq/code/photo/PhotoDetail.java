package com.yyqq.code.photo;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullGridView;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.commen.model.PhotoItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class PhotoDetail extends Activity implements AbOnListViewListener {
	public String tag = "PhotoDetail";
	private Activity context;
	private ArrayList<PhotoItem> data = new ArrayList<PhotoItem>();
	private AbPullGridView mAbPullGridView = null;
	private GridView mGridView = null;
	private AbTaskQueue mAbTaskQueue = null;
	private AbHttpUtil ab;
	private MyAdapter adapter;
	private TextView name;
	private TextView action;
	private TextView upload;
	boolean isDefault = false;
	boolean people = false;
	private String isShare;
	private int windowWidth;
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.photo_detail);
		people = getIntent().getBooleanExtra("people", false);
		isDefault = getIntent().getBooleanExtra("default", false);
		action = (TextView) findViewById(R.id.action);
		action.setOnClickListener(actionClick);
		if (isDefault || people) {
			action.setVisibility(View.GONE);
		}
		context = this;
		name = (TextView) findViewById(R.id.name);
		name.setText(getIntent().getStringExtra("name"));
		DisplayMetrics DM = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		mAbPullGridView = (AbPullGridView) findViewById(R.id.mPhotoGridView);
		// 开关默认打开
		mAbPullGridView.setPullRefreshEnable(true);
		mAbPullGridView.setPullLoadEnable(true);
		// 设置进度条的样式
		mAbPullGridView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullGridView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mGridView = mAbPullGridView.getGridView();
		// mGridView.setColumnWidth(150);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setNumColumns(3);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		adapter = new MyAdapter();
		mAbPullGridView.setAdapter(adapter);
		// 设置两种查询的事件
		mAbPullGridView.setAbOnListViewListener(this);
		upload = (TextView) findViewById(R.id.upload);
		if (isDefault || people) {
			upload.setVisibility(View.GONE);
		}
		upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder = new Builder(context);
				builder.setMessage("我们上传的是高清无损图片,请尽量使用WIFI/3G网络");
				builder.setNegativeButton("上传",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										PhoneAlbumActivity.class);
								intent.putExtra("album_id", getIntent()
										.getStringExtra("album_id"));
								intent.putExtra("iszhineng", false);
								startActivity(intent);
							}
						});
				builder.setNeutralButton("取消", null);
				builder.create().show();
			}
		});
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Config.showProgressDialog(context, false, null);
		onRefresh();
	}

	public OnClickListener actionClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, PhotoEdit.class);
			intent.putExtra("album_id", getIntent().getStringExtra("album_id"));
			intent.putExtra("name", getIntent().getStringExtra("name"));
			startActivity(intent);
		}
	};

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		if (people) {
			params.put("user_id", getIntent().getStringExtra("uid"));
		} else {
			params.put("user_id", Config.getUser(context).uid);
		}
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("album_id", getIntent().getStringExtra("album_id"));
		// android.util.Log.e("fanfan",
		// ServerMutualConfig.ImgList + "&" + params.toString());
		ab.get(ServerMutualConfig.ImgList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopRefresh();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data.clear();
								data_json = new JSONArray();
								if (json.getJSONArray("data").length() == 0) {
									Toast.makeText(context, "该照片夹没有照片",
											Toast.LENGTH_SHORT).show();
								} else {
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										PhotoItem item = new PhotoItem();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										data.add(item);
										data_json.put(json.getJSONArray("data")
												.getJSONObject(i));
									}
								}
								adapter.notifyDataSetChanged();
							} else {
								Toast.makeText(context,
										json.getString("reMsg"),
										Toast.LENGTH_SHORT).show();
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
						Config.showFiledToast(context);
					}
				});
	}

	JSONArray data_json = new JSONArray();

	@Override
	public void onLoadMore() {
		if (data.size() == 0) {
			mAbPullGridView.stopLoadMore();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		if (people) {
			params.put("user_id", getIntent().getStringExtra("uid"));
		} else {
			params.put("user_id", Config.getUser(context).uid);
		}
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("album_id", getIntent().getStringExtra("album_id"));
		params.put("last_id", data.get(data.size() - 1).img_id);
		ab.get(ServerMutualConfig.ImgList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								{
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										PhotoItem item = new PhotoItem();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										data.add(item);
										data_json.put(json.getJSONArray("data")
												.getJSONObject(i));
									}
									adapter.notifyDataSetChanged();
									mAbPullGridView.stopLoadMore();
								}
							} else {
								Toast.makeText(context,
										json.getString("reMsg"),
										Toast.LENGTH_SHORT).show();
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
						Config.showFiledToast(context);
					}
				});
	}

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.photo_detail_item, null);
			final PhotoItem item = data.get(position);
			ImageView img = (ImageView) convertView.findViewById(R.id.photo);
			// 计算图片尺寸
			int hi = windowWidth / 3;
			img.getLayoutParams().height = hi;
			img.getLayoutParams().width = hi;
			img.setScaleType(ScaleType.CENTER_CROP);
			MyApplication.getInstance().display(item.img_thumb, img,
					R.drawable.def_image);
			if (!TextUtils.isEmpty(getIntent().getStringExtra("xiuAlbum"))) {
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context, MainItemDetialActivity.class);
						intent.putExtra("img_id", item.img_id);
						startActivity(intent);
					}
				});
			} else {
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context, PhotoBigImage.class);
						intent.putStringArrayListExtra("imaWid", item.imaWid);
						intent.putStringArrayListExtra("imaHed", item.imaHed);
						intent.putExtra("default", isDefault);
						intent.putExtra("index", position);
						intent.putExtra("people", people);
						intent.putExtra("info", data_json.toString());
						intent.putExtra("uid", getIntent()
								.getStringExtra("uid"));
						intent.putExtra("friend",
								getIntent().getBooleanExtra("friend", false));
						intent.putExtra("name", context.getIntent()
								.getStringExtra("name"));
						intent.putExtra("isShare",
								getIntent().getStringExtra("isShare"));
						intent.putExtra("baby_name", getIntent()
								.getStringExtra("baby_name"));
						startActivity(intent);
					}
				});
			}

			return convertView;
		}
	}

}