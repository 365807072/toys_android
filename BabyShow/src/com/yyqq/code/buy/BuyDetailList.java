package com.yyqq.code.buy;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.model.BuyListItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BuyDetailList extends Activity implements OnPullDownListener {

	private final String TAG = "BuyDetailList";
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private Activity context;
	private ArrayList<BuyListItem> data = new ArrayList<BuyListItem>();
	private MyApplication app;
	private String buy_list;
	private String imgId = "";
	private TextView title;
	private String cate_name = "";
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.buy_detail_list);
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		app = (MyApplication) context.getApplication();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (adapter == null) {
			adapter = new MyAdapter();
		}
		listview.setAdapter(adapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		mPullDownView.setShowFooter();
		mPullDownView.setShowHeader();
		buy_list = getIntent().getStringExtra("buy_list");
		title = (TextView) findViewById(R.id.title);
		cate_name = getIntent().getStringExtra("cate_name");
		if (TextUtils.isEmpty(cate_name)) {
			if ("6".equals(buy_list)) {
				title.setText("孕妈");
			} else if ("5".equals(buy_list)) {
				title.setText("家居");
			} else if ("7".equals(buy_list)) {
				title.setText("海淘");
			} else if ("1".equals(buy_list)) {
				title.setText("今日推荐");
			}
		} else {
			title.setText(cate_name);
		}

		onRefresh();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("buy_list", buy_list);
//		Log.e("fanfan",
//				ServerMutualConfig.BuyDetailList + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.BuyDetailList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								BuyListItem item = new BuyListItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(item);
							}
							if (data.isEmpty()) {
								mPullDownView.setHideFooter();
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	@Override
	public void onMore() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("buy_list", buy_list);
		params.put("post_create_time",
				data.get(data.size() - 1).post_create_time + "");
//		android.util.Log.e("fanfan", ServerMutualConfig.BuyDetailList + "&" + params.toString());
		ab.get(ServerMutualConfig.BuyDetailList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								BuyListItem item = new BuyListItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(item);
							}
							if (data.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});

	}

	class ViewHolder {
		ImageView img;
		ImageView buy_biao;
		TextView msg;
		TextView current_price;
		TextView original_price;
		TextView is_postage;
		TextView reason;
		TextView business;
		TextView between_time;
		View view;
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
			final ViewHolder holder;
			convertView = inflater.inflate(R.layout.buy_listview_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.buy_img);
			holder.buy_biao = (ImageView) convertView
					.findViewById(R.id.buy_biao);
			holder.msg = (TextView) convertView.findViewById(R.id.description);
			holder.business = (TextView) convertView
					.findViewById(R.id.business);
			holder.current_price = (TextView) convertView
					.findViewById(R.id.current_price);
			holder.original_price = (TextView) convertView
					.findViewById(R.id.original_price);
			holder.original_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
			// Paint.UNDERLINE_TEXT_FLAG 下划线
			holder.is_postage = (TextView) convertView
					.findViewById(R.id.is_postage);
			holder.reason = (TextView) convertView.findViewById(R.id.reason);
			holder.between_time = (TextView) convertView
					.findViewById(R.id.between_time);
			View view;
			holder.view = convertView.findViewById(R.id.view);
			final BuyListItem item = data.get(position);
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);

					ArrayList<String> imgBig = new ArrayList<String>();
					ArrayList<String> imaHed = new ArrayList<String>();
					ArrayList<String> imaWid = new ArrayList<String>();
					for (int i = 0; i < item.ImageList.size(); i++) {
						imgBig.add(item.ImageList.get(i).img);
						imaHed.add(item.ImageList.get(i).img_height);
						imaWid.add(item.ImageList.get(i).img_width);
					}
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", imaWid);
					intent.putStringArrayListExtra("imaHed", imaHed);
					intent.putExtra("listIndex", position);
					intent.putExtra("isBuy", "isBuy");
					context.startActivity(intent);
				}
			});

			convertView.setTag(holder);

			// 图片
			String string = "";
			if (item.ImageList != null && item.ImageList.size() > 0) {
				string = item.ImageList.get(0).img_thumb;
			}
			app.display(string, holder.img, R.drawable.tutu);
			// 标 "  good_type": 2, 商品提示，1秒杀、2白菜价、3疯狂抢、4限时抢、5全网最低、6爆款
			if ("0".equals(item.good_type)) {
				holder.buy_biao.setVisibility(View.GONE);
			} else if ("1".equals(item.good_type)) {
				holder.buy_biao.setBackgroundResource(R.drawable.buy_miaosha);
			} else if ("2".equals(item.good_type)) {
				holder.buy_biao.setBackgroundResource(R.drawable.buy_baicaijia);
			} else if ("3".equals(item.good_type)) {
				holder.buy_biao
						.setBackgroundResource(R.drawable.buy_fengkuangqiang);
			} else if ("4".equals(item.good_type)) {
				holder.buy_biao.setBackgroundResource(R.drawable.buy_xianshi);
			} else if ("5".equals(item.good_type)) {
				holder.buy_biao.setBackgroundResource(R.drawable.buy_zuidi);
			} else {
				holder.buy_biao.setBackgroundResource(R.drawable.buy_baokuan);
			}
			// 商城
			holder.business.setText(item.business);
			// 时间
			if (TextUtils.isEmpty(item.between_time)
					|| " ".equals(item.between_time)) {
				holder.between_time.setVisibility(View.GONE);
			} else {
				holder.between_time.setText("还剩" + item.between_time);
			}
			// 描述
			holder.msg.setText(item.description);
			// 现价
			holder.current_price.setText("¥" + item.current_price);
			// 包邮
			if ("0".equals(item.is_postage)) {
				holder.is_postage.setText("元");
			} else {
				holder.is_postage.setText("包邮");
			}
			// 原价
			if (TextUtils.isEmpty(item.original_price)) {
				holder.original_price.setVisibility(View.GONE);
			} else {
				holder.original_price
						.setText("原价 " + item.original_price + "元");
			}
			// 动态
			if (TextUtils.isEmpty(item.reason)) {
				holder.reason.setVisibility(View.GONE);
			} else {
				holder.reason.setText("最新：" + item.reason);
			}
			// listview点击事件
			holder.view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					imgId = item.img_id;
					String strUri;
					if (!TextUtils.isEmpty(item.post_url)) {
						strUri = item.post_url;
						if (!strUri.startsWith("http")) {
							strUri = "http://" + strUri;
						}

						Intent intent = new Intent(context,
								WebViewActivity.class);
						intent.putExtra("webviewurl", strUri);
						intent.putExtra("msg", item.description);
						intent.putExtra("img", item.ImageList.get(0).img_thumb);
						intent.putExtra("webBuy", "webBuy");
						startActivity(intent);
						saveMarkBuyUrl();
					} else {
					}

				}

			});
			return convertView;
		}
	}

	public void saveMarkBuyUrl() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", imgId);
		// android.util.Log.e("fanfan", params.toString());
		ab.post(ServerMutualConfig.MarkBuyUrl, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								// Toast.makeText(context, "成功", 0).show();
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
					}
				});
	}

}
