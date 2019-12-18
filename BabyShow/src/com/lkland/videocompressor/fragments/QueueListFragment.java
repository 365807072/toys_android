package com.lkland.videocompressor.fragments;

import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lkland.util.Logger;
import com.lkland.videocompressor.compressor.QueuedFFmpegCompressor;
import com.lkland.videocompressor.listviewadapter.QueueAdapter;
import com.lkland.videocompressor.parser.ProgressPaser;
import com.lkland.videocompressor.responsehandler.OnProgressHandler;
import com.lkland.videocompressor.responsehandler.OnQueueHandler;
import com.lkland.videocompressor.responsemanager.ActivityResponseManager;
import com.lkland.videocompressor.responsemanager.IResponseManager;
import com.lkland.videocompressor.services.AbstractCompressionService;
import com.lkland.videocompressor.services.CompressionService;
import com.lkland.videocompressor.video.IVideo;
import com.yyqq.babyshow.R;

public class QueueListFragment extends Fragment implements IQueueList{
	ServiceConnection conn = new ServiceConnection(){
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Logger.log("");
			AbstractCompressionService service = ((AbstractCompressionService.LocalBinder)binder).getService();
			mCompressor = (QueuedFFmpegCompressor)service.getCompressor();
	    	IResponseManager arm = new ActivityResponseManager(QueueListFragment.this);

	    	OnProgressHandler prh = (OnProgressHandler)mCompressor.getOnProgressListener();
	    	OnQueueHandler qrh = (OnQueueHandler)mCompressor.getOnQueueListener();
	        Iterator<IVideo> it = mCompressor.getWaitingList();
        	mQueueAdapter.clear();
	        while(it.hasNext()){
	        	QueueListFragment.this.add(it.next());
	        } 
	        prh.addResponseManager(arm);
	        qrh.addResponseManager(arm);
        }

		@Override
		public void onServiceDisconnected(ComponentName name) {
//			prh.removeResponseManager(ActivityResponseManager.class);
//			qrh.removeResponseManager(ActivityResponseManager.class);
			Logger.log("");
			
		}
		
	};
	private QueuedFFmpegCompressor mCompressor;
//	private RecyclerView mRv;
	private QueueAdapter mQueueAdapter;
    private TextView mTvName;
    private TextView mCompressing;
	private ProgressBar mPb;
//	private FloatingActionButton mFabtnAdd;
	public QueueListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_queue_list, container, false);
		mTvName = (TextView)rootView.findViewById(R.id.tvName);
        mCompressing =(TextView)rootView.findViewById(R.id.tvCompressing);
		mTvName.setText("No Video Compressing");
		mPb = (ProgressBar)rootView.findViewById(R.id.pb);
		mPb.setProgress(0);

//		mRv = (RecyclerView)rootView.findViewById(R.id.rv);
		
		mQueueAdapter = new QueueAdapter(this.getActivity());

		mQueueAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IVideo video = (IVideo) v.getTag();
                mCompressor.remove(video);

            }
        });
//        mRv.setItemAnimator(new DefaultItemAnimator());
//		mRv.setAdapter(mQueueAdapter);
//		mRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//
//		 mFabtnAdd = (FloatingActionButton)rootView.findViewById(R.id.fabtnAdd);
//		 mFabtnAdd.setOnClickListener(new OnClickListener() {
//			 @Override
//			 public void onClick(View arg0) {
//				 CompressionOptionFragment fragment2 = new CompressionOptionFragment();
//				 FragmentManager fragmentManager = getFragmentManager();
//				 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//				 fragmentTransaction.replace(R.id.container, fragment2).addToBackStack("Test").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//				 fragmentTransaction.commit();
//			 }
//		 });
//		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mAdView.loadAd(adRequest);
		return rootView;
	}
	
	@Override
	public void onResume(){
		super.onResume();
        mPb.setMax(100);
        mPb.setProgress(0);
        mCompressing.setText("");
        mTvName.setText("No Video Compressing");
        Intent intent = new Intent(this.getActivity(), CompressionService.class);
        this.getActivity().bindService(intent, conn, Activity.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(mCompressor!=null) {
			((OnProgressHandler) mCompressor.getOnProgressListener()).removeResponseManager(ActivityResponseManager.class);
			((OnQueueHandler) mCompressor.getOnQueueListener()).removeResponseManager(ActivityResponseManager.class);
		}
		if(conn!=null)
        	this.getActivity().unbindService(conn);
	}


    @Override
    public void queueFinished() {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvName.setText("No Video Compressing");
                mPb.setMax(100);
                mPb.setProgress(0);
                mCompressing.setText("");
            }
        });

    }

    @Override
	public void progress(final IVideo video, final String str){
		this.getActivity().runOnUiThread(new Runnable() {
            @Override
			public void run() {
				ProgressPaser pp = new ProgressPaser();
				pp.parse(str);
				mPb.setMax(pp.getTotalTime());
				mPb.setProgress(pp.getTime());
                mCompressing.setText("Compressing");
				mTvName.setText(video.getInName());				
			}
			
		});
	}
	
	@Override
	public void add(final IVideo video) {
		this.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				mQueueAdapter.add(video);
			}
		});
	}

	@Override
	public void remove(final IVideo video) {
		this.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mQueueAdapter.remove(video);
			}
		});	
	}

	@Override
	public void pop(final IVideo video) {
		this.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
                mQueueAdapter.remove(video);
			}
		});
	}

	 private void addVideo(String input_file_path, String output_file_path, String output_file_name, String output_file_size){
	        Intent intent = new Intent( this.getActivity(),CompressionService.class);
	        intent.putExtra(CompressionService.TAG_ACTION,CompressionService.FLAG_ACTION_ADD_VIDEO);
	        intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH,input_file_path);
	        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, output_file_path);
	        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, output_file_name);
	        intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, output_file_size);
//	        Logger.log(compressor.jnistr());

	        this.getActivity().startService(intent);
	        //btn_input_file.setText(compressor.getString());
	    }
}
