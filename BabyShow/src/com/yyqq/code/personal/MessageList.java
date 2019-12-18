package com.yyqq.code.personal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.photo.ImageDetail;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.MessageItem;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class MessageList extends Activity implements OnPullDownListener {
	private String TAG = "fanfan_MessageList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private ArrayList<MessageItem> messageData = new ArrayList<MessageItem>();
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private TextView fride_title;
	
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
		fride_title = (TextView) findViewById(R.id.fride_title);
		fride_title.setText("消息");
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
			return messageData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return messageData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.message_item, null);
			final ViewHolder holder = new ViewHolder();

			holder.head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.action = (ImageView) convertView.findViewById(R.id.action);
			holder.new_icon = (ImageView) convertView
					.findViewById(R.id.new_icon);
			holder.rank = (ImageView) convertView.findViewById(R.id.rank);
			holder.ly_rank = (LinearLayout) convertView
					.findViewById(R.id.ly_rank);
			convertView.setTag(holder);
			final MessageItem item = messageData.get(position);
			// 头像
			MyApplication.getInstance().display(item.avatar, holder.head,
					R.drawable.def_head);
			holder.head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					if (item.user_id.equals(Config.getUser(context).uid)) {
						intent.putExtra("falg_return", true);
						intent.setClass(context, PersonalCenterActivity.class);
					} else {
						intent.setClass(context, UserInfo.class);
						intent.putExtra("uid", item.user_id);
					}
					startActivity(intent);
				}
			});
			// 用户名
			holder.name.setText(item.nick_name);
			// 等级
			if (!TextUtils.isEmpty(item.level_img)) {
				MyApplication.getInstance().display(item.level_img,
						holder.rank, position);
				holder.ly_rank.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(context, Rank.class);
						startActivity(intent);
					}
				});
			}
			// 消息
			holder.msg.setText(item.message);
			holder.action.setTag(item);
			if (0 == item.is_read) {
				holder.new_icon.setVisibility(View.VISIBLE);
			} else {
				holder.new_icon.setVisibility(View.GONE);
			}
			if ("41".equals(item.point)) {
				holder.img.setVisibility(View.GONE);
				holder.action.setVisibility(View.VISIBLE);
				holder.time.setVisibility(View.GONE);
				if (item.relation == 0) {
					holder.action.setBackgroundResource(R.drawable.no_focus);
					holder.action.setOnClickListener(addFriend);
				} else if (item.relation == 1) {
					holder.action.setBackgroundResource(R.drawable.focused);
					holder.action.setOnClickListener(removeFriend);
				} else if (item.relation == 2) {
					holder.action.setBackgroundResource(R.drawable.mutual);
					holder.action.setOnClickListener(removeFriend);
				}
			}
			if ("42".equals(item.point)) {
				holder.img.setVisibility(View.GONE);
				holder.action.setVisibility(View.VISIBLE);
				holder.time.setVisibility(View.GONE);
				if (item.is_agree == 0) {
					holder.action.setBackgroundResource(R.drawable.agree);
					holder.action.setOnClickListener(agreeClick);
				} else {
					holder.action.setBackgroundResource(R.drawable.agreed);
					holder.action.setOnClickListener(refuseClick);
				}
			}

			// 访问了你
			if ("63".equals(item.point)) {
				holder.img.setVisibility(View.GONE);
				holder.action.setVisibility(View.GONE);
			}
			// 合并成长记录
			if ("31".equals(item.point)) {
				holder.img.setVisibility(View.GONE);
				holder.time.setVisibility(View.GONE);
				holder.action.setBackgroundResource(R.drawable.hebing);
				if (0 == item.is_combin) {
					holder.action.setBackgroundResource(R.drawable.hebing);
					holder.action.setVisibility(View.VISIBLE);
					holder.action.setOnClickListener(hebingClick);
				} else {
					holder.action.setVisibility(View.GONE);
				}
			}
			// 人气宝宝
			if ("14".equals(item.point)) {
				holder.img.setVisibility(View.GONE);
				holder.action.setVisibility(View.GONE);
				holder.time.setVisibility(View.GONE);
			}
			// 图片
			if (item.message.contains("共享")) {
				holder.img.setVisibility(View.GONE);
			} else {
				MyApplication.getInstance().display(item.img_thumb, holder.img,
						R.drawable.deft_white_bg);
			}
			// 时间
			long dateTime = Config.getDateDays(
					Config.sdf.format(new Date(System.currentTimeMillis())),
					Config.sdf.format(new Date(item.create_time)));
			if (dateTime < 0) {
				holder.time.setText(Config.sdf
						.format(new Date(item.create_time)));
			}
			// /**
			// * 大于5天
			// */
			// else if (dateTime > 1000 * 60 * 60 * 24 * 5) {
			// holder.time.setText(Config.sdf
			// .format(new Date(item.create_time)));
			// }
			/**
			 * 大于1天
			 */
			else if (dateTime > 1000 * 60 * 60 * 24) {
				holder.time.setText((dateTime / 1000 / 60 / 60 / 24) + "天前");
			} else if (dateTime > 1000 * 60 * 60) {
				holder.time.setText((dateTime / 1000 / 60 / 60) + "小时前");
			} else if (dateTime > 1000 * 60) {
				holder.time.setText((dateTime / 1000 / 60) + "分钟前");
			} else {
				holder.time.setText((dateTime / 1000) + "秒前");
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setSysMessage();
					holder.new_icon.setVisibility(View.GONE);
					Intent intent = new Intent();
					if ("11".equals(item.point) || "12".equals(item.point)
							|| "13".equals(item.point)) {
						if(!TextUtils.isEmpty(item.video_url)){
							intent.setClass(context, VideoDetialActivity.class);
							Bundle bundel = new Bundle();
							PostBarTypeItem bean1 = new PostBarTypeItem();
							bean1.setImg_id(item.img_id);
							bean1.setVideo_url(item.video_url);
							bean1.setImg(item.img_thumb);
							bundel.putSerializable(VideoDetialActivity.VIIDEO_INFO,
									(Serializable) bean1);
							intent.putExtras(bundel);
						}else{
							intent.setClass(context, MainItemDetialActivity.class);
							intent.putExtra("img_id", item.root_img_id);
						}
					} else if ("21".equals(item.point)
							|| "22".equals(item.point)
							|| "23".equals(item.point)
							|| "24".equals(item.point)) {
						if(!TextUtils.isEmpty(item.video_url)){
							intent.setClass(context, VideoDetialActivity.class);
							Bundle bundel = new Bundle();
							PostBarTypeItem bean1 = new PostBarTypeItem();
							bean1.setImg_id(item.img_id);
							bean1.setVideo_url(item.video_url);
							bean1.setImg(item.img_thumb);
							bundel.putSerializable(VideoDetialActivity.VIIDEO_INFO,
									(Serializable) bean1);
							intent.putExtras(bundel);
						}else{
							intent.setClass(context, MainItemDetialActivity.class);
							intent.putExtra("isMessage", "1");
							intent.putExtra("img_id", item.root_img_id);
						}
						intent.putExtra("user_id", item.user_id);
					} else {
						if (item.user_id.equals(Config.getUser(context).uid)) {
							intent.putExtra("falg_return", true);
							intent.setClass(context, PersonalCenterActivity.class);
						} else {
							intent.setClass(context, UserInfo.class);
							intent.putExtra("uid", item.user_id);
						}
					}
					startActivity(intent);
				}

				private void setSysMessage() {
					SharedPreferences sp = context.getSharedPreferences(
							"babyshow_system_count", Context.MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putInt("notice", 0);
					edit.commit();
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		RoundAngleImageView head;
		TextView name;
		TextView msg;
		TextView time;
		ImageView img;
		ImageView action;
		ImageView new_icon;
		ImageView rank;
		LinearLayout ly_rank;
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("user_id", Config.getUser(context).uid);
//		android.util.Log.e(TAG,
//				ServerMutualConfig.ListingMyMessage + "&" + params.toString());
		ab.get(ServerMutualConfig.ListingMyMessage + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						messageData.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								MessageItem item = new MessageItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								messageData.add(item);
							}
							// if (messageData.isEmpty()) {
							// mPullDownView.setVisibility(View.GONE);
							// Intent intent = new Intent();
							// intent.setClass(context, MainTab.class);
							// intent.putExtra("tabid", 1);
							// startActivity(intent);
							// context.finish();
							// }
							if (messageData.isEmpty()) {
								mPullDownView.setHideFooter();
							}
							mPullDownView.RefreshComplete();// 这个事线程安全的
															// 可看源代码
							adapter.notifyDataSetChanged();
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
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	@Override
	public void onMore() {
		if (messageData.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("last_id", messageData.get(messageData.size() - 1).id);
		// android.util.Log.e("fanfan1", ServerMutualConfig.ListingMyMessage + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.ListingMyMessage + "&" + params.toString(),
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
								MessageItem item = new MessageItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								messageData.add(item);
							}
							adapter.notifyDataSetChanged();
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
					}
				});
	}

	public OnClickListener hebingClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			// 　　该接口有四个参数login_user_id，baby_id，share_user_id，share_baby_id
			// 分别对应下面返回结果中的标签名依次是：user_id，img_id，share_user_id，root_img_id
			MessageItem item = (MessageItem) v.getTag();
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", item.user_id);
			params.put("share_user_id", item.share_user_id);
			params.put("baby_id", item.img_id);
			params.put("share_baby_id", item.root_img_id);
