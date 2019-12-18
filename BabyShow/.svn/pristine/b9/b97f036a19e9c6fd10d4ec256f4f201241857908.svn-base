package com.yyqq.code.photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullGridView;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class MoveImageList extends Activity implements AbOnListViewListener {
	public String TAG = "MoveImageList";
	public Activity context;
	AbHttpUtil http;
	ArrayList<PhotoItem> data = new ArrayList<PhotoItem>();
	AbPullGridView mAbPullGridView;
	GridView mGridView;
	MyAdapter adapter;
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.photo_move);
		http = AbHttpUtil.getInstance(context);
		http.setDebug(Log.isDebug);

		mAbPullGridView = (AbPullGridView) findViewById(R.id.list);
		mAbPullGridView.setPullRefreshEnable(false);
		mAbPullGridView.setPullLoadEnable(true);

		mGridView = mAbPullGridView.getGridView();
		// mGridView.setColumnWidth(150);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(3);
		mGridView.setNumColumns(2);
		mGridView.setPadding(5, 5, 5, 5);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(3);
		if (adapter == null) {
			adapter = new MyAdapter();
		}
		mAbPullGridView.setAdapter(adapter);
		mAbPullGridView.setAbOnListViewListener(this);
		// 设置进度条的样式
		mAbPullGridView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullGridView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mGridView = mAbPullGridView.getGridView();
		onRefresh();
	}

	public class PhotoItem {
		public String id = "";
		public String album_name = "";
		public String album_cover = "";
		public long create_time = 0;
		public String img_count;
		public int is_default;
		public String is_show_album = "";

		public void fromJson(JSONObject json) throws JSONException {
			if (json.has("id")) {
				id = json.getInt("id") + "";
			}
			if (json.has("album_name")) {
				album_name = json.getString("album_name");
			}
			if (json.has("album_cover")) {
				album_cover = json.getString("album_cover");
			}
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}
			if (json.has("img_count")) {
				img_count = json.getInt("img_count") + "";
			}
			if (json.has("is_default")) {
				is_default = json.getInt("is_default");
			}
			if (json.has("is_show_album")) {
				is_show_album = json.getString("is_show_album") + "";
			}

		}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.photoitem, null);
			final PhotoItem item = data.get(position);
			ImageView defImage = (ImageView) convertView
					.findViewById(R.id.photo_defimg);
			MyApplication.getInstance().display(item.album_cover, defImage,
					R.drawable.def_head);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.album_name);
//			TextView desc = (TextView) convertView.findViewById(R.id.desc);
//			desc.setText(item.desc);
			TextView time = (TextView) convertView.findViewById(R.id.time);
			time.setText(sdf.format(new Date(item.create_time)));
			TextView num = (TextView) convertView.findViewById(R.id.photonum);
			num.setText(item.img_count);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("album_id", item.id);
					if (item.is_default == 0) {
						intent.setClass(context, MovePhotoList.class);
						intent.putExtra("from_album_id", getIntent()
								.getStringExtra("album_id"));
						intent.putExtra("album_id", item.id);
						intent.putExtra("ids", getIntent()
								.getStringExtra("ids"));
						startActivityForResult(intent, 165);
					} else {
						Toast.makeText(context, "不能移动到该相册", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			return convertView;
		}
	}

	@Override
	public void onRefresh() {
		getAlbum();
	}

	public void getAlbum() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		http.get(ServerMutualConfig.AlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopRefresh();// 这个事线程安全的 可看源代码
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								mAbPullGridView.setVisibility(View.VISIBLE);
								data.clear();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									PhotoItem item = new PhotoItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i));
									data.add(item);
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

	@Override
	public void onLoadMore() {
		if (data.isEmpty()) {
			return;
		}
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("last_id", data.get(data.size() - 1).id);
		http.get(ServerMutualConfig.AlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopLoadMore();// 这个事线程安全的 可看源代码
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

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 165) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

}
