package com.yyqq.code.photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullGridView;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MovePhotoList extends Activity implements AbOnListViewListener {
	int width, height;
	public String TAG = "MovePhotoList";
	ImageView importImage;
	Activity context;
	AbHttpUtil http;
	ArrayList<PhotoItem> data = new ArrayList<MovePhotoList.PhotoItem>();
	AbPullGridView mAbPullGridView;
	GridView mGridView;
	MyAdapter adapter;
	String album_id;
	RelativeLayout share;
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.photo_manager);
		context = this;
		album_id = getIntent().getStringExtra("album_id");
		share = (RelativeLayout) findViewById(R.id.share);
		share.setVisibility(View.GONE);
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		http = AbHttpUtil.getInstance(context);
		http.setDebug(Log.isDebug);

		mAbPullGridView = (AbPullGridView) findViewById(R.id.list);
		mAbPullGridView.setPullRefreshEnable(false);
		mAbPullGridView.setPullLoadEnable(true);

		mGridView = mAbPullGridView.getGridView();
		// mGridView.setColumnWidth(150);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(3);
		mGridView.setNumColumns(2);
		mGridView.setPadding(5, 5, 5, 5);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(3);
		if (adapter == null) {
			adapter = new MyAdapter();
		}
		mAbPullGridView.setAdapter(adapter);
		mAbPullGridView.setAbOnListViewListener(this);
		// 设置进度条的样式
		mAbPullGridView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullGridView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mGridView = mAbPullGridView.getGridView();
		onRefresh();
	}

	public OnClickListener importImageClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Intent intent = new Intent(context, PhoneAlbumActivity.class);
			intent.putExtra("iszhineng", true);
			intent.putExtra("album_id", album_id);
			startActivity(intent);
		}
	};
	public OnClickListener moreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// AlertDialog.Builder builder = new Builder(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.photo_more, null);
			TextView import_image = (TextView) view
					.findViewById(R.id.import_image);
			import_image.setOnClickListener(importImageClick);
			TextView newAlbum = (TextView) view.findViewById(R.id.create);
			newAlbum.setOnClickListener(newAlbumClick);
			// builder.setView(view);
			// dialog = builder.create();
			// dialog.show();
			dialog = new Dialog(context);
			setDialogLayoutParams(dialog, 40, Gravity.CENTER);
			dialog.setContentView(view, new LayoutParams(width - 40,
					LayoutParams.WRAP_CONTENT));
			showDialog(0);
		}
	};
	Dialog dialog;
	public OnClickListener newAlbumClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			AlertDialog.Builder builder = new Builder(context);
			final EditText name = new EditText(context);
			builder.setTitle("照片集名字");
			builder.setView(name);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (name.getText().toString().trim().length() > 0) {
								Config.showProgressDialog(context, false, null);
								AbRequestParams params = new AbRequestParams();
								params.put("user_id",
										Config.getUser(context).uid);
								params.put("album_id", album_id);
								params.put("album_name", name.getText()
										.toString().trim());
								http.post(ServerMutualConfig.NewAlbum, params,
										newAlbumListener);
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	};
	public AbStringHttpResponseListener newAlbumListener = new AbStringHttpResponseListener() {
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);
			JSONObject json;
			try {
				json = new JSONObject(content);
				if (json.getBoolean("success")) {
					getAlbum();
				}
				Toast.makeText(context, json.getString("reMsg"),
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
		public void onFailure(int statusCode, String content, Throwable error) {
			super.onFailure(statusCode, content, error);
			Config.showFiledToast(context);
		}
	};

	public void getAlbum() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("album_id", album_id);
		http.get(ServerMutualConfig.AlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopRefresh();// 这个事线程安全的 可看源代码
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								{
									mAbPullGridView.setVisibility(View.VISIBLE);
									data.clear();
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										PhotoItem item = new PhotoItem();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										data.add(item);
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
			if (json.has("is_show_album")) {
				is_show_album = json.getString("is_show_album") + "";
			}

		}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final PhotoItem item = data.get(position);
			{
				convertView = inflater.inflate(R.layout.photoitem, null);
				ImageView defImage = (ImageView) convertView
						.findViewById(R.id.photo_defimg);
				MyApplication.getInstance().display(item.album_cover, defImage,
						R.drawable.def_head);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				name.setText(item.album_name);
				TextView desc = (TextView) convertView.findViewById(R.id.desc);
				desc.setText("");
				TextView time = (TextView) convertView.findViewById(R.id.time);
				time.setText(sdf.format(new Date(item.create_time)));
				TextView num = (TextView) convertView
						.findViewById(R.id.photonum);
				num.setText(item.img_count);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(context);
						builder.setMessage("确定要移动到此相册吗?");
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Config.showProgressDialog(context,
												false, null);
										FinalHttp fh = new FinalHttp();
										AjaxParams params = new AjaxParams();
										params.put("user_id",
												Config.getUser(context).uid);
										params.put("img_ids", getIntent()
												.getStringExtra("ids"));
										params.put(
												"album_id",
												getIntent().getStringExtra(
														"from_album_id"));
										params.put("to_album_id", item.id);
										fh.post(ServerMutualConfig.MoveImgs,
												params,
												new AjaxCallBack<String>() {
													@Override
													public void onFailure(
															Throwable t,
															String strMsg) {
														super.onFailure(t,
																strMsg);
														Config.dismissProgress();
														Config.showFiledToast(context);
													}

													@Override
													public void onSuccess(
															String t) {
														super.onSuccess(t);
														try {
															JSONObject json = new JSONObject(
																	t);
															if (json.getBoolean("success")) {
																setResult(RESULT_OK);
																finish();
															}
															Toast.makeText(
																	context,
																	json.getString("reMsg"),
																	Toast.LENGTH_SHORT)
																	.show();
															android.util.Log.i(
																	TAG,
																	json.getString("reMsg"));
															Config.dismissProgress();
														} catch (JSONException e) {
															e.printStackTrace();
														}
													}
												});
									}
								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				});
			}
			return convertView;
		}
	}

	@Override
	public void onRefresh() {
		getAlbum();
	}

	@Override
	public void onLoadMore() {
		if (data.isEmpty()) {
			return;
		}
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("album_id", album_id);
		params.put("last_id", data.get(data.size() - 1).id);
		http.get(ServerMutualConfig.AlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopLoadMore();// 这个事线程安全的 可看源代码
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

}
