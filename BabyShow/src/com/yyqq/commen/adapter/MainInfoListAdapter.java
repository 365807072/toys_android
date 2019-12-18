package com.yyqq.commen.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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

import com.ab.util.AbAppUtil;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.business.BusinessList;
import com.yyqq.code.business.BusinessSelectedListActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseCategoryActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.code.video.VideoFullScreenActivity;
import com.yyqq.commen.model.MainItemImageBean;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.view.myVideoPlayerView;
import com.yyqq.framework.application.MyApplication;

public class MainInfoListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<MainListItemBean> data = null;
	int indexPosition = -1;
	private AlertDialog.Builder dialog;
	boolean noWifiPlay = false;
	boolean showQunBiao = false;
	private boolean showTag = false;
	
	public MainInfoListAdapter(Activity context, ArrayList<MainListItemBean> data, boolean showQunBiao) {
		super();
		this.context = context;
		this.data = data;
		this.showQunBiao = showQunBiao;
		inflater = (LayoutInflater) context	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		app = (MyApplication) context.getApplication();
	}

	public MainInfoListAdapter(Activity context, ArrayList<MainListItemBean> data, boolean showQunBiao, boolean showTag) {
		super();
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		app = (MyApplication) context.getApplication();
		this.showTag = showTag;
	}

	public MainInfoListAdapter(Activity context, ArrayList<MainListItemBean> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		app = (MyApplication) context.getApplication();
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if("1".equals(data.get(position).getImg_style())){
			return initImageView(convertView, data.get(position));
		}else if("2".equals(data.get(position).getImg_style())){
			return initVideoView(convertView, data.get(position), position);
		}else if("3".equals(data.get(position).getImg_style())){
			return initImageParallelView(convertView, data.get(position));
		}else if("4".equals(data.get(position).getImg_style())){
			return initGoView(convertView, data.get(position));
		}else if("5".equals(data.get(position).getImg_style())){
			return initPostView(convertView, data.get(position));
		}else if("6".equals(data.get(position).getImg_style())){
			return initBuyView(convertView, data.get(position));
		}else if("7".equals(data.get(position).getImg_style())){
			return initBuyView(convertView, data.get(position));
		}else if("8".equals(data.get(position).getImg_style())){
			return initImageAndTextView(convertView, data.get(position));
		}else if("9".equals(data.get(position).getImg_style())){
			return initView09(convertView, data.get(position));
		}else if("-1".equals(data.get(position).getImg_style())){
			return initViewOther(convertView, data.get(position));
		}else{
			return initBuyView(convertView, data.get(position));
		}
	}

	// 初始化纯文字布局
	private View initPostView(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type01, null);
		TextView tv_title = (TextView) convertView.findViewById(R.id.main_item_title);
		TextView tv_number = (TextView) convertView.findViewById(R.id.main_item_number);
		ImageView main_item_qunbiao = (ImageView) convertView.findViewById(R.id.main_item_qunbiao);
		ImageView group_main_isessence = (ImageView) convertView.findViewById(R.id.group_main_isessence);
		
		if(bean.isEssence()){
			group_main_isessence.setVisibility(View.VISIBLE);
		}else{
			group_main_isessence.setVisibility(View.GONE);
		}
		
		if(bean.getType().equals("31") || bean.getType().equals("32")){
			if(showQunBiao){
				main_item_qunbiao.setVisibility(View.VISIBLE);
				tv_number.setText((bean.getUser_name()+"").trim());
			}else{
				main_item_qunbiao.setVisibility(View.GONE);
				if(showTag){
					tv_number.setText((bean.getUser_name() + "   " +bean.getGroup_class_title()).trim());
				}else{
					tv_number.setText((bean.getUser_name() + "   " +bean.getPost_count() +"观看").trim());
				}
			}
		}else{
			if(showTag){
				tv_number.setText((bean.getUser_name() + "   " +bean.getGroup_class_title()).trim());
			}else{
				tv_number.setText((bean.getUser_name() + "   " +bean.getPost_count() +"观看").trim());
			}
		}
		
		tv_title.setText(bean.getImg_title());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentConsole(bean);
			}
		});
		return convertView;
	};

	// 初始化一张图片靠右显示布局
	private View initBuyView(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type02, null);
		TextView tv_title = (TextView) convertView.findViewById(R.id.main_item_title);
		TextView tv_number = (TextView) convertView.findViewById(R.id.main_item_number);
		ImageView iv_image = (ImageView) convertView.findViewById(R.id.main_item_image);
		ImageView video_hint = (ImageView) convertView.findViewById(R.id.video_hint);
		ImageView main_item_qunbiao = (ImageView) convertView.findViewById(R.id.main_item_qunbiao);
		ImageView group_main_isessence = (ImageView) convertView.findViewById(R.id.group_main_isessence);
		
		if(bean.isEssence()){
			group_main_isessence.setVisibility(View.VISIBLE);
		}else{
			group_main_isessence.setVisibility(View.GONE);
		}
		
		if(bean.getType().equals("31") || bean.getType().equals("32")){
			if(showQunBiao){
				tv_number.setText((bean.getUser_name()).trim());
				main_item_qunbiao.setVisibility(View.VISIBLE);
			}else{
				main_item_qunbiao.setVisibility(View.GONE);
				if(showTag){
					tv_number.setText((bean.getUser_name() + "   " +bean.getGroup_class_title()).trim());
				}else{
					tv_number.setText((bean.getUser_name() + "   " +bean.getPost_count() +"观看").trim());
				}
			}
		}else{
			if(showTag){
				tv_number.setText((bean.getUser_name() + "   " +bean.getGroup_class_title()).trim());
			}else{
				tv_number.setText((bean.getUser_name() + "   " +bean.getPost_count() +"观看").trim());
			}
		}

		if (bean.getImg_style().equals("7")) {
			video_hint.setVisibility(View.VISIBLE);
		} else {
			video_hint.setVisibility(View.GONE);
		}

		tv_title.setText(bean.getImg_title());
		ArrayList<MainItemImageBean> list = new ArrayList<MainItemImageBean>();
		app.display(bean.getImgList().get(0).getImg_thumb(), iv_image, R.drawable.deft_color);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentConsole(bean);
			}
		});
		return convertView;
	};

	// 初始化三张图片布局
	private View initGoView(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type03, null);
		
		TextView main_item_par_title = (TextView) convertView.findViewById(R.id.main_item_par_title);
		main_item_par_title.setText(bean.getImg_title().trim());
		
		ImageView iv_image01 = (ImageView) convertView.findViewById(R.id.main_item_image01);
		ImageView iv_image02 = (ImageView) convertView.findViewById(R.id.main_item_image02);
		ImageView iv_image03 = (ImageView) convertView.findViewById(R.id.main_item_image03);
		
		iv_image01.setLayoutParams(MyApplication.getMainImageViewParams());
		iv_image02.setLayoutParams(MyApplication.getMainImageViewParams());
		iv_image03.setLayoutParams(MyApplication.getMainImageViewParams());

		app.display(bean.getImgList().get(0).getImg_thumb(), iv_image01, R.drawable.deft_color);
		app.display(bean.getImgList().get(1).getImg_thumb(), iv_image02, R.drawable.deft_color);
		app.display(bean.getImgList().get(2).getImg_thumb(), iv_image03, R.drawable.deft_color);
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentConsole(bean);
			}
		});
		
		return convertView;
	};

	// 初始化一张大图布局
	private View initImageView(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type04, null);
		
		TextView main_item_par_title = (TextView) convertView.findViewById(R.id.main_item_par_title);
		main_item_par_title.setText(bean.getImg_title().trim());
		
		ImageView main_item_img = (ImageView) convertView.findViewById(R.id.main_item_img);
		app.display(bean.getImgList().get(0).getImg_thumb(), main_item_img, R.drawable.deft_color);
		
		main_item_img.setLayoutParams(MyApplication.getImageViewParams());
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentConsole(bean);
			}
		});
		return convertView;
	};
	
	// 单独一个视频播放（可直接本页播放）
	private View initVideoView(View convertView, final MainListItemBean bean, final int position){
		convertView = inflater.inflate(R.layout.item_video_console_view, null);
		
		LinearLayout video_play_hint = (LinearLayout) convertView.findViewById(R.id.video_play_hint);
		video_play_hint.setVisibility(View.VISIBLE);
		
		View video_botton_hint_line = (View)convertView.findViewById(R.id.video_botton_hint_line);
		video_botton_hint_line.setVisibility(View.VISIBLE);
		
		TextView main_item_par_title = (TextView) convertView.findViewById(R.id.main_item_par_title);
		main_item_par_title.setText(bean.getImg_title().trim());
		main_item_par_title.setTextColor(context.getResources().getColor(R.color.c_999999));
		
		TextView tv_number = (TextView) convertView.findViewById(R.id.main_item_par_username);
		tv_number.setText(bean.getUser_name());
		tv_number.setVisibility(View.GONE);

		final myVideoPlayerView video_layout;
		final ImageView video_start;
		final ImageView video_stop;
		final ImageView iv_video_thumb;
		final ImageView iv_video_bg;
		RelativeLayout video_player_all;
		LinearLayout video_play_layout;
		final ImageView iv_video_bg_up;
		final ProgressBar iv_video_loading;
		final ImageView iv_video_bg_hint;
		final ImageView iv_video_bg_hint02;
		ImageView iv_to_full;
		
		final Button video_type;
		final SeekBar skbProgress;
		final TextView video_new_time;
		final TextView video_all_length;
		final LinearLayout video_play_console;
		
		video_layout = (myVideoPlayerView) convertView.findViewById(R.id.video_layout);
		video_start = (ImageView) convertView.findViewById(R.id.iv_video_play);
		video_stop = (ImageView) convertView.findViewById(R.id.iv_video_stop);
		video_player_all = (RelativeLayout) convertView.findViewById(R.id.item_layout);
		iv_video_thumb = (ImageView) convertView.findViewById(R.id.iv_video_thumb);
		iv_video_bg = (ImageView) convertView.findViewById(R.id.iv_video_bg);
		video_play_layout = (LinearLayout) convertView.findViewById(R.id.video_play_layout);
		iv_video_bg_up = (ImageView) convertView.findViewById(R.id.iv_video_bg_up);
		iv_video_loading = (ProgressBar) convertView.findViewById(R.id.iv_video_loading);
		iv_video_bg_hint = (ImageView) convertView.findViewById(R.id.iv_video_bg_hint);
		iv_video_bg_hint02 = (ImageView) convertView.findViewById(R.id.iv_video_bg_hint02);
		video_type = (Button) convertView.findViewById(R.id.video_type);
		skbProgress = (SeekBar) convertView.findViewById(R.id.skbProgress);
		video_all_length = (TextView) convertView.findViewById(R.id.video_all_length);
		video_new_time = (TextView) convertView.findViewById(R.id.video_new_time);
		video_play_console = (LinearLayout) convertView.findViewById(R.id.video_play_console);
		iv_to_full = (ImageView) convertView.findViewById(R.id.video_to_fullScreen);
		
		iv_to_full.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, VideoFullScreenActivity.class);
				intent.putExtra(VideoFullScreenActivity.IMG_ID_KEY, bean.getImgList().get(0).getImg_id());
				context.startActivity(intent);
			}
		});
	
		// 动态修改是视频播放视图宽、高
		video_player_all.setLayoutParams(MyApplication.getVideoPlayerParams(-1, 2));
		
		// 更多按钮改为删除按钮
