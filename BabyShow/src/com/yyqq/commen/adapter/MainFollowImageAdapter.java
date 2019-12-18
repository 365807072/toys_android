package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.myVideoPlayerView;

public class MainFollowImageAdapter extends BaseAdapter {
	
	private Context context;
	private int width;
	private String user_id;
	private String img_id;
	ArrayList<String> imaStrings = null;
	MyGridView myGridView;
	ArrayList<String> imgBig = null;
	ArrayList<String> ImageList = null;
	ArrayList<String> imaWid = null;
	ArrayList<String> imaHed = null;

	public MainFollowImageAdapter(Context context, String user_id, String img_id, int width, ArrayList<String> imaStrings, MyGridView myGridView,
			ArrayList<String> imgBig, ArrayList<String> imaWid,
			ArrayList<String> imaHed) {
		this.context = context;
		this.user_id = user_id;
		this.img_id = img_id;
		this.width = width;
		this.imaStrings = imaStrings;
		this.myGridView = myGridView;
		this.imgBig = imgBig;
		this.imaWid = imaWid;
		this.imaHed = imaHed;
	}

	@Override
	public int getCount() {

		return imaStrings.size() < 3 ? imaStrings.size() : 2;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.new_grida_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.picture_size = (TextView) convertView
					.findViewById(R.id.picture_size);
			holder.ly_picture_page = (LinearLayout) convertView
					.findViewById(R.id.ly_picture_page);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 图片处理
		if (!TextUtils.isEmpty(imaWid.get(position))) {
			int newW = width / 2;
			int newH = width / 2;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					newW, newH);
			holder.img.setLayoutParams(params);
		}

		holder.img.setTag(imaStrings.get(position));
		ImageDownloader mDownloader = null;
		if (mDownloader == null) {
			mDownloader = new ImageDownloader();
		}
		if (mDownloader != null && position != 3) {
			mDownloader.imageDownload(imaStrings.get(position), holder.img,"/Download/cache_files", (Activity)context, new OnImageDownload() {

						@Override
						public void onDownloadSucc(Bitmap bitmap, String c_url,
								ImageView imageView) {
							ImageView imageView1 = (ImageView) myGridView
									.findViewWithTag(c_url);
							if (imageView1 != null) {
								imageView1.setImageBitmap(bitmap);
								imageView1.setTag("");
							}
						}
					});
		}
		if (position == 1) {
			holder.ly_picture_page.setVisibility(View.VISIBLE);
			holder.picture_size.setText(imaStrings.size() + "张");
		}
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainItemDetialActivity.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("img_id", img_id);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

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

}