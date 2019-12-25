package com.yyqq.code.photo;

import java.io.File;
import java.util.ArrayList;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import com.ab.util.AbFileUtil;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.HackyViewPager;
import com.yyqq.commen.view.PhotoView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ChangePhotoSize extends Activity {

	public String TAG = "ChangePhotoSize";
	private ViewPager mViewPager;
	public Activity context;
	int index;
	public ArrayList<String> data = null;
	public ArrayList<String> img_downList = null;
	private String isShare;
	private Button savePhoto, savePhone, cancle;
	private String filename = "", downurl = "";
	private String down_filename, down_downurl;
	private String thum_filename, thum_downdownurl;
	private String img_url = "";
	private FinalHttp fh;

	public ArrayList<String> ImaWid = null;
	public ArrayList<String> ImaHed = null;

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mViewPager = new HackyViewPager(this);
		setContentView(mViewPager);
		context = this;
		index = getIntent().getIntExtra("listIndex", 0);
		data = getIntent().getStringArrayListExtra("imgList");
		ImaWid = getIntent().getStringArrayListExtra("imaWid");
		ImaHed = getIntent().getStringArrayListExtra("imaHed");
		isShare = getIntent().getStringExtra("isShare");
		if (data == null) {
			data = new ArrayList<String>();
			data.add(getIntent().getStringExtra("img"));
		}
		img_downList = getIntent().getStringArrayListExtra("img_downList");
		mViewPager.setAdapter(new Myadapter(ImaWid, ImaHed));
		mViewPager.setCurrentItem(index);
		if (getIntent().getBooleanExtra("aotuplay", false)) {
			toplay();
		}
	}

	public class Myadapter extends PagerAdapter {

		private ArrayList<String> imaWid = null;
		private ArrayList<String> imaHed = null;

		public Myadapter(ArrayList<String> imaWid, ArrayList<String> imaHed) {
			this.imaWid = imaWid;
			this.imaHed = imaHed;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			View view = context.getLayoutInflater().inflate(
					R.layout.photo_changesize, null);
			final PhotoView photoView = (PhotoView) view.findViewById(R.id.img);
			ImageView play = (ImageView) view.findViewById(R.id.play);
			if ("isBuy".equals(getIntent().getStringExtra("isBuy"))) {
				play.setVisibility(View.GONE);
			} else {
				play.setVisibility(View.VISIBLE);
			}
			play.setOnClickListener(playClick);
			if (data.size() == 1) {
				if (data.get(position).startsWith("http:")) {
					MyApplication.getInstance().display(data.get(position),
							photoView, R.drawable.def_image);
				} else {

					File file = new File(data.get(position));
					Bitmap image = AbFileUtil.getBitmapFromSD(file);
					photoView.setImageBitmap(image);
				}

				play.setVisibility(View.GONE);
			} else {
				MyApplication.getInstance().display(data.get(position),
						photoView, R.drawable.def_image);
			}

			if (img_downList != null) {
				photoView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {

						thum_filename = data.get(position).substring(
								data.get(position).lastIndexOf("/") + 1,
								data.get(position).length());
						thum_downdownurl = data.get(position);
						down_filename = img_downList.get(position)
								.substring(
										img_downList.get(position).lastIndexOf(
												"/") + 1,
										img_downList.get(position).length());
						down_downurl = img_downList.get(position);
						new PopupWindows(context, photoView);
						return false;
					}
				});
			}
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	public OnClickListener playClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			toplay();
		}

	};

	private void toplay() {
		Intent intent = new Intent();
		intent.setClass(context, AnimPlayer.class);
		intent.putStringArrayListExtra("imgList", data);
		intent.putStringArrayListExtra("imaWid", ImaWid);
		intent.putStringArrayListExtra("imaHed", ImaHed);
		startActivity(intent);
	}

	class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.download_popupwindows,
					null);
			savePhoto = (Button) view.findViewById(R.id.savePhoto);
			savePhone = (Button) view.findViewById(R.id.savePhone);
			cancle = (Button) view.findViewById(R.id.can);
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			update();
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);

			savePhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					AlertDialog.Builder dialog = new Builder(context);
					dialog.setTitle("保存成功");
					if (getIntent().getBooleanExtra("friend", false)) {
						if ("1".equals(isShare)) {
							dialog.setMessage("请到相册-其它-下载查看");
							img_url = down_downurl;
						} else {
							dialog.setMessage("请到相册-其它-下载查看  "
									+ "   这里下载的是缩略图,要下载高清图片,请请求用户分享,到共享相册里面去下载");
							img_url = thum_downdownurl;
						}
					} else {
						dialog.setMessage("请到相册-其它-下载查看");
						img_url = down_downurl;
					}
					dialog.setNegativeButton("好的",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							});
					AjaxParams params = new AjaxParams();
					params.put("user_id", Config.getUser(context).uid);
					params.put("img_url", img_url);

					fh = new FinalHttp();
					fh.post(ServerMutualConfig.DownloadImg, params,
							new AjaxCallBack<String>() {

								@Override
								public void onFailure(Throwable t, String strMsg) {
									Config.dismissProgress();
									super.onFailure(t, strMsg);
								}

								@Override
								public void onSuccess(String t) {
									Config.dismissProgress();
									super.onSuccess(t);

								}

							});
					dialog.create().show();
				}
			});

			// 保存到手机
			savePhone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					AlertDialog.Builder dialog = new Builder(context);
					dialog.setTitle("下载照片");
					if (getIntent().getBooleanExtra("friend", false)) {
						if ("1".equals(isShare)) {
							dialog.setMessage("确定要下载该照片吗?");
						} else {
							dialog.setMessage("这里下载的是缩略图,要下载高清图片,请请求用户分享,到共享相册里面去下载");
						}
					} else {
						dialog.setMessage("确定要下载该照片吗?");
					}
					dialog.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Config.showProgressDialog(context, true,
											null);
									final String baby_name = getIntent()
											.getStringExtra("baby_name") + "/";
									File dir = new File(Config.DownFile
											+ baby_name);
									if (!dir.exists()) {
										dir.mkdirs();
									}
									FinalHttp fh = new FinalHttp();

									if (getIntent().getBooleanExtra("friend",
											false)) {
										if ("1".equals(isShare)) {
											filename = thum_filename;
											downurl = thum_downdownurl;
										} else {
											filename = down_filename;
											downurl = down_downurl;
										}
									} else {
										filename = down_filename;
										downurl = down_downurl;
									}
									fh.download(downurl, Config.DownFile
											+ baby_name + filename,
											new AjaxCallBack<File>() {
												@Override
												public void onFailure(
														Throwable t,
														String strMsg) {
													super.onFailure(t, strMsg);
													Config.dismissProgress();
													Config.showFiledToast(context);
												}

												@Override
												public void onSuccess(File t) {
													super.onSuccess(t);
													Config.dismissProgress();
													Toast.makeText(
															context,
															"图片已下载到"
																	+ Config.DownFile
																	+ baby_name
																	+ "文件夹中",
															Toast.LENGTH_LONG)
															.show();
												}
											});
								}
							});
					dialog.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					dialog.create().show();
				}

			});

			cancle.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

}