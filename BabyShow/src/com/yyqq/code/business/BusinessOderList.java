package com.yyqq.code.business;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.adapter.OrderDetailedOptionAdapter;
import com.yyqq.commen.model.OrderDetailedOptionBean;
import com.yyqq.commen.model.OrderItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class BusinessOderList extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_BusinessOderList";
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
//	private TextView more_coupon_tv;
	private TextView v;
	/** 头部view **/
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private EditText code;
	private Button bt_verify;

	ArrayList<OrderItem> dataOrderList = new ArrayList<OrderItem>();
	ArrayList<OrderDetailedOptionBean> optionList = new ArrayList<OrderDetailedOptionBean>();
	
	private String status = "0"; // 订单状态
	private String oldstatus = "0"; // 订单状态
	private String search_id = "";
	private Button order_type_01;
	private Button order_type_02;
	private Button order_type_03;
	private Button order_type_04;
	private Button expandtab_view;
	private LinearLayout order_detailed;
	private GridView order_detail_option_list;
	private TextView order_detailde_monery;
	private TextView order_detailde_number;
	private LinearLayout order_info_hint;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(order_detailed.isShown()){
				status = oldstatus;
				OrderTypeChange();
				order_detailed.setVisibility(View.GONE);
			}else{
				BusinessOderList.this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
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
		setContentView(R.layout.order_main_list);
		init();

	}

	private void init() {
		
		order_info_hint = (LinearLayout) findViewById(R.id.order_info_hint);
		order_detail_option_list = (GridView) findViewById(R.id.order_detail_option_list);
		order_detailde_monery = (TextView) findViewById(R.id.order_detailde_monery);
		order_detailde_number = (TextView) findViewById(R.id.order_detailde_number);
		order_detailed = (LinearLayout) findViewById(R.id.order_detailed);
		expandtab_view = (Button) findViewById(R.id.expandtab_view);
		order_type_01 = (Button) findViewById(R.id.order_type_01);
		order_type_02 = (Button) findViewById(R.id.order_type_02);
		order_type_03 = (Button) findViewById(R.id.order_type_03);
		order_type_04 = (Button) findViewById(R.id.order_type_04);
		
		expandtab_view.setVisibility(View.VISIBLE);
		expandtab_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				oldstatus = status;
				status = "4";
				OrderTypeChange();
				showDetailedOption();
			}
		});
		
		order_type_01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "0";
				search_id = "";
				OrderTypeChange();
			}
		});
		
		order_type_03.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "1";
				search_id = "";
				OrderTypeChange();
			}
		});
		
		order_type_02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "2";
				search_id = "";
				OrderTypeChange();
			}
		});
		
		order_type_04.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = "3";
				search_id = "";
				OrderTypeChange();
			}
		});
		
		order_detailed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				status = oldstatus;
				OrderTypeChange();
				order_detailed.setVisibility(View.GONE);
			}
		});
		
		v = (TextView) findViewById(R.id.v);
		v.setVisibility(View.VISIBLE);
		// 没数据的布局
		ly_no = (LinearLayout) findViewById(R.id.ly_no);
//		more_coupon_tv = (TextView) findViewById(R.id.more_coupon_tv);
//		more_coupon_tv.setVisibility(View.GONE);
		more_coupon_bt = (ImageView) findViewById(R.id.more_coupon_bt);
//		more_coupon_bt.setVisibility(View.GONE);
		more_coupon_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BusinessOderList.this.finish();
				MainTab.tabHost.setCurrentTab(0);
