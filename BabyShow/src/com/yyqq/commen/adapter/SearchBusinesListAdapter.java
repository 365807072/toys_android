package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.commen.model.BusinessListItem;
import com.yyqq.framework.application.MyApplication;

public class SearchBusinesListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<BusinessListItem> dataBusinessList;

	public SearchBusinesListAdapter(Context context, ArrayList<BusinessListItem> dataBusinessList) {
		super();
		this.context = context;
		this.dataBusinessList = dataBusinessList;
	}

	@Override
	public int getCount() {
		return dataBusinessList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataBusinessList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.business_list_item, null);
		ViewHolder holder = null;
		holder = new ViewHolder();
		// 商家名称
		holder.bs_name = (TextView) convertView.findViewById(R.id.bs_name);
		// 商家副标题
		holder.bs_subTitle = (TextView) convertView
				.findViewById(R.id.bs_subTitle);
		holder.bs_distance = (TextView) convertView
				.findViewById(R.id.bs_distance);
		holder.bs_img = (ImageView) convertView.findViewById(R.id.bs_img);
		// 秀秀价
		holder.xiu_price = (TextView) convertView.findViewById(R.id.xiu_price);
		// 原价
		holder.price = (TextView) convertView.findViewById(R.id.price);
		holder.count = (TextView) convertView.findViewById(R.id.count);
		convertView.setTag(holder);
		final BusinessListItem item = dataBusinessList.get(index);

		// 商家头像
		MyApplication.getInstance().display(item.business_pic, holder.bs_img,
				R.drawable.def_head);

		// 标题
		if (!TextUtils.isEmpty(item.business_title))
			holder.bs_name.setText(item.business_title);
		// 距离
		if (!TextUtils.isEmpty(item.distance))
			holder.bs_distance.setText(item.distance);
		// 商家副标题
		if (!TextUtils.isEmpty(item.subtitle))
			holder.bs_subTitle.setText(item.subtitle);
		// 秀秀价
		if (!TextUtils.isEmpty(item.business_babyshow_price1))
			holder.xiu_price.setText("¥" + item.business_babyshow_price1);
		// 原价
		if (!TextUtils.isEmpty(item.business_market_price1))
			holder.price.setText("¥" + item.business_market_price1);
		// 原价
		if (!TextUtils.isEmpty(item.order_count))
			holder.count.setText(item.order_count + "人购买");

		holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, BusinessDetailActivity.class);
				intent.putExtra("business_id", item.business_id);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView bs_subTitle;
		ImageView bs_img;
		TextView bs_distance;
		TextView bs_name;
		TextView xiu_price;
		TextView price;
		TextView count;
	}

}
