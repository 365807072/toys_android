package com.yyqq.code.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.commen.model.Review;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.FaceRelativeLayout;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewList extends Activity implements OnPullDownListener {
	private String TAG = "ReviewList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private RelativeLayout title;
	private ImageView refresh;
	private ArrayList<Review> data = new ArrayList<Review>();
	private MyAdapter adapter;
	private int width, height;
	private EditText edit_review;
	private ImageButton reviewBtn;
	private ImageButton faceBtn;
	private FaceRelativeLayout custom_face_layout;
	private boolean isMy;
	private String at_id = "";
	private String owner_id = "";
	private String img_id = "";

	
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
		setContentView(R.layout.review_list);
		context = this;
		init();
	}

	private void init() {
		isMy = getIntent().getBooleanExtra("ismy", false);
		edit_review = (EditText) findViewById(R.id.text);
		edit_review.setOnKeyListener(myKeyListener);
		reviewBtn = (ImageButton) findViewById(R.id.send);
		reviewBtn.setOnClickListener(reviewClick);
		faceBtn = (ImageButton) findViewById(R.id.face);
		faceBtn.setOnClickListener(faceClick);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		custom_face_layout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		custom_face_layout.setEditText(edit_review);
		img_id = getIntent().getStringExtra("img_id");
		owner_id = getIntent().getStringExtra("owner_id");
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(2);
		return super.onKeyDown(keyCode, event);

	}

	boolean isClearReview = false;
	private final OnKeyListener myKeyListener = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_DEL) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (edit_review.getText().toString().length() == 0) {
						isClearReview = true;
					}
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					if (isClearReview) {
						edit_review.setHint("");
						isClearReview = false;
					}
				}
				return false;
			}
			return false;
		}
	};

	AbHttpUtil ab;
	public OnClickListener reviewClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (edit_review.getText().toString().trim().length() > 0) {
				Config.showProgressDialog(context, false, null);
				AbRequestParams params = new AbRequestParams();
				params.put("user_id", Config.getUser(context).uid); // 用户id
				params.put("owner_id", owner_id); // 发秀秀的用户id
				params.put("at_id", at_id); // 回复评论的用户id
				params.put("demand", edit_review.getText().toString().trim()); // 评论或者回复评论的内容
				params.put("img_id", img_id); // 秀秀id
//				Log.e("fanfan",ServerMutualConfig.ReviewV1 + "&" + params);
				ab.post(ServerMutualConfig.ReviewV1, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										edit_review.setText("");
										edit_review.setHint(null);
										img_id = getIntent().getStringExtra(
												"img_id");
										onRefresh();
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
							public void onFailure(int statusCode,
									String content, Throwable error) {
								super.onFailure(statusCode, content, error);
								Config.showFiledToast(context);
							}
						});
			}
		}
	};

	public OnClickListener faceClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
				custom_face_layout.hideFaceView();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(ReviewList.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				custom_face_layout.showFaceView();
			}
		}
	};

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("img_id", getIntent().getStringExtra("img_id"));
		 android.util.Log.e("fanfan", ServerMutualConfig.ReviewList + "&" +
		 params.toString());
		ab.get(ServerMutualConfig.ReviewList + "&" + params.toString(),
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
		if (data.isEmpty()) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			Toast.makeText(context, "没有更多的评论了!", Toast.LENGTH_SHORT).show();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("img_id", getIntent().getStringExtra("img_id"));
		params.put("last_id", data.get(data.size() - 1).id);
		ab.get(ServerMutualConfig.ReviewList + "&" + params.toString(),
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
			convertView = inflater.inflate(R.layout.review_item, null);
			final Review item = data.get(position);
			if (isMy) {
				convertView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {

						Builder builder = new Builder(context);
						builder.setMessage("确认要删除吗?");
						builder.setNegativeButton("删除",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Config.showProgressDialog(context,
												false, null);
										AjaxParams params = new AjaxParams();
										params.put("user_id",
												Config.getUser(context).uid);
										params.put("review_id", item.id);
										FinalHttp fh = new FinalHttp();
										fh.post(ServerMutualConfig.DelReview,
												params,
												new AjaxCallBack<String>() {
													@Override
													public void onFailure(
															Throwable t,
															String strMsg) {
														super.onFailure(t,
																strMsg);
														Config.dismissProgress();
														Config.showFiledToast(context);
													}

													@Override
													public void onSuccess(
															String t) {
														super.onSuccess(t);
														Config.dismissProgress();
														try {
															JSONObject json = new JSONObject(
																	t);
															Toast.makeText(
																	context,
																	json.getString("reMsg"),
																	Toast.LENGTH_SHORT)
																	.show();
															onRefresh();
														} catch (Exception e) {
														}
													}
												});
									}
								});
						builder.setNeutralButton("取消", null);
						builder.create().show();

						return false;
					}
				});
			}
			TextView name, review, time;
			ImageView rank;
			rank = (ImageView) convertView.findViewById(R.id.rank);
			// 等级
			if (!TextUtils.isEmpty(item.level_img)) {
				MyApplication.getInstance().display(item.level_img, rank,
						position);
				rank.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(context, Rank.class);
						startActivity(intent);
					}
				});
			}
			name = (TextView) convertView.findViewById(R.id.name);
			review = (TextView) convertView.findViewById(R.id.review);
			time = (TextView) convertView.findViewById(R.id.time);
			name.setText(item.user_name);
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.getExpressionString(context, item.demand);
			review.setText(spannableString);
			long dateTime = Config.getDateDays(
					sdf.format(new Date(System.currentTimeMillis())),
					sdf.format(new Date(item.create_time)));
			if (dateTime < 0) {
				time.setText(sdf.format(new Date(item.create_time)));
			}

			/**
			 * 大于5天
			 */
			else if (dateTime > 1000 * 60 * 60 * 24 * 5) {
				time.setText(sdf.format(new Date(item.create_time)));
			}
			/**
			 * 大于1天
			 */
			else if (dateTime > 1000 * 60 * 60 * 24) {
				time.setText((dateTime / 1000 / 60 / 60 / 24) + "天前");
			} else if (dateTime > 1000 * 60 * 60) {
				time.setText((dateTime / 1000 / 60 / 60) + "小时前");
			} else if (dateTime > 1000 * 60) {
				time.setText((dateTime / 1000 / 60) + "分钟前");
			} else {
				time.setText((dateTime / 1000) + "秒前");
			}
			RoundAngleImageView head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			MyApplication.getInstance().display(item.avatar, head,
					R.drawable.def_head);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					edit_review.setText("@" + item.user_name + ":");
					edit_review.setSelection(item.user_name.length() + 2);
					at_id = item.user_id;
					// android.util.Log.e("fanfan", at_id);
				}
			});
			head.setOnClickListener(new OnClickListener() {
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
			return convertView;
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