//				Intent intent = new Intent(BusinessOderList.this, MainTab.class);
//				intent.putExtra("tabid", 1);
//				startActivity(intent);
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
				if(order_detailed.isShown()){
					status = oldstatus;
					OrderTypeChange();
					order_detailed.setVisibility(View.GONE);
				}else{
					BusinessOderList.this.finish();
				}
			}
		});

		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		inflater = LayoutInflater.from(context);
		/** 头部的view **/
		initHeadbar();

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
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		// mPullDownView.setPadding(0, 0, 0, 0);
		Config.showProgressDialog(context, false, null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					BusinessOderList.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							onRefresh();
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	/**
	 * 订单类型改变
	 * */
	private void OrderTypeChange(){
		
		order_info_hint.setVisibility(View.GONE);
		order_detailed.setVisibility(View.GONE);
		
		switch (Integer.parseInt(status)) {
		case 0:
			onRefresh(); // 刷新数据
			order_type_01.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			expandtab_view.setTextColor(getResources().getColor(R.color.black));
			break;
		case 1:
			onRefresh(); // 刷新数据
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			expandtab_view.setTextColor(getResources().getColor(R.color.black));
			break;
		case 2:
			onRefresh(); // 刷新数据
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			expandtab_view.setTextColor(getResources().getColor(R.color.black));
			break;
		case 3:
			onRefresh(); // 刷新数据
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.order_type_color));
			expandtab_view.setTextColor(getResources().getColor(R.color.black));
			break;
		case 4:
			order_type_01.setTextColor(getResources().getColor(R.color.black));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			expandtab_view.setTextColor(getResources().getColor(R.color.order_type_color));
			break;
		default:
			onRefresh(); // 刷新数据
			order_type_01.setTextColor(getResources().getColor(R.color.order_type_color));
			order_type_02.setTextColor(getResources().getColor(R.color.black));
			order_type_03.setTextColor(getResources().getColor(R.color.black));
			order_type_04.setTextColor(getResources().getColor(R.color.black));
			break;
		}
	}
	
	
	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.business_list_head, null);
		code = (EditText) mRelativeLayout.findViewById(R.id.code);
		bt_verify = (Button) mRelativeLayout.findViewById(R.id.bt_verify);

		bt_verify.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				if (code.getText().toString().trim().isEmpty()) {
					Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (code.getText().toString().trim().length() != 9) {
					Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				AbRequestParams params = new AbRequestParams();
				params.put("login_user_id", Config.getUser(context).uid);
				params.put("verification", code.getText().toString().trim());
				Config.showProgressDialog(context, false, null);
				// ab.get(ServerMutualConfig.WebSearchVerification + "&" +
				// params.toString() ,
				ab.get(ServerMutualConfig.searchVerification + "&"
						+ params.toString(),
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								Config.dismissProgress();

								try {
									JSONObject json = new JSONObject(content);
									if (!(boolean) json.getBoolean("success")) {
										Toast.makeText(context,
												json.getString("reMsg"),
												Toast.LENGTH_SHORT).show();
										return;
									}

									JSONObject json_data = json
											.getJSONObject("data");
									final AbRequestParams params = new AbRequestParams();
									params.put("login_user_id",
											Config.getUser(context).uid);
									params.put("order_id",
											json_data.getString("order_id"));
									params.put("verification", code.getText()
											.toString().trim());
									AlertDialog.Builder builder = new Builder(
											context);
									final TextView tv = new TextView(context);
									builder.setTitle("请确认如下信息：");
									tv.setText("   订单号："
											+ json_data.getString("order_num")
											+ "\n"
											+ "   内容："
											+ json_data
													.getString("business_package")
											+ "\n" + "   价格：￥"
											+ json_data.getString("price"));
									tv.setTextSize(20);
									tv.setTextColor(Color.WHITE);
									builder.setNegativeButton(
											"确认",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													arg0.dismiss();
													AddNewOrder(params);
												}
											});
									builder.setNeutralButton(
											"取消",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
									builder.setView(tv);
									builder.create().show();
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
							public void onFailure(int statusCode,
									String content, Throwable error) {
								super.onFailure(statusCode, content, error);
								Config.dismissProgress();
							}

							@Override
							public void sendSuccessMessage(int statusCode,
									String content) {
								super.sendSuccessMessage(statusCode, content);
								Config.dismissProgress();
							}

						});
			}
		});
	}

	@Override
	public void onRefresh() {
		if (null != code) {
			code.setText("");
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("status", status);
		params.put("post_create_time", "0");
		params.put("search_id", search_id);
		ab.get(ServerMutualConfig.BusinessOrderList + "&" + params.toString(),
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
							mPullDownView.RefreshComplete();// 这个事线程安全的
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
		
		ab.get(ServerMutualConfig.BusinessOrderDETAIL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						optionList.clear();
						try {
							JSONObject json = new JSONObject(content);
							OrderDetailedOptionBean bean;
							for (int i = 0; i < json.getJSONArray("data").length(); i++) {
								bean = new OrderDetailedOptionBean();
								bean.setSearch_time(json.getJSONArray("data").getJSONObject(i).getString("search_time"));
								bean.setSearch_id(json.getJSONArray("data").getJSONObject(i).getString("search_id"));
								bean.setMonery(json.getJSONArray("data").getJSONObject(i).getString("order_total_price"));
								bean.setOrder_num(json.getJSONArray("data").getJSONObject(i).getString("order_count"));
								optionList.add(bean);
							}
							
							if(optionList.size() != 0){
								OrderDetailedOptionAdapter adapter = new OrderDetailedOptionAdapter(optionList, BusinessOderList.this);
								order_detail_option_list.setAdapter(adapter);
								
								order_detail_option_list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
										search_id = optionList.get(position).getSearch_id();
										status = "4";
										onRefresh();
										OrderTypeChange();
										order_info_hint.setVisibility(View.VISIBLE);
										order_detailed.setVisibility(View.GONE);
										order_detailde_number.setText("订单总量：" + optionList.get(position).getOrder_num());
										order_detailde_monery.setText("总金额：￥" + optionList.get(position).getMonery());
									}
								});
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

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
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
		params.put("search_id", search_id);
		
		ab.get(ServerMutualConfig.BusinessOrderList + "&" + params.toString(),
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
			convertView = inflater.inflate(R.layout.business_oder_list_item,
					null);

			ViewHolder holder = null;
			holder = new ViewHolder();
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

			if (!TextUtils.isEmpty(item.order_num)) {
				holder.order_num.setText(item.order_num);
			}

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

			if (!TextUtils.isEmpty(item.verification))
				holder.order_code.setText(item.verification);
			holder.order_code.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("请输入验证码");
					final EditText edit_code = new EditText(context);
					edit_code.setSingleLine(true);
					edit_code.setMaxEms(14);
					edit_code.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							if (Config.getMsgNum(s.toString()) < 0) {
								edit_code.setText(s.subSequence(0, s.length()
										- count));
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});
					builder.setView(edit_code);
					builder.setNegativeButton("验证",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									final String scode = edit_code.getText()
											.toString().trim();
									if (scode.length() > 0) {
										Config.showProgressDialog(context,
												true, null);
										AbRequestParams params = new AbRequestParams();
										params.put("login_user_id",
												Config.getUser(context).uid);
										params.put("order_id", item.order_id);
										params.put("verification", scode);
										// android.util.Log
										// .e(TAG,
										// ServerMutualConfig.BusinessVerification
										// + "&"
										// + params.toString());
										AddNewOrder(params);
									}
								}
							});
					builder.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.create().show();
				}
			});

			if (!TextUtils.isEmpty(item.price))
				holder.price.setText("¥" + item.price);

			holder.order_combo.setText(item.packageName);
			
