package com.yyqq.commen.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.yyqq.babyshow.R;
import com.yyqq.code.video.VideoDetialActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayerUtils implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	private TextView video_new_time;
	private TextView video_all_length;
	private Activity activity;
	public int position;
	private int duration;
	private Button video_type;

	public VideoPlayerUtils(Activity activity, SurfaceView surfaceView, SeekBar skbProgress, TextView video_new_time, TextView video_all_length, Button video_type) {
		this.activity = activity;
		this.skbProgress = skbProgress;
		this.video_all_length = video_all_length;
		this.video_new_time = video_new_time;
		this.video_type = video_type;
		
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			position = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();

			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Date date = new Date(duration);
					SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
					String dateStr = sdf.format(date);
					video_all_length.setText(dateStr);
					
					date = new Date(position);
					SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
					String dateStr02 = sdf02.format(date);
					video_new_time.setText(dateStr02 + "");
				}
			});
			
			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	// *****************************************************

	public void play() {
		mediaPlayer.start();
		 if (position > 0){
			 Log.e("==", position+"");
             mediaPlayer.seekTo(position);
		 }
	}
	
	public void play(int time) {
		mediaPlayer.start();
		 if (time > 0){
			 Log.e("==", time+"");
             mediaPlayer.seekTo(time);
		 }
	}

	public void playUrl(String videoUrl) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();// prepare之后自动播放
//			 mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
//			mediaPlayer.release();
//			mediaPlayer = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.e("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		Log.e("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("mediaPlayer", "surface destroyed");
	}

	/**
	 * 通过onPrepared播放
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
		}
		Log.e("mediaPlayer", "onPrepared");
	}

	/**
	 * 播放结束后的操作
	 * */
	@SuppressLint("NewApi")
	@Override
	public void onCompletion(MediaPlayer arg0) {
		position = 0;
		video_type.setBackground(activity.getResources().getDrawable(R.drawable.video_start));
		VideoDetialActivity.isStart = false;
//		if(null != VideoDetialActivity.video_play_option){
//			VideoDetialActivity.video_play_option.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		if(bufferingProgress != 0 && skbProgress.getMax() != 0 && mediaPlayer.getCurrentPosition() != 0 && mediaPlayer.getDuration() != 0){
			skbProgress.setSecondaryProgress(bufferingProgress);
			int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		}
	}
	
}
