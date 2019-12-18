package com.yyqq.code.main;

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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.code.video.VideoFullScreenActivity;
import com.yyqq.commen.model.GoPostItem;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.ReleaseItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.commen.view.myVideoPlayerView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class Release extends Activity implements OnPullDownListener {

	private String TAG = "fanfan_Release";
	private PullDownView mPullDownView;
	private ListView listview;
	private Activity context;
	private String uid = "";
	private int width;
	private AbHttpUtil ab;
	private MyAdapter adapter;
	private final String fileName = "release.txt";
	private TextView fride_title;
	private ArrayList<ReleaseItem> data = new ArrayList<ReleaseItem>();
	private RelativeLayout none;
	private ImageDownloader mDownloader;
	private ImageView refresh;
	private String mOptions = "";
	private int pageSize;
	private String comeFrom = "";
	// 分享的4个参数
	private String share_title = "";
	private String share_img = "";
	private String share_imgId = "";
	private String share_userId = "";
	private String share_link = "";
	private AlertDialog.Builder dialog;
	private WindowManager windowsManager = null;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		windowsManager = this.getWindowManager();
		setContentView(R.layout.release);

		if (getIntent().hasExtra("uid")) {
			uid = getIntent().getStringExtra("uid");
		}

		fride_title = (TextView) findViewById(R.id.fride_title);
		none = (RelativeLayout) findViewById(R.id.none);
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		refresh = (ImageView) findViewById(R.id.refresh);
		refresh.setOnClickListener(refreshClick);
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
		if (!Config.isConn(this)) {
			try {
				getlistData(Config.read(context, fileName));
			} catch (Exception e) {
			}
		}
		
		onRefresh();
	}

	public OnClickListener refreshClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			listview.setSelection(1);
			onRefresh();
		}
	};
	
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
	
	protected Activity getActivity() {
		return this;
	}

	@Override
	public void onRefresh() {
		pageSize = 1;
		AbRequestParams params = new AbRequestParams();
		if (getIntent().hasExtra("user")) {
			fride_title.setText("TA发布的");
		} else {
			fride_title.setText("我发布的");
		}
		params.put("user_id", uid);
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("page", pageSize + "");
		ab.get(ServerMutualConfig.MyImgsV5 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						Config.save(context, content, fileName);
						getlistData(content);
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
		params.put("user_id", uid);
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("page", (++pageSize) + "");
		ab.get(ServerMutualConfig.MyImgsV5 + "&" + params.toString(),
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
								ReleaseItem item = new ReleaseItem();
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
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView time;
		TextView msg;
		TextView review;
		TextView zan;
		TextView permission;
		ImageView permission_relation;
		ImageView img_link;
		TextView msg_link;
		LinearLayout ly_link;
		ImageView come_from;
		
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

		@SuppressLint({ "NewApi", "ResourceAsColor" })
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
				final ReleaseItem item = data.get(position);
				
				convertView = inflater.inflate(R.layout.release_item, null);
				final ViewHolder holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.mGridView = (MyGridView) convertView.findViewById(R.id.n_img);
				holder.permission_relation = (ImageView) convertView.findViewById(R.id.permission_relation);
				holder.ly_link = (LinearLayout) convertView.findViewById(R.id.ly_link);
				holder.img_link = (ImageView) convertView.findViewById(R.id.img_link);
				holder.msg_link = (TextView) convertView.findViewById(R.id.msg_link);
				holder.more = (ImageView) convertView.findViewById(R.id.more);
				holder.come_from = (ImageView) convertView.findViewById(R.id.come_from);
				holder.msg = (TextView) convertView.findViewById(R.id.msg);
				holder.review = (TextView) convertView.findViewById(R.id.review);
				holder.video_layout = (myVideoPlayerView) convertView.findViewById(R.id.video_layout);
				holder.video_start = (ImageView) convertView.findViewById(R.id.iv_video_play);
				holder.video_stop = (ImageView) convertView.findViewById(R.id.iv_video_stop);
				holder.video_player_all = (RelativeLayout) convertView.findViewById(R.id.item_layout);
				holder.iv_video_thumb = (ImageView) convertView.findViewById(R.id.iv_video_thumb);
				holder.iv_video_bg = (ImageView) convertView.findViewById(R.id.iv_video_bg);
				holder.video_play_layout = (LinearLayout) convertView.findViewById(R.id.video_play_layout);
				holder.iv_video_bg_up = (ImageView) convertView.findViewById(R.id.iv_video_bg_up);
				holder.iv_video_loading = (ProgressBar) convertView.findViewById(R.id.iv_video_loading);
				holder.iv_video_bg_hint = (ImageView) convertView.findViewById(R.id.iv_video_bg_hint);
				holder.iv_video_bg_hint02 = (ImageView) convertView.findViewById(R.id.iv_video_bg_hint02);
				holder.video_type = (Button) convertView.findViewById(R.id.video_type);
				holder.skbProgress = (SeekBar) convertView.findViewById(R.id.skbProgress);
				holder.video_all_length = (TextView) convertView.findViewById(R.id.video_all_length);
				holder.video_new_time = (TextView) convertView.findViewById(R.id.video_new_time);
				holder.video_play_console = (LinearLayout) convertView.findViewById(R.id.video_play_console);
				holder.iv_to_full = (ImageView) convertView.findViewById(R.id.video_to_fullScreen);
				convertView.setTag(holder);
				
				if(!item.video_url.isEmpty()){
					
					// 动态修改是视频播放视图宽、高
					holder.video_player_all.setLayoutParams(MyApplication.getVideoPlayerParams(-1));
					holder.video_play_console.setVisibility(View.GONE);
					
					// 竖图直接覆盖展示
					if(Integer.parseInt(item.img_thumb_width) < Integer.parseInt(item.img_thumb_height)){
						MyApplication.getInstance().display(item.video_image_url, holder.iv_video_thumb, R.drawable.def_image);
						holder.iv_video_thumb.setVisibility(View.VISIBLE);
					}else{
						holder.iv_video_bg_hint.setVisibility(View.GONE);
						holder.iv_video_bg_hint02.setVisibility(View.GONE);
						holder.iv_video_thumb.setVisibility(View.GONE);
					}
					MyApplication.getInstance().display(item.video_image_url, holder.iv_video_bg_up, R.drawable.def_image);
					MyApplication.getInstance().display(item.video_image_url, holder.iv_video_bg, R.drawable.def_image);
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
							Release.this.startActivity(intent);
						}
					});
					
					holder.video_start.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							if(holder.video_layout.isPlaying()){
								holder.video_layout.suspend();
								return;
							}
							
							for (int i = 0 ; i < data.size() ; i++) {
								data.get(i).isPlay = false;
								if(position == i){
									data.get(i).isPlay = true;
								}
							}
							adapter.notifyDataSetChanged();
						}
					});
					
					holder.video_stop.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(holder.video_layout.isPlaying()){
								holder.video_layout.pause();
								holder.video_stop.setVisibility(View.GONE);
							}else{
								holder.video_stop.setVisibility(View.GONE);
							}
						}
					});
					
					holder.video_player_all.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(holder.video_layout.isPlaying()){
								holder.video_start.setVisibility(View.GONE);
								holder.video_stop.setVisibility(View.VISIBLE);
							}else{
								holder.video_start.setVisibility(View.VISIBLE);
								holder.video_stop.setVisibility(View.GONE);
							}
						}
					});
					
					holder.video_layout.setOnPreparedListener(new OnPreparedListener() {  
						
						@Override  
						public void onPrepared(MediaPlayer mediaPlayer) { 
							Release.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									if(Integer.parseInt(item.img_thumb_width) > Integer.parseInt(item.img_thumb_height)){
										ViewTreeObserver vto2 = holder.video_layout.getViewTreeObserver();    
										vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
											@Override    
											public void onGlobalLayout() {  
												holder.video_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
												holder.video_layout.setLayoutParams(MyApplication.getVideoPlayerParams(R.id.item_layout));
											}    
										});    
									}
									
									holder.iv_video_bg_up.setVisibility(View.GONE);
									holder.iv_video_loading.setVisibility(View.GONE);
									holder.iv_video_bg_hint.setVisibility(View.GONE);
									holder.video_play_console.setVisibility(View.VISIBLE);
								}
							});
						}  
					});  
					
					holder.video_layout.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							holder.indexPosition = -1;
							for (int i = 0 ; i < data.size() ; i++) {
								data.get(i).isPlay = false;
							}
							adapter.notifyDataSetChanged();
						}
					});
					
					holder.video_type.setOnClickListener(new OnClickListener() {
						
						@SuppressLint("NewApi")
						@Override
						public void onClick(View v) {
							if(holder.video_layout.isPlaying()){
								holder.video_layout.pause();
								holder.video_type.setBackground(getResources().getDrawable(R.drawable.video_start));
							}else{
								holder.video_layout.start();
								holder.video_type.setBackground(getResources().getDrawable(R.drawable.video_stop));
							}
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
					
					holder.skbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
						int progress;
						
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress,
								boolean fromUser) {
							this.progress = progress * holder.video_layout.getDuration() / seekBar.getMax();
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							holder.video_layout.seekTo(progress);
						}
					});

					if(item.isPlay){
						if(AbAppUtil.isWifi(Release.this)){
							holder.indexPosition = -1;
							
							final Handler handleProgress = new Handler() {
								public void handleMessage(Message msg) {
									
									holder.position = holder.video_layout.getCurrentPosition();
									holder.duration = holder.video_layout.getDuration();
									
									Date date = new Date(holder.duration);
									SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
									String dateStr = sdf.format(date);
									
									date = new Date(holder.position);
									SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
									String dateStr02 = sdf02.format(date);
									
									if (holder.duration > 0 && holder.position > holder.indexPosition) {
										holder.video_all_length.setText(dateStr);
										holder.video_new_time.setText(dateStr02 + "");
										long pos = holder.skbProgress.getMax() * holder.position / holder.duration;
										holder.skbProgress.setProgress((int) pos);
										holder.indexPosition = holder.position;
									}
								};
							};
							
							Timer videoPlayerTimer = new Timer();
							videoPlayerTimer.schedule(new TimerTask() {
								
								@Override
								public void run() {
									if (holder.video_layout == null){
										return;
									}
									if (holder.video_layout.isPlaying() && holder.skbProgress.isPressed() == false) {
										handleProgress.sendEmptyMessage(0);
									}
								}
							}, 0, 50);
							
							MyApplication.getInstance().display(item.video_image_url, holder.iv_video_bg, R.drawable.def_image);
							holder.iv_video_thumb.setVisibility(View.GONE);
							holder.video_start.setVisibility(View.GONE);
							holder.iv_video_bg_hint.setVisibility(View.VISIBLE);
							holder.iv_video_loading.setVisibility(View.VISIBLE);
							
							Uri uri = Uri.parse(item.video_url);
							MediaController mc = new MediaController(getActivity());
							mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
							holder.video_layout.setMediaController(mc);
							holder.video_layout.setVideoURI(uri);
							holder.video_layout.requestFocus();
							holder.video_layout.start();
						}else{
							dialog = new Builder(Release.this);
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
													
													holder.position = holder.video_layout.getCurrentPosition();
													holder.duration = holder.video_layout.getDuration();
													
													Date date = new Date(holder.duration);
													SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
													String dateStr = sdf.format(date);
													
													date = new Date(holder.position);
													SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
													String dateStr02 = sdf02.format(date);
													
													if (holder.duration > 0 && holder.position > holder.indexPosition) {
														holder.video_all_length.setText(dateStr);
														holder.video_new_time.setText(dateStr02 + "");
														long pos = holder.skbProgress.getMax() * holder.position / holder.duration;
														holder.skbProgress.setProgress((int) pos);
														holder.indexPosition = holder.position;
													}
												};
											};
											
											Timer videoPlayerTimer = new Timer();
											videoPlayerTimer.schedule(new TimerTask() {
												
												@Override
												public void run() {
													if (holder.video_layout == null){
														return;
													}
													if (holder.video_layout.isPlaying() && holder.skbProgress.isPressed() == false) {
														handleProgress.sendEmptyMessage(0);
													}
												}
											}, 0, 50);
											
											MyApplication.getInstance().display(item.video_image_url, holder.iv_video_bg, R.drawable.def_image);
											holder.iv_video_thumb.setVisibility(View.GONE);
											holder.video_start.setVisibility(View.GONE);
											holder.iv_video_bg_hint.setVisibility(View.VISIBLE);
											holder.iv_video_loading.setVisibility(View.VISIBLE);
											
											Uri uri = Uri.parse(item.video_url);
											MediaController mc = new MediaController(getActivity());
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
										public void onClick(DialogInterface arg0, int arg1) {
											holder.noWifiPlay = false;
										}
									});
							dialog.create().show();	
						}
					}
					}else if (item.imaStrings.size() > 1) {
						holder.video_player_all.setVisibility(View.GONE);
						holder.img.setVisibility(View.GONE);
						holder.mGridView.setVisibility(View.VISIBLE);
						holder.mGridView
						.setAdapter(new ImageAdapter(item.imaStrings,
								holder.mGridView, item.imaBig, item.imaWid,
								item.imaHed));
					} else if (item.imaStrings.size() == 1) {
						holder.video_player_all.setVisibility(View.GONE);
						holder.mGridView.setVisibility(View.GONE);
						
						MyApplication.getInstance().display(item.img_thumb, holder.img, R.drawable.def_image);
						View view = (RelativeLayout) convertView.findViewById(R.id.img_ly);
						view.setBackgroundColor(R.color.imgbg);
						view.getBackground().setAlpha(19);
						
						view.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context, ChangePhotoSize.class);
								intent.putExtra("imgList", item.imaBig);
								context.startActivity(intent);
							}
						});
						
