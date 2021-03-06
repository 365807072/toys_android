package com.yyqq.commen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.lkland.util.FileUtils;
import com.lkland.videocompressor.services.CompressionService;
import com.mob.MobSDK;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.ShowShowMainActivity;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.postbar.PostBarType;
/*
 *  视频压缩相关
 * */
public class VideoComperssUtils {
	
	// 上传秀秀icon是否显示
	public static boolean uploadIcon = false;
	
	// 上传话题icon是否显示
	public static boolean postuploadIcon = false;
	
	// 视频文件后缀
	public static String VIDEO_FILE_SUFFIX = ".mp4"; 
	
	// 压缩服务是否完成
	public static boolean comperssServiceComplete = false;
	
	// 视频上传需要的参数
	public static AbRequestParams videoUploadParams;
	
	// 压缩后是否执行上传操作
	public static boolean executeUpload = false;
	
	// 压缩后的视频文件在SD卡的存储目录
	public static final String VIDEO_FILE_PATH = "BBBJVideo"; 
	
	// 压缩后的视频文件名称
	public static final String VIDEO_FILE_NAME = "BBBJ"; 
	
	// 压缩后的视频文件大小目标值（实际大小在此大小左右，非绝对）
	public static String VIDEO_FILE_SIZE = "5"; 
	
	public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
	
