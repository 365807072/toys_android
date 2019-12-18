package com.yyqq.code.personal;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.FriendItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class FriendList extends Activity implements OnPullDownListener {
	private String TAG = "FriendList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private ImageView refresh;
	private ArrayList<FriendItem> data = new ArrayList<FriendItem>();
	private MyAdapter adapter;
	private int width, height;
	private String isFocus;
	private String uid;
	private AbHttpUtil ab;
	private int pageSize;
	
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
		setContentView(R.layout.friend_list);
		isFocus = getIntent().getStringExtra("isFocus");
		uid = getIntent().getStringExtra("uid");
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
		Config.showProgressDialog(context, false, null);
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		onRefresh();
	}

	@Override
	protected void onStart() {
		super.onStart();
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
			convertView = inflater.inflate(R.layout.friend_item, null);
			final FriendItem item = data.get(position);
			TextView name;
			ImageView rank;
			LinearLayout ly_rank;
			rank = (ImageView) convertView.findViewById(R.id.rank);
			ly_rank = (LinearLayout) convertView.findViewById(R.id.ly_rank);
			// 等级
			if (!TextUtils.isEmpty(item.level_img)) {
				MyApplication.getInstance().display(item.level_img, rank,
						position);
				ly_rank.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(context, Rank.class);
						startActivity(intent);
					}
				});
			}
			name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.nick_name);
			RoundAngleImageView head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			MyApplication.getInstance().display(item.avatar, head,
					R.drawable.def_head);
			ImageView action = (ImageView) convertView
					.findViewById(R.id.action);
			if (true) {
				action.setTag(item);
				setFocus(action, item);
				if (!uid.equals(Config.getUser(context).uid)) {
					action.setVisibility(View.GONE);
				}
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						if (item.user_id.equals(Config.getUser(context).uid)) {
							intent.setClass(context, PersonalCenterActivity.class);
						} else {
							intent.setClass(context, UserInfo.class);
							intent.putExtra("uid", item.user_id);
						}
						startActivity(intent);
					}
				});
			} else {
				action.setVisibility(View.GONE);
			}
			return convertView;
		}

		public void setFocus(View focus, FriendItem people) {
			/**
			 * 0 = 陌生人 1 = 已经关注 2=互相关注,3 = 对方关注我
			 */
			switch (people.relation) {
			case 0:
			case 3:
				focus.setBackgroundResource(R.drawable.no_focus);
				focus.setOnClickListener(addFriend);
				break;
			case 1:
				focus.setBackgroundResource(R.drawable.focused);
				focus.setOnClickListener(removeFriend);
				break;
			case 2:
				focus.setBackgroundResource(R.drawable.mutual);
				focus.setOnClickListener(removeFriend);
				break;
			}
		}

		public OnClickListener addFriend = new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final FriendItem people = (FriendItem) v.getTag();
				Config.showProgressDialog(context, false, null);
				AbRequestParams params = new AbRequestParams();
				params.put("user_id", Config.getUser(context).uid);
				params.put("idol_id", people.user_id);
				ab.post(ServerMutualConfig.FocusOn, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										// people.relation = json.getJSONObject(
										// "data").getInt("relation");
										people.relation = 2;
										setFocus(v, people);
									}
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
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
								super.onFailure(statusCode, content, error);
							}
						});
			}
		};
		public OnClickListener removeFriend = new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final FriendItem people = (FriendItem) v.getTag();
				Config.showProgressDialog(context, false, null);
				AbRequestParams params = new AbRequestParams();
				params.put("user_id", Config.getUser(context).uid);
				params.put("idol_id", people.user_id);
				ab.post(ServerMutualConfig.CancelFocus, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										people.relation = 0;
										setFocus(v, people);
									}
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
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
								super.onFailure(statusCode, content, error);
							}
						});
			}
		};
	}

	@Override
	public void onRefresh() {
		pageSize = 1;
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", uid);
		params.put("page", pageSize + "");
		// android.util.Log.e("fanfan", ServerMutualConfig.IdolListV2 + "&" +
		// params.toString());
		ab.get(ServerMutualConfig.IdolListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data.clear();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									FriendItem item = new FriendItem();
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
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
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
		params.put("user_id", uid);
		params.put("page", (++pageSize) + "");
		// android.util.Log.e("fanfan", ServerMutualConfig.IdolListV2 + "&" +
		// params.toString());
		ab.get(ServerMutualConfig.IdolListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									FriendItem item = new FriendItem();
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
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
}
