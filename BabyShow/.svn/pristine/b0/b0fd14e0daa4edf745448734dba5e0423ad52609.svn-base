package com.yyqq.commen.service;

import com.yyqq.babyshow.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class AnimPlayerService extends Service {
	private MediaPlayer mp;
	int i;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		i = intent.getIntExtra("music", 1);
		switch (i) {
		case 1:
			mp = MediaPlayer.create(this, R.raw.music1);
			break;
		case 2:
			mp = MediaPlayer.create(this, R.raw.music2);
			break;
		case 3:
			mp = MediaPlayer.create(this, R.raw.music3);
			break;
		case 4:
			mp = MediaPlayer.create(this, R.raw.music4);
			break;
		case 5:
			mp = MediaPlayer.create(this, R.raw.music5);
			break;
		case 6:
			mp = MediaPlayer.create(this, R.raw.music6);
			break;
		case 7:
			mp = MediaPlayer.create(this, R.raw.music7);
			break;
		default:
			break;
		}
		mp.setLooping(true);
		mp.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
	}

}
