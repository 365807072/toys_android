package com.yyqq.commen.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbAppUtil;
import com.mob.MobSDK;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.ShowShowMainActivity;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.code.video.VideoFullScreenActivity;
import com.yyqq.commen.model.PictureItem;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.myVideoPlayerView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class MainFollowListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<PictureItem> data;
	private AbHttpUtil ab;
	private int width;;
	private String share_title;
	private String share_img;
	private String share_imgId;
	private String share_userId;
	
	public MainFollowListAdapter(Context context, ArrayList<PictureItem> data, AbHttpUtil ab) {
		super();
		this.context = context;
		this.data = data;
		this.ab = ab;
	}

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

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.mypicture_item, null);

		DisplayMetrics DM = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		
		final ViewHolder holder = new ViewHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.img);
		holder.mGridView = (MyGridView) convertView.findViewById(R.id.n_img);
		holder.permission_relation = (ImageView) convertView
				.findViewById(R.id.permission_relation);
		holder.ly_link = (LinearLayout) convertView.findViewById(R.id.ly_link);
		holder.img_link = (ImageView) convertView.findViewById(R.id.img_link);
		holder.msg_link = (TextView) convertView.findViewById(R.id.msg_link);
		holder.more = (ImageView) convertView.findViewById(R.id.more);
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.rank = (ImageView) convertView.findViewById(R.id.rank);
		holder.ly_rank = (LinearLayout) convertView.findViewById(R.id.ly_rank);
		holder.msg = (TextView) convertView.findViewById(R.id.msg);
		holder.head = (RoundAngleImageView) convertView.findViewById(R.id.head);
		holder.ly_time_1 = (RelativeLayout) convertView
				.findViewById(R.id.ly_time_1);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.ly_time_2 = (RelativeLayout) convertView
				.findViewById(R.id.ly_time_2);
		holder.month = (TextView) convertView.findViewById(R.id.month);
		holder.year = (TextView) convertView.findViewById(R.id.year);
		holder.day = (TextView) convertView.findViewById(R.id.day);
		holder.attention = (ImageView) convertView.findViewById(R.id.attention);
		holder.review = (TextView) convertView.findViewById(R.id.review);
		holder.video_layout = (myVideoPlayerView) convertView
				.findViewById(R.id.video_layout);
		holder.video_start = (ImageView) convertView
				.findViewById(R.id.iv_video_play);
		holder.video_stop = (ImageView) convertView
				.findViewById(R.id.iv_video_stop);
		holder.video_player_all = (RelativeLayout) convertView
				.findViewById(R.id.item_layout);
		holder.iv_video_thumb = (ImageView) convertView
				.findViewById(R.id.iv_video_thumb);
		holder.iv_video_bg = (ImageView) convertView
				.findViewById(R.id.iv_video_bg);
		holder.video_play_layout = (LinearLayout) convertView
				.findViewById(R.id.video_play_layout);
		holder.iv_video_bg_up = (ImageView) convertView
				.findViewById(R.id.iv_video_bg_up);
		holder.iv_video_loading = (ProgressBar) convertView
				.findViewById(R.id.iv_video_loading);
		holder.iv_video_bg_hint = (ImageView) convertView
				.findViewById(R.id.iv_video_bg_hint);
		holder.iv_video_bg_hint02 = (ImageView) convertView
				.findViewById(R.id.iv_video_bg_hint02);
		holder.video_type = (Button) convertView.findViewById(R.id.video_type);
		holder.skbProgress = (SeekBar) convertView
				.findViewById(R.id.skbProgress);
		holder.video_all_length = (TextView) convertView
				.findViewById(R.id.video_all_length);
		holder.video_new_time = (TextView) convertView
				.findViewById(R.id.video_new_time);
		holder.video_play_console = (LinearLayout) convertView
				.findViewById(R.id.video_play_console);
		holder.iv_to_full = (ImageView) convertView.findViewById(R.id.video_to_fullScreen);
		convertView.setTag(holder);

		final PictureItem item = data.get(position);

		if (!item.video_url.isEmpty()) {

			holder.video_player_all.setLayoutParams(MyApplication.getVideoPlayerParams(-1));
			
			// 更多按钮改为删除按钮
//			holder.more.setImageBitmap(BitmapFactory.decodeResource(convertView.getResources(),R.drawable.vide_delete));
			holder.video_play_console.setVisibility(View.GONE);

			// 竖图直接覆盖展示
			if (Integer.parseInt(item.img_thumb_width) < Integer.parseInt(item.img_thumb_height)) {
				MyApplication.getInstance().display(item.video_image_url, holder.iv_video_thumb, R.drawable.def_image);
				holder.iv_video_thumb.setVisibility(View.VISIBLE);
			} else {
				holder.iv_video_bg_hint.setVisibility(View.GONE);
				holder.iv_video_bg_hint02.setVisibility(View.GONE);
				holder.iv_video_thumb.setVisibility(View.GONE);
			}
			MyApplication.getInstance().display(item.video_image_url,
					holder.iv_video_bg_up, R.drawable.def_image);
			MyApplication.getInstance().display(item.video_image_url,
					holder.iv_video_bg, R.drawable.def_image);
			holder.img.setVisibility(View.GONE);
			holder.mGridView.setVisibility(View.GONE);

			holder.video_play_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PostBarTypeItem bean = new PostBarTypeItem();
					bean.setImg_id(item.img_id);
					bean.setVideo_url(item.video_url);
					bean.setImg(item.video_image_url);
					bean.setImg_thumb_width(item.img_thumb_width);
					bean.setImg_thumb_height(item.img_thumb_height);

					Intent intent = new Intent(v.getContext(), VideoDetialActivity.class);
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, bean);
					intent.putExtras(bun);
					context.startActivity(intent);
				}
			});
			
			holder.iv_to_full.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, VideoFullScreenActivity.class);
					intent.putExtra(VideoFullScreenActivity.IMG_ID_KEY, item.img_id);
					context.startActivity(intent);
				}
			});

			holder.video_start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (holder.video_layout.isPlaying()) {
						holder.video_layout.suspend();
						holder.video_start.setVisibility(View.VISIBLE);
					} else {
						for (int i = 0; i < data.size(); i++) {
							data.get(i).isPlay = false;
							if (position == i) {
								data.get(i).isPlay = true;
							}
						}
						notifyDataSetChanged();
					}
				}
			});

			holder.video_stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (holder.video_layout.isPlaying()) {
						holder.video_layout.pause();
						holder.video_stop.setVisibility(View.GONE);
					} else {
						holder.video_stop.setVisibility(View.GONE);
					}
				}
			});

			holder.video_player_all.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (holder.video_layout.isPlaying()) {
						holder.video_start.setVisibility(View.GONE);
						holder.video_stop.setVisibility(View.VISIBLE);
					} else {
						holder.video_start.setVisibility(View.VISIBLE);
						holder.video_stop.setVisibility(View.GONE);
					}
				}
			});

			holder.video_layout.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (Integer.parseInt(item.img_thumb_width) > Integer.parseInt(item.img_thumb_height)) {
								holder.video_layout.setLayoutParams(MyApplication.getVideoPlayerParams(R.id.item_layout));
							}

							holder.iv_video_bg_up.setVisibility(View.GONE);
							holder.iv_video_loading.setVisibility(View.GONE);
							holder.iv_video_bg_hint.setVisibility(View.GONE);
							holder.video_play_console.setVisibility(View.VISIBLE);
						}
					});
				}
			});

			holder.video_layout
					.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							holder.indexPosition = -1;
							for (int i = 0; i < data.size(); i++) {
								data.get(i).isPlay = false;
							}
							notifyDataSetChanged();
						}
					});

			holder.video_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (holder.video_play_console.isShown()) {
						holder.video_play_console.setVisibility(View.GONE);
					} else {
						holder.video_play_console.setVisibility(View.VISIBLE);
					}
				}
			});

			holder.video_type.setOnClickListener(new OnClickListener() {

				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					if (holder.video_layout.isPlaying()) {
						holder.video_layout.pause();
						holder.video_type.setBackground(context.getResources().getDrawable(R.drawable.video_start));
					} else {
						holder.video_layout.start();
						holder.video_type.setBackground(context.getResources().getDrawable(R.drawable.video_stop));
					}
				}
			});

			holder.skbProgress
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						int progress;

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							this.progress = progress
									* holder.video_layout.getDuration()
									/ seekBar.getMax();
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							holder.video_layout.seekTo(progress);
						}
					});

			if (item.isPlay) {
				if (AbAppUtil.isWifi(context)) {
					holder.indexPosition = -1;

					final Handler handleProgress = new Handler() {
						public void handleMessage(Message msg) {

							holder.position = holder.video_layout
									.getCurrentPosition();
							holder.duration = holder.video_layout.getDuration();

							Date date = new Date(holder.duration);
							SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
							String dateStr = sdf.format(date);

							date = new Date(holder.position);
							SimpleDateFormat sdf02 = new SimpleDateFormat(
									"mm:ss");
							String dateStr02 = sdf02.format(date);

							if (holder.duration > 0
									&& holder.position > holder.indexPosition) {
								holder.video_all_length.setText(dateStr);
								holder.video_new_time.setText(dateStr02 + "");
								long pos = holder.skbProgress.getMax()
										* holder.position / holder.duration;
								holder.skbProgress.setProgress((int) pos);
								holder.indexPosition = holder.position;
							}
						};
					};

					Timer videoPlayerTimer = new Timer();
					videoPlayerTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							if (holder.video_layout == null) {
								return;
							}
							if (holder.video_layout.isPlaying()
									&& holder.skbProgress.isPressed() == false) {
								handleProgress.sendEmptyMessage(0);
							}
						}
					}, 0, 50);

					MyApplication.getInstance().display(item.video_image_url,
							holder.iv_video_bg, R.drawable.def_image);
					holder.iv_video_thumb.setVisibility(View.GONE);
					holder.video_start.setVisibility(View.GONE);
					holder.iv_video_bg_hint.setVisibility(View.VISIBLE);
					holder.iv_video_loading.setVisibility(View.VISIBLE);

					Uri uri = Uri.parse(item.video_url);
					MediaController mc = new MediaController(context);
					mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
					holder.video_layout.setMediaController(mc);
					holder.video_layout.setVideoURI(uri);
					holder.video_layout.requestFocus();
					holder.video_layout.start();
				} else {
					Builder dialog = new Builder(context);
					dialog.setTitle("温馨提示");
					dialog.setMessage("您当前并未连接WIFI，继续播放将使用手机流量，是否继续？");
					dialog.setNegativeButton("继续",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									holder.indexPosition = -1;

									final Handler handleProgress = new Handler() {
										public void handleMessage(Message msg) {

											holder.position = holder.video_layout
													.getCurrentPosition();
											holder.duration = holder.video_layout
													.getDuration();

											Date date = new Date(
													holder.duration);
											SimpleDateFormat sdf = new SimpleDateFormat(
													"mm:ss");
											String dateStr = sdf.format(date);

											date = new Date(holder.position);
											SimpleDateFormat sdf02 = new SimpleDateFormat(
													"mm:ss");
											String dateStr02 = sdf02
													.format(date);

											if (holder.duration > 0
													&& holder.position > holder.indexPosition) {
												holder.video_all_length
														.setText(dateStr);
												holder.video_new_time
														.setText(dateStr02 + "");
												long pos = holder.skbProgress
														.getMax()
														* holder.position
														/ holder.duration;
												holder.skbProgress
														.setProgress((int) pos);
												holder.indexPosition = holder.position;
											}
										};
									};

									Timer videoPlayerTimer = new Timer();
									videoPlayerTimer.schedule(new TimerTask() {

										@Override
										public void run() {
											if (holder.video_layout == null) {
												return;
											}
											if (holder.video_layout.isPlaying()
													&& holder.skbProgress
															.isPressed() == false) {
												handleProgress
														.sendEmptyMessage(0);
											}
										}
									}, 0, 50);

									MyApplication.getInstance().display(
											item.video_image_url,
											holder.iv_video_bg,
											R.drawable.def_image);
									holder.iv_video_thumb
											.setVisibility(View.GONE);
									holder.video_start.setVisibility(View.GONE);
									holder.iv_video_bg_hint
											.setVisibility(View.VISIBLE);
									holder.iv_video_loading
											.setVisibility(View.VISIBLE);

									Uri uri = Uri.parse(item.video_url);
									MediaController mc = new MediaController(context);
									mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
									holder.video_layout.setMediaController(mc);
									holder.video_layout.setVideoURI(uri);
									holder.video_layout.requestFocus();
									holder.video_layout.start();
								}

							});
					dialog.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									holder.noWifiPlay = false;
								}
							});
					dialog.create().show();
				}
			}
		} else if (item.imaStrings.size() > 1) {
			holder.video_player_all.setVisibility(View.GONE);
			holder.mGridView.setVisibility(View.VISIBLE);
			holder.img.setVisibility(View.GONE);
			holder.mGridView.setAdapter(new MainFollowImageAdapter(context, item.user_id, item.img_id, width, item.imaStrings, holder.mGridView, item.imaBig, item.imaWid, item.imaHed));
		} else if (item.imaStrings.size() == 1) {
			holder.video_player_all.setVisibility(View.GONE);
			holder.mGridView.setVisibility(View.GONE);

			MyApplication.getInstance().display(item.img_thumb, holder.img,
					R.drawable.def_image);
			View view = (RelativeLayout) convertView.findViewById(R.id.img_ly);
			view.setBackgroundColor(R.color.imgbg);
			view.getBackground().setAlpha(19);

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, ChangePhotoSize.class);
//					intent.putExtra("imgList", item.imaBig);
//					context.startActivity(intent);
					Intent intent = new Intent(context, MainItemDetialActivity.class);
					intent.putExtra("user_id", item.user_id);
					intent.putExtra("img_id", item.img_id);
					context.startActivity(intent);
				}
			});

		} else {
			holder.video_player_all.setVisibility(View.GONE);
			holder.mGridView.setVisibility(View.GONE);
			holder.img.setVisibility(View.GONE);
		}

		// 等级
		if (!TextUtils.isEmpty(item.level_img)) {
			MyApplication.getInstance().display(item.level_img, holder.rank,
					position);
			holder.ly_rank.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, Rank.class);
					context.startActivity(intent);
				}
			});
		}

		// 添加关注
		final RelativeLayout time_show = (RelativeLayout) convertView
				.findViewById(R.id.time_show);
		final ImageView attention = (ImageView) convertView
				.findViewById(R.id.attention);
		time_show.setVisibility(View.VISIBLE);
		attention.setVisibility(View.GONE);
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
											time_show.setVisibility(View.VISIBLE);
											attention.setVisibility(View.GONE);
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
			});
		} else {
			time_show.setVisibility(View.VISIBLE);
			attention.setVisibility(View.GONE);
		}

		// 权限判断
		if ("0".equals(item.img_cate)) {
			holder.permission_relation.setVisibility(View.GONE);
		} else if ("1".equals(item.img_cate)) {
			holder.permission_relation.setVisibility(View.VISIBLE);
			holder.permission_relation
					.setBackgroundResource(R.drawable.permission_friend);
		}

		// 更多操作
		holder.more.setTag(item);
		holder.more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				share_userId = item.user_id;
				
				PictureItem item = (PictureItem) v.getTag();
				share_title = item.description;
				share_imgId = item.img_id;
				
				if (item.imaStrings.size() > 1) {
					share_img = item.imaStrings.get(0);
				} else if (item.imaStrings.size() == 1) {
					share_img = item.img_thumb;
				}