//			Log.e(TAG,params.toString());
			ab.post(ServerMutualConfig.CombineDiaryV2, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									// Intent intent = new Intent();
									// intent.setClass(context, MainTab.class);
									// intent.putExtra("tabid", 1);
									// startActivity(intent);
									// context.finish();
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
									onRefresh();
								} else {
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
									onRefresh();
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
						}
					});
		}

	};

	public OnClickListener addFriend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			MessageItem item = (MessageItem) v.getTag();
			AbRequestParams params = new AbRequestParams();
			params.put("user_id", Config.getUser(context).uid);
			params.put("idol_id", item.user_id);
			ab.post(ServerMutualConfig.FocusOn, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									// getMessage(false);
									onRefresh();
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
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
						}
					});
		}
	};
	public OnClickListener removeFriend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MessageItem item = (MessageItem) v.getTag();
			Config.showProgressDialog(context, false, null);
			AbRequestParams params = new AbRequestParams();
			params.put("user_id", Config.getUser(context).uid);
			params.put("idol_id", item.user_id);
			ab.post(ServerMutualConfig.CancelFocus, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									// getMessage(false);
									onRefresh();
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
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
						}
					});
		}
	};

	public OnClickListener refuseClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final MessageItem item = (MessageItem) v.getTag();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("确定要取消共享吗?");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Config.showProgressDialog(context, true, null);
							AbRequestParams params = new AbRequestParams();
							params.put("user_id", Config.getUser(context).uid);
							params.put("share_id", item.user_id);
							params.put("share_type", 1 + "");
							params.put("is_agree", 0 + "");
							ab.post(ServerMutualConfig.DoShare, params,
									new AbStringHttpResponseListener() {
										@Override
										public void onSuccess(int statusCode,
												String content) {
											super.onSuccess(statusCode, content);
											try {
												JSONObject json = new JSONObject(
														content);
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
												// getMessage(false);
												onRefresh();
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
											super.onFailure(statusCode,
													content, error);
											Config.showFiledToast(context);
										}
									});
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
		}
	};

	public OnClickListener agreeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final MessageItem item = (MessageItem) v.getTag();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("确定要将相册共享给" + item.nick_name + "吗?");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Config.showProgressDialog(context, true, null);
							AbRequestParams params = new AbRequestParams();
							params.put("user_id", Config.getUser(context).uid);
							params.put("share_id", item.user_id);
							params.put("share_type", 1 + "");
							params.put("is_agree", 1 + "");
							ab.post(ServerMutualConfig.DoShare, params,
									new AbStringHttpResponseListener() {
										@Override
										public void onSuccess(int statusCode,
												String content) {
											super.onSuccess(statusCode, content);
											try {
												JSONObject json = new JSONObject(
														content);
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
												// getMessage(false);
												onRefresh();
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
											super.onFailure(statusCode,
													content, error);
											Config.showFiledToast(context);
										}
									});
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
		}
	};
}
