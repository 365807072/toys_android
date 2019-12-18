package com.yyqq.code.business;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.model.OrderItem;
import com.yyqq.commen.model.RedPacketItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class UserOderList extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_UserOderList";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private int width;
	private MyApplication app;
	private RelativeLayout general_ly;
	private TextView general_title;
	private LinearLayout ly_no;
	private ImageView more_coupon_bt;
	private TextView v;
	private ImageView bt_redBack;
	private RedPacketItem item;
	private String checkacket;
	private String orderId;
	private PopupPacket pop;
	private String showPacket = "";

	private String mainTitle = "";
	private String share_title, share_img, share_link;

	ArrayList<OrderItem> dataOrderList = new ArrayList<OrderItem>();
	private String status = "0"; // 订单状态
	private Button order_type_01;
	private Button order_type_02;
	private Button order_type_03;
	private Button order_type_04;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(context, MainTab.class);
			intent.putExtra("tabid", 3);
			startActivity(intent);
			context.finish();
		}
		return false;
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.order_main_list);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					UserOderList.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							checkPack();
							onRefresh();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void init() {
		
//		expandtab_view = (ExpandTabView) findViewById(R.id.expandtab_view);
		order_type_01 = (Button) findViewById(R.id.order_type_01);
		order_type_02 = (Button) findViewById(R.id.order_type_02);
		order_type_03 = (Button) findViewById(R.id.order_type_03);
		order_type_04 = (Button) findViewById(R.id.order_type_04);
		
		order_type_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "0";
				OrderTypeChange();
			}
		});
		
		order_type_03.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "1";
				OrderTypeChange();
			}
		});
		
		order_type_02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "2";
				OrderTypeChange();
			}
		});
		
		order_type_04.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "3";
				OrderTypeChange();
			}
		});
		
		
		showPacket = getIntent().getStringExtra("showPacket");
		bt_redBack = (ImageView) findViewById(R.id.bt_redBack);
		v = (TextView) findViewById(R.id.v);
		v.setVisibility(View.VISIBLE);
		// 没数据的布局
		ly_no = (LinearLayout) findViewById(R.id.ly_no);
		more_coupon_bt = (ImageView) findViewById(R.id.more_coupon_bt);
		more_coupon_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, MainTab.class);
				intent.putExtra("tabid", 0);
				startActivity(intent);
				UserOderList.this.finish();
			}
		});
		// 标题
		general_title = (TextView) findViewById(R.id.general_title);
		general_title.setText("我的订单");
		// 返回
		general_ly = (RelativeLayout) findViewById(R.id.general_ly);
		general_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, MainTab.class);
				intent.putExtra("tabid", 4);
				startActivity(intent);
				context.finish();
			}
		});

		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);

		app = (MyApplication) this.getApplication();
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
		Config.showProgressDialog(context, false, null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					UserOderList.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							onRefresh();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 订单类型改变
	 * */
	private void OrderTypeChange(){
		
		onRefresh(); // 刷新数据
		
		switch (Integer.parseInt(status)) {
		case 0:
			order_type_01.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			break;
		case 1:
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			break;
		case 2:
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			break;
		case 3:
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.order_type_color));
			break;

		default:
			order_type_01.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			break;
		}
	}
	
	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("status", status);
		params.put("post_create_time", "0");
		ab.get(ServerMutualConfig.UserOrderList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						dataOrderList.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								OrderItem item = new OrderItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								dataOrderList.add(item);
							}
							if (dataOrderList.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								ly_no.setVisibility(View.VISIBLE);
							}else{
								mPullDownView.setVisibility(View.VISIBLE);
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
						Config.dismissProgress();
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
		if (dataOrderList.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("status", status);
		params.put("post_create_time", dataOrderList.get(dataOrderList.size() - 1).post_create_time + "");
		ab.get(ServerMutualConfig.UserOrderList + "&" + params.toString(),
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
								OrderItem item = new OrderItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								dataOrderList.add(item);
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
		RelativeLayout ly_business;
		TextView store_name; // 店铺名称
		TextView time_state; // 过期时间
		TextView order_num; // 订单号
		TextView state; // 消费状态
		TextView order_code; // 验证码
		TextView order_combo; // 套餐
		TextView price; // 价格
		TextView tv_code; // 提示
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataOrderList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataOrderList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.user_oder_list_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			holder.ly_business = (RelativeLayout) convertView
					.findViewById(R.id.ly_business);
			holder.store_name = (TextView) convertView
					.findViewById(R.id.store_name);
			holder.time_state = (TextView) convertView
					.findViewById(R.id.user_time);
			holder.order_num = (TextView) convertView
					.findViewById(R.id.oder_num);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.order_code = (TextView) convertView
					.findViewById(R.id.oder_code);
			holder.order_combo = (TextView) convertView
					.findViewById(R.id.oder_combo);
			holder.price = (TextView) convertView.findViewById(R.id.oder_price);
			holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
			convertView.setTag(holder);
			final OrderItem item = dataOrderList.get(index);
			if ("1".equals(item.order_role)) {
				holder.tv_code.setText("一经对方同意，即刻开通同步权限");
			} else {
				holder.tv_code.setText("验证码：");
			}
			
			if (!TextUtils.isEmpty(item.business_title))
				holder.store_name.setText(item.business_title + "  >");

			if (!TextUtils.isEmpty(item.business_time))
				holder.time_state.setText(item.business_time + " 过期");

			if (!TextUtils.isEmpty(item.order_num))
				holder.order_num.setText(item.order_num);

			if (!TextUtils.isEmpty(item.status)) {
				if (!TextUtils.isEmpty(item.status)) {
					if ("1".equals(item.status)) {
						holder.state.setText("未消费");
					} else if ("2".equals(item.status)) {
						holder.state.setText("未支付");
					} else if ("3".equals(item.status)) {
						holder.state.setText("已完成");
						holder.tv_code.setText("验证时间：");
					} else if ("4".equals(item.status)) {
						holder.state.setText("退款中");
					} else if ("5".equals(item.status)) {
						holder.state.setText("已退款");
					} else {
						holder.state.setText("待上门付款");
					}
				}
			}

			if ("1".equals(item.order_role)) {
				holder.order_code.setVisibility(View.GONE);
			} else {
				holder.order_code.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(item.verification))
					holder.order_code.setText(item.verification);
			}

			if (!TextUtils.isEmpty(item.price))
				holder.price.setText("¥" + item.price);

			if ("1".equals(item.order_role)) {
				holder.order_combo.setVisibility(View.GONE);
			} else {
				holder.order_combo.setVisibility(View.VISIBLE);
				holder.order_combo.setText(item.packageName);
//				if (!TextUtils.isEmpty(item.packages)) {
//					if ("1".equals(item.packages)) {
//						holder.order_combo.setText("套餐一");
//					} else if ("2".equals(item.packages)) {
//						holder.order_combo.setText("套餐二");
//					} else if ("3".equals(item.packages)) {
//						holder.order_combo.setText("套餐三");
//					} else {
//						holder.order_combo.setText("套餐四");
//					}
//				}
			}

			holder.ly_business.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					if ("1".equals(item.order_role)) {
//
//					} else {
//						Intent intent = new Intent();
//						intent.setClass(context, BusinessDetailActivity.class);
//						intent.putExtra("business_id", item.business_id);
//						startActivity(intent);
//					}
					
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("拨打 " + item.business_tel);
					builder.setPositiveButton("拨号",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(Intent.ACTION_CALL);
									intent.setData(Uri.parse("tel:" + item.business_tel));
									startActivity(intent);
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

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("1".equals(item.order_role)) {

					} else {
						Intent intent = new Intent();
						intent.setClass(context, OderDetail.class);
						intent.putExtra("order_id", item.order_id);
						startActivity(intent);
					}
				}
			});

			return convertView;
		}
	}

	private void showShare() {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		share_title = item.packet_description;
		share_img = "https://api.meimei.yihaoss.top/static/defaultimg/smallpacket.png";
		share_link = "http://www.meimei.yihaoss.top/packet.html?user_id="
				+ Config.getUser(context).uid + "&order_id=" + item.order_id;
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setTitle(item.mainTitle);
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setImageUrl(share_img);
					paramsToShare.setUrl(share_link);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(share_title + share_link);
					paramsToShare.setImageUrl(share_img);
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(item.mainTitle); // title微信、QQ空间
					paramsToShare.setTitleUrl(share_link);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_title);
					paramsToShare.setImageUrl(share_img);
					paramsToShare.setSiteUrl(share_link); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle(item.mainTitle);
					paramsToShare.setTitleUrl(share_link);
					paramsToShare.setText(share_title);
					paramsToShare.setImageUrl(share_img);
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setTitle(item.mainTitle);
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setImageUrl(share_img);
					paramsToShare.setUrl(share_link);
				}

			}
		});
		// 启动分享GUI
		oks.show(context);
	}

	public void checkPack() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		ab.get(ServerMutualConfig.CheckPacket + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							item = new RedPacketItem();
							item.fromJson(json.getJSONObject("data"));
							if ("1".equals(item.check_packet)) {
								// 支付完跳转弹出分享红包提示
								if (!TextUtils.isEmpty(showPacket)) {
									pop = new PopupPacket(context, bt_redBack);
								}
								bt_redBack.setVisibility(View.VISIBLE);
								bt_redBack.setOnClickListener(packetClick);
							} else {
								bt_redBack.setVisibility(View.GONE);
							}
							showPacket = "";
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	public OnClickListener packetClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showShare();
			ChangeOrderShareState();
		}
	};
	
	/**
	 * 红包分享以后交互服务器
	 */
	public void ChangeOrderShareState() {
		AbRequestParams params = new AbRequestParams();
		params.put("order_id", item.order_id);
		params.put("share_state", "1");
		ab.get(ServerMutualConfig.OrderShareState + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject json;
						try {
							json = new JSONObject(content);
							if (json.getBoolean("success")) {
//								Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
							} 
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					}

					@Override
					public void onFinish() {
						super.onFinish();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	/**
	 * 
	 * @author fanying 红包提示
	 */
	public class PopupPacket extends PopupWindow {
		public PopupPacket(Context mContext, View parent) {
			Button go_share, cancle_share;
			View view = View.inflate(mContext, R.layout.red_packet_pop, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_1));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			go_share = (Button) view.findViewById(R.id.go_share);
			cancle_share = (Button) view.findViewById(R.id.cancle_share);
			/**
			 * 拍摄一张
			 */
			go_share.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					showShare();
				}
			});

			/**
			 * 取消
			 */
			cancle_share.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

}