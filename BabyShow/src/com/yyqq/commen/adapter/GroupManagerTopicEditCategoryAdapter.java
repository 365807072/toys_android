package com.yyqq.commen.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.GroupManagerTopicBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class GroupManagerTopicEditCategoryAdapter extends BaseAdapter {

	private String group_id = "";
	private Activity context = null;
	private LayoutInflater inflater = null;
	private AbHttpUtil ab;
	private ArrayList<GroupManagerTopicBean.Group_class> groupManagerTopicBeanist;

	public GroupManagerTopicEditCategoryAdapter(String group_id, Activity context, LayoutInflater inflater, ArrayList<GroupManagerTopicBean.Group_class> groupManagerTopicBeanist, AbHttpUtil ab) {
		super();
		this.group_id = group_id;
		this.context = context;
		this.inflater = inflater;
		this.groupManagerTopicBeanist = groupManagerTopicBeanist;
		this.ab = ab;
	}

	@Override
	public int getCount() {
		return groupManagerTopicBeanist.size();
	}

	@Override
	public Object getItem(int position) {
		return groupManagerTopicBeanist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_group_topic_edit, null);
		
		TextView item_group_topic_title = (TextView) convertView.findViewById(R.id.item_group_topic_title);
		item_group_topic_title.setText(groupManagerTopicBeanist.get(position).getTitle());
		
		ImageView item_group_topic_delete = (ImageView) convertView.findViewById(R.id.item_group_topic_delete);
		if(groupManagerTopicBeanist.get(position).getGroup_class_state().equals("1")){
			item_group_topic_delete.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.group_topic_edit_down));
		}else{
			item_group_topic_delete.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.group_topic_edit_up));
		}
		
		return convertView;
	}

}
