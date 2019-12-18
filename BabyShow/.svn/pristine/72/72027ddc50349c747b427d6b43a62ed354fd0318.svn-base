package com.yyqq.code.photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.buy.BuyShow;
import com.yyqq.code.grow.GrowEditActivity;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.AddNewPostContinueActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.postbar.PostBarAddActivity;
import com.yyqq.code.toyslease.AddNewToysLeaseActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.TotiPotentGridView;
import com.yyqq.commen.view.TotiPotentGridView.ICommViewListener;
import com.yyqq.framework.adapter.MyBaseAdapter;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class PhotoEdit extends Activity implements ICommViewListener {
	public String tag = "PhotoEdit";
	private Activity context;
	private AbHttpUtil ab;
	private MyAdapter adapter;
	private TextView name, fin;
	private ImageView delete, move, show;
	private TotiPotentGridView loadDataView = null;
	private GridView gridView = null;
	private LinearLayout bottom_root;
	private String isAlbum;

	private int selectTotal = 0;
	
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
		setContentView(R.layout.photo_edit);
		context = this;

		isAlbum = getIntent().getStringExtra("isAlbum");
		delete = (ImageView) findViewById(R.id.delete);
		move = (ImageView) findViewById(R.id.move);
		show = (ImageView) findViewById(R.id.show);

		bottom_root = (LinearLayout) findViewById(R.id.bottom_root);
		fin = (TextView) findViewById(R.id.fin);
		fin.setOnClickListener(showClick);
		name = (TextView) findViewById(R.id.name);
		name.setText(getIntent().getStringExtra("name"));
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		loadDataView = (TotiPotentGridView) findViewById(R.id.loaddatagridview);
		loadDataView.setCommViewListener(this);
		gridView = loadDataView.getGridView();
		adapter = new MyAdapter(this);
		gridView.setAdapter(adapter);
		Config.showProgressDialog(context, true, null);
		loadDataView.initData();
		// onRefresh();
		if ("isAlbum".equals(getIntent().getStringExtra("isAlbum"))) {
			bottom_root.setVisibility(View.GONE);
			fin.setVisibility(View.VISIBLE);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				FinalHttp fh = new FinalHttp();
				fh.post(ServerMutualConfig.ImportToDiaryV3, params,
						new AjaxCallBack<String>() {
							@Override
							public void onFailure(Throwable t, String strMsg) {
								Config.dismissProgress();
								super.onFailure(t, strMsg);
								Toast.makeText(context, strMsg, 1).show();
							}

							@Override
							public void onSuccess(String t) {
								Config.dismissProgress();
								super.onSuccess(t);
								Intent intent = new Intent();
								intent.setClass(context, GrowEditActivity.class);
								intent.putExtra("baby_id", getIntent()
										.getStringExtra("baby_id"));
								intent.putExtra("album_id", getIntent()
										.getStringExtra("album_id1"));
								intent.putExtra(
										"grow_detail_title",
										getIntent().getStringExtra(
												"grow_detail_title"));
								startActivity(intent);
								context.finish();
							}

						});
			}
		}
	};

	public class PhotoItem {
		public String img_id;
		public String img_down = "";
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";
		JSONObject json;
		public boolean isSelect = false;

		public void fromJson(JSONObject json) throws JSONException {
			this.json = json;
			if (json.has("img_id")) {
				img_id = json.getString("img_id") + "";
			}
			if (json.has("img_down")) {
				img_down = json.getString("img_down");
			}
			if (json.has("img_thumb")) {
				img_thumb = json.getString("img_thumb");
			}

			if (json.has("img")) {
				img = json.getString("img");
			}
			if (json.has("img_width")) {
				img_width = json.getString("img_width");
			}
			if (json.has("img_height")) {
				img_height = json.getString("img_height");
			}
			if (json.has("img_thumb_width")) {
				img_thumb_width = json.getString("img_thumb_width");
			}
			if (json.has("img_thumb_height")) {
				img_thumb_height = json.getString("img_thumb_height");
			}

		}
	}

	/**
	 * 多图秀一下
	 */
	public static String imgUris = "";
	StringBuilder imgUri;
	public OnClickListener showClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			imgUris = "";
			imgUri = new StringBuilder(imgUris);
			Intent intent = new Intent();
			if (adapter.getList().size() > 0) {
				if (TextUtils.isEmpty(imgUris)) {
					imgUri.append("[");
				}
				for (int i = 0; i < adapter.getList().size(); i++) {
					PhotoItem item = (PhotoItem) adapter.getList().get(i);
					if (item.isSelect) {
						imgUri.append('"');
						imgUri.append(item.img_down);
						imgUri.append('"');
						imgUri.append(",");

						BimpUtil.drr.add(item.img_thumb);
					}
				}

				if (imgUri.length() > 0) {
					imgUris = imgUri.toString();
					intent.putExtra("imgUri", imgUri.toString());
				}
			} else {
			}
			if ("isPostBar".equals(getIntent().getStringExtra("tag"))) {
				intent.setClass(context, PostBarAddActivity.class);
				intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
			} else if ("isBuy".equals(getIntent().getStringExtra("tag"))) {
				intent.setClass(context, BuyShow.class);
			}else if (AddNewToysLeaseActivity.AddNewToysLeaseActivity_TAG.equals(getIntent().getStringExtra("tag"))) {
				if(null != AddNewToysLeaseActivity.addNewToysLeaseActivity){
					AddNewToysLeaseActivity.addNewToysLeaseActivity.finish();
				}
				intent.setClass(context, AddNewToysLeaseActivity.class);
				intent.putExtra("showImage", true);
			}else if ("gen".equals(getIntent().getStringExtra("tag"))) {
				intent.setClass(context, MainItemDetialActivity.class);
				intent.putExtra("user_id", getIntent()
						.getStringExtra("user_id"));
				intent.putExtra("img_id", getIntent().getStringExtra("img_id"));
				intent.putExtra("is_save", getIntent()
						.getStringExtra("is_save"));
				intent.putExtra("tan", "tan");
			} else if ("video".equals(getIntent().getStringExtra("tag"))) {
				if(null != VideoDetialActivity.videoDetialActivity){
					VideoDetialActivity.videoDetialActivity.finish();
				}
				intent.setClass(context, VideoDetialActivity.class);
				intent.putExtra("user_id", getIntent()
						.getStringExtra("user_id"));
				intent.putExtra("img_id", getIntent().getStringExtra("img_id"));
				intent.putExtra("is_save", getIntent()
						.getStringExtra("is_save"));
				intent.putExtra("tan", "tan");
			} else if ("isGrowEdit".equals(getIntent().getStringExtra("tag"))) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						Send1();
					}
				}).start();
				return;
			}
			
			if(!AddNewPostActivity.AddNewActivity_TAG.equals(getIntent().getStringExtra("tag")) && !AddNewPostContinueActivity.AddNewContinueActivity_TAG.equals(getIntent().getStringExtra("tag")) && !"isPostBar".equals(getIntent().getStringExtra("tag"))){
				startActivity(intent);
			}
			PhotoEdit.this.finish();
			
			if ("gen".equals(getIntent().getStringExtra("tag")) || "video".equals(getIntent().getStringExtra("tag"))) {
				context.finish();
			}
		}

	};

	/**
	 * 成长记录编辑，上传照片
	 */
	final AjaxParams params = new AjaxParams();
	final FinalHttp fh = new FinalHttp();

	public void Send1() {
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", getIntent().getStringExtra("album_id1"));
		params.put("imgUrls", imgUris.substring(0, imgUris.length()-1)+"]");
		handler.sendEmptyMessage(1);
	}

	String imgIds = "";
	String[] haha;
	public OnClickListener deleteClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int selectNum = 0;
			imgIds = "";
			for (int i = 0; i < adapter.getList().size(); i++) {
				PhotoItem item = (PhotoItem) adapter.getList().get(i);
				if (item.isSelect) {
					selectNum += 1;
					imgIds += item.img_id + ",";
				}
			}
			imgIds = imgIds.substring(0, imgIds.length() - 1);
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage("您确定要删除选中的" + selectNum + "张照片吗?");
			builder.setPositiveButton("删除",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Config.showProgressDialog(context, false, null);
							FinalHttp fh = new FinalHttp();
							AjaxParams params = new AjaxParams();
							params.put("user_id", Config.getUser(context).uid);
							params.put("img_id", imgIds);
							params.put("do_type", 1 + "");
							params.put("login_user_id",
									Config.getUser(context).uid);
							// Log.e(tag, " params = " + params);
							fh.post(ServerMutualConfig.DoImg, params,
									new AjaxCallBack<String>() {
										@Override
										public void onFailure(Throwable t,
												String strMsg) {
											super.onFailure(t, strMsg);
											Config.dismissProgress();
											Config.showFiledToast(context);
										}

										@Override
										public void onSuccess(String t) {
											super.onSuccess(t);
											JSONObject json;
											try {
												json = new JSONObject(t);
												// Log.e(tag, "json = " + json);
												if (json.getBoolean("success")) {
													handler.post(new Runnable() {
														@Override
														public void run() {
															loadDataView
																	.initData();
														}
													});
												} else {
													Config.dismissProgress();
												}
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
											} catch (JSONException e) {
												e.printStackTrace();
												Config.dismissProgress();
											}
										}
									});
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

	public OnClickListener moveClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, MoveImageList.class);
			intent.putExtra("album_id", getIntent().getStringExtra("album_id"));
			imgIds = "";
			if (adapter.getList().size() > 0) {
				for (int i = 0; i < adapter.getList().size(); i++) {
					PhotoItem item = (PhotoItem) adapter.getList().get(i);
					if (item.isSelect) {
						imgIds += item.img_id + ",";
					}
				}
				if (imgIds.length() > 0) {
					imgIds = imgIds.substring(0, imgIds.length() - 1);
					intent.putExtra("ids", imgIds);
					startActivityForResult(intent, 165);
					context.finish();
				}

			} else {
			}
		}
	};

	public void setButtonAction(List<Object> list) {
		int selectNum = 0;
		for (int i = 0; i < list.size(); i++) {
			PhotoItem item = (PhotoItem) list.get(i);
			if (item.isSelect) {
				selectNum += 1;
			}
		}
		if (selectNum == 0) {
			delete.setOnClickListener(null);
			move.setOnClickListener(null);
			show.setOnClickListener(null);
			fin.setOnClickListener(null);
		} else {
			delete.setOnClickListener(deleteClick);
			move.setOnClickListener(moveClick);
			show.setOnClickListener(showClick);
			fin.setOnClickListener(showClick);
		}
	}

	public class MyAdapter extends MyBaseAdapter {
		public MyAdapter(Context context) {
			this.mContext = context;
		}

		public void setAbsListView(AbsListView absListView) {
			this.absListView = absListView;
		}

		public void setList(List<Object> alObjects, boolean boo) {
			this.alObjects.addAll(alObjects);
			if (boo) {
				notifyDataSetChanged();
			}
		}

		public List getList() {
			return alObjects;
		}

		public void clear() {
			this.alObjects.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return alObjects.size();
		}

		@Override
		public Object getItem(int arg0) {
			return alObjects.get(arg0);
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
			convertView = inflater.inflate(R.layout.photo_edit_item, null);
			final PhotoItem item = (PhotoItem) alObjects.get(position);
			ImageView img = (ImageView) convertView.findViewById(R.id.photo);
			// TextView desc = (TextView) convertView.findViewById(R.id.desc);
			MyApplication.getInstance().display(item.img_thumb, img,
					R.drawable.def_image);
			// desc.setText(item.desc);
			final ImageView select = (ImageView) convertView
					.findViewById(R.id.select);
			if (item.isSelect) {
				select.setBackgroundResource(R.drawable.sure);
			} else {
				select.setBackgroundResource(R.drawable.nosure);
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ((BimpUtil.drr.size() + selectTotal) < Config.SELECTEDIMAGECOUNT) {
						item.isSelect = !item.isSelect;
						if (item.isSelect) {
							select.setBackgroundResource(R.drawable.sure);
							selectTotal++;
						} else {
							select.setBackgroundResource(R.drawable.nosure);
							selectTotal--;
						}

					} else if ((BimpUtil.drr.size() + selectTotal) >= Config.SELECTEDIMAGECOUNT) {
						if (item.isSelect) {
							item.isSelect = !item.isSelect;
							select.setBackgroundResource(R.drawable.nosure);
							selectTotal--;
						} else {
							Toast.makeText(context,
									"最多选择" + Config.SELECTEDIMAGECOUNT + "张图片",
									1).show();
						}
					}

					setButtonAction(getList());

				}
			});
			return convertView;
		}
	}

	@Override
	public List<Object> doInBackGround(int CurrentPage) {
		return objects(CurrentPage);
	}

	public List<Object> objects(int currentpage) {
		final ArrayList<Object> arrayList = new ArrayList<Object>();
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("album_id", getIntent().getStringExtra("album_id"));
		// Log.e(tag, "currentpage=" + currentpage);
		if (currentpage != 1) {
			params.put("last_id", ((PhotoItem) (adapter.getList().get(adapter
					.getList().size() - 1))).img_id);
		}
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(ServerMutualConfig.ImgList + "&"
				+ params.toString());
		try {
			httpResponse = new DefaultHttpClient().execute(httpGet);
			Config.dismissProgress();
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
				try {
					JSONObject json = new JSONObject(
							result.replaceAll("\r", ""));
					if (json.getBoolean("success")) {
						if (json.getJSONArray("data").length() == 0) {
						} else {
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								PhotoItem item = new PhotoItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								arrayList.add(item);
							}
						}
					} else {
						Toast.makeText(context, json.getString("reMsg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	@Override
	public void callBackListData(List<Object> list) {
		adapter.setList(list, true);
	}

	@Override
	public void onHeadRefresh() {
		adapter.clear();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 165) {
				finish();
			}
		}
	}
}