//		more.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.vide_delete));
		video_play_console.setVisibility(View.GONE);
		
		// 竖图直接覆盖展示
		if(Integer.parseInt(bean.getImgList().get(0).getImg_thumb_width()) < Integer.parseInt(bean.getImgList().get(0).getImg_thumb_height())){
			MyApplication.getInstance().display(bean.getImg_thumb(), iv_video_thumb, R.drawable.def_image);
			 iv_video_thumb.setVisibility(View.VISIBLE);
		}else{
			 iv_video_bg_hint.setVisibility(View.GONE);
			 iv_video_bg_hint02.setVisibility(View.GONE);
			 iv_video_thumb.setVisibility(View.GONE);
		}
		MyApplication.getInstance().display(bean.getImg_thumb(),  iv_video_bg_up, R.drawable.def_image);
		MyApplication.getInstance().display(bean.getImg_thumb(),  iv_video_bg, R.drawable.def_image);
		
		 video_play_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentConsole(bean);
			}
		});
		
		 video_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(video_layout.isPlaying()){
					 video_layout.suspend();
					return;
				}
				
				for (int i = 0 ; i < data.size() ; i++) {
					data.get(i).setPlay(false);
					if(position == i){
						data.get(i).setPlay(true);
					}
				}
				notifyDataSetChanged();
			}
		});
		
		 video_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( video_layout.isPlaying()){
					 video_layout.pause();
					 video_stop.setVisibility(View.GONE);
				}else{
					 video_stop.setVisibility(View.GONE);
				}
			}
		});
		
		 video_player_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( video_layout.isPlaying()){
					 video_start.setVisibility(View.GONE);
					 video_stop.setVisibility(View.VISIBLE);
				}else{
					 video_start.setVisibility(View.VISIBLE);
					 video_stop.setVisibility(View.GONE);
				}
			}
		});
		
		 video_layout.setOnPreparedListener(new OnPreparedListener() {  
			
			@Override  
			public void onPrepared(MediaPlayer mediaPlayer) { 
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						if(Integer.parseInt(bean.getImgList().get(0).getImg_thumb_width()) > Integer.parseInt(bean.getImgList().get(0).getImg_thumb_height())){
							ViewTreeObserver vto2 =  video_layout.getViewTreeObserver();    
							vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
								@Override    
								public void onGlobalLayout() {  
									 video_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
									 video_layout.setLayoutParams(MyApplication.getVideoPlayerParams(R.id.item_layout));
								}    
							});    
						}
						
						 iv_video_bg_up.setVisibility(View.GONE);
						 iv_video_loading.setVisibility(View.GONE);
						 iv_video_bg_hint.setVisibility(View.GONE);
						 video_play_console.setVisibility(View.VISIBLE);
					}
				});
			}  
		});  
		
		 video_layout.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				indexPosition = -1;
				for (int i = 0 ; i < data.size() ; i++) {
					data.get(i).setPlay(false);
				}
				notifyDataSetChanged();
			}
		});
		
		 video_type.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if( video_layout.isPlaying()){
					 video_layout.pause();
					 video_type.setBackground(context.getResources().getDrawable(R.drawable.video_start));
				}else{
					 video_layout.start();
					 video_type.setBackground(context.getResources().getDrawable(R.drawable.video_stop));
				}
			}
		});
		
		 skbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			int progress;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress *  video_layout.getDuration() / seekBar.getMax();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				 video_layout.seekTo(progress);
			}
		});

		 if(bean.isPlay()){
			 if(AbAppUtil.isWifi(context)){
				  indexPosition = -1;
				 
				 final Handler handleProgress = new Handler() {
					 public void handleMessage(Message msg) {
						 
						 int position =  video_layout.getCurrentPosition();
						 int duration =  video_layout.getDuration();
						 
						 Date date = new Date(duration);
						 SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
						 String dateStr = sdf.format(date);
						 
						 date = new Date(position);
						 SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
						 String dateStr02 = sdf02.format(date);
						 
						 if ( duration > 0 &&  position >  indexPosition) {
							  video_all_length.setText(dateStr);
							  video_new_time.setText(dateStr02 + "");
							 long pos =  skbProgress.getMax() *  position /  duration;
							  skbProgress.setProgress((int) pos);
							  indexPosition =  position;
						 }
					 };
				 };
				 
				 Timer videoPlayerTimer = new Timer();
				 videoPlayerTimer.schedule(new TimerTask() {
					 
					 @Override
					 public void run() {
						 if ( video_layout == null){
							 return;
						 }
						 if ( video_layout.isPlaying() &&  skbProgress.isPressed() == false) {
							 handleProgress.sendEmptyMessage(0);
						 }
					 }
				 }, 0, 50);
				 
				 MyApplication.getInstance().display(bean.getImg_thumb(),  iv_video_bg, R.drawable.def_image);
				  iv_video_thumb.setVisibility(View.GONE);
				  video_start.setVisibility(View.GONE);
				  iv_video_bg_hint.setVisibility(View.VISIBLE);
				  iv_video_loading.setVisibility(View.VISIBLE);
				 
				 Uri uri = Uri.parse(bean.getVideo_url());
				 MediaController mc = new MediaController(context);
				 mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
				  video_layout.setMediaController(mc);
				  video_layout.setVideoURI(uri);
				  video_layout.requestFocus();
				  video_layout.start();
			 }else{
				 dialog = new Builder(context);
				 dialog.setTitle("温馨提示");
				 dialog.setMessage("您当前并未连接WIFI，继续播放将使用手机流量，是否继续？");
				 dialog.setNegativeButton("继续",
						 new DialogInterface.OnClickListener() {
					 
					 @Override
					 public void onClick(DialogInterface dialog,
							 int which) {
						  indexPosition = -1;
						 
						 final Handler handleProgress = new Handler() {
							 public void handleMessage(Message msg) {
								 
								 int position =  video_layout.getCurrentPosition();
								 int duration =  video_layout.getDuration();
								 
								 Date date = new Date(duration);
								 SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
								 String dateStr = sdf.format(date);
								 
								 date = new Date(position);
								 SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
								 String dateStr02 = sdf02.format(date);
								 
								 if (duration > 0 && position > indexPosition) {
									  video_all_length.setText(dateStr);
									  video_new_time.setText(dateStr02 + "");
									 long pos =  skbProgress.getMax() *  position /  duration;
									  skbProgress.setProgress((int) pos);
									  indexPosition =  position;
								 }
							 };
						 };
						 
						 Timer videoPlayerTimer = new Timer();
						 videoPlayerTimer.schedule(new TimerTask() {
							 
							 @Override
							 public void run() {
								 if ( video_layout == null){
									 return;
								 }
								 if ( video_layout.isPlaying() &&  skbProgress.isPressed() == false) {
									 handleProgress.sendEmptyMessage(0);
								 }
							 }
						 }, 0, 50);
						 
						 MyApplication.getInstance().display(bean.getImg_thumb(),  iv_video_bg, R.drawable.def_image);
						  iv_video_thumb.setVisibility(View.GONE);
						  video_start.setVisibility(View.GONE);
						  iv_video_bg_hint.setVisibility(View.VISIBLE);
						  iv_video_loading.setVisibility(View.VISIBLE);
						 
						 Uri uri = Uri.parse(bean.getVideo_url());
						 MediaController mc = new MediaController(context);
						 mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
						  video_layout.setMediaController(mc);
						  video_layout.setVideoURI(uri);
						  video_layout.requestFocus();
						  video_layout.start();
					 }
					 
				 });
				 dialog.setPositiveButton("取消",
						 new DialogInterface.OnClickListener() {
					 
					 @Override
					 public void onClick(DialogInterface arg0, int arg1) {
						  noWifiPlay = false;
					 }
				 });
				 dialog.create().show();	
			 }
		 }
		return convertView;
	}
	
	// 1大图 + 2小图
	private View initImageParallelView(View convertView, final MainListItemBean bean){
		convertView = inflater.inflate(R.layout.item_main_type05, null);
		final ImageView main_item05_image01 = (ImageView) convertView.findViewById(R.id.main_item05_image01);
		ImageView main_item05_image02 = (ImageView) convertView.findViewById(R.id.main_item05_image02);
		ImageView main_item05_image03 = (ImageView) convertView.findViewById(R.id.main_item05_image03);
		TextView main_item_par_title = (TextView) convertView.findViewById(R.id.main_item_par_title);
		
		main_item_par_title.setText(bean.getImg_title());
		MyApplication.getInstance().display(bean.getImgList().get(0).getImg_thumb(),  main_item05_image01, R.drawable.def_image);
		MyApplication.getInstance().display(bean.getImgList().get(1).getImg_thumb(),  main_item05_image02, R.drawable.def_image);
		MyApplication.getInstance().display(bean.getImgList().get(2).getImg_thumb(),  main_item05_image03, R.drawable.def_image);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentConsole(bean);
			}
		});
		
		return convertView;
	}
	
	// 左图右文视图
	@SuppressLint("NewApi")
	private View initImageAndTextView(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type07, null);
		
		LinearLayout main_item_07_title = (LinearLayout) convertView.findViewById(R.id.main_item_07_title);
		if(null != bean.getHot_time_title()){
			if(!bean.getHot_time_title().isEmpty()){
				main_item_07_title.setVisibility(View.VISIBLE);
				TextView main_item_title = (TextView) convertView.findViewById(R.id.main_item_title);
				main_item_title.setText(bean.getHot_time_title());
			}
		}
		
		final ImageView main_item05_image01 = (ImageView) convertView.findViewById(R.id.main_item05_image01);
		ImageView main_item05_image02 = (ImageView) convertView.findViewById(R.id.main_item05_image02);
		
		TextView main_item05_text01 = (TextView) convertView.findViewById(R.id.main_item05_text01);
		TextView main_item05_text02 = (TextView) convertView.findViewById(R.id.main_item05_text02);
		TextView main_item05_text03 = (TextView) convertView.findViewById(R.id.main_item05_text03);
		final RelativeLayout main_item05_textparent = (RelativeLayout) convertView.findViewById(R.id.main_item05_textparent);
		
		MyApplication.getInstance().display(bean.getImg_thumb(), main_item05_image01, R.drawable.deft_color);
		main_item05_text03.setText(bean.getUser_name());
		main_item05_text02.setText(bean.getImg_title());
