package com.yyqq.commen.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.group.GroupManagerCategoryActivity;
import com.yyqq.code.group.SuperGroupActivity;
import com.yyqq.commen.model.GroupManagerTopicBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class GroupManagerTopicAdapter extends BaseAdapter {

	private String group_id = "";
	private Activity context = null;
	private LayoutInflater inflater = null;
	private AbHttpUtil ab;
	private ArrayList<GroupManagerTopicBean> groupManagerTopicBeanist;
	private ListView group_topic_edit_list;
	private LinearLayout group_topic_edit;
	private ListView group_topic_edit_list_essence;
	private LinearLayout group_topic_edit_essence;
	public static int index = 0;

	public GroupManagerTopicAdapter(String group_id, Activity context, LayoutInflater inflater, ArrayList<GroupManagerTopicBean> groupManagerTopicBeanist, AbHttpUtil ab, ListView group_topic_edit_list, LinearLayout group_topic_edit, ListView group_topic_edit_list_essence, LinearLayout group_topic_edit_essence) {
		super();
		this.group_id = group_id;
		this.context = context;
		this.inflater = inflater;
		this.groupManagerTopicBeanist = groupManagerTopicBeanist;
		this.ab = ab;
		this.group_topic_edit_list = group_topic_edit_list;
		this.group_topic_edit = group_topic_edit;
		this.group_topic_edit_list_essence = group_topic_edit_list_essence;
		this.group_topic_edit_essence = group_topic_edit_essence;
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
		convertView = inflater.inflate(R.layout.item_group_topic, null);
		
		TextView item_group_topic_title = (TextView) convertView.findViewById(R.id.item_group_topic_title);
		item_group_topic_title.setText(groupManagerTopicBeanist.get(position).getImg_title());
		TextView item_group_topic_des = (TextView) convertView.findViewById(R.id.item_group_topic_des);
		item_group_topic_des.setText(groupManagerTopicBeanist.get(position).getImg_description());
		
		ImageView item_group_topic_img = (ImageView) convertView.findViewById(R.id.item_group_topic_img);
		MyApplication.getInstance().display(groupManagerTopicBeanist.get(position).getImgList().get(0).getImg_thumb(), item_group_topic_img, R.drawable.def_image);
		
		TextView item_group_topic_edit = (TextView) convertView.findViewById(R.id.item_group_topic_edit);
		item_group_topic_edit.setText(groupManagerTopicBeanist.get(position).getShow_essence_name());
		item_group_topic_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				index = position;
				final GroupManagerTopicEditEssenceAdapter adapter = new GroupManagerTopicEditEssenceAdapter(group_id, context, inflater, groupManagerTopicBeanist.get(position).getEditList(), ab);
				group_topic_edit_list_essence.setAdapter(adapter);
				group_topic_edit_essence.setVisibility(View.VISIBLE);
				group_topic_edit_list_essence.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int clickOpt, long id) {
						for(int i = 0 ; i < groupManagerTopicBeanist.get(position).getEditList().size(); i++){
							if(i == clickOpt){
								groupManagerTopicBeanist.get(position).getEditList().get(i).setEssence_state("1");
							}else{
								groupManagerTopicBeanist.get(position).getEditList().get(i).setEssence_state("0");
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
		
		TextView item_group_topic_category = (TextView) convertView.findViewById(R.id.item_group_topic_category);
		item_group_topic_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(groupManagerTopicBeanist.get(position).getCategoryList().size() == 0){
					Toast.makeText(context, "还没有分类，来添加吧", 3).show();
					Intent intent = new Intent(context, GroupManagerCategoryActivity.class);
					intent.putExtra(SuperGroupActivity.GROUP_ID, group_id);
					context.startActivity(intent);
				}else{
					index = position;
					final GroupManagerTopicEditCategoryAdapter adapter = new GroupManagerTopicEditCategoryAdapter(group_id, context, inflater, groupManagerTopicBeanist.get(position).getCategoryList(), ab);
					group_topic_edit_list.setAdapter(adapter);
					group_topic_edit.setVisibility(View.VISIBLE);
					group_topic_edit_list.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int clickOpt, long id) {
							if(groupManagerTopicBeanist.get(position).getCategoryList().get(clickOpt).getGroup_class_state().equals("0")){
								groupManagerTopicBeanist.get(position).getCategoryList().get(clickOpt).setGroup_class_state("1");
							}else{
								groupManagerTopicBeanist.get(position).getCategoryList().get(clickOpt).setGroup_class_state("0");
							}
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
		
		ImageView item_group_topic_delete = (ImageView) convertView.findViewById(R.id.item_group_topic_delete);
		item_group_topic_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("确认删除 \"" + groupManagerTopicBeanist.get(position).getImg_title() + "\" 吗？");
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						deleteTopic(position);
					}
				});
				
				builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
		return convertView;
	}
	

	private void showAddView(final int position){
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setTitle("请输入新分类名称");
//		final EditText edit_name = new EditText(context);
//		edit_name.setSingleLine(true);
//		edit_name.setMaxEms(10);
//		edit_name.setText(groupManagerTopicBeanist.get(position));
//		edit_name.setSelection(groupManagerTopicBeanist.get(position).length());
//		builder.setView(edit_name);
//		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				if(edit_name.getText().toString().trim().length() > 10){
//					Toast.makeText(context, "分类名称过长，最多10个字",Toast.LENGTH_SHORT).show();
//					return;
//				}
//				addCategory(position, edit_name.getText().toString().trim());
//				arg0.dismiss();
//				((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
//			}
//		});
//		
//		builder.setNeutralButton("取消",
//				new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
//			}
//		});
//		
//		final AlertDialog dialog = builder.create();
//		dialog.show();
//		
//		edit_name.setOnEditorActionListener(new OnEditorActionListener(){
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				addCategory(position, edit_name.getText().toString().trim());
//				dialog.dismiss();
//				((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
//				return false;
//			}
//		});
//		
//		edit_name.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//		            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//		       }
//			}
//		});
	}
	
	/**
	 * 修改分类名称
	 * */
	private void addCategory(final int position, final String categoryStr){
//		Config.showProgressDialog(context, false, null);
//		AbRequestParams params = new AbRequestParams();
//		params.put("login_user_id", Config.getUser(context).uid);
//		params.put("group_class_id", groupManagerTopicBeanist.get(position));
//		params.put("group_id", group_id);
//		params.put("title", categoryStr);
//		ab.post(ServerMutualConfig.editGroupCategory, params, new AbStringHttpResponseListener() {
//			
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						super.onSuccess(statusCode, content);
//						Config.dismissProgress();
//						try {
//							JSONObject json = new JSONObject(content);
//							if (json.getBoolean("success")) {
//								groupManagerTopicBeanist.remove(position);
//								groupManagerTopicBeanist.add(position, categoryStr);
//								notifyDataSetChanged();
//								Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//
//					@Override
//					public void onFinish() {
//						super.onFinish();
//					}
//
//					@Override
//					public void onFailure(int statusCode,
//							String content, Throwable error) {
//						super.onFailure(statusCode, content, error);
//					}
//				});
	}
	
	
	/**
	 * 删除帖子
	 * */
	private void deleteTopic(final int position){
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", groupManagerTopicBeanist.get(position).getImg_id());
		params.put("group_id", group_id);
		ab.post(ServerMutualConfig.DelGroupListing, params,new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					JSONObject json = new JSONObject(content);
					if (json.getBoolean("success")) {
						groupManagerTopicBeanist.remove(position);
						groupManagerTopicBeanist.remove(position);
						notifyDataSetChanged();
					}
					Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode,
					String content, Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}

}
