package com.lkland.videocompressor.fragments;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lkland.util.FileUtils;
import com.lkland.util.Logger;
import com.lkland.videocompressor.services.CompressionService;
import com.lkland.videocompressor.validations.AbstractCompressionOptionsValidator;
import com.lkland.videocompressor.validations.ValidationFactory;
import com.yyqq.babyshow.R;

public class CompressionOptionFragment extends Fragment {
	public static final int REQUEST_CODE_INPUT_VIDEO_PATH = 1;
	public static final int REQUEST_CODE_OUTPUT_DIRECTORY_PATH = 2;

	TextView tvErrorMsg;
	Button btnInVideoPath;
	Button btnOutPath;
	EditText etOutName;
	EditText etOutSize;
	Button btnCompress;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_compression_options, container, false);
		initializeUI(rootView);
		return rootView;
	}
	
	public void initializeUI(View rootView){
		tvErrorMsg = (TextView)rootView.findViewById(R.id.tvErrorMsg);
		btnInVideoPath = (Button)rootView.findViewById(R.id.btnInVideoPath);
		btnOutPath = (Button)rootView.findViewById(R.id.btnOutPath);
		etOutName = (EditText)rootView.findViewById(R.id.etOutName);
		etOutSize = (EditText)rootView.findViewById(R.id.etOutSize);
		btnCompress = (Button)rootView.findViewById(R.id.btnCompress);

		tvErrorMsg.setText("");
		btnOutPath.setText(FileUtils.createFolderInExternalStorageDirectory("VideoCompressor"));
//		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mAdView.loadAd(adRequest);
		setListeners();

//		btnOutPath.setEnabled(false);
	}
	
	public void setListeners(){
		btnCompress.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String inPathAndName = btnInVideoPath.getText().toString();
				String outPath = btnOutPath.getText().toString();
				String outName = etOutName.getText().toString();
				String outSize = etOutSize.getText().toString();

				ValidationFactory validationFactory = new ValidationFactory();
				int ret = validationFactory.getValidator(inPathAndName, outPath, outName, outSize).validate();
				if(ret!= AbstractCompressionOptionsValidator.PASS){
					tvErrorMsg.setText(validationFactory.getErrorMsgPresenter().present(ret));
					return;
				}

				Intent intent = new Intent(CompressionOptionFragment.this.getActivity(), CompressionService.class);
		        intent.putExtra(CompressionService.TAG_ACTION,CompressionService.FLAG_ACTION_ADD_VIDEO);
		        intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, inPathAndName);
		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, outPath);
		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, outName);
		        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, outSize);
		        CompressionOptionFragment.this.getActivity().startService(intent);
		        FragmentManager fm = getActivity().getFragmentManager();
	            fm.popBackStack();
			}	
		});
		
		btnInVideoPath.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
				mediaChooser.setType("video/*");
				startActivityForResult(mediaChooser, REQUEST_CODE_INPUT_VIDEO_PATH);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
	    	try{
	    		if(resultCode==Activity.RESULT_CANCELED) return;
	    		switch(requestCode){
		    		case REQUEST_CODE_INPUT_VIDEO_PATH:
//                        Uri uri = data.getData();
//                        if ("content".equalsIgnoreCase(uri.getScheme())) {
//                            String[] projection = { "_data" };
//                            Cursor cursor = null;
//
//                            try {
//                                cursor = this.getActivity().getContentResolver().query(uri, null, null, null, null);
//                                String[] columnNames = cursor.getColumnNames();
//                                for(int i=0; i<cursor.getColumnCount();i++){
//                                    Logger.log(cursor.getString(i));
//                                }
//                                Logger.log(columnNames.length);
//
//                                int column_index = cursor.getColumnIndexOrThrow("_data");
//                                if (cursor.moveToFirst()) {
//                                    Logger.log(cursor.getString(column_index));
//                                }
//                            } catch (Exception e) {
//                                // Eat it
//                            }
//                        }
//                        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                            Logger.log(uri.getPath());
//                        }
		    			Uri selectedImage = data.getData();
						String path = FileUtils.getPath(this.getActivity(), selectedImage);
						if(path.equals(FileUtils.ERROR_NOT_LOCAL_FILE)){
							btnInVideoPath.setHint(path);
							return;
						}
						Pattern p = Pattern.compile(".*"+File.separator+"(.*)\\..*");
						Matcher m = p.matcher(path);
						if(m.matches()){
							etOutName.setText(m.group(1));
						}
						btnInVideoPath.setText(path);
						Logger.log(path);
		    			break;
		    		}
		}catch(Exception e){
	    	Logger.log(e);
	    }
	}
	
	@Override
	public void onResume(){
		super.onResume();

	}

}
