package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.Review;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.framework.application.MyApplication;

public class MainItemComment02Adapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<Review> commentList;
	private AbHttpUtil ab;
	private EditText ed_user_input;
	
	public MainItemComment02Adapter(Activity context, LayoutInflater inflater, MyApplication app, ArrayList<Review> commentList, AbHttpUtil ab, EditText ed_user_input) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);;
		this.app = app;
		this.commentList = commentList;
		this.ab = ab;
	}

	@Override
	public int getCount() {
		return commentList.size() == 0 ? 0 : commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_main_detial04_item, null);
		
		TextView item_commen02_text = (TextView) convertView.findViewById(R.id.item_commen02_text);
		
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, commentList.get(position).user_name + "：" + commentList.get(position).demand);
		item_commen02_text.setText(spannableString);
		
		return convertView;
	}

	
}
