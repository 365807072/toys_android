package com.yyqq.commen.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.postbar.PostReviewList;
import com.yyqq.commen.model.PostShowItem;
import com.yyqq.commen.model.PostShowItem.Image;
import com.yyqq.commen.model.PostShowItem.Review;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class PostDetailAdapter extends BaseAdapter {
	
	private ArrayList<PostShowItem> dataPostShow;
	private Activity context;
	private String post_title;
	private String post_text;
	private AbHttpUtil ab;
	private int width;
	private int height;
	private String post_img = "";

	public PostDetailAdapter(){}
	
	public PostDetailAdapter(ArrayList<PostShowItem> dataPostShow,
			Activity context, String post_title, String post_text,
			AbHttpUtil ab, int width, int height, String post_img) {
		super();
		this.dataPostShow = dataPostShow;
		this.context = context;
		this.post_title = post_title;
		this.post_text = post_text;
		this.ab = ab;
		this.width = width;
		this.height = height;
		this.post_img = post_img;
	}

	@Override
	public int getCount() {
		return dataPostShow.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataPostShow.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = null;

		convertView = inflater.inflate(R.layout.postshow_item, null);
		holder = new ViewHolder();
		holder.postTitle = (TextView) convertView.findViewById(R.id.post_title);
		holder.img = (ImageView) convertView.findViewById(R.id.img);
		holder.mGridView = (MyGridView) convertView.findViewById(R.id.n_img);
		holder.more = (ImageView) convertView.findViewById(R.id.more);
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.msg = (TextView) convertView.findViewById(R.id.msg);
		holder.ly_link = (LinearLayout) convertView.findViewById(R.id.ly_link);
		holder.img_link = (ImageView) convertView.findViewById(R.id.img_link);
		holder.msg_link = (TextView) convertView.findViewById(R.id.msg_link);
		holder.head = (RoundAngleImageView) convertView.findViewById(R.id.head);
		holder.rank = (ImageView) convertView.findViewById(R.id.rank);
		holder.ly_rank = (LinearLayout) convertView.findViewById(R.id.ly_rank);
		holder.review = (TextView) convertView.findViewById(R.id.review);
		holder.check_reviewall = (TextView) convertView
				.findViewById(R.id.check_reviewall);
		convertView.setTag(holder);
		
		final PostShowItem item = dataPostShow.get(position);
		ScrollView scrollview = (ScrollView) convertView
				.findViewById(R.id.img_ly);
		if (item.ImageList.size() == 0) {
			scrollview.setVisibility(View.GONE);
		}
		LinearLayout imgLinear = new LinearLayout(context);
		addImg(scrollview, imgLinear, item.ImageList, position);

		// 标题
		if (TextUtils.isEmpty(item.img_title)) {
			holder.postTitle.setVisibility(View.GONE);
		} else {
			post_title = item.img_title;
			holder.postTitle.setText(post_title);
		}

		// 等级
		if (!TextUtils.isEmpty(item.level_img)) {
			MyApplication.getInstance().display(item.level_img, holder.rank,
					position);
			holder.ly_rank.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, Rank.class);
					context.startActivity(intent);
				}
			});
		}

		// 更多操作
		if (item.user_id.equals(Config.getUser(context).uid)) {
			holder.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Builder builder = new Builder(context);
					builder.setMessage("确认要删除吗?");
					builder.setNegativeButton("删除",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Config.showProgressDialog(context, false,
											null);
									AjaxParams params = new AjaxParams();
									params.put("user_id",
											Config.getUser(context).uid);
									params.put("img_ids", item.img_id);
									FinalHttp fh = new FinalHttp();
									fh.post(ServerMutualConfig.DelListingShow,
											params, new AjaxCallBack<String>() {
												@Override
												public void onFailure(
														Throwable t,
														String strMsg) {
													super.onFailure(t, strMsg);
													Config.dismissProgress();
													Config.showFiledToast(context);
												}

												@Override
												public void onSuccess(String t) {
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
														dataPostShow.remove(position);
														notifyDataSetChanged();
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
			holder.more.setVisibility(View.GONE);
		}

		// 链接
		if (!TextUtils.isEmpty(item.post_url)) {
			holder.ly_link.setVisibility(View.VISIBLE);
			MyApplication.getInstance().display(item.post_url_image,
					holder.img_link, R.drawable.fabu_default_link);
			holder.msg_link.setText(item.post_url_title);
		} else {
			holder.ly_link.setVisibility(View.GONE);
		}
		holder.ly_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String strUri;
				if (!TextUtils.isEmpty(item.post_url)) {
					strUri = item.post_url;
					if (!strUri.startsWith("http")) {
						strUri = "http://" + strUri;
					}
					Intent intent = new Intent(context, WebViewActivity.class);
					intent.putExtra("webviewurl", strUri);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "错误的网页", 0).show();
				}

			}

		});
		holder.name.setText(item.user_name);

		holder.head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (item.user_id.equals(Config.getUser(context).uid)) {
					intent.setClass(context, PersonalCenterActivity.class);
					intent.putExtra("falg_return", true);
				} else {
					intent.setClass(context, UserInfo.class);
					intent.putExtra("uid", item.user_id);
				}
				context.startActivity(intent);
			}
		});
		MyApplication.getInstance().display(item.avatar, holder.head,
				R.drawable.def_head);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long dateTime = Config.getDateDays(
				sdf.format(new Date(System.currentTimeMillis())),
				sdf.format(new Date(item.create_time)));
		if (dateTime < 0) {
			holder.time.setText(sdf.format(new Date(item.create_time)));
		}
		/**
		 * 超过一天
		 */
		else if (dateTime > 1000 * 60 * 60 * 24) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			holder.time.setText(sdf1.format(new Date(item.create_time)));
		}
		/**
		 * 大于1天
		 */
		else if (dateTime > 1000 * 60 * 60) {
			holder.time.setText((dateTime / 1000 / 60 / 60) + "小时前");
		} else if (dateTime > 1000 * 60) {
			holder.time.setText((dateTime / 1000 / 60) + "分钟前");
		} else {
			holder.time.setText((dateTime / 1000) + "秒前");
		}

		// 描述
		if (item.description.equals("")) {
			holder.msg.setVisibility(View.GONE);
		} else {
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.getExpressionString(context, item.description);
			holder.msg.setText(spannableString);
			post_text = item.description;
		}

		// 赞
		final TextView zan = (TextView) convertView.findViewById(R.id.zan);
		zan.setText(item.admire_count);
		if (item.isZan) {
			zan.setBackgroundResource(R.drawable.fabu_zan_sel);
		} else {
			zan.setBackgroundResource(R.drawable.fabu_zan);
		}
		zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				if (!item.isZan) {
					AbRequestParams params = new AbRequestParams();
					params.put("user_id", Config.getUser(context).uid);
					params.put("admire_id", item.user_id);
					params.put("img_id", item.img_id);
					ab.post(ServerMutualConfig.PostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									item.admire_count = (Integer
											.parseInt(item.admire_count) + 1)
											+ "";
									zan.setText(item.admire_count);
									item.isZan = true;
									zan.setBackgroundResource(R.drawable.fabu_zan_sel);
								}

								@Override
								public void onFinish() {
									super.onFinish();
									Config.dismissProgress();
								}

								@Override
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				} else {
					AbRequestParams params = new AbRequestParams();
					params.put("user_id", Config.getUser(context).uid);
					params.put("admire_id", item.user_id);
					params.put("img_id", item.img_id);
					// android.util.Log.e("fanfan",
					// ServerMutualConfig.CancelPostAdmireNew +
					// params.toString());
					ab.post(ServerMutualConfig.CancelPostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									item.admire_count = (Integer
											.parseInt(item.admire_count) - 1)
											+ "";
									item.isZan = false;
									zan.setBackgroundResource(R.drawable.fabu_zan);
									zan.setText(item.admire_count);
								}

								@Override
								public void onFinish() {
									super.onFinish();
									Config.dismissProgress();
								}

								@Override
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				}
			}
		});

		// 回复
		holder.review.setText(item.review_count);
		holder.review.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, PostReviewList.class);
				intent.putExtra("img_id", item.img_id);
				intent.putExtra("owner_id", item.user_id);
				intent.putExtra("ismy",
						item.user_id.equals(Config.getUser(context).uid));
				context.startActivityForResult(intent, 1);
			}
		});

		if (item.reviews.size() > 0) {
			LinearLayout reviews_root[] = new LinearLayout[4];
			reviews_root[0] = (LinearLayout) convertView
					.findViewById(R.id.review_1_root);
			reviews_root[1] = (LinearLayout) convertView
					.findViewById(R.id.review_2_root);
			reviews_root[2] = (LinearLayout) convertView
					.findViewById(R.id.review_3_root);
			reviews_root[3] = (LinearLayout) convertView
					.findViewById(R.id.review_4_root);
			reviews_root[0].setVisibility(View.GONE);
			reviews_root[1].setVisibility(View.GONE);
			reviews_root[2].setVisibility(View.GONE);
			reviews_root[3].setVisibility(View.GONE);
			if (item.reviews.size() >= 4) {
				for (int i = 0; i < item.reviews.size(); i++) {
					Review reviewitem = item.reviews.get(i);
					SpannableString spannableString = FaceConversionUtil
							.getInstace().getExpressionString(context,
									reviewitem.review);
					((TextView) (reviews_root[i].getChildAt(0)))
							.setText(reviewitem.name);
					((TextView) (reviews_root[i].getChildAt(1)))
							.setText(spannableString);
					reviews_root[i].setVisibility(View.VISIBLE);
				}
			} else if (item.reviews.size() < 4) {
				holder.check_reviewall.setVisibility(View.GONE);
				for (int i = 0; i < item.reviews.size(); i++) {
					Review reviewitem = item.reviews.get(i);
					SpannableString spannableString = FaceConversionUtil
							.getInstace().getExpressionString(context,
									reviewitem.review);
					((TextView) (reviews_root[i].getChildAt(0)))
							.setText(reviewitem.name);
					((TextView) (reviews_root[i].getChildAt(1)))
							.setText(spannableString);
					reviews_root[i].setVisibility(View.VISIBLE);
				}
			}

			holder.check_reviewall.setText("查看所有" + item.review_count + "评论");
			holder.check_reviewall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PostReviewList.class);
					intent.putExtra("img_id", item.img_id);
					intent.putExtra("ismy",
							item.user_id.equals(Config.getUser(context).uid));
					context.startActivity(intent);
				}
			});
		} else {
			RelativeLayout review_root = (RelativeLayout) convertView
					.findViewById(R.id.review_root);
			review_root.setVisibility(View.GONE);
		}

		return convertView;
	}

	private void addImg(ScrollView scrollview, LinearLayout imgLinear,
			final ArrayList<Image> imageList, final int position) {
		imgLinear.setOrientation(LinearLayout.VERTICAL);
		imgLinear.setGravity(Gravity.CENTER_HORIZONTAL);
		imgLinear.setPadding(0, 5, 0, 5);
		for (int i = 0; i < imageList.size(); i++) {
			final ImageView img = new ImageView(context);
			img.setId(i);
			img.setPadding(0, 5, 0, 5);
			MyApplication.getInstance().display(imageList.get(i).img_thumb,
					img, R.drawable.def_image);
			// 大图
			int newW = 0, newH = 0;
			if (imageList.size() > 0) {
				if (imageList.get(i).img_thumb_width == imageList.get(i).img_thumb_height) {
					newW = width;
					newH = width;
				} else if (Float.parseFloat(imageList.get(i).img_thumb_width) > Float
						.parseFloat(imageList.get(i).img_thumb_height)) {
					float h = 0;
					float sx = (float) ((float) width / (float) (Integer
							.parseInt(imageList.get(i).img_thumb_width)));
					h = (float) ((float) Float
							.parseFloat(imageList.get(i).img_thumb_height) * (float) sx);
					newW = width;
					newH = (int) h;
				} else {
					float sx = (float) (width * 0.72)
							/ (Float.parseFloat(imageList.get(i).img_thumb_width));
					newW = (int) ((float) (((Float
							.parseFloat(imageList.get(i).img_thumb_width)) * sx)));
					newH = (int) ((float) ((Float
							.parseFloat(imageList.get(i).img_thumb_height)) * sx));
				}
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						newW, newH);
				params.addRule(RelativeLayout.BELOW, R.id.head_root);
				img.setLayoutParams(params);
				img.setScaleType(ScaleType.FIT_XY);
				imgLinear.addView(img);
				post_img = imageList.get(0).img_thumb;
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								ChangePhotoSize.class);

						ArrayList<String> imgBig = new ArrayList<String>();
						ArrayList<String> imaHed = new ArrayList<String>();
						ArrayList<String> imaWid = new ArrayList<String>();
						for (int j = 0; j < imageList.size(); j++) {
							imgBig.add(imageList.get(j).img);
							imaHed.add(imageList.get(j).img_height);
							imaWid.add(imageList.get(j).img_width);
						}
						intent.putStringArrayListExtra("imgList", imgBig);
						intent.putStringArrayListExtra("imaWid", imaWid);
						intent.putStringArrayListExtra("imaHed", imaHed);
						intent.putExtra("listIndex", img.getId());
						context.startActivity(intent);
					}
				});
			}
		}
		scrollview.addView(imgLinear);
	}

	/* 单图显示 */
	private void getImage(String imagePath,
			final ArrayList<PostShowItem.Image> imgList, final ImageView view,
			final int positon, final View convertView) {
		view.setTag(imagePath);
		ImageDownloader mDownloader = null;
		if (mDownloader == null) {
			mDownloader = new ImageDownloader();
		}
		if (mDownloader != null) {
			mDownloader.imageDownload(imagePath, view,
					"/Download/cache_files/", context, new OnImageDownload() {
						@Override
						public void onDownloadSucc(Bitmap bitmap, String c_url,
								ImageView imageView) {
							ImageView imageView1 = (ImageView) convertView
									.findViewWithTag(c_url);
							if (imageView1 != null) {
								imageView1.setImageBitmap(bitmap);
								imageView1.setTag("");
							}

						}
					});
		}

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChangePhotoSize.class);
				ArrayList<String> list = new ArrayList<String>();
				list.add(imgList.get(0).img);
				intent.putExtra("imgList", list);
				context.startActivity(intent);
			}
		});
	}
	
	class ViewHolder {
		TextView postTitle;
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView time;
		TextView msg;
		TextView review;
		TextView zan;
		TextView check_reviewall;
		ImageView img_link;
		TextView msg_link;
		LinearLayout ly_link;
		ImageView rank;
		LinearLayout ly_rank;
	}
}


