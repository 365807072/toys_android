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
 * 邀请页-玩具列表
 * */
public class InviteToysListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<InviteSelectToysBean> data = null;

	public InviteToysListAdapter(Activity context, LayoutInflater inflater, ArrayList<InviteSelectToysBean> data) {
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
		convertView = inflater.inflate(R.layout.item_invite_toys, null);
		ImageView icon = (ImageView) convertView.findViewById(R.id.item_friend_icon);
		TextView name = (TextView) convertView.findViewById(R.id.item_friend_name);
		TextView type = (TextView) convertView.findViewById(R.id.item_friend_type);
		
		MyApplication.getInstance().display(data.get(position).getImg(), icon, R.drawable.def_image);
		name.setText(data.get(position).getPrize_title());
		type.setText(data.get(position).getPrize_description());
		
		return convertView;
	}

}
