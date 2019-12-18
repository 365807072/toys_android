package com.yyqq.commen.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.ReviewList;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.PostBarItem;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class MyCollection extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "MyCollection";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	ArrayList<PostBarItem> dataPostList = new ArrayList<PostBarItem>();
	private MyApplication app;
	private RelativeLayout none;

	public void onResume() {
		super.onResume();
		onRefresh();
	}

	public void onPause() {
		super.onPause();
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.my_collection);
		ab = AbHttpUtil.getInstance(context);
		app = (MyApplication) this.getApplication();
		none = (RelativeLayout) findViewById(R.id.none);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		listview.setDivider(null);
		listview.setDividerHeight(5);

		if (adapter == null) {
			adapter = new MyAdapter();
		}

		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		ab.get(ServerMutualConfig.ListingSaveList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							dataPostList.clear();
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								PostBarItem item = new PostBarItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								dataPostList.add(item);
							}
							if (dataPostList.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								none.setVisibility(View.VISIBLE);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	@Override
	public void onMore() {
		if (dataPostList.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("create_time",
				dataPostList.get(dataPostList.size() - 1).create_time + "");
		ab.get(ServerMutualConfig.ListingSaveList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								PostBarItem item = new PostBarItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								dataPostList.add(item);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	class ViewHolder {
		TextView postTitle;
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
		ImageView rank;
		LinearLayout ly_rank;
	}

	private String img_id;
	private String user_id;

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataPostList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataPostList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.post_bar_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			final PostBarItem item = dataPostList.get(arg0);
			holder.postTitle = (TextView) convertView
					.findViewById(R.id.postTitle);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.rank = (ImageView) convertView.findViewById(R.id.rank);
			holder.ly_rank = (LinearLayout) convertView
					.findViewById(R.id.ly_rank);
			holder.review = (ImageView) convertView.findViewById(R.id.review);
			holder.check_reviewall = (TextView) convertView
					.findViewById(R.id.review_num);

			// 标题
			holder.postTitle.setText(item.img_title);
			// 图片
			holder.img = (ImageView) convertView.findViewById(R.id.post_img);
			String string = "";
			if (item.ImageList != null && item.ImageList.size() > 0) {
				string = item.ImageList.get(0).img_thumb;
			}
			app.display(string, holder.img, R.drawable.tutu);

			// 等级
			if (!TextUtils.isEmpty(item.level_img)) {
				MyApplication.getInstance().display(item.level_img,
						holder.rank, arg0);
				holder.ly_rank.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(context, Rank.class);
						startActivity(intent);
					}
				});
			}
			// 描述
			if (item.description.equals("")) {
				holder.msg.setVisibility(View.GONE);
			} else {
				holder.msg.setText(item.description);
			}

			// 时间
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
			 * 一天以内
			 */
			else if (dateTime > 1000 * 60 * 60) {
				holder.time.setText((dateTime / 1000 / 60 / 60) + "小时前");
			} else if (dateTime > 1000 * 60) {
				holder.time.setText((dateTime / 1000 / 60) + "分钟前");
			} else {
				holder.time.setText((dateTime / 1000) + "秒前");
			}

			// 用戶名
			holder.name.setText(item.user_name);
			holder.check_reviewall.setText(item.review_count);
			img_id = item.img_id;
			user_id = item.user_id;

			holder.review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, ReviewList.class);
					intent.putExtra("img_id", item.img_id);
					intent.putExtra("ismy",
							item.user_id.equals(Config.getUser(context).uid));

					startActivity(intent);

				}
			});
			holder.check_reviewall.setText(item.review_count);

			getZan(item, convertView);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(item.video_url.isEmpty()){
						Intent intent = new Intent(context, MainItemDetialActivity.class);
						intent.putExtra("user_id", item.user_id);
						intent.putExtra("img_id", item.img_id);
						intent.putExtra("is_save", item.is_save);
						intent.putExtra("MyCollection", "MyCollection");
						context.startActivity(intent);
					}else{
						PostBarTypeItem bean = new PostBarTypeItem();
						bean.setImg_id(item.img_id);
						bean.setVideo_url(item.video_url);
						bean.setImg(item.ImageList.get(0).img_thumb);
						bean.setImg_thumb_width(item.ImageList.get(0).img_thumb_width);
						bean.setImg_thumb_height(item.ImageList.get(0).img_thumb_height);

						Intent intent = new Intent(v.getContext(), VideoDetialActivity.class);
						Bundle bun = new Bundle();
						bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, bean);
						intent.putExtras(bun);
						context.startActivity(intent);
					}
				}
			});
			return convertView;
		}

		private void getZan(final PostBarItem item, View convertView) {
			final TextView zanNum = (TextView) convertView
					.findViewById(R.id.zan_num);
			final ImageView zan = (ImageView) convertView
					.findViewById(R.id.zan);

			if ("1".equals(item.is_admire)) { // 赞了=1，不赞=0
				zan.setBackgroundResource(R.drawable.buy_zan_selection);
			} else {
				zan.setBackgroundResource(R.drawable.buy_zan);
			}
			zanNum.setText(item.admire_count + "");
		}
	}

	class GridAdapter extends BaseAdapter {
		MyGridView myGridView;
		PostBarItem collectionItem;
		private ImageDownloader mDownloader;

		public GridAdapter(PostBarItem collectionItem, MyGridView myGridView) {
			this.collectionItem = collectionItem;
			this.myGridView = myGridView;
		}

		@Override
		public int getCount() {
			return collectionItem.ImageList.size() < 4 ? collectionItem.ImageList
					.size() : 3;
		}

		@Override
		public Object getItem(int position) {
			return collectionItem.ImageList.get(position);
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
			holder.img.setTag(collectionItem.ImageList.get(position).img_thumb);
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null) {
				mDownloader.imageDownload(
						collectionItem.ImageList.get(position).img_thumb,
						holder.img, "/Download/cache_files", context,
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
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, MainItemDetialActivity.class);
					intent.putExtra("user_id", collectionItem.user_id);
					intent.putExtra("img_id", collectionItem.img_id);
					intent.putExtra("is_save", collectionItem.is_save);
					intent.putExtra("MyCollection", "MyCollection");
					context.startActivity(intent);
				}
			});
			return convertView;
		}

	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
