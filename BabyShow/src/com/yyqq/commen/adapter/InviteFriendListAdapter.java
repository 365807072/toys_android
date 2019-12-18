package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.InviteFriendBean;
import com.yyqq.framework.application.MyApplication;

/**
 * 邀请页-邀请历史
 * */
public class InviteFriendListAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private ArrayList<InviteFriendBean> data = null;

	public InviteFriendListAdapter(Activity context, LayoutInflater inflater, ArrayList<InviteFriendBean> data) {
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
		convertView = inflater.inflate(R.layout.item_invite_friend, null);
		ImageView icon = (ImageView) convertView.findViewById(R.id.item_friend_icon);
		TextView name = (TextView) convertView.findViewById(R.id.item_friend_name);
		TextView type = (TextView) convertView.findViewById(R.id.item_friend_type);
		TextView item_friend_type_name = (TextView) convertView.findViewById(R.id.item_friend_type_name);
		ImageView type_icon = (ImageView) convertView.findViewById(R.id.item_friend_type_icon);
		
		MyApplication.getInstance().display(data.get(position).getAvatar(), icon, R.drawable.def_image);
		name.setText(data.get(position).getNick_name());
		type.setText(data.get(position).getInv_description());
		item_friend_type_name.setText(data.get(position).getStatus_title());
		
		if(data.get(position).getInv_button().equals("0")){ // 白色
			type_icon.setBackgroundResource(R.drawable.invite_type_01);
			item_friend_type_name.setTextColor(Color.parseColor("#fd6363"));
		}else{ // 灰色
			type_icon.setBackgroundResource(R.drawable.invite_type_02);
			item_friend_type_name.setTextColor(Color.parseColor("#ffffff"));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}

}