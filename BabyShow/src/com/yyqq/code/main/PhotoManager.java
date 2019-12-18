package com.yyqq.code.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.photo.PeoplePhotoManager;
import com.yyqq.code.photo.PhotoDetail;
import com.yyqq.code.photo.PhotoList;
import com.yyqq.code.photo.PhotoThem;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.VideoList;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class PhotoManager extends Activity {
	private int width, height;
	private final String TAG = "PhotoManager";
	private Activity context;
	private AbHttpUtil http;
	private ArrayList<PhotoItem> data = new ArrayList<PhotoManager.PhotoItem>();
	private ArrayList<SharePhotoItem> sharedata = new ArrayList<SharePhotoItem>();
	private MyAdapter adapter;
	private ShareAdapter adapter1;
	private ImageView more;
	private MyGridView myPhoto, sharePhoto;
	private RelativeLayout none;
	private LinearLayout root;
	private ImageView login_regist;
	private MyApplication myMyApplication;
	private String isAlbum;
	private LinearLayout root2;
	private final String fileName = "photomanager.txt";
	private final String fileName1 = "photomanager_share.txt";
	public static PhotoManager photoManager;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ("1".equals(myMyApplication.getVisitor())) {
			SharedPreferences shared = this.getSharedPreferences(
					"babyshow_user", this.MODE_PRIVATE);
			Editor editor = shared.edit();
			editor.remove("user");
			editor.commit();
			myMyApplication.setVisitor("");
		} else if (!TextUtils.isEmpty(getIntent().getStringExtra("isAlbum"))) {
			finish();
		} else {
			context.finish();
		}
		return false;
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.my_photo_manager);
		photoManager = this;
		isAlbum = getIntent().getStringExtra("isAlbum");
		myPhoto = (MyGridView) findViewById(R.id.my_photo);
		sharePhoto = (MyGridView) findViewById(R.id.share_photo);

		context = this;
		myMyApplication = (MyApplication) context.getApplication();
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		http = AbHttpUtil.getInstance(context);
		http.setDebug(Log.isDebug);
		none = (RelativeLayout) findViewById(R.id.none);
		root = (LinearLayout) findViewById(R.id.root);
		root2 = (LinearLayout) findViewById(R.id.root2);
		login_regist = (ImageView) findViewById(R.id.login_regist);
		login_regist.setOnClickListener(loginClick);

		if ("isAlbum".equals(getIntent().getStringExtra("isAlbum"))) {
			root2.setVisibility(View.GONE);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if ("1".equals(myMyApplication.getVisitor())) {
			Intent intent = new Intent();
			intent.setClass(context, WebLoginActivity.class);
			startActivity(intent);
		} else {
			getAlbum();
			getShareAlbum();
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public OnClickListener loginClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			myMyApplication.setVisitor("");
			Intent intent = new Intent();
			intent.setClass(context, WebLoginActivity.class);
			startActivity(intent);
		}
	};

	public OnClickListener importImageClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Intent intent = new Intent(context, PhoneAlbumActivity.class);
			startActivity(intent);
		}
	};

	public OnClickListener moreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.photo_more, null);
			TextView import_image = (TextView) view
					.findViewById(R.id.import_image);
			import_image.setOnClickListener(importImageClick);
			dialog = new Dialog(context);
			setDialogLayoutParams(dialog, 40, Gravity.CENTER);
			dialog.setContentView(view, new LayoutParams(width - 40,
					LayoutParams.WRAP_CONTENT));
			showDialog(0);
		}
	};
	Dialog dialog;

	public void getAlbum() {
		if (!Config.isConn(this)) {
			try {
				getlistData(Config.read(this, fileName));
				return;
			} catch (Exception e) {
			}
		}
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
//		params.put("user_id", "132837");
//		android.util.Log.e("fanfan", ServerMutualConfig.AlbumList + "&" + params.toString());
		http.get(ServerMutualConfig.AlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.save(context, content, fileName);
						getlistData(content);
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
						Config.showFiledToast(context);
					}
				});
	}

	public void getShareAlbum() {
		if (!Config.isConn(this)) {
			try {
				getSharelistData(Config.read(this, fileName1));
			} catch (Exception e) {
			}
		}
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		// android.util.Log.e("fanfan", ServerMutualConfig.ShareAlbum + "&"
		// + params.toString());
		http.get(ServerMutualConfig.ShareAlbum + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.save(context, content, fileName1);
						getSharelistData(content);
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
						Config.showFiledToast(context);
					}
				});
	}

	public class PhotoItem {
		public String id = "";
		public String album_name = "";
		public String album_cover = "";
		public long create_time = 0;
		public String img_count;
		public int is_default;
		public String is_show_album = "";
		public String is_video = "";

		public void fromJson(JSONObject json) throws JSONException {
			if (json.has("id")) {
				id = json.getInt("id") + "";
			}
			if (json.has("album_name")) {
				album_name = json.getString("album_name");
			}
			if (json.has("album_cover")) {
				album_cover = json.getString("album_cover");
			}
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}
			if (json.has("img_count")) {
				img_count = json.getInt("img_count") + "";
			}
			if (json.has("is_default")) {
				is_default = json.getInt("is_default");
			}
			if (json.has("is_video")) {
				is_video = json.getString("is_video") + "";
			}
			if (json.has("is_show_album")) {
				is_show_album = json.getString("is_show_album") + "";
			}

		}
	}

	public class SharePhotoItem {
		public String id = "";
		public String user_id = "";
		public String nick_name = "";
		public String avatar = "";
		public int is_video;

		public void fromJson(JSONObject json) throws JSONException {
			if (json.has("id")) {
				id = json.getInt("id") + "";
			}
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			if (json.has("is_video")) {
				is_video = json.getInt("is_video");
			}
		}
	}

	public class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if ("isAlbum".equals(getIntent().getStringExtra("isAlbum"))) {
				return data.size() - 2;
			}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.photo_grid_item, null);
			final PhotoItem item = data.get(position);
			ImageView defImage = (ImageView) convertView
					.findViewById(R.id.photo_defimg);
			MyApplication.getInstance().display(item.album_cover, defImage,
					R.drawable.def_head);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.album_name);

			// TextView num = (TextView)
			// convertView.findViewById(R.id.photonum);
			// num.setText(item.img_count);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("album_id", item.id);
					intent.putExtra("baby_name", item.album_name);
					if (!"0".equals(item.is_video)) {
						intent.setClass(context, VideoList.class);
						intent.putExtra("uid", Config.getUser(context).uid);
					}else if (item.is_default == 0) {
						intent.setClass(context, PhotoList.class);
						intent.putExtra("isAlbum",
								getIntent().getStringExtra("isAlbum"));
						intent.putExtra("tag", getIntent()
								.getStringExtra("tag"));
						intent.putExtra("album_id1", getIntent()
								.getStringExtra("album_id1"));
						intent.putExtra("baby_id",
								getIntent().getStringExtra("baby_id"));
						intent.putExtra("grow_detail_title", getIntent()
								.getStringExtra("grow_detail_title"));
						intent.putExtra("user_id",
								getIntent().getStringExtra("user_id"));
						intent.putExtra("img_id",
								getIntent().getStringExtra("img_id"));
						intent.putExtra("is_save",
								getIntent().getStringExtra("is_save"));
						intent.putExtra("group_id",
								getIntent().getStringExtra("group_id"));
					} else if ("主题相册".equals(item.album_name)) {
						intent.putExtra("name", item.album_name);
						intent.putExtra("default", true);
						intent.putExtra("baby_name", item.album_name);
						intent.setClass(PhotoManager.this, PhotoThem.class);
					} else {
						intent.putExtra("name", item.album_name);
						intent.putExtra("default", true);
						intent.putExtra("baby_name", item.album_name);
						intent.putExtra("xiuAlbum", "xiuAlbum");
						intent.setClass(PhotoManager.this, PhotoDetail.class);
					}
					startActivity(intent);
					context.finish();
					if ("gen".equals(getIntent().getStringExtra("tag"))) {
						context.finish();
					}
				}
			});
			return convertView;
		}
	}

	public class ShareAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return sharedata.size();
		}

		@Override
		public Object getItem(int arg0) {
			return sharedata.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.photo_grid_item, null);
			final SharePhotoItem item = sharedata.get(position);
			ImageView defImage = (ImageView) convertView
					.findViewById(R.id.photo_defimg);
			MyApplication.getInstance().display(item.avatar, defImage,
					R.drawable.def_head);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.nick_name);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							PeoplePhotoManager.class);
					intent.putExtra("uid", item.user_id);
					intent.putExtra("userName", item.nick_name);
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 描述：设置弹出Dialog的属性.
	 * 
	 * @param dialog
	 *            弹出Dialog
	 * @param dialogPadding
	 *            如果Dialog不是充满屏幕，要设置这个值
	 * @param gravity
	 *            the gravity
	 */
	private void setDialogLayoutParams(Dialog dialog, int dialogPadding,
			int gravity) {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// 此处可以设置dialog显示的位置
		window.setGravity(gravity);
		// 设置宽度
		lp.width = width - dialogPadding;
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		// 背景透明
		// lp.screenBrightness = 0.2f;
		// lp.alpha = 0.8f;
		lp.dimAmount = 0f;
		window.setAttributes(lp);
		// 添加动画
		window.setWindowAnimations(android.R.style.Animation_Dialog);
		// 设置点击屏幕Dialog不消失
		dialog.setCanceledOnTouchOutside(true);
	}

	/**
	 * 描述：对话框初始化.
	 * 
	 * @param id
	 *            the id
	 * @return the dialog
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		return dialog;
	}

	private void getlistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			if (json.getBoolean("success")) {
				data.clear();
				for (int i = 0; i < json.getJSONArray("data").length(); i++) {
					PhotoItem item = new PhotoItem();
					item.fromJson(json.getJSONArray("data").getJSONObject(i));
					data.add(item);
				}
				adapter = new MyAdapter();
				myPhoto.setAdapter(adapter);
			} else {
				Toast.makeText(context, json.getString("reMsg"),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getSharelistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			if (json.getBoolean("success")) {
				sharedata.clear();
				for (int i = 0; i < json.getJSONArray("data").length(); i++) {
					SharePhotoItem item = new SharePhotoItem();
					item.fromJson(json.getJSONArray("data").getJSONObject(i));
					sharedata.add(item);
				}
				adapter1 = new ShareAdapter();
				sharePhoto.setAdapter(adapter1);
			} else {
				Toast.makeText(context, json.getString("reMsg"),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}