package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;

public class SearchUserListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<SearchItem> searchData;
	private LayoutInflater inflater;
	
	public SearchUserListAdapter(Context context, ArrayList<SearchItem> searchData) {
		super();
		this.context = context;
		this.searchData = searchData;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return searchData.size();
	}

	@Override
	public Object getItem(int position) {
		return searchData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder1 holder1 = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_listview_item, null);
			holder1 = new ViewHolder1();
			holder1.img = (ImageView) convertView.findViewById(R.id.head);
			holder1.name = (TextView) convertView.findViewById(R.id.name);
			holder1.head_root = (RelativeLayout) convertView.findViewById(R.id.head_root);
			convertView.setTag(holder1);

		} else {
			holder1 = (ViewHolder1) convertView.getTag();
		}
		convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		holder1.name.setText(searchData.get(position).nick_name);
		holder1.img.setTag(searchData.get(position).avatar);
		// 头像
		MyApplication.getInstance().display(searchData.get(position).avatar, holder1.img, R.drawable.def_head);

		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (Config.getUser(context).uid.equals(searchData.get(position).id)) {
					intent.setClass(context, PersonalCenterActivity.class);
					intent.putExtra("falg_return", true);
				} else {
					intent.setClass(context, UserInfo.class);
				}
				intent.putExtra("uid", searchData.get(position).id);
				((Context) context).startActivity(intent);
			}
		});
		
		return convertView;
	}

	class ViewHolder1 {
		ImageView img;
		TextView name;
		RelativeLayout head_root;
	}
}
