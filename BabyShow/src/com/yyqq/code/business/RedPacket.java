package com.yyqq.code.business;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.model.RedPacketItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.ServerMutualConfig;

public class RedPacket extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_RedPacket";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private RelativeLayout general_ly;
	private TextView general_title;
	private LinearLayout ly_no;
	private TextView more_coupon_bt;
	private TextView v;

	ArrayList<RedPacketItem> redPacketList = new ArrayList<RedPacketItem>();
	
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
		context = this;
		setContentView(R.layout.general_left_list);
		init();
	}

	private void init() {
		findViewById(R.id.bus_title_hint_text).setVisibility(View.GONE);
		v = (TextView) findViewById(R.id.v);
		v.setVisibility(View.GONE);
		// 没数据的布局
		ly_no = (LinearLayout) findViewById(R.id.ly_no);
		more_coupon_bt = (TextView) findViewById(R.id.more_coupon_bt);
		more_coupon_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RedPacket.this.finish();
				MainTab.tabHost.setCurrentTab(0);
//				Intent intent = new Intent();
//				intent.setClass(context, BusinessList.class);
//				startActivity(intent);
			}
		});
		// 标题
		general_title = (TextView) findViewById(R.id.general_title);
		general_title.setText("秀秀红包仅限秀秀商家在线支付使用");
		// 返回
		general_ly = (RelativeLayout) findViewById(R.id.general_ly);
		general_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		});

		ab = AbHttpUtil.getInstance(context);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
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
		mPullDownView.setHideHeader();
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		// mPullDownView.setPadding(0, 0, 0, 0);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		android.util.Log.e(TAG,
				ServerMutualConfig.PacketList + "&" + params.toString());
		ab.get(ServerMutualConfig.PacketList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						redPacketList.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								RedPacketItem item = new RedPacketItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								redPacketList.add(item);
							}
							if (redPacketList.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								ly_no.setVisibility(View.GONE);
							}
							// mPullDownView.RefreshComplete();// 这个事线程安全的
							// 可看源代码
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
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
		if (redPacketList.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("post_create_time",
				redPacketList.get(redPacketList.size() - 1).post_create_time
						+ "");
		// android.util.Log.e(TAG,
		// ServerMutualConfig.PacketList + "&" + params.toString());
		ab.get(ServerMutualConfig.PacketList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								RedPacketItem item = new RedPacketItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								redPacketList.add(item);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	class ViewHolder {
		TextView packet_price; // 红包价格
		TextView packet_type; // 红包类型
		TextView packet_msg; // 红包描述
		TextView expiration; // 过期时间
		TextView ExpiryDate; // 有效期
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return redPacketList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return redPacketList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.red_packet_list_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			holder.packet_price = (TextView) convertView
					.findViewById(R.id.packet_price);
			holder.packet_type = (TextView) convertView
					.findViewById(R.id.packet_type);
			holder.packet_msg = (TextView) convertView
					.findViewById(R.id.packet_msg);
			holder.expiration = (TextView) convertView
					.findViewById(R.id.expiration);
			holder.ExpiryDate = (TextView) convertView
					.findViewById(R.id.ExpiryDate);
			convertView.setTag(holder);
			RedPacketItem item = redPacketList.get(index);

			if (!TextUtils.isEmpty(item.packet_price))
				holder.packet_price.setText(item.packet_price);
			if (!TextUtils.isEmpty(item.packet_type))
				holder.packet_type.setText(item.packet_type);
			if (!TextUtils.isEmpty(item.packet_msg))
				holder.packet_msg.setText(item.packet_msg);
			if (!TextUtils.isEmpty(item.expiration))
				holder.expiration.setText(item.expiration);
			if (!TextUtils.isEmpty(item.ExpiryDate))
				holder.ExpiryDate.setText(item.ExpiryDate);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				}
			});
			return convertView;
		}
	}

}
