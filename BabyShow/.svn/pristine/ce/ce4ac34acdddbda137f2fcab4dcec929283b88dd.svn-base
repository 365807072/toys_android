package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.model.MainItemBusinessBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 所有页面商品列表item适配
 * */
public class AllBusinessItemAdapter extends BaseAdapter {
	
	public static int fromPage = -1; // 1 = 首页
	
	private Context context;
	private ArrayList<MainItemBusinessBean> data;
	private AbHttpUtil ab;
	
	public AllBusinessItemAdapter(Context context, ArrayList<MainItemBusinessBean> data, AbHttpUtil ab) {
		super();
		this.context = context;
		this.data = data;
		this.ab = ab;
	}

	@Override
	public int getCount() {
		return data.size()==0 ? 0 : data.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_home_main, null);
		
//		RelativeLayout main_item_top = (RelativeLayout) convertView.findViewById(R.id.main_item_top);
//		ImageView main_item_bottom = (ImageView) convertView.findViewById(R.id.main_item_bottom);
//		main_item_top.setVisibility(View.VISIBLE);
//		main_item_bottom.setVisibility(View.GONE);
//		ImageView main_item_img = (ImageView) convertView.findViewById(R.id.main_item_img);
//		TextView main_item_name = (TextView) convertView.findViewById(R.id.mian_item_name);
//		TextView main_item_price01 = (TextView) convertView.findViewById(R.id.mian_item_price01);
//		TextView main_item_price02 = (TextView) convertView.findViewById(R.id.mian_item_price02);
//		ImageView mian_item_add = (ImageView) convertView.findViewById(R.id.mian_item_add);
//		final ImageView mian_item_delete = (ImageView) convertView.findViewById(R.id.mian_item_delete);
//		final TextView main_item_number = (TextView) convertView.findViewById(R.id.mian_item_number);
//		
//		MyApplication.getInstance().display(data.get(position).getShopsImage1(), main_item_img, R.drawable.image_load);
//		main_item_name.setText(Html.fromHtml("<b style=\"letter-spacing:80px;\">"+data.get(position).getShopsName()+"</b>"));
//		main_item_price01.setText((Html.fromHtml("<b style=\"letter-spacing:80px;\">"+ data.get(position).getShopsDiscountPrice()+ "</b>")));
//		main_item_price02.setText("￥"+data.get(position).getShopsPrice());
//		
//		if(data.get(position).getCartNumber().equals("") || Integer.parseInt(data.get(position).getCartNumber()) == 0){
//			main_item_number.setVisibility(View.GONE);
//			mian_item_delete.setVisibility(View.GONE);
//		}else{
//			main_item_number.setText(data.get(position).getCartNumber());
//			main_item_number.setVisibility(View.VISIBLE);
//			mian_item_delete.setVisibility(View.VISIBLE);
//		}
		
		return convertView;
	}

}
