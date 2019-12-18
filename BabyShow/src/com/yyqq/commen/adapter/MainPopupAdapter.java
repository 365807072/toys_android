package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.framework.application.MyApplication;

public class MainPopupAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<SearchItem> searchData;
	private LayoutInflater inflater;
	
	public MainPopupAdapter(Context context, ArrayList<MainListItemBean> data, ArrayList<SearchItem> searchData) {
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder1 holder1 = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_listview_item, null);
			holder1 = new ViewHolder1();
			holder1.img = (ImageView) convertView.findViewById(R.id.head);
			holder1.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder1);

		} else {
			holder1 = (ViewHolder1) convertView.getTag();
		}
		holder1.name.setText(searchData.get(position).nick_name);
		holder1.img.setTag(searchData.get(position).avatar);
		// 头像
		MyApplication.getInstance().display(searchData.get(position).avatar, holder1.img, R.drawable.def_head);

		return convertView;
	}
}

class ViewHolder1 {
	ImageView img;
	TextView name;
}