package com.yyqq.code.personal;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class GrowList extends Activity implements OnPullDownListener {
	private String TAG = "GrowList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private ArrayList<SearchItem> data = new ArrayList<SearchItem>();
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private LinearLayout general_ly1;
	
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
		setContentView(R.layout.grow_list);
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

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// android.util.Log.e("fanfan", ServerMutualConfig.BabysIdolList + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.BabysIdolList + "&" + params.toString(),
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
									SearchItem item = new SearchItem();
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
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("post_create_time",
				data.get(data.size() - 1).post_create_time + "");
		// android.util.Log.e("fanfan", ServerMutualConfig.BabysIdolList + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.BabysIdolList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									SearchItem item = new SearchItem();
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

	class ViewHolder {
		TextView userName;
		TextView babyName;
		ImageView attention;
		ImageView head;
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
		public View getView(final int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grow_search_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			holder.babyName = (TextView) convertView
					.findViewById(R.id.babyName);
			holder.attention = (ImageView) convertView
					.findViewById(R.id.attention);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			final SearchItem item = data.get(index);
			holder.userName.setText(item.nick_name);
			holder.babyName.setText(item.description);
			MyApplication.getInstance().display(item.avatar, holder.head,
					R.drawable.def_head);
			if ("1".equals(item.is_each_others)) {// 同意了
				holder.attention
						.setBackgroundResource(R.drawable.grow_cancle_attention);
			} else {// 没同意
				holder.attention.setBackgroundResource(R.drawable.agree);
			}
			holder.attention.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if ("1".equals(item.is_each_others)) {
						Builder builder = new Builder(context);
						builder.setMessage("取消后TA就不能同步您的成长记录了");
						builder.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										AbRequestParams params = new AbRequestParams();
										params.put("login_user_id",
												Config.getUser(context).uid);
										params.put("babys_idol_id",
												item.babys_idol_id);
										params.put("is_each_others", "2");
										// android.util.Log
										// .e("fanfan",
										// ServerMutualConfig.EditBabysIdol
										// + "&"
										// + params.toString());
										ab.get(ServerMutualConfig.EditBabysIdol
												+ "&" + params.toString(),
												new AbStringHttpResponseListener() {
													@Override
													public void onSuccess(
															int statusCode,
															String content) {
														super.onSuccess(
																statusCode,
																content);
														try {
															JSONObject json = new JSONObject(
																	content);
															if (json.getBoolean("success")) {
																Toast.makeText(
																		context,
																		json.getString("reMsg"),
																		Toast.LENGTH_SHORT)
																		.show();
																onRefresh();
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
													}

													@Override
													public void onFailure(
															int statusCode,
															String content,
															Throwable error) {
														super.onFailure(
																statusCode,
																content, error);
													}
												});
									}
								});
						builder.setNeutralButton("取消", null);
						builder.create().show();
					} else {
						AbRequestParams params = new AbRequestParams();
						params.put("login_user_id", Config.getUser(context).uid);
						params.put("babys_idol_id", item.babys_idol_id);
						params.put("is_each_others", "1");
						// android.util.Log.e(
						// "fanfan",
						// ServerMutualConfig.EditBabysIdol + "&"
						// + params.toString());
						ab.get(ServerMutualConfig.EditBabysIdol + "&"
								+ params.toString(),
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
												onRefresh();
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
									}

									@Override
									public void onFailure(int statusCode,
											String content, Throwable error) {
										super.onFailure(statusCode, content,
												error);
									}
								});
					}

				}
			});
			return convertView;
		}

	}

}
