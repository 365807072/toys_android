package com.yyqq.code.photo;

import java.io.Serializable;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.code.video.VideoFullScreenActivity;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.VideoListItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class VideoList extends Activity implements OnPullDownListener {

	private String TAG = "fanfan_VideoList";
	private PullDownView mPullDownView;
	private ListView listview;
	private Activity context;
	private String uid = "";
	private int width;
	private AbHttpUtil ab;
	private MyAdapter adapter;
	private TextView fride_title;
	private ArrayList<VideoListItem> data = new ArrayList<VideoListItem>();
	private RelativeLayout none;
	private ImageView refresh;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.release);

		if (getIntent().hasExtra("uid")) {
			uid = getIntent().getStringExtra("uid");
		}

		fride_title = (TextView) findViewById(R.id.fride_title);
		fride_title.setText("视频");
		none = (RelativeLayout) findViewById(R.id.none);
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		refresh = (ImageView) findViewById(R.id.refresh);
		refresh.setVisibility(View.GONE);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
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
		Config.showProgressDialog(context, false, null);

		onRefresh();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("user_id", uid);
		android.util.Log.e(TAG,
				ServerMutualConfig.GetVideoList + "&" + params.toString());
		ab.get(ServerMutualConfig.GetVideoList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								VideoListItem item = new VideoListItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(item);
							}
							if (data.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								none.setVisibility(View.VISIBLE);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
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
		params.put("user_id", uid);
		params.put("post_create_time", data.get(data.size() - 1).post_create_time);
		ab.get(ServerMutualConfig.GetVideoList + "&" + params.toString(),
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
								VideoListItem item = new VideoListItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
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
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	class ViewHolder {
		ImageView play;
		RelativeLayout video_hint;
		ImageView video_hint_img;
		ImageView video_hint_play;
		RelativeLayout my_show_videoLayout;
		ViewGroup videoLayout;
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

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final VideoListItem item = data.get(position);
			ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.video_list_item, null);
			holder = new ViewHolder();
			holder.my_show_videoLayout = (RelativeLayout) convertView
					.findViewById(R.id.my_show_video);
			holder.play = (ImageView) convertView
					.findViewById(R.id.iv_video_playicon);
			holder.videoLayout = (ViewGroup) convertView
					.findViewById(R.id.video_layout);
			holder.video_hint = (RelativeLayout) convertView
					.findViewById(R.id.video_hint);
			holder.video_hint_img = (ImageView) convertView
					.findViewById(R.id.video_hint_img);
			holder.video_hint_play = (ImageView) convertView
					.findViewById(R.id.video_hint_play);
			convertView.setTag(holder);

			MyApplication.getInstance().display(item.img, holder.video_hint_img, R.drawable.def_image);
			
			holder.video_hint.setVisibility(View.VISIBLE);
			holder.my_show_videoLayout.setVisibility(View.GONE);
			
			holder.play = (ImageView) convertView.findViewById(R.id.iv_video_playicon);
			holder.videoLayout = (ViewGroup) convertView.findViewById(R.id.video_layout);
			holder.video_hint_play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					PostBarTypeItem bean = new PostBarTypeItem();
					bean.setImg_id(item.img_id);
					bean.setVideo_url(item.video_url);
					bean.setImg(item.img);
					
					Intent intent = new Intent(v.getContext(), VideoDetialActivity.class);
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, bean);
					intent.putExtras(bun);
					VideoList.this.startActivity(intent);
				}
			});

			// 时间
			TextView day = (TextView) convertView.findViewById(R.id.day);
			day.setText(item.create_time);
			return convertView;
		}

	}

}