	public static final int REQUEST_CODE_INPUT_VIDEO_PATH = 1;
	public static final int REQUEST_CODE_OUTPUT_DIRECTORY_PATH = 2;
	private static Intent intent;
	private static Activity sharedContext = null;
	
	
	/**
	 * 启动压缩服务
	 * 
	 * 1. 上下文对象
	 * 3. 源文件路径
	 * */
	@SuppressLint("NewApi")
	public static void startComperssService(Activity activity,String video_path) {
		
		if(!video_path.isEmpty()){
			comperssServiceComplete = false;
			intent = new Intent(activity, CompressionService.class);
			intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
			intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, video_path);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, FileUtils.createFolderInExternalStorageDirectory(VIDEO_FILE_PATH));
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, VIDEO_FILE_NAME);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, VIDEO_FILE_SIZE);
			activity.startService(intent);
		}else{
			Toast.makeText(activity, "请重新选择视频", Toast.LENGTH_SHORT).show();
		}
		
	}	

	
	
	/**
	 * 启动压缩服务
	 * 
	 * 1. 上下文对象
	 * 2. 调用系统相册返回后，重写onActivityResult()中的Intent对象
	 * */
	public static void startComperssService(Activity activity, int requestCode, int resultCode, Intent resultIntent) {
		
		 // 根据onActivityResult() 中 resultIntent获取视频文件地址
		String path = getVideoPath(activity, resultIntent);
		
		if(resultCode!= activity.RESULT_CANCELED && requestCode == REQUEST_CODE_INPUT_VIDEO_PATH){
			comperssServiceComplete = false;
			intent = new Intent(activity, CompressionService.class);
			intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
			intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, path);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, FileUtils.createFolderInExternalStorageDirectory(VIDEO_FILE_PATH));
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, VIDEO_FILE_NAME);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, VIDEO_FILE_SIZE);
			activity.startService(intent);
		}else{
			Toast.makeText(activity, "请重新选择视频", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	/**
	 * 启动压缩服务
	 * 
	 * 1. 上下文对象
	 * 2. 调用系统相册返回后，重写onActivityResult()中，Intent对象调用getData()方法获取的Uri对象
	 * */
	public static void startComperssService(Activity activity, int requestCode, int resultCode, Uri uri) {
		
		// 根据Uri获取视频文件地址
		String path = getVideoPath(activity, uri);
		
		if(resultCode!= activity.RESULT_CANCELED && requestCode == REQUEST_CODE_INPUT_VIDEO_PATH){
			comperssServiceComplete = false;
			intent = new Intent(activity, CompressionService.class);
			intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
			intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, path);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, FileUtils.createFolderInExternalStorageDirectory(VIDEO_FILE_PATH));
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, VIDEO_FILE_NAME);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, VIDEO_FILE_SIZE);
			activity.startService(intent);
		}else{
			Toast.makeText(activity, "请重新选择视频", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	/**
	 * 启动压缩服务
	 * 
	 * 1. 上下文对象
	 * 2. 来源
	 * 3. 源文件路径
	 * 4. 压缩后文件地址
	 * 5. 压缩后文件名
	 * 6. 压缩至目标大小
	 * */
	public static void startComperssService(Activity activity, 
			int requestCode, int resultCode, String action,
			String input_file_path, String output_file_path,
			String output_file_name, String output_file_size) {
		
		if(resultCode!= activity.RESULT_CANCELED && requestCode == REQUEST_CODE_INPUT_VIDEO_PATH){
			comperssServiceComplete = false;
			intent = new Intent(activity, CompressionService.class);
			intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
			intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, input_file_path);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, output_file_path);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, output_file_name);
			intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, output_file_size);
			activity.startService(intent);
		}else{
			Toast.makeText(activity, "请重新选择视频", Toast.LENGTH_SHORT).show();
		}
		
	}	
	
	
	/**
	 * 停止压缩服务
	 * */
	public static void stopComperssService(Activity activity){
		activity.stopService(intent);
	}
	
	/**
	 * 调用本地视频库
	 * */
	public static void intentLocationVideo(Activity activity) {
		Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
		mediaChooser.setType("video/*");
		activity.startActivityForResult(mediaChooser, REQUEST_CODE_INPUT_VIDEO_PATH);
	}

	
	
	
	/**
	 * 根据resultIntent获取视频文件地址
	 * 
	 * 1. 上下文对象
	 * 2. 调用系统相册返回后，重写onActivityResult()中的Intent对象
	 * */
	public static String getVideoPath(Activity activity, Intent resultIntent){
		if(null != resultIntent && null != activity){
			Uri uri = resultIntent.getData();
			return FileUtils.getPath(activity, uri);
		}else{
			return null;
		}
	}
	
	
	
	
	/**
	 * 根据Uri获取视频文件地址
	 * 
	 * 1. 上下文对象
	 * 2. 调用系统相册返回后，重写onActivityResult()中，Intent对象调用getData()方法获取的Uri对象
	 * */
	public static String getVideoPath(Activity activity, Uri uri){
		return FileUtils.getPath(activity, uri);
	}

	
	/**
	 * 视频上传
	 * */
	public static void videoUpload(final Activity con, String uploadPath, final NotificationManager notificationManager, final String hintText){
		
		executeUpload = false;
		comperssServiceComplete = false;
		
		AbHttpUtil abhttp;
		abhttp = AbHttpUtil.getInstance(con);
		
		abhttp.post(uploadPath, getVideoParams(), new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getString("reCode").equals("200")){
						if(null != notificationManager){
							VideoComperssUtils.uploadIcon = false;
							VideoComperssUtils.postuploadIcon = false;
							notificationManager.cancel(2);
							Notification notification = new Notification(R.drawable.icon, hintText, System.currentTimeMillis());
							notification.flags |= Notification.FLAG_ONGOING_EVENT; 
							notification.setLatestEventInfo(con, "自由环球租赁", hintText, null);
							notificationManager.notify(3, notification);
							notificationManager.cancel(3);
							Toast.makeText(con, hintText, Toast.LENGTH_LONG).show();
							sharedContext = con;
							if (AddNewPostActivity.isSelectWechat) {
								String sharedUrl = "http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + new JSONObject(content).getJSONObject("data").getString("img_id") + "&user_id=" + new JSONObject(content).getJSONObject("data").getString("user_id");
								shareWechat(con,"https://api.meimei.yihaoss.top/" + new JSONObject(content).getJSONObject("data").getString("img"), "自由环球租赁", sharedUrl);
							}
							if (AddNewPostActivity.isSelectSina) {
								String sharedUrl = "http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + new JSONObject(content).getJSONObject("data").getString("img_id") + "&user_id=" + new JSONObject(content).getJSONObject("data").getString("user_id");
								shareSinaWeibo(con, "https://api.meimei.yihaoss.top/" + new JSONObject(content).getJSONObject("data").getString("img"), "自由环球租赁", sharedUrl);
							}
							
							con.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									if(null != ShowShowMainActivity.upload_icon){
										ShowShowMainActivity.upload_icon.setVisibility(View.GONE);
									}
									
									if(null != PostBarActivity.upload_icon){
										PostBarActivity.upload_icon.setVisibility(View.GONE);
									}
									
									if(null != PostBarType.upload_icon){
										PostBarType.upload_icon.setVisibility(View.GONE);
									}
								}
							});
						}
					}else{
						notificationManager.cancel(2);
						Toast.makeText(con, new JSONObject(content).getString("reMsg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}
	
	/**
	 * 获取视频缩略图的File对象
	 * */
	@SuppressLint({ "NewApi", "SdCardPath" })
	public static File getVideoThumbnailFile(String filePath) {  
        Bitmap bitmap = null;  
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
            retriever.setDataSource(filePath);  
            bitmap = retriever.getFrameAtTime();  
        }   
        catch(IllegalArgumentException e) {  
            e.printStackTrace();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }   
        finally {  
            try {  
                retriever.release();  
            }   
            catch (RuntimeException e) {  
                e.printStackTrace();  
            }  
        }  
        
        return saveBitmap2file(bitmap, VIDEO_FILE_NAME + ".jpg");
    }  
	
	/**
	 * 获取视频缩略图的Bitmap对象
	 * */
	@SuppressLint({ "NewApi", "SdCardPath" })
	public static Bitmap getVideoThumbnailBitmap(String filePath) {  
        Bitmap bitmap = null;  
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
            retriever.setDataSource(filePath);  
            bitmap = retriever.getFrameAtTime();  
        }   
        catch(IllegalArgumentException e) {  
            e.printStackTrace();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }   
        finally {  
            try {  
                retriever.release();  
            }   
            catch (RuntimeException e) {  
                e.printStackTrace();  
            }  
        }  
        
        return bitmap;
    }  
	
	/**
	 * 将Bitmap保存至本地，并返回File对象
	 * */
	public static File saveBitmap2file(Bitmap bmp,String filename){  
		CompressFormat format= Bitmap.CompressFormat.JPEG;  
		int quality = 100;  
		OutputStream stream = null;  
		try {  
			
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VIDEO_FILE_PATH/");
			if (!file.exists()){
				file.mkdirs();
			}
			stream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VIDEO_FILE_PATH/" + filename);  
			
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		}  
		
		bmp.compress(format, quality, stream);  
		return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VIDEO_FILE_PATH/" + filename);
	}  
	
	/**
	 * 设置视频上传需要的参数
	 * */
	public static void setVideoParams(AbRequestParams params){
		videoUploadParams = new AbRequestParams();
		videoUploadParams = params;
	};
	
	/**
	 * 获取视频上传需要的参数
	 * */
	public static AbRequestParams getVideoParams(){
		return videoUploadParams;
	};
	
	
	/**
	 * 获取文件大小
	 * */
	public static double getFileOrFilesSize(String filePath, int sizeType) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小", "获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
	}
	
	/**
	 * 获取视频时长
	 * */
	@SuppressLint("NewApi")
	public static String getVideoDuration(String mUri){
		android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();  
		String duration = "";
		try {  
			if (mUri != null) {  
				HashMap<String, String> headers = new HashMap<String, String>();  
				if (headers == null) {  
					headers = new HashMap<String, String>();  
					headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");  
				}  
				mmr.setDataSource(mUri, headers);  
			} 
			
			String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);  
			String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);  
			duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)+"";  
			
		} catch (Exception ex) {  
		} finally {  
			mmr.release();  
		}  
		return duration;
	}

	
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}
	
	private static long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}
	
	private static double FormetFileSize(long fileS, int sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
		case SIZETYPE_B:
			fileSizeLong = Double.valueOf(df.format((double) fileS));
			break;
		case SIZETYPE_KB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
			break;
		case SIZETYPE_MB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
			break;
		case SIZETYPE_GB:
			fileSizeLong = Double.valueOf(df
					.format((double) fileS / 1073741824));
			break;
		default:
			break;
		}
		return fileSizeLong;
	}
	
	public static void shareWechat(Activity context, String imgUrl, String msg, String sharedUrl) {
		MobSDK.init(context);
		cn.sharesdk.wechat.moments.WechatMoments.ShareParams sp = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_TEXT);
		sp.setTitle("晒晒我家宝宝最新萌照|最美的宝宝成长记录");
		sp.setText(msg);
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setImageUrl(imgUrl);
		sp.setUrl(sharedUrl);
		Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
		weixin.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weixin.share(sp);
	}

	public static void shareSinaWeibo(Activity context, String shearUrlImg, String msg, String sharedUrl) {
		MobSDK.init(context);
		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		sp.setText("晒晒我家宝宝最新萌照|最美的宝宝成长记录  " + sharedUrl);
		sp.setImageUrl(sharedUrl);
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	public static PlatformActionListener paListener = new PlatformActionListener() {
		
		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// android.util.Log.e("fff +onError: ", arg0.toString());

		}

		@Override
		public void onComplete(final Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
//				Toast.makeText(context, "分享成功", 0).show();
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// android.util.Log.e("fff +onCancel: ", arg0.toString());
		}

	};

}