//						getImage(item.imaStrings.get(0), item.imaBig, holder.img,
//								position, convertView);
					}else{
						holder.video_player_all.setVisibility(View.GONE);
						holder.mGridView.setVisibility(View.GONE);
						holder.img.setVisibility(View.GONE);
					}
					
					// 标签
					if ("1".equals(item.come_from)) {
						holder.come_from
						.setBackgroundResource(R.drawable.come_from_show);
					} else if ("2".equals(item.come_from)) {
						holder.come_from
						.setBackgroundResource(R.drawable.come_from_post);
					} else {
						holder.come_from
						.setBackgroundResource(R.drawable.come_from_buy);
					}
					
					// 链接
					if (!TextUtils.isEmpty(item.post_url)) {
						holder.ly_link.setVisibility(View.VISIBLE);
						MyApplication.getInstance().display(item.post_url_image,
								holder.img_link, R.drawable.fabu_default_link);
						holder.msg_link.setText(item.post_url_title);
					} else {
						holder.ly_link.setVisibility(View.GONE);
					}
					holder.ly_link.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							String strUri;
							if (!TextUtils.isEmpty(item.post_url)) {
								strUri = item.post_url;
								if (!strUri.startsWith("http")) {
									strUri = "http://" + strUri;
								}
								Intent intent = new Intent(context,
										WebViewActivity.class);
								intent.putExtra("webviewurl", strUri);
								startActivity(intent);
							} else {
								Toast.makeText(context, "错误的网页", 0).show();
							}
						}
						
					});
					
					// 权限判断
					if ("0".equals(item.img_cate)) {
//				holder.permission.setVisibility(View.GONE);
						holder.permission_relation.setVisibility(View.GONE);
					} else if ("1".equals(item.img_cate)) {
						holder.permission_relation
						.setBackgroundResource(R.drawable.permission_friend);
					} else {
						holder.permission_relation
						.setBackgroundResource(R.drawable.permission_share);
					}
					
					// 更多操作
					holder.more.setTag(item);
					holder.more.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ReleaseItem item = (ReleaseItem) v.getTag();
							
							if(!item.img_title.isEmpty()){
								share_title = item.img_title;
							}else{
								share_title = "自由环球租赁";
							}
							
							if(item.description.isEmpty()){
								item.description = "精彩宝宝美一瞬间";
							}
							
							share_imgId = item.img_id;
							share_userId = item.user_id;
							comeFrom = item.come_from;
							
							if(item.video_url.isEmpty()){
								if (item.imaStrings.size() > 1) {
									share_img = item.imaStrings.get(0);
								} else if (item.imaStrings.size() == 1) {
									share_img = item.img_thumb;
								}
								if ("1".equals(comeFrom)) {
									share_link = "http://www.meimei.yihaoss.top/share.php?"+ "img_id=" + share_imgId + "&user_id=" + share_userId;
								} else if ("2".equals(comeFrom)) {
									share_link = "http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?img_id=" + share_imgId + "&user_id=" + share_userId;
								}
							}else{
								share_link = "http://www.meimei.yihaoss.top/fenxiang/video.html?img_id=" + share_imgId + "&user_id=" + share_userId;
							}
							showShare(item);
						}
					});
					
					// 时间
					TextView month = (TextView) convertView.findViewById(R.id.month);
					TextView year = (TextView) convertView.findViewById(R.id.year);
					TextView day = (TextView) convertView.findViewById(R.id.day);
					long dateTime = Config.getDateDays(
							sdf.format(new Date(System.currentTimeMillis())),
							sdf.format(new Date(item.create_time)));
					if (dateTime <= 1000 * 60 * 60 * 24 * 1) {
						month.setVisibility(View.GONE);
						year.setVisibility(View.GONE);
						day.setText("今天");
						day.setTextSize((float) 30);
					}
					/**
					 * 大于2天
					 */
					else if (dateTime <= 1000 * 60 * 60 * 24 * 2) {
						month.setVisibility(View.GONE);
						year.setVisibility(View.GONE);
						day.setText("昨天");
						day.setTextSize((float) 30);
					} else {
						String[] str = Config.getStringDateShort(item.create_time);
						day.setText(str[2]);
						month.setText(str[1] + "月");
						year.setText(str[0] + "年");
					}
					// 描述
					if (item.description.equals("")) {
						holder.msg.setVisibility(View.GONE);
					} else {
						SpannableString spannableString = FaceConversionUtil
								.getInstace().getExpressionString(context,
										item.description);
						holder.msg.setText(spannableString);
						if (item.description.startsWith("//来自☆话题// ")) {
							holder.msg.setTextColor(Color.RED);
							holder.msg.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									AbRequestParams params = new AbRequestParams();
									params.put("user_id", item.user_id);
									params.put("login_user_id",
											Config.getUser(context).uid);
									params.put("img_id", item.img_id);
									params.put("create_time", item.create_time + "");
									// Log.e("fanfan", ServerMutualConfig.GoToPost + "&"
									// + params.toString());
									ab.get(ServerMutualConfig.GoToPost + "&"
											+ params.toString(),
											new AbStringHttpResponseListener() {
										@Override
										public void onSuccess(int statusCode,
												String content) {
											super.onSuccess(statusCode, content);
											try {
												JSONObject json = new JSONObject(
														content);
												GoPostItem postItem = new GoPostItem();
												postItem.fromJson(json
														.getJSONObject("data"));
												// item.fromJson(json.getJSONArray("data")
												// .getJSONObject(i)); 取数组
												Intent intent = new Intent(context, MainItemDetialActivity.class);
												intent.putExtra("img_id",
														postItem.img_id);
												intent.putExtra("user_id",
														item.user_id);
												intent.putExtra("is_save",
														postItem.is_save);
												context.startActivity(intent);
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
											super.onFailure(statusCode,
													content, error);
										}
									});
								}
							});
						}
					}
					
					int newW, newH;
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
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(newW, newH);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.msg);
					holder.img.setLayoutParams(params);
					
					final TextView zan = (TextView) convertView.findViewById(R.id.zan);
					if (item.isZan) {
						zan.setBackgroundResource(R.drawable.fabu_zan_sel);
					} else {
						zan.setBackgroundResource(R.drawable.fabu_zan);
					}
					zan.setText((item.admire_count));
					zan.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Config.showProgressDialog(context, false, null);
							if (!item.isZan) {
								AbRequestParams params = new AbRequestParams();
								params.put("user_id", Config.getUser(context).uid);
								params.put("admire_id", item.user_id);
								params.put("img_id", item.img_id);
								params.put("login_user_id", Config.getUser(context).uid);
								params.put("admire_user_id", item.user_id);
								if ("1".equals(item.come_from)) {
									mOptions = ServerMutualConfig.Admire;
								} else if ("2".equals(item.come_from)) {
									mOptions = ServerMutualConfig.PostAdmireNew;
								} 
//						else {
//							mOptions = ServerMutualConfig.BuyAdmire;
//						}
								ab.post(mOptions, params,
										new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										item.admire_count = (Integer
												.parseInt(item.admire_count) + 1)
												+ "";
										zan.setText((item.admire_count));
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
								params.put("login_user_id", Config.getUser(context).uid);
								params.put("admire_user_id", item.user_id);
								if ("1".equals(item.come_from)) {
									mOptions = ServerMutualConfig.CancelAdmire;
								} else if ("2".equals(item.come_from)) {
									mOptions = ServerMutualConfig.CancelPostAdmireNew;
								} 
//						else {
//							mOptions = ServerMutualConfig.CancelBuyAdmire;
//						}
								ab.post(mOptions, params, new AbStringHttpResponseListener() {
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
					holder.review.setText(item.review_count);
					holder.review.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							/**
							 * 判断类型是否是视频，是的话点击评论跳转至视频播放页面
							 */
							if(item.video_url.isEmpty()){
								Intent intent = new Intent();
								intent.setClass(context, MainItemDetialActivity.class);
								intent.putExtra("img_id", item.img_id);
								startActivity(intent);
							}else{
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
								Release.this.startActivity(intent);
							}
						}
					});
					
					// if ("2".equals(item.come_from)) {
					// convertView.setOnClickListener(new View.OnClickListener() {
					//
					// @Override
					// public void onClick(View arg0) {
					// Intent intent = new Intent();
					// intent.setClass(context, PostShow_Detail.class);
					// intent.putExtra("user_id", item.user_id);
					// intent.putExtra("img_id", item.img_id);
					// }
					// });
					// }
//				}
					
				return convertView;
//			}
		}

		class ClickListener implements OnClickListener {
			String title;
			String url;
			ViewGroup parent;
			int position;
			ImageView play;
			RelativeLayout video_hint;
			RelativeLayout my_show_videoLayout;
			
			public void setData(ViewGroup parent, String title, String url,
					int position, RelativeLayout video_hint, RelativeLayout my_show_videoLayout) {
				this.parent = parent;
				this.title = title;
				this.url = url;
				this.position = position;
				this.video_hint = video_hint;
				this.my_show_videoLayout = my_show_videoLayout;
			}

			@Override
			public void onClick(View view) {
				
//				my_show_videoLayout.setVisibility(View.VISIBLE);
//				video_hint.setVisibility(View.GONE);
//				
//				View container = getContainer();
//				if (container == null || this.parent == null) {
//					return;
//				}
//				if (container.getParent() != null) {
//					((ViewGroup) container.getParent()).removeAllViews();
//				}
//				this.parent.addView(container);
//
//				VDVideoListInfo infoList = new VDVideoListInfo();
//				VDVideoInfo info = new VDVideoInfo();
//				info = new VDVideoInfo();
//				info.mTitle = title;
//				info.mPlayUrl = url;
//				infoList.addVideoInfo(info);
//				videoView.open(getActivity(), infoList);
//				videoView.play(0);
				
				for (int i = 0 ; i < data.size() ; i++) {
					data.get(i).isPlay = false;
					if(position == i){
						data.get(i).isPlay = true;
					}
				}
				
				adapter.notifyDataSetChanged();
			}
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

	class ImageAdapter extends BaseAdapter {
		ArrayList<String> imaStrings = null;
		MyGridView myGridView;
		ArrayList<String> imgBig = null;
		ArrayList<String> ImageList = null;
		ArrayList<String> imaWid = null;
		ArrayList<String> imaHed = null;

		public ImageAdapter(ArrayList<String> imaStrings,
				MyGridView myGridView, ArrayList<String> imgBig,
				ArrayList<String> imaWid, ArrayList<String> imaHed) {
			this.imaStrings = imaStrings;
			this.myGridView = myGridView;
			this.imgBig = imgBig;
			this.imaWid = imaWid;
			this.imaHed = imaHed;
		}

		@Override
		public int getCount() {

			return imaStrings.size() < 7 ? imaStrings.size() : 6;
		}

		@Override
		public Object getItem(int position) {
			return imaStrings.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.release_grida_item,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setTag(imaStrings.get(position));
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null && position != 5) {
				mDownloader.imageDownload(imaStrings.get(position), holder.img,
						"/Download/cache_files", context,
						new OnImageDownload() {

							@Override
							public void onDownloadSucc(Bitmap bitmap,
									String c_url, ImageView imageView) {
								ImageView imageView1 = (ImageView) myGridView
										.findViewWithTag(c_url);
								if (imageView1 != null) {
									imageView1.setImageBitmap(bitmap);
									imageView1.setTag("");
								}

							}
						});
			} else {
				holder.img.setBackgroundResource(R.drawable.more);
			}
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", imaWid);
					intent.putStringArrayListExtra("imaHed", imaHed);
					intent.putExtra("listIndex", position);
					context.startActivity(intent);
				}
			});
			return convertView;
		}

	}

	private void getlistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			for (int i = 0; i < json.getJSONArray("data").length(); i++) {
				ReleaseItem item = new ReleaseItem();
				item.fromJson(json.getJSONArray("data").getJSONObject(i));
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

	private void showShare(final ReleaseItem item) {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		Bitmap collect = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_delete);
		Bitmap collectSel = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_delete);
		String label = getResources().getString(R.string.delete);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				deleteItem(item);
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
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setUrl(share_link);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(share_title + share_link);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(share_title); // title微信、QQ空间
					paramsToShare.setTitleUrl(share_link);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(item.description);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setSiteUrl(share_link); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					paramsToShare.setTitleUrl(share_link);
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
					paramsToShare.setUrl(share_link);
				}

			}
		});
		// 启动分享GUI
		oks.show(context);
	}
	
	/**
	 * 删除条目
	 * */
	private void deleteItem(final ReleaseItem item){
		if (share_userId.equals(Config.getUser(context).uid)) {
			Builder builder = new Builder(context);
			builder.setMessage("确认要删除吗?");
			builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Config.showProgressDialog(context, false, null);
					AjaxParams params = new AjaxParams();
					
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("user_id", Config.getUser(context).uid);
					params.put("img_ids", item.img_id);
					params.put("img_id", item.img_id);
					params.put("group_id", item.group_id);
					
					FinalHttp fh = new FinalHttp();
					
					fh.post(ServerMutualConfig.DelListingShow, params, new AjaxCallBack<String>() {
						@Override
						public void onFailure( Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							Config.showFiledToast(context);
						}
						
						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject json = new JSONObject(t);
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								onRefresh();
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}  
	
	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context context, float pxValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}   

}
