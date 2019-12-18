package com.lkland.videocompressor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lkland.videocompressor.services.CompressionService;
import com.yyqq.babyshow.R;

public class TestApplication extends Activity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        Button addVideo = (Button)this.findViewById(R.id.btnAddVideo);
        addVideo.setOnClickListener(new OnClickListener(){
        	int i=0;
			@Override
			public void onClick(View arg0) {
				   addVideo("/storage/emulated/0/DCIM/100MEDIA/VIDEO0057.mp4"
			        		,"/storage/emulated/0/VideoCompressor/"
			        		,String.valueOf(i++)
			        		,"10");
			        addVideo("/storage/emulated/0/DCIM/100MEDIA/VIDEO0041.mp4"
			        		,"/storage/emulated/0/VideoCompressor"
			        		,String.valueOf(i++)
			        		,"10");
			}
        });
    }
    
    private void addVideo(String input_file_path, String output_file_path, String output_file_name, String output_file_size){
        Intent intent = new Intent(TestApplication.this,CompressionService.class);
        intent.putExtra(CompressionService.TAG_ACTION,CompressionService.FLAG_ACTION_ADD_VIDEO);
        intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH,input_file_path);
        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, output_file_path);
        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, output_file_name);
        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, output_file_size);
//        Logger.log(compressor.jnistr());

        TestApplication.this.startService(intent);
        //btn_input_file.setText(compressor.getString());
    }

    
}
