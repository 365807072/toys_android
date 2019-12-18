package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyqq.babyshow.R;
import com.yyqq.code.business.PayActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.commen.model.BusinessPackageBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 商家详情-套餐列表adapter
 * 
 * */
public class BusinessPackageAdapter extends BaseAdapter {

	private String Business_id;
	private String isBoss;
	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<BusinessPackageBean> data = null;
	private ScrollView scrollView;
	
	public BusinessPackageAdapter(Activity context,String Business_id,String isBoss, ArrayList<BusinessPackageBean> data, ScrollView scrollView) {
		super();
		this.scrollView = scrollView;
		this.Business_id = Business_id;
		this.isBoss = isBoss;
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		app = (MyApplication) context.getApplication();
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_business_package_list, null);
		ImageView business_pay1 = (ImageView) convertView.findViewById(R.id.business_pay1);
		TextView current_price1 = (TextView) convertView.findViewById(R.id.current_price1); // 秀秀价
		TextView original_price1 = (TextView) convertView.findViewById(R.id.original_price1); // 店面价
		TextView original_price0 = (TextView) convertView.findViewById(R.id.current_price0); // 玩具租赁会员价
		TextView business_combo1 = (TextView) convertView.findViewById(R.id.business_combo1);
		TextView business_combo1_times = (TextView) convertView.findViewById(R.id.business_combo1_times);
		TextView business_combo1_time = (TextView) convertView.findViewById(R.id.business_combo1_time);
		TextView business_package_name = (TextView) convertView.findViewById(R.id.business_package_name);
		
		current_price1.setText("￥" + data.get(position).getPresentPrice());
		original_price1.setText("店面价 ￥" + data.get(position).getOriginalPrice());
		original_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
		business_combo1.setText(data.get(position).getPackageCotent());
		business_combo1_times.setText(data.get(position).getFrequency());
		business_combo1_time.setText(data.get(position).getValidityPeriod());
		business_package_name.setText(data.get(position).getPackageName());
		original_price0.setText("￥" + data.get(position).getBusiness_member_price());
		
		if ("1".equals(data.get(position).getIsBuy())) {
			business_pay1.setBackgroundResource(R.drawable.business_no_pay);
		}
		
		business_pay1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("1".equals(data.get(position).getIsBuy())) {
					return;
				}
				if ("1".equals(isBoss)) {
					Toast.makeText(context, "您是商家，不可以下单哦", 0).show();
				} else if ("1".equals(app.getVisitor())) {
					Intent intent = new Intent();
					intent.setClass(context, WebLoginActivity.class);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(context, PayActivity.class);
					intent.putExtra("business_id", Business_id);
					intent.putExtra("combo1", data.get(position).getPackageId());
					context.startActivity(intent);
				}
			}
		});
		scrollView.scrollTo(0, 0) ;
		return convertView;
	}

}
