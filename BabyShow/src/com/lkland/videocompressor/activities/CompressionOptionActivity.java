package com.lkland.videocompressor.activities;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lkland.util.FileUtils;
import com.lkland.util.Logger;
import com.lkland.videocompressor.services.CompressionService;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.VideoComperssUtils;

public class CompressionOptionActivity extends Activity {
	public static final int REQUEST_CODE_INPUT_VIDEO_PATH = 1;
	public static final int REQUEST_CODE_OUTPUT_DIRECTORY_PATH = 2;

	Button btnInVideoPath;
	Button btnOutPath;
	EditText etOutName;
	EditText etOutSize;
	Button btnCompress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compression_options);
		initializeUI();
	}

	public void initializeUI(){
		btnInVideoPath = (Button)this.findViewById(R.id.btnInVideoPath);
		btnOutPath = (Button)this.findViewById(R.id.btnOutPath);
		etOutName = (EditText)this.findViewById(R.id.etOutName);
		etOutSize = (EditText)this.findViewById(R.id.etOutSize);
		btnCompress = (Button)this.findViewById(R.id.btnCompress);
		setListeners();
		btnOutPath.setText(FileUtils.createFolderInExternalStorageDirectory("VideoCompressor"));
//		btnOutPath.setEnabled(false);
	}
	
	public void setListeners(){
		btnCompress.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(CompressionOptionActivity.this, CompressionService.class);
//		        intent.putExtra(CompressionService.TAG_ACTION,CompressionService.FLAG_ACTION_ADD_VIDEO);
//		        intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, btnInVideoPath.getText().toString());
//		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, btnOutPath.getText().toString());
//		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, etOutName.getText().toString());
//		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, etOutSize.getText().toString());
//		        CompressionOptionActivity.this.startService(intent);
			}	
		});
		
		btnInVideoPath.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				VideoComperssUtils.intentLocationVideo(CompressionOptionActivity.this);
//				Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
//                mediaChooser.setType("video/*");
//                startActivityForResult(mediaChooser, REQUEST_CODE_INPUT_VIDEO_PATH);
			}
		});
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		VideoComperssUtils.startComperssService(CompressionOptionActivity.this, requestCode, resultCode, data);
//	    	try{
//	    		if(resultCode==RESULT_CANCELED) return;
//	    		switch(requestCode){
//		    		case REQUEST_CODE_INPUT_VIDEO_PATH:
//		    			Uri selectedImage = data.getData();
//						String path = FileUtils.getPath(this, selectedImage);
//						if(path.equals(FileUtils.ERROR_NOT_LOCAL_FILE)){
//							btnInVideoPath.setText(path);
//							return;
//						}
//						Pattern p = Pattern.compile(".*"+File.separator+"(.*)\\..*");
//						Matcher m = p.matcher(path);
//						if(m.matches()){
//							etOutName.setText(m.group(1));
//						}
//						btnInVideoPath.setText(path);
//						Logger.log(path);
//		    			break;
//	    		}
//		    			
//		}catch(Exception e){
//	    	Logger.log(e);
//	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compression_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
