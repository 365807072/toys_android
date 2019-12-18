package com.yyqq.code.photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.task.AbTaskQueue;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoThem extends Activity implements OnPullDownListener {
	public String TAG = "PhotoThem";
	Activity context;
	ArrayList<PhotoItem> data = new ArrayList<PhotoThem.PhotoItem>();
	private PullDownView mPullDownView = null;
	ListView listview;
	private AbTaskQueue mAbTaskQueue = null;
	AbHttpUtil ab;
	MyAdapter adapter;
	public ArrayList<String> ImaWid = null;
	public ArrayList<String> ImaHed = null;
	boolean isDefault = false;
	boolean people = false;

	ImageDownloader mDownloader;

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.them_albm);
		ImaWid = getIntent().getStringArrayListExtra("imaWid");
		ImaHed = getIntent().getStringArrayListExtra("imaHed");
		people = getIntent().getBooleanExtra("people", false);
		isDefault = getIntent().getBooleanExtra("default", false);
		context = this;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);

		adapter = new MyAdapter();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		// mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		// mPullDownView.setShowHeader();
		/*
		 * upload = (TextView) findViewById(R.id.upload); if (isDefault ||
		 * people) { upload.setVisibility(View.GONE); }
		 * upload.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Builder builder = new
		 * Builder(context); builder.setMessage("我们上传的是高清无损图片,请尽量使用WIFI/3G网络");
		 * builder.setNegativeButton("上传", new DialogInterface.OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * Intent intent = new Intent(context, TestPicActivity.class);
		 * intent.putExtra("album_id", getIntent() .getStringExtra("album_id"));
		 * intent.putExtra("iszhineng", false); startActivity(intent); } });
		 * builder.setNeutralButton("取消", null); builder.create().show(); } });
		 */
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Config.showProgressDialog(context, false, null);
		onRefresh();
	}

	public OnClickListener actionClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, PhotoEdit.class);
			intent.putExtra("album_id", getIntent().getStringExtra("album_id"));
			intent.putExtra("name", getIntent().getStringExtra("name"));
			startActivity(intent);
		}
	};

	public class PhotoItem {
		public String img_id = "";
		public long create_time = 0;

		public String img_down = "";
		public String img = "";
		public int img_width;
		public int img_height;
		public String img_thumb = "";
		public int img_thumb_width;
		public int img_thumb_height;

		public ArrayList<String> imaStrings = new ArrayList<String>();
		public ArrayList<String> imaBig = new ArrayList<String>();
		public ArrayList<String> img_downList = new ArrayList<String>();
		public ArrayList<String> imaWid = new ArrayList<String>();
		public ArrayList<String> imaHed = new ArrayList<String>();
		JSONObject json;

		public void fromJson(JSONObject json) throws JSONException {
			this.json = json;
			if (json.has("img_id")) {
				img_id = json.getInt("img_id") + "";
			}
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}

			JSONArray imgArray = json.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				img_down = imgArray.getJSONObject(i).getString("img_down");
				img = imgArray.getJSONObject(i).getString("img");
				img_width = imgArray.getJSONObject(i).getInt("img_width");
				img_height = imgArray.getJSONObject(i).getInt("img_height");
				img_thumb = imgArray.getJSONObject(i).getString("img_thumb");
				img_thumb_width = imgArray.getJSONObject(i).getInt(
						"img_thumb_width");
				img_thumb_height = imgArray.getJSONObject(i).getInt(
						"img_thumb_height");
				imaStrings.add(img_thumb);
				imaBig.add(img);
				img_downList.add(img_down);
				imaWid.add(String.valueOf(img_width));
				imaHed.add(String.valueOf(img_height));
			}
		}
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		if (people) {
			params.put("user_id", getIntent().getStringExtra("uid"));
		} else {
			params.put("user_id", Config.getUser(context).uid);
		}
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("album_id", getIntent().getStringExtra("album_id"));
		android.util.Log.e("fanfan", ServerMutualConfig.ImgListV2 + "&"
				+ params.toString());
		ab.get(ServerMutualConfig.ImgListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data.clear();
								data_json = new JSONArray();
								if (json.getJSONArray("data").length() == 0) {
									Toast.makeText(context, "该照片夹没有照片",
											Toast.LENGTH_SHORT).show();
								} else {
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										PhotoItem item = new PhotoItem();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										data.add(item);
										data_json.put(json.getJSONArray("data")
												.getJSONObject(i));
									}
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
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
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

	JSONArray data_json = new JSONArray();

	@Override
	public void onMore() {
		if (data.size() == 0) {
			return;
		}
		AbRequestParams params = new AbRequestParams();
		if (people) {
			params.put("user_id", getIntent().getStringExtra("uid"));
		} else {
			params.put("user_id", Config.getUser(context).uid);
		}
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("album_id", getIntent().getStringExtra("album_id"));
		params.put("last_id", data.get(data.size() - 1).img_id);
		ab.get(ServerMutualConfig.ImgListV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								{
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										PhotoItem item = new PhotoItem();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										data.add(item);
										data_json.put(json.getJSONArray("data")
												.getJSONObject(i));
									}
									adapter.notifyDataSetChanged();
									mPullDownView.RefreshComplete();// 这个事线程安全的
									mPullDownView.notifyDidMore();
								}
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
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.them_albm_item, null);
				holder.mGridView = (MyGridView) convertView
						.findViewById(R.id.n_img);
				holder.time = (TextView) convertView
						.findViewById(R.id.albm_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final PhotoItem item = data.get(position);
			holder.time.setText(sdf.format(new Date(item.create_time)));
			holder.mGridView
					.setAdapter(new ImageAdapter(item, holder.mGridView));
			return convertView;
		}
	}

	class ViewHolder {
		MyGridView mGridView;
		TextView time;
		ImageView img;
	}

	class ViewHolder1 {
		ImageView img;
	}

	class ImageAdapter extends BaseAdapter {
		MyGridView myGridView;
		PhotoItem item;

		public ImageAdapter(PhotoItem item, MyGridView myGridView) {
			this.myGridView = myGridView;
			this.item = item;
		}

		@Override
		public int getCount() {
			return item.imaStrings.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return item.imaStrings.get(position);
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
			ViewHolder1 holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.photo_theme_item, null);
				holder = new ViewHolder1();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder1) convertView.getTag();
			}
			if (position == 0) {
				holder.img.setBackgroundResource(R.drawable.themeplayer);
			} else {
				holder.img.setTag(item.imaStrings.get(position - 1));
				if (mDownloader == null) {
					mDownloader = new ImageDownloader();
				}
				if (mDownloader != null) {
					mDownloader.imageDownload(
							item.imaStrings.get(position - 1), holder.img,
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
				}
			}
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == 0) {
						Intent intent = new Intent();
						intent.setClass(context, AnimPlayer.class);
						intent.putStringArrayListExtra("imgList", item.imaBig);
						intent.putStringArrayListExtra("imaWid", item.imaWid);
						intent.putStringArrayListExtra("imaHed", item.imaHed);
						startActivity(intent);
					} else {
						Intent intent = new Intent(context,
								ChangePhotoSize.class);
						intent.putStringArrayListExtra("img_downList",
								item.img_downList);
						intent.putStringArrayListExtra("imaWid", item.imaWid);
						intent.putStringArrayListExtra("imaHed", item.imaHed);
						intent.putStringArrayListExtra("imgList", item.imaBig);
						intent.putExtra("friend",
								getIntent().getBooleanExtra("friend", false));
						intent.putExtra("isShare",
								getIntent().getStringExtra("isShare"));
						intent.putExtra("listIndex", position - 1);
						context.startActivity(intent);
					}
				}
			});

			return convertView;
		}

	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
