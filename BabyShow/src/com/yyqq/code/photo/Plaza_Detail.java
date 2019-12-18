package com.yyqq.code.photo;

import java.util.ArrayList;
import java.util.Date;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.ReviewList;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Plaza_Detail extends Activity {
	private final String TAG = "Plaza_Detail";
	private Activity context;
	private String img_id;
	private AbHttpUtil ab;
	private ImageView img, review, zan, more;
	private TextView name, time, msg, zanNum, check_reviewall;
	private RoundAngleImageView head;
	private int width, height;
	private RelativeLayout review_root;
	private MyGridView mGridView;
	ImageDownloader mDownloader;
	private ListView reviews;
	private reviewsAdapter adapter;
	private PlazaItem item;
	private TextView review_name, review_content;
	public ArrayList<String> ImaWid = null;
	public ArrayList<String> ImaHed = null;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.plaza_detail);
		mDownloader = new ImageDownloader();
		item = new PlazaItem();
		initView();
		getImageInfo();
	}

	private void initView() {
		ImaWid = getIntent().getStringArrayListExtra("imaWid");
		ImaHed = getIntent().getStringArrayListExtra("imaHed");
		mGridView = (MyGridView) findViewById(R.id.n_img);
		review_root = (RelativeLayout) findViewById(R.id.review_root);
		review = (ImageView) findViewById(R.id.review);
		check_reviewall = (TextView) findViewById(R.id.check_reviewall);
		zanNum = (TextView) findViewById(R.id.zan_num);
		zan = (ImageView) findViewById(R.id.zan);
		more = (ImageView) findViewById(R.id.more);
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		img_id = getIntent().getStringExtra("img_id");
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		img = (ImageView) findViewById(R.id.img);
		name = (TextView) findViewById(R.id.name);
		head = (RoundAngleImageView) findViewById(R.id.head);
		time = (TextView) findViewById(R.id.time);
		msg = (TextView) findViewById(R.id.msg);

		reviews = (ListView) findViewById(R.id.reviews);
		adapter = new reviewsAdapter();

	}

	public class reviewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return item.reviews.size();
		}

		@Override
		public Object getItem(int arg0) {
			return item.reviews.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.plaza_detail_listitem, null);
			review_name = (TextView) convertView.findViewById(R.id.review_name);
			review_name.setText(item.reviews.get(position).name);
			review_content = (TextView) convertView
					.findViewById(R.id.review_content);
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.getExpressionString(context,
							item.reviews.get(position).review);
			review_content.setText(spannableString);
			// review_content.setText(item.reviews.get(position).review);
			return convertView;
		}

	}

	public void getImageInfo() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("img_id", img_id);
		params.put("user_id", Config.getUser(context).uid);
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("is_post", getIntent().getStringExtra("is_post"));
		android.util.Log.i(TAG, "URL = " + ServerMutualConfig.PostImgInfo + "&"
				+ params.toString());
		ab.get(ServerMutualConfig.PostImgInfo + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							JSONObject info;
							info = json.getJSONArray("data").getJSONObject(0);
							item.fromJson(info);
							name.setText(item.user_name);

							if (item.imgs.size() == 1) {
								mGridView.setVisibility(View.GONE);
								img.setVisibility(View.VISIBLE);
								mDownloader.imageDownload(
										item.imaStrings.get(0), img,
										"/Download/cache_files", context,
										new OnImageDownload() {

											@Override
											public void onDownloadSucc(
													Bitmap bitmap,
													String c_url,
													ImageView imageView) {
												imageView
														.setImageBitmap(bitmap);
											}
										});
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent(context,
												ChangePhotoSize.class);
										intent.putExtra("img",
												item.imaBig.get(0));
										intent.putStringArrayListExtra(
												"imaWid", item.imaWid);
										intent.putStringArrayListExtra(
												"imaHed", item.imaHed);
										context.startActivity(intent);
									}
								});
							} else {
								mGridView.setVisibility(View.VISIBLE);
								img.setVisibility(View.GONE);
								mGridView.setAdapter(new ImageAdapter(
										item.imaStrings, mGridView,
										item.imaBig, item.imaWid, item.imaHed));
							}

							head.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent();
									if (item.user_id.equals(Config
											.getUser(context).uid)) {
										intent.setClass(context, PersonalCenterActivity.class);
									} else {
										intent.setClass(context, UserInfo.class);
										intent.putExtra("uid", item.user_id);
									}
									startActivity(intent);
								}
							});

							if (item.user_id.equals(Config.getUser(context).uid)) {
								more.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Builder builder = new Builder(context);
										builder.setMessage("确认要删除吗?");
										builder.setNegativeButton(
												"删除",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														Config.showProgressDialog(
																context, false,
																null);
														AjaxParams params = new AjaxParams();
														params.put(
																"user_id",
																Config.getUser(context).uid);
														params.put("img_ids",
																item.img_id);
														FinalHttp fh = new FinalHttp();
//														ServerMutualConfig.DelShow
														fh.post( "",
																params,
																new AjaxCallBack<String>() {
																	@Override
																	public void onFailure(
																			Throwable t,
																			String strMsg) {
																		super.onFailure(
																				t,
																				strMsg);
																		Config.dismissProgress();
																		Config.showFiledToast(context);
																	}

																	@Override
																	public void onSuccess(
																			String t) {
																		super.onSuccess(t);
																		Config.dismissProgress();
																		try {
																			JSONObject json = new JSONObject(
																					t);
																			Toast.makeText(
																					context,
																					json.getString("reMsg"),
																					Toast.LENGTH_SHORT)
																					.show();
																			context.finish();
																		} catch (Exception e) {
																		}
																	}
																});
													}
												});
										builder.setNeutralButton("取消", null);
										builder.create().show();
									}
								});
							} else {
								more.setVisibility(View.GONE);
							}

							MyApplication.getInstance().display(item.avatar,
									head, R.drawable.def_head);
							long dateTime = Config.getDateDays(
									Config.sdf.format(new Date(System
											.currentTimeMillis())), Config.sdf
											.format(new Date(item.create_time)));
							if (dateTime < 0) {
								time.setText(Config.sdf.format(new Date(
										item.create_time)));
							}
							/**
							 * 大于5天
							 */
							else if (dateTime > 1000 * 60 * 60 * 24 * 5) {
								time.setText(Config.sdf.format(new Date(
										item.create_time)));
							}
							/**
							 * 大于1天
							 */
							else if (dateTime > 1000 * 60 * 60 * 24) {
								time.setText((dateTime / 1000 / 60 / 60 / 24)
										+ "天前");
							} else if (dateTime > 1000 * 60 * 60) {
								time.setText((dateTime / 1000 / 60 / 60)
										+ "小时前");
							} else if (dateTime > 1000 * 60) {
								time.setText((dateTime / 1000 / 60) + "分钟前");
							} else {
								time.setText((dateTime / 1000) + "秒前");
							}
							// time.setText(item.time);
							// if (!TextUtils.isEmpty(item.description)) {
							if ("".equals(item.description)) {
								msg.setVisibility(View.GONE);
							} else {
								msg.setText(item.description);
							}

							int newW, newH;
							if (item.imgs.get(0).img_thumb_width == item.imgs
									.get(0).img_thumb_height) {
								newW = width;
								newH = width;
							} else if (Float.parseFloat(item.imgs.get(0).img_thumb_width) > Float
									.parseFloat(item.imgs.get(0).img_thumb_height)) {
								float h = 0;
								float sx = (float) ((float) width / (float) (Integer
										.parseInt(item.imgs.get(0).img_thumb_width)));
								h = (float) ((float) Float.parseFloat(item.imgs
										.get(0).img_thumb_height) * (float) sx);
								newW = width;
								newH = (int) h;
							} else {
								float sx = (float) (width / 3 * 2)
										/ (Float.parseFloat(item.imgs.get(0).img_thumb_width));
								newW = (int) ((float) (((Float
										.parseFloat(item.imgs.get(0).img_thumb_width)) * sx)));
								newH = (int) ((float) ((Float
										.parseFloat(item.imgs.get(0).img_thumb_height)) * sx));
							}
							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									newW, newH);
							params.addRule(RelativeLayout.BELOW, R.id.msg);
							img.setLayoutParams(params);

							if (item.isZan) {
								zan.setBackgroundResource(R.drawable.zan_pressed);
							} else {
								zan.setBackgroundResource(R.drawable.zan);
							}
							zan.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Config.showProgressDialog(context, false,
											null);
									if (!item.isZan) {
										AbRequestParams params = new AbRequestParams();
										params.put("user_id",
												Config.getUser(context).uid);
										params.put("admire_id", item.user_id);
										params.put("img_id", item.img_id);
										ab.post(ServerMutualConfig.Admire,
												params,
												new AbStringHttpResponseListener() {
													@Override
													public void onSuccess(
															int statusCode,
															String content) {
														super.onSuccess(
																statusCode,
																content);
														item.admire_count = (Integer
																.parseInt(item.admire_count) + 1)
																+ "";
														zanNum.setText((item.admire_count)
																+ "条赞");
														item.isZan = true;
														zan.setBackgroundResource(R.drawable.zan_pressed);
													}

													@Override
													public void onFinish() {
														super.onFinish();
														Config.dismissProgress();
													}

													@Override
													public void onFailure(
															int statusCode,
															String content,
															Throwable error) {
														super.onFailure(
																statusCode,
																content, error);
														Config.showFiledToast(context);
													}
												});
									} else {
										AbRequestParams params = new AbRequestParams();
										params.put("user_id",
												Config.getUser(context).uid);
										params.put("admire_id", item.user_id);
										params.put("img_id", item.img_id);
										ab.post(ServerMutualConfig.CancelAdmire,
												params,
												new AbStringHttpResponseListener() {
													@Override
													public void onSuccess(
															int statusCode,
															String content) {
														super.onSuccess(
																statusCode,
																content);
														item.admire_count = (Integer
																.parseInt(item.admire_count) - 1)
																+ "";
														zanNum.setText((item.admire_count)
																+ "条赞");
														item.isZan = false;
														zan.setBackgroundResource(R.drawable.zan);
													}

													@Override
													public void onFinish() {
														super.onFinish();
														Config.dismissProgress();
													}

													@Override
													public void onFailure(
															int statusCode,
															String content,
															Throwable error) {
														super.onFailure(
																statusCode,
																content, error);
														Config.showFiledToast(context);
													}
												});
									}
								}
							});
							zanNum.setText(item.admire_count + "条赞");
							review.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(context,
											ReviewList.class);
									intent.putExtra("img_id", item.img_id);
									intent.putExtra("ismy",
											item.user_id.equals(Config
													.getUser(context).uid));
									startActivity(intent);
								}
							});

							// 评论
							if (item.reviews.size() > 0) {
								reviews.setAdapter(adapter);
								Utility.setListViewHeightBasedOnChildren(reviews);
							} else {
								review_root.setVisibility(View.GONE);
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
						finish();
					}
				});
	}

	public class PlazaItem {
		// 数据 img 中的参数
		public String img_id = "";
		public String user_id = "";
		public String user_name = "";
		public String avatar = "";
		public String avatar_origin = "";

		// img数组中的数据
		public ArrayList<Img> imgs = new ArrayList<Img>();

		/* 以下是img数组中的数据 */
		// public String img_down = "";
		// public String img = "";
		// public int img_width;
		// public int img_height;
		// public String img_thumb = "";
		// public String img_thumb_width = "";
		// public String img_thumb_height = "";
		/* 以上是img数组中的数据 */

		public String share_img_id = "";
		public String description = "";
		public String admire_count = "";
		public String review_count = "";
		public long create_time;
		public String img_name = "";
		public String img_cate = "";
		public String is_top = "";
		public String is_admire = "";

		// reviews数组
		public ArrayList<Review> reviews = new ArrayList<Review>();

		public ArrayList<String> imaStrings = new ArrayList<String>();
		public ArrayList<String> imaBig = new ArrayList<String>();
		public ArrayList<String> imaWid = new ArrayList<String>();
		public ArrayList<String> imaHed = new ArrayList<String>();
		public String img_width = "";
		public String img_height = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		public boolean isZan = false;
		public String tag = "MyShowItem";
		public JSONObject json;

		public void fromJson(JSONObject json) {
			try {
				JSONObject info = json.getJSONObject("img");
				this.json = json;
				img_id = info.getInt("img_id") + "";
				user_id = info.getInt("user_id") + "";
				user_name = info.getString("user_name");
				avatar = info.getString("avatar");
				avatar_origin = info.getString("avatar_origin");

				JSONArray imgArray = info.getJSONArray("img");
				for (int i = 0; i < imgArray.length(); i++) {
					imaStrings.add(imgArray.getJSONObject(i).getString(
							"img_thumb"));
					imaBig.add(imgArray.getJSONObject(i).getString("img"));
					img_width = imgArray.getJSONObject(i)
							.getString("img_width");
					img_height = imgArray.getJSONObject(i).getString(
							"img_height");
					imaWid.add(img_width);
					imaHed.add(img_height);
//					Log.e("yyy", "imaBig :" + imaBig.get(i));
				}

				// 获取img数组里面的东西
				JSONArray imgsArray = info.getJSONArray("img");
				for (int i = 0; i < imgsArray.length(); i++) {
					Img img = new Img();
					img.fromJson(imgArray.getJSONObject(i));
//					Log.e("yyy", "img :" + img.toString());
					imgs.add(img);
				}

				share_img_id = info.getString("share_img_id");
				description = info.getString("description");
				admire_count = info.getInt("admire_count") + "";
				review_count = info.getString("review_count") + "";
				create_time = info.getLong("create_time");
				img_name = info.getString("img_name");
				img_cate = info.getString("img_cate");
				is_top = info.getString("is_top") + "";
				is_admire = info.getString("is_admire");

				// 回复数组里面的数据
				JSONArray reviewArray = json.getJSONArray("reviews");
				if (info.getInt("is_admire") == 0) {
					isZan = false;
				} else {
					isZan = true;
				}
				for (int i = 0; i < reviewArray.length(); i++) {
					Review review = new Review();
					review.fromJson(reviewArray.getJSONObject(i));
					reviews.add(review);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public class Img {
		/* 以下是img数组中的数据 */
		public String img_down = "";
		public String img = "";
		public String img_width;
		public String img_height;
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		/* 以上是img数组中的数据 */

		public void fromJson(JSONObject json) {
			try {
				img_down = json.getString("img_down");
				img = json.getString("img");
				img_width = json.getString("img_width");
				img_height = json.getString("img_height");
				img_thumb = json.getString("img_thumb");
				img_thumb_width = json.getString("img_thumb_width");
				img_thumb_height = json.getString("img_thumb_height");

				android.util.Log.e("yyy", "fromJson width :" + img_thumb_width);
				android.util.Log.e("yyy", "fromJson height :"
						+ img_thumb_height);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class Review {
		public String id = "";
		public String name = "";
		public String review = "";

		public void fromJson(JSONObject json) {
			try {
				id = json.getString("id");
				name = json.getString("user_name");
				review = json.getString("demand");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class ImageAdapter extends BaseAdapter {
		ArrayList<String> imaStrings = null;
		MyGridView myGridView;
		ArrayList<String> imgBig = null;
		ArrayList<String> imaWid = null;
		ArrayList<String> imaHed = null;

		public ImageAdapter(ArrayList<String> imaStrings,
				MyGridView myGridView, ArrayList<String> imgBig,
				ArrayList<String> imaWid, ArrayList<String> imaHed) {
			this.imaStrings = imaStrings;
			this.myGridView = myGridView;
			this.imgBig = imgBig;
			this.imaWid = imaWid;
			this.imaHed = imaHed;
		}

		@Override
		public int getCount() {

			return imaStrings.size() < 7 ? imaStrings.size() : 6;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.myshow_grida_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setTag(imaStrings.get(position));
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null && position != 5) {
				mDownloader.imageDownload(imaStrings.get(position), holder.img,
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
			} else {
				holder.img.setBackgroundResource(R.drawable.more);
			}
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO click img
					Intent intent = new Intent(context, ChangePhotoSize.class);
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", item.imaWid);
					intent.putStringArrayListExtra("imaHed", item.imaHed);
					intent.putExtra("listIndex", position);
					context.startActivity(intent);
				}
			});
			return convertView;
		}

	}

	public static class Utility {

		public static void setListViewHeightBasedOnChildren(ListView listview) {

			// 获取ListView对应的Adapter

			ListAdapter listAdapter = listview.getAdapter();

			if (listAdapter == null) {

				// pre-condition

				return;

			}

			int totalHeight = 0;

			for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目

				View listItem = listAdapter.getView(i, null, listview);

				listItem.measure(0, 0); // 计算子项View 的宽高

				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

			}

			ViewGroup.LayoutParams params = listview.getLayoutParams();

			params.height = totalHeight
					+ (listview.getDividerHeight() * (listAdapter.getCount() - 1));
			// listView.getDividerHeight()获取子项间分隔符占用的高度
			// params.height最后得到整个ListView完整显示需要的高度

			listview.setLayoutParams(params);

		}

	}

	class ViewHolder {
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView time;
		TextView msg;
		ImageView review;
		TextView zanNum;
		ImageView zan;
		TextView check_reviewall;
	}
}