//			if (!TextUtils.isEmpty(item.packages)) {
//				if ("1".equals(item.packages)) {
//					holder.order_combo.setText("套餐一");
//				} else if ("2".equals(item.packages)) {
//					holder.order_combo.setText("套餐二");
//				} else if ("3".equals(item.packages)) {
//					holder.order_combo.setText("套餐三");
//				} else {
//					holder.order_combo.setText("套餐四");
//				}
//			}

			holder.order_combo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, OderDetail.class);
					intent.putExtra("order_id", item.order_id);
					intent.putExtra("isBoss", "isBoss");
					startActivity(intent);
				}
			});

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});

			return convertView;
		}
	}

	/**
	 * 添加新订单
	 * 
	 * */
	private void AddNewOrder(AbRequestParams params) {
		Config.showProgressDialog(context, false, null);
		// ab.get(ServerMutualConfig.WebBusinessVerification
		// + "&" + params.toString(),
		ab.get(ServerMutualConfig.BusinessVerification + "&"
				+ params.toString(), new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				JSONObject json;
				try {
					json = new JSONObject(content);
					if (json.getBoolean("success")) {
						Toast.makeText(context, json.getString("reMsg"),
								Toast.LENGTH_SHORT).show();
						onRefresh();
					} else {
						Toast.makeText(context, json.getString("reMsg"),
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
				Config.dismissProgress();
			}

			@Override
			public void sendSuccessMessage(int statusCode, String content) {
				super.sendSuccessMessage(statusCode, content);
				Config.dismissProgress();
			}

		});
	}
	
	/**
	 * 显示可查看的明细选项
	 * */
	private void showDetailedOption(){
		order_detailed.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示订单总量和总金额
	 * */
	private void showDetailedNumber(){
		order_info_hint.setVisibility(View.VISIBLE);
	}
}
