package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.InviteSelectToysBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 可选择玩具列表
 * */
public class InviteSelectToysAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<InviteSelectToysBean> data = null;

	public InviteSelectToysAdapter(Activity context, LayoutInflater inflater, ArrayList<InviteSelectToysBean> data) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.data = data;
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
		convertView = inflater.inflate(R.layout.item_invite_select_toys, null);
		ImageView icon = (ImageView) convertView.findViewById(R.id.item_friend_icon);
		TextView item_toys_number = (TextView) convertView.findViewById(R.id.item_toys_number);
		
		if(data.get(position).getPrize_number().isEmpty() || data.get(position).getPrize_number().equals("0")){
			item_toys_number.setVisibility(View.GONE);
		}else{
			item_toys_number.setVisibility(View.VISIBLE);
			item_toys_number.setText("×" + data.get(position).getPrize_number());
		}
		MyApplication.getInstance().display(data.get(position).getImg(), icon, R.drawable.def_image);
		
		return convertView;
	}

}
