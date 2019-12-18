package com.yyqq.code.video;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 视频全屏播放
 * 
 * @author lyl
 * */
public class VideoFullScreenActivity extends BaseActivity implements OnPullDownListener{
	
	public static String IMG_ID_KEY = "IMG_ID_KEY";
	private int ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  
	public static String VIDEO_URL = ""; // 要播放的视频地址
	public static int VIDEO_TIME = 0; // 开始播放时间
	private AbHttpUtil ab;
	private String img_id = "";
	private PostBarTypeItem bean = new PostBarTypeItem();
	public int position;
	private int duration;
	private int indexPosition = -1;
	
	private com.yyqq.commen.view.myVideoPlayerView video_layout;
	private ImageView iv_video_thumb; // 背景图
	private ImageView video_start; // 开始播放按钮
	private ImageView video_stop; // 停止播放按钮
	private ImageView iv_video_bg_hint;
	private ProgressBar iv_video_loading;
	private RelativeLayout video_full_title_hint;
	private ImageView video_full_finish;
	private ImageView video_full_shared;
	private RelativeLayout video_full_all;
	private LinearLayout video_play_console;
	private TextView video_title;
	private ImageView iv_video_play;
	private RelativeLayout item_layout;
	
	private Button video_type;
	private SeekBar skbProgress;
	private TextView video_new_time;
	private TextView video_all_length;
	private TextView video_info;
	private TextView item_comment_title;
	private ImageView iv_video_bg_hint02;
	private ImageView iv_video_bg;
	private ImageView video_to_fullScreen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 	WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏  
		setRequestedOrientation(ORIENTATION); // 设置横竖屏   
		setContentView(R.layout.activity_video_full_screen);
	}

	@Override
	protected void initView() {
		video_layout = (com.yyqq.commen.view.myVideoPlayerView) findViewById(R.id.video_layout);
		video_full_title_hint = (RelativeLayout) findViewById(R.id.video_full_title_hint);
		video_full_finish = (ImageView) findViewById(R.id.video_full_finish);
		video_full_shared = (ImageView) findViewById(R.id.video_full_shared);
		video_full_all = (RelativeLayout) findViewById(R.id.video_full_all);
		video_play_console = (LinearLayout) findViewById(R.id.video_play_console);
		video_title = (TextView) findViewById(R.id.video_title);
		iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
		video_start = (ImageView) findViewById(R.id.iv_video_play);
		iv_video_loading = (ProgressBar) findViewById(R.id.iv_video_loading);
		iv_video_bg_hint02 = (ImageView) findViewById(R.id.iv_video_bg_hint02);
		iv_video_bg = (ImageView) findViewById(R.id.iv_video_bg);
		video_to_fullScreen = (ImageView) findViewById(R.id.video_to_fullScreen);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(VideoFullScreenActivity.this);
		img_id = getIntent().getStringExtra(IMG_ID_KEY);
		onRefresh();
	}

	@Override
	protected void setListener() {
		
		video_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoFullScreenActivity.this.finish();
			}
		});
		
		video_to_fullScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoFullScreenActivity.this.finish();
			}
		});
		
		video_full_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoFullScreenActivity.this.finish();
			}
		});
		
		video_full_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(video_play_console.isShown() || video_full_title_hint.isShown()){
					video_play_console.setVisibility(View.GONE);
					video_full_title_hint.setVisibility(View.GONE);
				}else{
					video_play_console.setVisibility(View.VISIBLE);
					video_full_title_hint.setVisibility(View.VISIBLE);
				}
			}
		});
		
		video_layout.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				video_play_console.setVisibility(View.VISIBLE);
				video_full_title_hint.setVisibility(View.VISIBLE);
				indexPosition = -1;
				video_start.setVisibility(View.VISIBLE);
				video_type.setBackground(getResources().getDrawable(R.drawable.video_start));
			}
		});

		video_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoPlay();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	/**
	 * 开始播放
	 * */
	private void VideoPlay(){
		
		video_start.setVisibility(View.GONE);
		iv_video_loading.setVisibility(View.VISIBLE);
		video_play_console.setVisibility(View.VISIBLE);
		video_full_title_hint.setVisibility(View.VISIBLE);
		
		Uri uri = Uri.parse(bean.video_url);
		MediaController mc = new MediaController(this);
		mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
		video_layout.setMediaController(mc);
		video_layout.setVideoURI(uri);
		video_layout.requestFocus();
//		int fullTime = VIDEO_TIME > 5000 ? VIDEO_TIME-3000 : VIDEO_TIME;
//		video_layout.seekTo(20000);
		video_layout.start(); 
		
		
		video_layout.setOnPreparedListener(new OnPreparedListener() {  
			
			@Override  
			public void onPrepared(MediaPlayer mediaPlayer) { 
				if(Integer.parseInt(bean.img_thumb_width) > Integer.parseInt(bean.img_thumb_height)){
					ViewTreeObserver vto2 =  video_layout.getViewTreeObserver();    
					vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
						@Override    
						public void onGlobalLayout() {  
							video_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
							video_layout.setLayoutParams(MyApplication.getVideoFullParams(R.id.item_layout, false));
						}    
					});
				}else{
					ViewTreeObserver vto2 = video_layout.getViewTreeObserver();    
					vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
						@Override    
						public void onGlobalLayout() {  
							video_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
							video_layout.setLayoutParams(MyApplication.getVideoFullParams(R.id.item_layout, true));
						}    
					});    
				}
				
