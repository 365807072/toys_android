package com.yyqq.code.business;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.Review;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessReviewList extends Activity implements OnPullDownListener {
	public String TAG = "fanfan_BusinessReviewList";
	public Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private ArrayList<Review> data = new ArrayList<Review>();
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private MyApplication app;

	private LinearLayout general_ly1;
	private TextView general_title;
	
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
		setContentView(R.layout.general_left_list);
		context = this;
		app = (MyApplication) context.getApplication();
		// 标题
		general_title = (TextView) findViewById(R.id.general_title);
		general_title.setText("评论");
		// 返回
		general_ly1 = (LinearLayout) findViewById(R.id.general_ly1);
		general_ly1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		});
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
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
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("business_id", getIntent().getStringExtra("business_id"));
		android.util.Log.e(TAG, ServerMutualConfig.BusinessCommentList + "&"
				+ params.toString());
		ab.get(ServerMutualConfig.BusinessCommentList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								Review review = new Review();
								review.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(review);
							}
							if (json.getJSONArray("data").length() == 0) {
								Toast.makeText(context, "暂时没有评论,快评论一下吧!",
										Toast.LENGTH_SHORT).show();
							}
							adapter.notifyDataSetChanged();
							mPullDownView.notifyDidMore();
							mPullDownView.RefreshComplete();
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
	public void onMore() {
		if (data.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("business_id", getIntent().getStringExtra("business_id"));
		params.put("post_create_time",
				data.get(data.size() - 1).post_create_time);
		ab.get(ServerMutualConfig.BusinessCommentList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								Review review = new Review();
								review.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(review);
							}
							adapter.notifyDataSetChanged();
							mPullDownView.notifyDidMore();
							mPullDownView.RefreshComplete();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.business_review_item, null);
			final Review item = data.get(position);
			TextView user_name; // 评论用户
			TextView user_time; // 评论时间
			ImageView grade3; // 用户评价
			TextView user_msg; // 评论内容

			user_name = (TextView) convertView.findViewById(R.id.user_name);
			if (!TextUtils.isEmpty(item.user_name)) {
				user_name.setText(item.user_name);
			}
			user_time = (TextView) convertView
					.findViewById(R.id.expiration_time);
			if (!TextUtils.isEmpty(item.user_time)) {
				user_time.setText(item.user_time);
			}
			grade3 = (ImageView) convertView.findViewById(R.id.grade3);
			if (!TextUtils.isEmpty(item.grade3)) {
				app.getInstance().display(item.grade3, grade3, 0);
			}
			user_msg = (TextView) convertView.findViewById(R.id.user_msg);
			if (!TextUtils.isEmpty(item.user_msg)) {
				user_msg.setText(item.user_msg);
			}

			return convertView;
		}
	}

}