//				if (item.video_url.isEmpty()) {
				ShowShowMainActivity.isRefresh = false;
					showShare(item, "0", position);
//				} else {
//					showShare(item, "1", position);
//				}
			}
		});

		holder.name.setText(item.user_name);

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
				context.startActivity(intent);
			}
		});
		MyApplication.getInstance().display(item.avatar, holder.head,
				R.drawable.def_head);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 时间
		long dateTime = Config.getDateDays(
				sdf.format(new Date(System.currentTimeMillis())),
				sdf.format(new Date(Long.parseLong(item.post_create_time))));
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
			String[] str = Config.getStringDateShort(Long.parseLong(item.post_create_time));
			holder.day.setText(str[2]);
			holder.month.setText(str[1] + "月");
			holder.year.setText(str[0] + "年");
		}
		// 描述
		if ("".equals(item.description)) {
			holder.msg.setVisibility(View.GONE);
		} else {
			holder.msg.setText(item.description);
			holder.msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, MainItemDetialActivity.class);
					intent.putExtra("user_id", item.user_id);
					intent.putExtra("img_id", item.img_id);
					context.startActivity(intent);
				}
			});
//			if (item.description.startsWith("//来自☆话题// ")) {
//
//				holder.msg.setTextColor(Color.RED);
//				if (!"0".equals(item.group_id)) {
//					holder.msg.setOnClickListener(new View.OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							Intent intent = new Intent();
//							intent.setClass(context, GroupMainActivity.class);
//							intent.putExtra("group_id", item.group_id);
//							context.startActivity(intent);
//						}
//
//					});
//				} else {
//					holder.msg.setOnClickListener(new View.OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							// if(item.video_url.isEmpty()){
//							// PostBarTypeItem bean = new PostBarTypeItem();
//							// bean.setImg_id(item.img_id);
//							// bean.setVideo_url(item.video_url);
//							// bean.setImg(item.video_image_url);
//							// bean.setImg_thumb_width(item.img_thumb_width);
//							// bean.setImg_thumb_height(item.img_thumb_height);
//							//
//							// Intent intent = new Intent(getActivity(),
//							// VideoDetialActivity.class);
//							// Bundle bun = new Bundle();
//							// bun.putSerializable(VideoDetialActivity.VIIDEO_INFO,
//							// bean);
//							// intent.putExtras(bun);
//							// context.startActivity(intent);
//							// }else{
//
//							AbRequestParams params = new AbRequestParams();
//							params.put("user_id", item.user_id);
//							params.put("login_user_id",
//									Config.getUser(context).uid);
//							params.put("img_id", item.img_id);
//							params.put("create_time", item.post_create_time + "");
//							ab.get(ServerMutualConfig.GoToPost + "&"
//									+ params.toString(),
//									new AbStringHttpResponseListener() {
//										@Override
//										public void onSuccess(int statusCode,
//												String content) {
//											super.onSuccess(statusCode, content);
//											try {
//												JSONObject json = new JSONObject(
//														content);
//												GoPostItem postItem = new GoPostItem();
//												postItem.fromJson(json
//														.getJSONObject("data"));
//												Intent intent = new Intent(
//														context,
//														MainItemDetialActivity.class);
//												intent.putExtra("img_id",
//														postItem.img_id);
//												intent.putExtra("user_id",
//														item.user_id);
//												intent.putExtra("is_save",
//														postItem.is_save);
//												context.startActivity(intent);
//											} catch (JSONException e) {
//												e.printStackTrace();
//											}
//										}
//
//										@Override
//										public void onFinish() {
//											super.onFinish();
//										}
//
//										@Override
//										public void onFailure(int statusCode,
//												String content, Throwable error) {
//											super.onFailure(statusCode,
//													content, error);
//										}
//									});
//							// }
//						}
//					});
//				}
//			} else if (!"0".equals(item.cate_id)) {
//				holder.msg.setTextColor(Color.RED);
//				holder.msg.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						Intent intent = new Intent(context,
//								SpecialDetailGrid.class);
//						intent.putExtra("cate_id", item.cate_id);
//						context.startActivity(intent);
//					}
//				});
//
//			}
		}
		// 大图
		int newW = 0, newH = 0;
		if (item.img_thumb_width == item.img_thumb_height) {
			newW = width;
			newH = width;
		} else if (Float.parseFloat(item.img_thumb_width) > Float
				.parseFloat(item.img_thumb_height)) {
			float h = 0;
			float sx = (float) ((float) width / (float) (Integer
					.parseInt(item.img_thumb_width)));
			h = (float) ((float) Float.parseFloat(item.img_thumb_height) * (float) sx);
			newW = width;
			newH = (int) h;
		} else {
			float sx = (float) (width / 3 * 2)
					/ (Float.parseFloat(item.img_thumb_width));
			newW = (int) ((float) (((Float.parseFloat(item.img_thumb_width)) * sx)));
			newH = (int) ((float) ((Float.parseFloat(item.img_thumb_height)) * sx));
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				newW, newH);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.head_root);
		holder.img.setLayoutParams(params);

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
				if (!item.isZan) {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("admire_user_id", item.user_id);
					params.put("img_id", item.img_id);

					ab.post(ServerMutualConfig.PostAdmireNew, params, new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							item.admire_count = (Integer
									.parseInt(item.admire_count) + 1) + "";
							zan.setText(item.admire_count);
							item.isZan = true;
							zan.setBackgroundResource(R.drawable.fabu_zan_sel);
						}

						@Override
						public void onFinish() {
							super.onFinish();
						}

						@Override
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
						}

					});
				} else {

					AbRequestParams params = new AbRequestParams();
					params.put("user_id", Config.getUser(context).uid);
					params.put("admire_id", item.user_id);
					params.put("img_id", item.img_id);
					ab.post(ServerMutualConfig.CancelPostAdmireNew, params, new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							item.admire_count = (Integer
									.parseInt(item.admire_count) - 1) + "";
							item.isZan = false;
							zan.setBackgroundResource(R.drawable.fabu_zan);
							zan.setText(item.admire_count);
						}

						@Override
						public void onFinish() {
							super.onFinish();
						}

						@Override
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
						}
					});
				}
			}
		});
		holder.review.setText(item.review_count);
		holder.review.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				/**
				 * 判断类型是否是视频，是的话点击评论跳转至视频播放页面
				 */
				if (item.video_url.isEmpty()) {
//					Intent intent = new Intent(context, ReviewList.class);
//					intent.putExtra("img_id", item.img_id);
//					intent.putExtra("owner_id", item.user_id);
//					intent.putExtra("ismy",
//							item.user_id.equals(Config.getUser(context).uid));
//					context.startActivity(intent);
					
					Intent intent = new Intent(context, MainItemDetialActivity.class);
					intent.putExtra("user_id", item.user_id);
					intent.putExtra("img_id", item.img_id);
					context.startActivity(intent);

				} else {
					PostBarTypeItem bean = new PostBarTypeItem();
					bean.setImg_id(item.img_id);
					bean.setVideo_url(item.video_url);
					bean.setImg(item.video_image_url);
					bean.setImg_thumb_width(item.img_thumb_width);
					bean.setImg_thumb_height(item.img_thumb_height);

					Intent intent = new Intent(v.getContext(),
							VideoDetialActivity.class);
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, bean);
					intent.putExtras(bun);
					context.startActivity(intent);
				}
			}
		});
		// }

		return convertView;
	}

	// private void getImage(String imagePath,
	// final ArrayList<String> BigImagePath, final ImageView view,
	// final int positon, final View convertView) {
	// view.setTag(imagePath);
	// ImageDownloader mDownloader = null;
	// if (mDownloader == null) {
	// mDownloader = new ImageDownloader();
	// }
	// if (mDownloader != null) {
	// mDownloader.imageDownload(imagePath, view,
	// "/Download/cache_files/", context,
	// new OnImageDownload() {
	// @Override
	// public void onDownloadSucc(Bitmap bitmap,
	// String c_url, ImageView imageView) {
	// ImageView imageView1 = (ImageView) convertView
	// .findViewWithTag(c_url);
	// if (imageView1 != null) {
	// imageView1.setImageBitmap(bitmap);
	// imageView1.setTag("");
	// }
	//
	// }
	// });
	// }
	//
	// view.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent(context, ChangePhotoSize.class);
	// intent.putExtra("imgList", BigImagePath);
	// context.startActivity(intent);
	// }
	// });
	// }

	class ViewHolder {
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView msg;
		TextView review;
		TextView zan;
		ImageView permission_relation;
		ImageView img_link;
		TextView msg_link;
		LinearLayout ly_link;
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

		// 视频播放
		myVideoPlayerView video_layout;
		ImageView video_start;
		ImageView video_stop;
		ImageView iv_video_thumb;
		ImageView iv_video_bg;
		RelativeLayout video_player_all;
		LinearLayout video_play_layout;
		ImageView iv_video_bg_up;
		ProgressBar iv_video_loading;
		ImageView iv_video_bg_hint;
		ImageView iv_video_bg_hint02;
		ImageView iv_to_full;

		public int indexPosition;
		public int position;
		private int duration;
		private Button video_type;
		private SeekBar skbProgress;
		private TextView video_new_time;
		private TextView video_all_length;
		private LinearLayout video_play_console;
		private boolean noWifiPlay = false;
	}
	
	private void showShare(final PictureItem item, final String isVideo, final int position) {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, context.getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		Bitmap collect = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.logo_delete);
		Bitmap collectSel = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.logo_delete);
		String label = context.getResources().getString(R.string.delete);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				deleteItem(item, isVideo, position);
			}
		};
		oks.setCustomerLogo(collect, label, listener);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@SuppressLint("NewApi")
			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					
					if(share_title.isEmpty()){
						paramsToShare.setTitle("自由环球租赁");
					}else{
						paramsToShare.setTitle(share_title);
					}
					
					if(item.description.isEmpty()){
						paramsToShare.setText("精彩宝宝成长美一瞬间");
					}else{
						paramsToShare.setText(item.description);
					}
					
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					if(item.video_url.isEmpty()){
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}else{
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}
				} else if ("SinaWeibo".equals(platform.getName())) {
					if(item.video_url.isEmpty()){
						paramsToShare.setText(share_title + "http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}else{
						paramsToShare.setText(share_title + "http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(""); // title微信、QQ空间
					paramsToShare.setSite(context.getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					if(item.video_url.isEmpty()){
						paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}else{
						paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					if(item.video_url.isEmpty()){
						paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}else{
						paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					if(item.video_url.isEmpty()){
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}else{
						paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + share_imgId + "&user_id=" + share_userId);
					}
				}
			}
		});
		// 启动分享GUI
		oks.show(context);
	}
	
	/**
	 * 删除条目
	 * */
	private void deleteItem(final PictureItem item, final String isVideo, final int position){
		if (share_userId.equals(Config.getUser(context).uid)) {
			Builder builder = new Builder(context);
			builder.setMessage("确认要删除吗?");
			builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Config.showProgressDialog(context, false, null);
					AjaxParams params = new AjaxParams();
					
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("img_id", item.img_id);
					params.put("group_id", item.group_id);
					params.put("user_id", Config.getUser(context).uid);
					params.put("img_ids", item.img_id);
					FinalHttp fh = new FinalHttp();
					String url = "";
					if(item.group_id.isEmpty()){
						url = ServerMutualConfig.DelListingShow;
					}else{
						url = ServerMutualConfig.DelGroupListing;
					}
					fh.post( url , params, new AjaxCallBack<String>() {
						@Override
						public void onFailure( Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							Config.showFiledToast(context);
						}
						
						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Config.dismissProgress();
							try {
								JSONObject json = new JSONObject(t);
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								data.remove(position);
								notifyDataSetChanged();
							} catch (Exception e) {
							}
						}
					});
				}
			});
			builder.setNeutralButton("取消", null);
			builder.create().show();
		} else {
			Toast.makeText(context, "不可以删除别人的哦", 0).show();
		}
		
	}

}
