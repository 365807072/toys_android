package com.yyqq.code.photo;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.ImageBucketAdapter;
import com.yyqq.commen.model.ImageBucket;
import com.yyqq.commen.service.CrashHandlerUtils;
import com.yyqq.commen.utils.AlbumHelper;
import com.yyqq.commen.utils.Config;


/**
 * 手机相册列表（一级）
 * 
 * lyl
 * */
public class PhoneAlbumActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_image_bucket);
		
//		// 收集错误
//		CrashHandlerUtils crashHandler = CrashHandlerUtils.getInstance();    
//		crashHandler.init(this);

		helper = new AlbumHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
		
//		throw new NullPointerException();  
	}

	/**
	 * 结束
	 */

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);

		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(PhoneAlbumActivity.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(PhoneAlbumActivity.this, PhoneAlbumDetialActivity.class);
				intent.putExtra("album_id", getIntent().getStringExtra("album_id"));
				intent.putExtra("iszhineng", getIntent().getBooleanExtra("iszhineng", false));
				intent.putExtra(PhoneAlbumActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
				intent.putExtra("tag", getIntent().getStringExtra("tag"));
				intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
				intent.putExtra("img_id",getIntent().getStringExtra("img_id"));
				intent.putExtra("is_save",getIntent().getStringExtra("is_save"));
				intent.putExtra("baby_id", getIntent().getStringExtra("baby_id"));
				intent.putExtra("album_id", getIntent().getStringExtra("album_id"));
				intent.putExtra("grow_detail_title", getIntent().getStringExtra("grow_detail_title"));
				intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
				intent.putExtra(Config.SELECTNAME, getIntent().getIntExtra(Config.SELECTNAME, 10));
				startActivity(intent);
				PhoneAlbumActivity.this.finish();
			}
		});
	}
}