//		main_item05_text03.setText(bean.getImg_description());
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentConsole(bean);
			}
		});
		
		return convertView;
	}
	
	
	/**
	 * 跳转管理
	 * 1. 被点击的View
	 * 2. 跳转至页面
	 * */
	public void IntentConsole(final MainListItemBean bean){
		if(Integer.parseInt(bean.getType()) == 32){ // 群详情
			GroupRelevantUtils.CheckIntent(context, bean.getImgList().get(0).getImg_id());
		}else{
			Intent intent = null;
			switch (Integer.parseInt(bean.getType())) {
			case 1:
				// 帖子列表
				intent = new Intent(context, PostBarActivity.class);
				intent.putExtra("tag_id", bean.getTag_id());
				intent.putExtra("img_ids", bean.getImg_ids());
				intent.putExtra("type", bean.getType());
				intent.putExtra("img_title", bean.getImg_title());
				break;
			case 2:
				// 帖子详情
				intent = new Intent(context, MainItemDetialActivity.class);
				intent.putExtra("user_id", bean.getImgList().get(0).getUser_id());
				intent.putExtra("img_id", bean.getImgList().get(0).getImg_id());
				break;
			case 3:
				// 视频详情
				intent = new Intent(context, VideoDetialActivity.class);
				PostBarTypeItem postBean = new PostBarTypeItem();
				postBean.setImg_id(bean.getImgList().get(0).getImg_id());
				postBean.setUser_id(bean.getImgList().get(0).getUser_id());
				postBean.setVideo_url(bean.getVideo_url());
				postBean.setImg(bean.getImgList().get(0).getImg_thumb());
				postBean.setImg_thumb_width(bean.getImgList().get(0).getImg_thumb_width());
				postBean.setImg_thumb_height(bean.getImgList().get(0).getImg_thumb_height());
				Bundle bun = new Bundle();
				bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, postBean);
				intent.putExtras(bun);
				break;
			case 5:
				// 视频列表
				intent = new Intent(context, PostBarActivity.class);
				intent.putExtra("tag_id", bean.getTag_id());
				intent.putExtra("type", bean.getType());
				intent.putExtra("img_ids", bean.getImg_ids());
				intent.putExtra("img_title", bean.getImg_title());
				break;
			case 21:
				// 商家列表
//				intent = new Intent(context, BusinessList.class);
//				intent.putExtra("tag_id", bean.getTag_id());
//				intent.putExtra("img_ids", bean.getImg_ids());
//				intent.putExtra("img_title", bean.getImg_title());
				
				intent = new Intent(context, MainTab.class);
				break;
			case 22:
				// 商家详情
				intent = new Intent(context, BusinessDetailActivity.class);
				intent.putExtra("business_id", bean.getImgList().get(0).getImg_id());
				break;
			case 31:
				// 群列表
				intent = new Intent(context, PostBarActivity.class);
				intent.putExtra("tag_id", bean.getTag_id());
				intent.putExtra("type", bean.getType());
				intent.putExtra("img_ids", bean.getImg_ids());
				intent.putExtra("img_title", bean.getImg_title());
				intent.putExtra("groupType", bean.isGroupType());
				break;
			case 41:
				// 外链
				intent = new Intent(context, MainItemDetialWebActivity.class);
				intent.putExtra(MainItemDetialWebActivity.LINK_URL, bean.getPost_url());
				break;
			case 51:
				// 玩具
				intent = new Intent(context, ToysLeaseMainTabActivity.class);
//				intent.putExtra(ToysLeaseCategoryActivity.CATEGORY_ID_KEY, bean.getImgList().get(0).getImg_id());
				break;
			case 4:
				// 精选商家列表
				intent = new Intent(context, BusinessSelectedListActivity.class);
				intent.putExtra(BusinessSelectedListActivity.CITY_ID, bean.getImg_ids());
				intent.putExtra(BusinessSelectedListActivity.CITY_NAME, bean.getImg_title());
				break;
			case 7:
				// 玩具详情
				intent = new Intent(context, ToysLeaseDetailActivity.class);
				intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, bean.getImg_ids());
				break;
			}
			if(null != intent){
				context.startActivity(intent);
			}
		}
	}

	
	// 初始化一张大图布局
	private View initView09(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_type09, null);
		
		ImageView main_item_img = (ImageView) convertView.findViewById(R.id.main_item_img);
		app.display(bean.getImgList().get(0).getImg_thumb(), main_item_img, R.drawable.deft_color);
		
		main_item_img.setLayoutParams(MyApplication.getImageViewParams());
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				IntentConsole(bean);
			}
		});
		return convertView;
	};
	
	
	// 初始化首页顶部“精彩成长美一瞬间”
	private View initViewOther(View convertView, final MainListItemBean bean) {
		convertView = inflater.inflate(R.layout.item_main_other, null);
		return convertView;
	};
}