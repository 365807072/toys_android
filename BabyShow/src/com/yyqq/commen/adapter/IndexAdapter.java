package com.yyqq.commen.adapter;
//package net.xinhuamm.entity;
//
//import java.util.List;
//
//import com.yyqq.babypromise.R;
//import com.yyqq.login.Login;
//import com.yyqq.user.BabyUserInfo;
//import com.yyqq.user.People;
//import com.yyqq.utils.Config;
//import com.yyqq.utils.MyApplication;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
///**
// * @类名:IndexAdapter
// * @描述:TODO(首页适配器)
// * @作者:zhaohao
// * @时间 2013-3-4 下午4:06:18
// */
//public class IndexAdapter extends MyBaseAdapter
//{
//    private LayoutInflater inflater = null;
//    
//    public IndexAdapter(Context context)
//    {
//        inflater = LayoutInflater.from(context);
//        this.mContext = context;
//    }
//
//    public void setAbsListView(AbsListView absListView)
//    {
//        this.absListView = absListView;
//    }
//
//    public void setList(List<Object> alObjects,boolean boo)
//    {
//        this.alObjects.addAll(alObjects);
//        if(boo)
//        {
//            notifyDataSetChanged();
//        }
//    }
//
//    public void clear()
//    {
//        this.alObjects.clear();
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public View getView(int position,View convertView,ViewGroup parent)
//    {
//        ViewHolder viewHolder = null;
//        if(convertView == null)
//        {
//            convertView = inflater.inflate(R.layout.fujin_grid_item,parent,false);
//            viewHolder = new ViewHolder();
//            viewHolder.tvIndexItemValue = (TextView)convertView.findViewById(R.id.tvIndexItemValue);
//            viewHolder.ivIndexItemimg = (ImageView)convertView.findViewById(R.id.ivIndexItemimg);
//            convertView.setTag(viewHolder);
//        }
//        else
//        {
//            viewHolder = (ViewHolder)convertView.getTag();
//        }
//        final People people = (People)alObjects.get(position);
//        viewHolder.tvIndexItemValue.setText(people.dis);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,h); 
//        if( !TextUtils.isEmpty(people.headThumbUrl))
//        {
//            MyApplication.getInstance().display(people.headThumbUrl,viewHolder.ivIndexItemimg,R.drawable.def_head);
//        }
//        convertView.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(Config.isGuest(mContext))
//                {
//                    mContext.startActivity(new Intent(mContext,Login.class));
//                }
//                else
//                {
//                    Intent intent = new Intent(mContext,BabyUserInfo.class);
//                    intent.putExtra("uid",people.uid);
//                    mContext.startActivity(intent);
//                }
//            
//            }
//        });
//        return convertView;
//    }
//
//    static class ViewHolder
//    {
//        TextView tvIndexItemValue;
//        ImageView ivIndexItemimg;
//    }
//
//    @Override
//    public int getCount()
//    {
//        return alObjects.size();
//    }
//
//    @Override
//    public Object getItem(int arg0)
//    {
//        return alObjects.get(arg0);
//    }
//
//    @Override
//    public long getItemId(int arg0)
//    {
//        return arg0;
//    }
// }
