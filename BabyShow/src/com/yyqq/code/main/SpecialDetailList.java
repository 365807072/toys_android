package com.yyqq.code.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.model.SpecialDetailListItem;
import com.yyqq.commen.model.SpecialDetailListItem.Img;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class SpecialDetailList extends Activity implements OnPullDownListener {
	private String TAG = "fanfan_SpecialDetailList";
	private Activity context;
	private PullDownView mPullDownView;
	private ListView listview;
	private ArrayList<SpecialDetailListItem> data = new ArrayList<SpecialDetailListItem>();
	private MyAdapter adapter;
	private int width;
	private MyApplication myMyApplication;
	private String cate_id, cateName;
	private ImageView listBt, gridBt;
	private AbHttpUtil ab;
	private TextView tt, visit;
	// 分享的4个参数fz
	private String share_title = "";
	private String share_img = "";
	private String share_imgId = "";
	private String share_userId = "";

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// this.startActivity(new Intent(this,MainTab.class));
	// this.finish();
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		onRefresh();
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		myMyApplication = (MyApplication) context.getApplication();
		setContentView(R.layout.special_list);
		cate_id = getIntent().getStringExtra("cate_id");
		mPullDownView = (PullDownView) findViewById(R.id.list);
		listBt = (ImageView) findViewById(R.id.listBt);
		listBt.setOnClickListener(listClick);
		gridBt = (ImageView) findViewById(R.id.gridBt);
		gridBt.setOnClickListener(gridClick);
		tt = (TextView) findViewById(R.id.tt);
		visit = (TextView) findViewById(R.id.visit);
		visit.setOnClickListener(visitClick);
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
		DisplayMetrics DM = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		onRefresh();
	}

	public OnClickListener visitClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if ("1".equals(myMyApplication.getVisitor())) {
				myMyApplication.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, AddNewPostActivity.class);
				intent.putExtra("cate_id", cate_id);
				startActivity(intent);
			}
			
		}
	};
	public OnClickListener listClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailList.class);
			intent.putExtra("cate_id", cate_id);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};
	public OnClickListener gridClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailGrid.class);
			intent.putExtra("cate_id", cate_id);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("cate_id", cate_id);
		android.util.Log.e(TAG, ServerMutualConfig.SpecialDetailList + "&"
				+ params.toString());
		ab.get(ServerMutualConfig.SpecialDetailList + "&" + params.toString(),
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
									SpecialDetailListItem item = new SpecialDetailListItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i), content);
									data.add(item);
								}
								tt.setText(data.get(0).cate_name);
								cateName = data.get(0).cate_name;
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
		params.put("cate_id", cate_id);
		params.put("last_id", data.get(data.size() - 1).rank);
		// android.util.Log.e("fanfan-FAN", ServerMutualConfig.SpecialDetailList
		// +
		// "&"
		// + params.toString());
		ab.get(ServerMutualConfig.SpecialDetailList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									SpecialDetailListItem item = new SpecialDetailListItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i), content);
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

	@SuppressLint("ResourceAsColor")
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
			ViewHolder holder = null;
			convertView = inflater.inflate(R.layout.special_detail_list_item,
					null);
			holder = new ViewHolder();
			final SpecialDetailListItem item = data.get(position);
			// 头像
			holder.head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			MyApplication.getInstance().display(item.avatar, holder.head,
					R.drawable.def_head);
			holder.head.setOnClickListener(new OnClickListener() {
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

			// 用户名
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.name.setText(item.nick_name);
			// 等级
			holder.rank = (ImageView) convertView.findViewById(R.id.rank);
			holder.ly_rank = (LinearLayout) convertView
					.findViewById(R.id.ly_rank);
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
			// 时间
			holder.ly_time_1 = (RelativeLayout) convertView
					.findViewById(R.id.ly_time_1);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.ly_time_2 = (RelativeLayout) convertView
					.findViewById(R.id.ly_time_2);
			holder.month = (TextView) convertView.findViewById(R.id.month);
			holder.year = (TextView) convertView.findViewById(R.id.year);
			holder.day = (TextView) convertView.findViewById(R.id.day);
			// 时间
			long dateTime = Config.getDateDays(
					sdf.format(new Date(System.currentTimeMillis())),
					sdf.format(new Date(item.create_time)));
			/* 一天之内的 */
			if (dateTime <= 1000 * 60 * 60 * 24) {
				if (dateTime > 1000 * 60 * 60) {
					holder.time.setText((dateTime / 1000 / 60 / 60) + "小时前");
				} else if (dateTime > 1000 * 60) {
					holder.time.setText((dateTime / 1000 / 60) + "分钟前");
				} else {
					holder.time.setText((dateTime / 1000) + "秒前");
				}
				holder.ly_time_1.setVisibility(View.VISIBLE);
				holder.ly_time_2.setVisibility(View.GONE);
			}
			/**
			 * 大于1天
			 */
			else {
				holder.ly_time_2.setVisibility(View.VISIBLE);
				holder.ly_time_1.setVisibility(View.GONE);
				String[] str = Config.getStringDateShort(item.create_time);
				holder.day.setText(str[2]);
				holder.month.setText(str[1] + "月");
				holder.year.setText(str[0] + "年");
			}
			// 关注按钮
			holder.attention = (ImageView) convertView
					.findViewById(R.id.attention);
			final RelativeLayout time_show = (RelativeLayout) convertView
					.findViewById(R.id.time_show);
			final ImageView attention = (ImageView) convertView
					.findViewById(R.id.attention);
			if ("0".equals(item.is_focus)) { // 0表示不是好友，1是好友
				time_show.setVisibility(View.GONE);
				attention.setVisibility(View.VISIBLE);
				attention.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Config.showProgressDialog(context, false, null);
						AbRequestParams params = new AbRequestParams();
						params.put("user_id", Config.getUser(context).uid);
						params.put("idol_id", item.user_id);
						// Log.e("fanfan", params.toString());
						ab.post(ServerMutualConfig.FocusOn, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										try {
											JSONObject json = new JSONObject(
													content);
											if (json.getBoolean("success")) {
												time_show
														.setVisibility(View.VISIBLE);
												attention
														.setVisibility(View.GONE);
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
										super.onFailure(statusCode, content,
												error);
									}
								});
					}
				});
			} else {
				time_show.setVisibility(View.VISIBLE);
				attention.setVisibility(View.GONE);
			}
			// 图片
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			int newW = 0, newH = 0;
			Img specialItemImg = item.listImg.get(0);
			if (specialItemImg.img_thumb_width == specialItemImg.img_thumb_height) {
				newW = width;
				newH = width;
			} else if (Float.parseFloat(specialItemImg.img_thumb_width) > Float
					.parseFloat(specialItemImg.img_thumb_height)) {
				float h = 0;
				float sx = (float) ((float) width / (float) (Integer
						.parseInt(specialItemImg.img_thumb_width)));
				h = (float) ((float) Float
						.parseFloat(specialItemImg.img_thumb_height) * (float) sx);
				newW = width;
				newH = (int) h;
			} else {
				float sx = (float) (width / 3 * 2)
						/ (Float.parseFloat(specialItemImg.img_thumb_width));
				newW = (int) ((float) (((Float
						.parseFloat(specialItemImg.img_thumb_width)) * sx)));
				newH = (int) ((float) ((Float
						.parseFloat(specialItemImg.img_thumb_height)) * sx));
			}
			View view = (RelativeLayout) convertView.findViewById(R.id.img_ly);
			view.setBackgroundColor(R.color.imgbg);
			view.getBackground().setAlpha(19);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					newW, newH);
			params.addRule(RelativeLayout.BELOW, R.id.head_root);
			holder.img.setLayoutParams(params);
			holder.img.setTag(specialItemImg);
			holder.img.setScaleType(ScaleType.CENTER_CROP);
			MyApplication.getInstance().display(specialItemImg.img_thumb,
					holder.img, R.drawable.deft_color);

			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					ArrayList<String> imgBig = new ArrayList<String>();
					ArrayList<String> imaHed = new ArrayList<String>();
					ArrayList<String> imaWid = new ArrayList<String>();
					for (int i = 0; i < item.listImg.size(); i++) {
						imgBig.add(item.listImg.get(i).img);
						imaHed.add(item.listImg.get(i).img_height);
						imaWid.add(item.listImg.get(i).img_width);
					}
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", imaWid);
					intent.putStringArrayListExtra("imaHed", imaHed);
					intent.putExtra("listIndex", 0);
					context.startActivity(intent);
				}
			});
			// 描述
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			if (TextUtils.isEmpty(item.img_description)) {
				holder.msg.setVisibility(View.GONE);
			} else {
				holder.msg.setText(item.img_description);
			}
			// 赞
			final TextView zan = (TextView) convertView.findViewById(R.id.zan);
			zan.setText(item.admire_count);
			if (item.isZan) {
				zan.setBackgroundResource(R.drawable.fabu_zan_sel);
			} else {
				zan.setBackgroundResource(R.drawable.fabu_zan);
			}
			zan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Config.showProgressDialog(context, false, null);
					if (!item.isZan) {
						AbRequestParams params = new AbRequestParams();
						params.put("user_id", Config.getUser(context).uid);
						params.put("admire_id", item.user_id);
						params.put("img_id", item.img_id);
						ab.post(ServerMutualConfig.Admire, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										item.admire_count = (Integer
												.parseInt(item.admire_count) + 1)
												+ "";
										zan.setText(item.admire_count);
										item.isZan = true;
										zan.setBackgroundResource(R.drawable.fabu_zan_sel);
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
					} else {
						AbRequestParams params = new AbRequestParams();
						params.put("user_id", Config.getUser(context).uid);
						params.put("admire_id", item.user_id);
						params.put("img_id", item.img_id);
						ab.post(ServerMutualConfig.CancelAdmire, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										item.admire_count = (Integer
												.parseInt(item.admire_count) - 1)
												+ "";
										item.isZan = false;
										zan.setBackgroundResource(R.drawable.fabu_zan);
										zan.setText(item.admire_count);
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
					}
				}
			});

			// 评论
			holder.review = (TextView) convertView.findViewById(R.id.review);
			holder.review.setText(item.review_count);
			holder.review.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ReviewList.class);
					intent.putExtra("img_id", item.img_id);
					intent.putExtra("owner_id", item.user_id);
					intent.putExtra("ismy",
							item.user_id.equals(Config.getUser(context).uid));
					startActivity(intent);
				}
			});
			// 分享按钮
			holder.shareBt = (ImageView) convertView.findViewById(R.id.shareBt);
			holder.shareBt.setTag(item);
			holder.shareBt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SpecialDetailListItem item = (SpecialDetailListItem) v
							.getTag();
					share_title = "我在宝宝秀秀参与了【" + cateName + "】活动，我是"
							+ item.rsort + "号，请大家帮我投一票吧！爱你哟";
					share_imgId = item.img_id;
					share_userId = item.user_id;

					if (item.listImg.size() > 1) {
						share_img = item.listImg.get(0).img_thumb;
					} else if (item.listImg.size() == 1) {
						share_img = item.listImg.get(0).img_thumb;
					}
					showShare();
				}
			});
			// 更多操作
			holder.more = (ImageView) convertView.findViewById(R.id.more);
			holder.more.setTag(item);
			holder.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Builder builder = new Builder(context);
					builder.setMessage("确认要删除吗?");
					builder.setNegativeButton("删除",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									AjaxParams params = new AjaxParams();
									params.put("user_id",
											Config.getUser(context).uid);
									params.put("img_ids", item.img_id);
									FinalHttp fh = new FinalHttp();
									fh.post(ServerMutualConfig.DelSpecial,
											params, new AjaxCallBack<String>() {
												@Override
												public void onFailure(
														Throwable t,
														String strMsg) {
													super.onFailure(t, strMsg);
													Config.showFiledToast(context);
												}

												@Override
												public void onSuccess(String t) {
													super.onSuccess(t);
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
				}
			});
			// 头像列表
			holder.h1 = (RoundAngleImageView) convertView.findViewById(R.id.h1);
			holder.h2 = (RoundAngleImageView) convertView.findViewById(R.id.h2);
			holder.h3 = (RoundAngleImageView) convertView.findViewById(R.id.h3);
			holder.h4 = (RoundAngleImageView) convertView.findViewById(R.id.h4);
			holder.h5 = (RoundAngleImageView) convertView.findViewById(R.id.h5);
			if (data.get(position).listAvatar != null
					&& data.get(position).listAvatar.size() > 0) {
				for (int i = 0; i < data.get(position).listAvatar.size(); i++) {
					if (i == 0)
						MyApplication.getInstance().display(
								data.get(position).listAvatar.get(i).avatar,
								holder.h1, R.drawable.def_head);
					else if (i == 1)
						MyApplication.getInstance().display(
								data.get(position).listAvatar.get(i).avatar,
								holder.h2, R.drawable.def_head);
					else if (i == 2)
						MyApplication.getInstance().display(
								data.get(position).listAvatar.get(i).avatar,
								holder.h3, R.drawable.def_head);
					else if (i == 3)
						MyApplication.getInstance().display(
								data.get(position).listAvatar.get(i).avatar,
								holder.h4, R.drawable.def_head);
					else if (i == 4)
						MyApplication.getInstance().display(
								data.get(position).listAvatar.get(i).avatar,
								holder.h5, R.drawable.def_head);
				}
			}
			// 互动人数
			holder.renshu = (TextView) convertView.findViewById(R.id.renshu);
			holder.renshu.setText("有" + item.interaction_count + "人看了TA");
			convertView.setTag(holder);
			return convertView;
		}

		private void getImage(String imagePath,
				final ArrayList<String> BigImagePath, final ImageView view,
				final int positon, final View convertView) {
			view.setTag(imagePath);
			ImageDownloader mDownloader = null;
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null) {
				mDownloader.imageDownload(imagePath, view,
						"/Download/cache_files/", context,
						new OnImageDownload() {
							@Override
							public void onDownloadSucc(Bitmap bitmap,
									String c_url, ImageView imageView) {
								ImageView imageView1 = (ImageView) convertView
										.findViewWithTag(c_url);
								if (imageView1 != null) {
									imageView1.setImageBitmap(bitmap);
									imageView1.setTag("");
								}

							}
						});
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					intent.putExtra("imgList", BigImagePath);
					context.startActivity(intent);
				}
			});
		}
	}

	class ViewHolder {
		ImageView more;
		ImageView img;
		TextView name;
		RoundAngleImageView head;
		TextView msg;
		TextView review;
		TextView zan;
		ImageView attention;
		RelativeLayout ly_time;
		TextView picture_size;
		LinearLayout ly_picture_page;
		RelativeLayout ly_time_1;
		TextView time;
		RelativeLayout ly_time_2;
		TextView month;
		TextView year;
		TextView day;
		ImageView rank;
		LinearLayout ly_rank;
		ImageView renqi;
		ImageView h1, h2, h3, h4, h5;
		TextView renshu;
		ImageView shareBt;
	}

	private void showShare() {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id=" + share_imgId + "&user_id="
									+ share_userId);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(share_title
							+ "http://www.meimei.yihaoss.top/share.php?"
							+ "img_id=" + share_imgId + "&user_id="
							+ share_userId);
					// android.util.Log.e("fanfan",
					// "http://www.meimei.yihaoss.top/share.php?"
					// + "img_id=" + share_imgId + "&user_id="
					// + share_userId);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(""); // title微信、QQ空间
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setSiteUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId);
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id=" + share_imgId + "&user_id="
									+ share_userId);
				}

			}
		});
		// 启动分享GUI
		oks.show(context);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}