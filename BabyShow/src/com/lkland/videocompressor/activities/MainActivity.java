package com.lkland.videocompressor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lkland.util.FileUtils;
import com.lkland.videocompressor.fragments.QueueListFragment;
import com.yyqq.babyshow.R;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FileUtils.createFolderInExternalStorageDirectory("VideoCompressor");
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new QueueListFragment()).commit();
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
	}

	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount()!=0)
			getFragmentManager().popBackStack();
	}
}
