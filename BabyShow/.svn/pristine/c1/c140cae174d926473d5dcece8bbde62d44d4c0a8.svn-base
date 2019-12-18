package com.yyqq.code.main;

import java.io.File;
import java.util.ArrayList;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;

public class LoadDeviceImage extends Activity {
	private GridView mPhotoWall;
	private PhotoWallAdapter adapter;
	ImageButton upload;
	public String tag = "LoadDeviceImage";
	Context context;
	Bitmap loadBitmap;
	private ArrayList<String> filenames = new ArrayList<String>();
	private ArrayList<Boolean> gridviewSelected = new ArrayList<Boolean>();

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.load_device_image);
		loadBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		context = this;
		mPhotoWall = (GridView) findViewById(R.id.photo_wall);
		adapter = new PhotoWallAdapter(LoadDeviceImage.this, 0, filenames,
				mPhotoWall);
		mPhotoWall.setAdapter(adapter);
		Config.showProgressDialog(context, false, null);
		new Thread() {
			public void run() {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File sdcardfile = Environment.getExternalStorageDirectory();
					LoadDeviceImage.this.readSdFileName(sdcardfile);

				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						Config.dismissProgress();
					}
				});
			}
		}.start();
	}

	private void readSdFileName(File sdcardfile) {
		File[] files = sdcardfile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.readSdFileName(file);
			} else {
				// Log.e("picView",file.);
				String filepath = file.getAbsolutePath();
				if (filepath.endsWith("jpg") /*
											 * || filepath.endsWith("gif") ||
											 * filepath.endsWith("bmp") ||
											 * filepath.endsWith("png")
											 */) {
					filenames.add(filepath);
					gridviewSelected.add(false);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * GridView的适配器，负责异步从网络上下载图片展示在照片墙上。
	 * 
	 * @author guolin
	 */
	public class PhotoWallAdapter extends ArrayAdapter<String> implements
			OnScrollListener {
		/**
		 * GridView的实例
		 */
		private GridView mPhotoWall;
		/**
		 * 第一张可见图片的下标
		 */
		private int mFirstVisibleItem;
		/**
		 * 一屏有多少张图片可见
		 */
		private int mVisibleItemCount;
		/**
		 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
		 */
		private boolean isFirstEnter = true;
		Activity context;
		ArrayList<String> imageThumbUrls = new ArrayList<String>();

		public PhotoWallAdapter(Activity context, int textViewResourceId,
				ArrayList<String> objects, GridView photoWall) {
			super(context, textViewResourceId, objects);
			this.context = context;
			mPhotoWall = photoWall;
			imageThumbUrls = objects;
			mPhotoWall.setOnScrollListener(this);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final String url = getItem(position);
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.photo_layout, null);
			} else {
				view = convertView;
			}
			final ImageView photo = (ImageView) view.findViewById(R.id.photo);
			// 给ImageView设置一个Tag，保证异步加载图片时不会乱序
			photo.setTag(url);
			final ImageView photoSelected = (ImageView) view
					.findViewById(R.id.photo_selected);
			photo.setImageBitmap(loadBitmap);
			if (gridviewSelected.get(position)) {
				photoSelected.setVisibility(View.VISIBLE);
			} else {
				photoSelected.setVisibility(View.GONE);
			}
			// setImageView(url,photo);
			// loadBitmaps(firstVisibleItem,visibleItemCount);
			// try
			// {
			// final String imageUrl = imageThumbUrls.get(position);
			// // Log.e(tag,"i=" + imageUrl);
			// //
			// MyApplication.getInstance().display((imageThumbUrls.get(i)),(ImageView)mPhotoWall.findViewWithTag(imageUrl),R.drawable.ic_launcher);
			// if(imageUrl.startsWith("http"))
			// {
			// MyApplication.getInstance().display((imageThumbUrls.get(position)),(ImageView)mPhotoWall.findViewWithTag(imageUrl),R.drawable.ic_launcher);
			// }
			// else
			// {
			// BitmapFactory.Options opts = new BitmapFactory.Options();
			// opts.inJustDecodeBounds = true;
			// int imgWidth = 0;
			// int imgHeight = 0;
			// Bitmap mBitmap = BitmapFactory.decodeFile(imageUrl,opts);
			// imgWidth = opts.outWidth;
			// imgHeight = opts.outHeight;
			// opts = null;
			// BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// Config.setNewopts(context,newOpts,imgWidth,imgHeight);
			// mBitmap = BitmapFactory.decodeFile(imageUrl,newOpts);
			// photo.setImageBitmap(mBitmap);
			// }
			// }
			// catch(Exception e)
			// {
			// e.printStackTrace();
			// }
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (gridviewSelected.get(position)) {
						gridviewSelected.set(position, false);
						photoSelected.setVisibility(View.GONE);
					} else {
						photoSelected.setVisibility(View.VISIBLE);
						gridviewSelected.set(position, true);
					}
				}
			});
			return view;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
			if (scrollState == SCROLL_STATE_IDLE) {
				loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
			} else {
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;
			// 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
			// 因此在这里为首次进入程序开启下载任务。
			if (isFirstEnter && visibleItemCount > 0) {
				loadBitmaps(firstVisibleItem, visibleItemCount);
				isFirstEnter = false;
			}
		}

		/**
		 * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
		 * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
		 * 
		 * @param firstVisibleItem
		 *            第一个可见的ImageView的下标
		 * @param visibleItemCount
		 *            屏幕中总共可见的元素数
		 */
		private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
			try {
				for (int i = firstVisibleItem; i < firstVisibleItem
						+ visibleItemCount; i++) {
					final String imageUrl = imageThumbUrls.get(i);
					if (imageUrl.startsWith("http")) {
						MyApplication.getInstance().display(
								(imageThumbUrls.get(i)),
								(ImageView) mPhotoWall
										.findViewWithTag(imageUrl),
								R.drawable.ic_launcher);
					} else {
						((ImageView) mPhotoWall.findViewWithTag(imageUrl))
								.setImageBitmap(loadBitmap);
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;
						int imgWidth = 0;
						int imgHeight = 0;
						Bitmap mBitmap = BitmapFactory.decodeFile(imageUrl,
								opts);
						imgWidth = opts.outWidth;
						imgHeight = opts.outHeight;
						opts = null;
						BitmapFactory.Options newOpts = new BitmapFactory.Options();
						Config.setNewopts(context, newOpts, imgWidth, imgHeight);
						mBitmap = BitmapFactory.decodeFile(imageUrl, newOpts);
						((ImageView) mPhotoWall.findViewWithTag(imageUrl))
								.setImageBitmap(mBitmap);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}