//				可实现定时播放
//				video_layout.pause();
//				mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
//					
//					@Override
//					public void onSeekComplete(MediaPlayer mp) {
//						video_layout.start();
//					}
//				});
				indexPosition = -1;
				iv_video_loading.setVisibility(View.GONE);
				video_play_console.setVisibility(View.GONE);
				video_full_title_hint.setVisibility(View.GONE);
				video_type.setBackground(getResources().getDrawable(R.drawable.video_stop));
			}  
		});  

	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("img_id", img_id);

		/**
		 * 获取视频详情
		 * */
		ab.get(ServerMutualConfig.VIDEO_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject jsonCon = new JSONObject(content).getJSONObject("data");
							
							bean.is_group_tag = jsonCon.getString("is_group_tag");
							bean.group_tag_id = jsonCon.getString("group_tag_id");
							bean.group_name = jsonCon.getString("group_tag_name");
							bean.img_title = jsonCon.getString("img_title");
							bean.img = jsonCon.getString("img_thumb");
							bean.description = jsonCon.getString("img_description");
							bean.video_url = jsonCon.getString("video_url");
							bean.img_thumb_height = jsonCon.getString("img_thumb_height");
							bean.img_thumb_width = jsonCon.getString("img_thumb_width");
							
							video_title.setText(bean.img_title);
							MyApplication.getInstance().display(bean.img, iv_video_bg, R.drawable.deft_color);
							iv_video_bg_hint02.setVisibility(View.VISIBLE);
//							video_info.setText(jsonCon.getString("img_description"));
//							item_detial_username.setText(jsonCon.getString("user_name"));
//							item_detial_time.setText(jsonCon.getString("create_time"));
//							video_play_count.setText(jsonCon.getString("post_count") + "次播放");
//							user_id = jsonCon.getString("user_id");
//							zanNumber = jsonCon.getString("admire_count");
//							
//							if(bean.img_thumb_height.isEmpty() || bean.img_thumb_width.isEmpty()){
//								bean.img_thumb_height = jsonCon.getString("img_thumb_height");
//								bean.img_thumb_width = jsonCon.getString("img_thumb_width");
//								if(AbAppUtil.isWifi(VideoFullScreenActivity.this)){
//									videoPlay();
//								}else{
//									showHintDialog();
//								};
//							}
							
							initVideoConsole();
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Config.dismissProgress();
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

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 视频操控区初始化
	 * */
	private void initVideoConsole(){
		
		video_type = (Button) findViewById(R.id.video_type);
		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		video_all_length = (TextView) findViewById(R.id.video_all_length);
		video_new_time = (TextView) findViewById(R.id.video_new_time);
		video_play_console = (LinearLayout) findViewById(R.id.video_play_console);
		item_layout = (RelativeLayout) findViewById(R.id.item_layout);
		
		item_layout.setLayoutParams(MyApplication.getVideoFullParams(-1, false));
		
		video_type.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(video_layout.isPlaying()){
					video_layout.pause();
					video_type.setBackground(getResources().getDrawable(R.drawable.video_start));
				}else{
					video_layout.start();
					video_start.setVisibility(View.GONE);
					video_type.setBackground(getResources().getDrawable(R.drawable.video_stop));
				}
			}
		});
		
		skbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			int progress;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress * video_layout.getDuration() / seekBar.getMax();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				video_layout.seekTo(progress);
			}
		});
		
		Timer videoPlayerTimer = new Timer();
		videoPlayerTimer.schedule(mTimerTask, 0, 50);
		
		VideoPlay();
	}
	
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (video_layout == null){
				return;
			}
			if (video_layout.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			position = video_layout.getCurrentPosition();
			duration = video_layout.getDuration();

			VideoFullScreenActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Date date = new Date(duration);
					SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
					String dateStr = sdf.format(date);
					
					date = new Date(position);
					SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
					String dateStr02 = sdf02.format(date);
					
					if (duration > 0 && position > indexPosition) {
						video_all_length.setText(dateStr);
						video_new_time.setText(dateStr02 + "");
						long pos = skbProgress.getMax() * position / duration;
						skbProgress.setProgress((int) pos);
						indexPosition = position;
					}
				}
			});
		};
	};

}