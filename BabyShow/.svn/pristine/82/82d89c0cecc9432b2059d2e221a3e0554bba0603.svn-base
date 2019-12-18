package com.lkland.videocompressor.listviewadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkland.videocompressor.video.IVideo;
import com.yyqq.babyshow.R;

public class QueueItemAdapter extends BaseAdapter{
	private ArrayList<IVideo> mList;
	private Activity mActivity;
	private View.OnClickListener mOnClickListener;
	
	public QueueItemAdapter(Activity act, ArrayList<IVideo> list){
		this.mList = list;
		mActivity = act;
	}
	
	public QueueItemAdapter(Activity act){
		mActivity = act;
		mList = new ArrayList<IVideo>();
	}
	
	public void add(IVideo video){
		mList.add(video);
		this.notifyDataSetChanged();
	}
	
	public void remove(IVideo video){
		mList.remove(video);
		this.notifyDataSetChanged();
	}
	
	public void setOnClickListener(View.OnClickListener lis){
		this.mOnClickListener = lis;
	}
	
	public void clear(){
		mList.clear();
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public IVideo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewStub vs =null;
		if(convertView==null){
	        LayoutInflater inflater =(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(R.layout.list_item_queue, parent, false);
			vs = new ViewStub();
			vs.Name = (TextView)convertView.findViewById(R.id.tvName);
			vs.Cancel = (ImageView)convertView.findViewById(R.id.IvCancel);
			vs.Cancel.setOnClickListener(mOnClickListener);
			convertView.setTag(vs);
		}
		vs = (ViewStub)convertView.getTag();
		vs.Name.setText(this.getItem(position).getInName());
		vs.Cancel.setTag(this.getItem(position));
		return convertView;
	}
	
	private class ViewStub{
		TextView Name;
		ImageView Cancel;
	}
	
}
