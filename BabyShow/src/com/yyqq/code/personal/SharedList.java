package com.yyqq.code.personal;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.SharedItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class SharedList extends Activity implements OnPullDownListener {
	private final String TAG = "SharedList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private TextView describe;
	private ArrayList<SharedItem> data = new ArrayList<SharedItem>();
	private MyAdapter adapter;
	private String isShareMan;
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
		setContentView(R.layout.shared_list);
		isShareMan = getIntent().getStringExtra("isShareMan");
		uid = getIntent().getStringExtra("uid");
		mPullDownView = (PullDownView) findViewById(R.id.list);
		describe = (TextView) findViewById(R.id.describe);
		// describe.setText("我共享的：TA的相册显示在我的相册中；我可下载共享相册里的高清相片。\n共享我的：我的相册显示在在TA的相册中；TA可以下载共享相册里的高清相片。");
		// if (isShareMan.equals("0")) {
		// if (uid.equals(Config.getUser(context).uid)) {
		// describe.setText("我的共享：对方宝宝的相册，直接出现在我的相册中；我可下载共享相册里的高清相片");
		// }
		// } else {
		// if (uid.equals(Config.getUser(context).uid)) {
		// describe.setText("共享我的：我宝宝的相册，会出现在以上人员的相册中；他们可以下载共享相册里的高清相片");
		// }
		// }
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
			convertView = inflater.inflate(R.layout.shared_item, null);
			final SharedItem item = data.get(position);
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
			MyApplication.getInstance().display(item.head, head,
					R.drawable.def_head);
			ImageView action = (ImageView) convertView
					.findViewById(R.id.action);
			if (uid.equals(Config.getUser(context).uid)) {
				action.setTag(item);
				setFocus(action, item);
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

		public void setFocus(View focus, SharedItem people) {
			/**
			 * 0 = 没有共享 1 = 共享 2 互相共享
			 */
			switch (people.isShare) {
			case 0:
				break;
			case 1:
				if (isShareMan.equals("0")) {
					focus.setBackgroundResource(R.drawable.my_share);
					focus.setOnClickListener(cancelShare);
				} else if (isShareMan.equals("1")) {
					focus.setBackgroundResource(R.drawable.share_me_relation);
					focus.setOnClickListener(doShare);
				}
				break;
			case 2:
				focus.setBackgroundResource(R.drawable.share_inch);
				if (isShareMan.equals("0")) {
					focus.setOnClickListener(cancelShare);
				} else if (isShareMan.equals("1")) {
					focus.setOnClickListener(doShare);
				}
				break;
			}
		}

		public OnClickListener cancelShare = new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final SharedItem people = (SharedItem) v.getTag();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("宝宝秀秀");
				builder.setMessage("如果取消共享，在你的共享相册里将不会显示TA的相册了哦");
				builder.setPositiveButton("取消共享",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Config.showProgressDialog(context, false, null);
								AbRequestParams params = new AbRequestParams();
								params.put("user_id",
										Config.getUser(context).uid);
								params.put("share_id", people.user_id);
								ab.post(ServerMutualConfig.CancelShare, params,
										new AbStringHttpResponseListener() {
											@Override
											public void onSuccess(
													int statusCode,
													String content) {
												super.onSuccess(statusCode,
														content);
												try {
													JSONObject json = new JSONObject(
															content);
													if (json.getBoolean("success")) {
														onRefresh();
													}
													Toast.makeText(
															context,
															json.getString("reMsg"),
															Toast.LENGTH_SHORT)
															.show();
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
											public void onFailure(
													int statusCode,
													String content,
													Throwable error) {
												super.onFailure(statusCode,
														content, error);
											}
										});
							}
						});
				builder.setNeutralButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.create().show();
			}
		};
		public OnClickListener doShare = new OnClickListener() {
			@Override
			public void onClick(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("宝宝秀秀");
				builder.setMessage("如果取消共享，在TA的共享相册里将不会显示你的相册了哦");
				builder.setPositiveButton("不让TA看了",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								final SharedItem people = (SharedItem) v
										.getTag();
								Config.showProgressDialog(context, false, null);
								AbRequestParams params = new AbRequestParams();
								params.put("user_id",
										Config.getUser(context).uid);
								params.put("share_id", people.user_id);
								params.put("share_type", 1 + "");
								params.put("is_agree", 0 + "");
								ab.post(ServerMutualConfig.DoShare, params,
										new AbStringHttpResponseListener() {
											@Override
											public void onSuccess(
													int statusCode,
													String content) {
												super.onSuccess(statusCode,
														content);
												try {
													JSONObject json = new JSONObject(
															content);
													if (json.getBoolean("success")) {
														onRefresh();
													}
													Toast.makeText(
															context,
															json.getString("reMsg")
																	+ "-----",
															Toast.LENGTH_SHORT)
															.show();
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
											public void onFailure(
													int statusCode,
													String content,
													Throwable error) {
												super.onFailure(statusCode,
														content, error);
											}
										});
							}
						});
				builder.setNeutralButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.create().show();

			}
		};
	}

	@Override
	public void onRefresh() {
		pageSize = 1;
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", uid);
		params.put("page", pageSize + "");
		// android.util.Log.e("fanfan", ServerMutualConfig.ShareListV2 + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.ShareListV2 + "&" + params.toString(),
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
									SharedItem item = new SharedItem();
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
		// android.util.Log.e("fanfan", ServerMutualConfig.ShareListV2 + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.ShareListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									SharedItem item = new SharedItem();
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