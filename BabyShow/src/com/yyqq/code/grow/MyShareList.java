package com.yyqq.code.grow;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.commen.model.MyShareListItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyShareList extends Activity implements OnPullDownListener {
	public String tag = "MyShareList";
	public Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private ArrayList<MyShareListItem> data = new ArrayList<MyShareListItem>();
	private String babyId = "";
	
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
		setContentView(R.layout.myshare_list);
		context = this;
		babyId = getIntent().getStringExtra("baby_id");
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
		// mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("baby_id", babyId);
//		Log.e("fanfan", ServerMutualConfig.MyShareList + "&"
//				+ params.toString());
		ab.get(ServerMutualConfig.MyShareList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								MyShareListItem myShareItem = new MyShareListItem();
								myShareItem.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(myShareItem);
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
		params.put("baby_id", babyId);
		data.clear();
		ab.get(ServerMutualConfig.MyShareList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								MyShareListItem myShareItem = new MyShareListItem();
								myShareItem.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(myShareItem);
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
			convertView = inflater.inflate(R.layout.mysharelist_item, null);
			final MyShareListItem item = data.get(position);
			TextView babyName, userName;
			// 宝宝名字
			babyName = (TextView) convertView.findViewById(R.id.babyName);
			babyName.setText(item.baby_name);
			// 用户名字
			userName = (TextView) convertView.findViewById(R.id.userName);
			userName.setText(item.nick_name);
			// 头像
			RoundAngleImageView head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					if (item.user_id.equals(Config.getUser(context).uid)) {
						intent.setClass(context, PersonalCenterActivity.class);
						intent.putExtra("falg_return", true);
					} else {
						intent.setClass(context, UserInfo.class);
						intent.putExtra("uid", item.user_id);
					}
					startActivity(intent);
				}
			});
			MyApplication.getInstance().display(item.user_avatar, head,
					R.drawable.def_head);
			// 是否同步
			ImageView action = (ImageView) convertView
					.findViewById(R.id.action);
			if ("0".equals(item.is_common)) {
				action.setBackgroundResource(R.drawable.hebing);
				action.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						AbRequestParams params = new AbRequestParams();
						params.put("login_user_id", Config.getUser(context).uid);
						params.put("baby_id", babyId);
						params.put("share_baby_id", item.share_baby_id);
						params.put("share_user_id", item.user_id);
						// android.util.Log.e("fanfan", params.toString());
						ab.post(ServerMutualConfig.CombineSendMail, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										try {
											JSONObject json = new JSONObject(
													content);
											if (json.getBoolean("success")) {
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
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
									public void onFailure(int statusCode,
											String content, Throwable error) {
										super.onFailure(statusCode, content,
												error);
										Config.showFiledToast(context);
									}

								});
						/*
						 * Intent intent = new Intent(); intent = new
						 * Intent(context, Login.class);
						 * intent.putExtra("hebing", "hebing");
						 * intent.putExtra("baby_id", babyId);
						 * intent.putExtra("user_id", item.user_id);
						 * intent.putExtra("share_baby_id", item.share_baby_id);
						 * startActivity(intent);
						 */
					}
				});
			} else {
				action.setBackgroundResource(R.drawable.yihebing);
			}

			return convertView;
		}
	}

}
