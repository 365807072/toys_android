package com.yyqq.commen.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.User;
import com.yyqq.framework.application.MyApplication;

public class Config {
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static int SELECTEDIMAGECOUNT = 9;
	public static String SELECTNAME = "selectedimagecountname";
	public static Uri imageUri = Uri
			.parse("file:///sdcard/yyqq/babyshow/image/temp_image.jpg");
	public static int IMAGE_CAMERA_RESULT = 1;
	public static int IMAGE_IMAGE_RESULT = 2;
	public static final int CROP_BIG_PICTURE = 3;

	public static void cropImageUri(Activity activity, Uri uri, int outputX,
			int outputY, int requestCode) {
		
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yyqq/babyshow/image/");
		if (!file.exists()){
			file.mkdirs();
		}
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file:///sdcard/yyqq/babyshow/image/temp_image.jpg"));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * MD5加密，32位
	 * 
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}

	public static final int HEADWIDTH = 640;
	public static final int MAXPHOTONUM = 8;

	public static User getUser(Context context) {
		if (context == null) {
			return new User();
		}
		final SharedPreferences sp = context.getSharedPreferences(
				"babyshow_user", Context.MODE_PRIVATE);
		User user = new User();
		try {
			user.fromJson(new JSONObject(sp.getString("user", "")));
		} catch (JSONException e) {
			e.printStackTrace();
			return new User();
		}
		return user;
	}

	static String regEx = "[^0-9]";

	/**
	 * 检测手机号是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean btnValidatePhoneNum(String email) {
		if (Pattern.compile(regEx).matcher(email).matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static void showFiledToast(Context context) {
		dismissProgress();
		Toast.makeText(context, "失败,请重试!", Toast.LENGTH_SHORT).show();
	}

	public static boolean isGobackHome = false;
	public static final String RecordFile = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/yyqq/babyshow/record/";
	public static final String ImageFile = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/yyqq/babyshow/image/";
	public static final String HeadFile = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/yyqq/babyshow/head/";
	public static final String DownFile = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/宝宝秀秀相册/";

	public static void setActivityState(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		MyApplication.getInstance().addActivity(activity);
		// activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public static boolean isOpenRing = true;
	public static boolean isOpenvib = true;
	public static long lastringTime = 0;

	/**
	 * 播放声音
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void ring(Context context) {
		SharedPreferences sp = context.getSharedPreferences("baby_userinfo",
				Context.MODE_PRIVATE);
		isOpenRing = sp.getBoolean("ring", true);
		if (!isOpenRing) {
			return;
		}
		if (System.currentTimeMillis() - lastringTime < 3000) {
			return;
		}
		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			// Uri alert =
			// Uri.parse("android.resource://"+context.getApplicationContext().getPackageName()+"/raw/"+R.raw.ring);
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				player.setLooping(false);
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		lastringTime = System.currentTimeMillis();
	}

	public static long lastVibtime = 0;

	/**
	 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次 long milliseconds
	 * ：震动的时长，单位是毫秒 long[] pattern ：自定义震动模式 。
	 * 数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * 
	 * @param activity
	 * @param pattern
	 * @param isRepeat
	 */
	public static void Vibrate(Context Activity/*
												 * ,long[] pattern,boolean
												 * isRepeat
												 */) {
		SharedPreferences sp = Activity.getSharedPreferences("baby_userinfo",
				Context.MODE_PRIVATE);
		isOpenvib = sp.getBoolean("vib", true);
		if (!isOpenvib) {
			return;
		}
		if (System.currentTimeMillis() - lastVibtime < 3000) {
			return;
		}
		Vibrator vib = (Vibrator) Activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(500);
		lastVibtime = System.currentTimeMillis();
	}

	public static ProgressDialog progress = null;

	public static void showProgressDialog(Context context, boolean canCancel,
			final ProgressCancelListener listener) {
		dismissProgress();
		progress = new ProgressDialog(context);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage("加载中");
		progress.setCancelable(canCancel);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		progress.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (listener != null) {
					listener.progressCancel();
				}
			}
		});
	}

	public static interface ProgressCancelListener {
		public void progressCancel();
	}

	public static void dismissProgress() {
		try {
			if (progress != null /* && progress.isShowing() */) {
				progress.dismiss();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * DP转PX
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * PX转DP
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			boolean de = file.delete();
		}
	}

	/*
	 * 以屏幕大小为界
	 */
	public static void setNewopts(Activity context, BitmapFactory.Options opts,
			int srcW, int srcH) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// int minSrc = Math.min(srcW, srcH);
		// int maxSrc = Math.max(srcW, srcH);
		// int minSrn = Math.min(dm.widthPixels, dm.heightPixels);
		// int maxSrn = Math.max(dm.widthPixels, dm.heightPixels);
		//
		// if(minSrc * maxSrn < maxSrc * minSrn) {
		// opts.outWidth = (srcW * maxSrn) / maxSrc;
		// opts.outHeight = (srcH * maxSrn) / maxSrc;
		// } else {
		// opts.outWidth = (srcW * minSrn) / minSrc;
		// opts.outHeight = (srcH * minSrn) / minSrc;
		// }
		/*
		 * 同向
		 */
		if ((srcH > srcW && dm.heightPixels > dm.widthPixels)
				|| (srcH < srcW && dm.heightPixels < dm.widthPixels)) {
			opts.inSampleSize = Math.min(srcH / dm.heightPixels, srcW
					/ dm.widthPixels);
		} else {
			opts.inSampleSize = Math.min(srcW / dm.heightPixels, srcH
					/ dm.widthPixels);
		}
		// DebugLog.logi("opts.inSampleSize:" + opts.inSampleSize);
		// DebugLog.logi("scaleToHalf: srcW:" + srcW + "  srcH:" + srcH);
		// DebugLog.logi("scaleToHalf: screenW:" + dm.widthPixels + "  screenH:"
		// + dm.heightPixels);
		// DebugLog.logi("scaleToHalf: opts.outWidth:"+opts.outWidth +
		// "  opts.outHeight:"+opts.outHeight);
		// opts.inSampleSize = 4;
		opts.inJustDecodeBounds = false;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	/*
	 * 以屏幕大小为界
	 */
	public static void setSmallNewopts(Activity context,
			BitmapFactory.Options opts, int srcW, int srcH) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int minSrc = Math.min(srcW, srcH);
		int maxSrc = Math.max(srcW, srcH);
		int minSrn = Math.min(dm.widthPixels, dm.heightPixels);
		int maxSrn = Math.max(dm.widthPixels, dm.heightPixels);
		if (minSrc * maxSrn < maxSrc * minSrn) {
			opts.outWidth = (srcW * maxSrn) / maxSrc;
			opts.outHeight = (srcH * maxSrn) / maxSrc;
		} else {
			opts.outWidth = (srcW * minSrn) / minSrc;
			opts.outHeight = (srcH * minSrn) / minSrc;
		}
		/*
		 * 同向
		 */
		if ((srcH > srcW && dm.heightPixels > dm.widthPixels)
				|| (srcH < srcW && dm.heightPixels < dm.widthPixels)) {
			opts.inSampleSize = Math.min(srcH / dm.heightPixels, srcW
					/ dm.widthPixels);
		} else {
			opts.inSampleSize = Math.min(srcW / dm.heightPixels, srcH
					/ dm.widthPixels);
		}
		// DebugLog.logi("opts.inSampleSize:" + opts.inSampleSize);
		// DebugLog.logi("scaleToHalf: srcW:" + srcW + "  srcH:" + srcH);
		// DebugLog.logi("scaleToHalf: screenW:" + dm.widthPixels + "  screenH:"
		// + dm.heightPixels);
		// DebugLog.logi("scaleToHalf: opts.outWidth:"+opts.outWidth +
		// "  opts.outHeight:"+opts.outHeight);
		// opts.inSampleSize = 4;
		opts.inJustDecodeBounds = false;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	public static Bitmap small(Bitmap bitmap, float sx, float sy) {
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	public static String TAG = "Config";
	private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean fileIsExists(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	public static long getDateDays(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long betweenTime = 0;
		try {
			Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期 Date
											// dateBegin = sdf.parse(date2);
			Date date22 = sdf.parse(date2);
			betweenTime = date.getTime() - date22.getTime();
			// betweenTime = betweenTime / 1000 / 60 / 60 / 24;
		} catch (Exception e) {
		}
		return betweenTime;
	}

	public static long getDateDaysYear(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long betweenTime = 0;
		try {
			Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期 Date
											// dateBegin = sdf.parse(date2);
			Date date22 = sdf.parse(date2);
			betweenTime = date.getTime() - date22.getTime();
			// betweenTime = betweenTime / 1000 / 60 / 60 / 24;
		} catch (Exception e) {
		}
		return betweenTime;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String[] getStringDateShort(long create_time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(create_time);
		return dateString.split("-");
	}

	private static long exitTime = 0;

	public static boolean onKeyDown(int keyCode, KeyEvent event,
			Activity context) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			// intent.addCategory(Intent.CATEGORY_HOME);
			// context.startActivity(intent);
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT)
							.show();
					exitTime = System.currentTimeMillis();
				} else {
					// // Intent intent = new Intent(Intent.ACTION_MAIN);
					// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
					// // intent.addCategory(Intent.CATEGORY_HOME);
					// // context.startActivity(intent);
					MyApplication.getInstance().stopAll();
					// File file = new File("/storage/sdcard0/cache_files/");
					// DataCleanManager.deleteFilesByDirectory(file);
					// File files1 = new File(Environment
					// .getExternalStorageDirectory().getAbsolutePath()
					// + "/formats/");
					// DataCleanManager.deleteFilesByDirectory(files1);
					// File file2 = new File("/storage/sdcard1/cache_files/");
					// DataCleanManager.deleteFilesByDirectory(file2);
					// File file3 = new File(Environment
					// .getExternalStorageDirectory().getAbsolutePath()
					// + "/download/");
					// DeleteRecursive(file3);
					// File file4 = new File("/storage/download/cache_files/");
					// DataCleanManager.deleteFilesByDirectory(file4);
					// File file5 = new File(
					// "/storage/sdcard0/download/cache_files/");
					// DataCleanManager.deleteFilesByDirectory(file5);
					// File file6 = new File("/storage/sdcard0/宝宝秀秀相册/");
					// DataCleanManager.deleteFilesByDirectory(file6);
					// File file7 = new File(
					// "/storage/sdcard0/Download/cache_files/");
					// // Config.deleteFile(Config.HeadFile);
					// // Config.deleteFile(Config.ImageFile);
					// Config.deleteFile(Environment.getExternalStorageDirectory()
					// .getAbsolutePath() + "/cache_files");
					//
					// deleteAllFile("/cache_files");
				}
				return true;
			}
			return true;
		}
		return false;
	}

	public static void deleteAllFile(String paths) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + paths;
		deleteFile(new File(path));
	}

	public static void deleteFile(File oldPath) {
		if (oldPath.isDirectory()) {
			File[] files = oldPath.listFiles();
			for (File file : files) {
				deleteFile(file);
				file.delete();
			}
		} else {
			oldPath.delete();
		}
	}

	public static void DeleteRecursive(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File temp = new File(dir, children[i]);
				if (temp.isDirectory()) {
					DeleteRecursive(temp);
				} else {
					boolean b = temp.delete();
					if (b == false) {
					}
				}
			}
			dir.delete();
		}
	}

	public static boolean onKeyDownExit(int keyCode, KeyEvent event,
			Activity context) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			// intent.addCategory(Intent.CATEGORY_HOME);
			// context.startActivity(intent);
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT)
							.show();
					exitTime = System.currentTimeMillis();
				} else {
					MyApplication.getInstance().stopAll();
				}
				return true;
			}
			return true;
		}
		return false;
	}

	private static Bitmap bitmap;
	public static int passwordMinLength = 6;
	public static int passwordMaxLength = 10;
	public static boolean isBreakNetWork;

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		int num = 0;
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				// return true;
				num += 1;
			}
		}
		if (num == source.length()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isEmojiCharacter(char codePoint) {
		// boolean returnValue = false;
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
		// if(0x2100 <= hs && hs <= 0x27ff)
		// {
		// returnValue = true;
		// }
		// else if(0x2B05 <= hs && hs <= 0x2b07)
		// {
		// returnValue = true;
		// }
		// else if(0x2934 <= hs && hs <= 0x2935)
		// {
		// returnValue = true;
		// }
		// else if(0x3297 <= hs && hs <= 0x3299)
		// {
		// returnValue = true;
		// }
		// else if(hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 ||
		// hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50)
		// {
		// returnValue = true;
		// }
		// return returnValue;
	}

	/**
	 * 用户昵称 uid nickname
	 */
	public static HashMap<String, String> Usernickname = new HashMap<String, String>();
	/**
	 * 用户头像 uid 头像url
	 */
	public static HashMap<String, String> HeadUrls = new HashMap<String, String>();

	/**
	 * 判断当前界面是否是桌面
	 */
	public static boolean outApp(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		if (rti.get(0).topActivity.getPackageName().indexOf("com.yyqq") == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = null;
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else {
			}
		}
		if (buf == null) {
			return source;// 如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {
				// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}

	public static long getMsgNum(String msg) {
		double len = 0;
		for (int i = 0; i < msg.length(); i++) {
			int temp = (int) msg.charAt(i);
			if (temp > 0 && temp < 127) {
				len += 1;
			} else {
				len += 2;
			}
		}
		// System.out.println("字符串中a的字数为：" + Math.round(len) + "个");
		return Math.round(14 - len);
	}

	/**
	 * 将网页链接转换成一个东西
	 * 
	 * @param view
	 *            显示的view
	 * @param str
	 *            该字符串
	 * @param obj
	 *            需要改变的图片
	 * @return
	 */
	public static SpannableStringBuilder getLinkString(TextView view,
			String str, Object obj, Context context) {
		SpannableStringBuilder style = null;
		// String htmlLinkText = "你好欢迎 www.baidu.com "
		// + "我们都是好孩子 http://help.baidu.com/question?prod_en=webmaster"
		// +
		// "&class=%CD%F8%D2%B3%CB%D1%CB%F7%CC%D8%C9%AB%B9%A6%C4%DC&id=1000913#05"
		// +" http://v.qq.com/cover/w/wauvu6spdz5dsa7/u0014rpe7xt.html?pgv_ref=aio2012&ptlang=2052";
		// str ="新网址 ："+ htmlLinkText;
		view.setAutoLinkMask(Linkify.WEB_URLS);
		view.setText(Html.fromHtml(str));
		view.setMovementMethod(LinkMovementMethod.getInstance());

		CharSequence text = view.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) view.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			style = new SpannableStringBuilder(text);
			for (URLSpan url : urls) {
				ImageSpan span = new ImageSpan(context, R.drawable.add);
				style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			view.setText(style);
		}
		return style;
	}

	/**
	 * 缓存机制
	 */

	// 保存用户输入的内容到文件
	@SuppressLint("NewApi")
	public static void save(Context context, String content, String filename) {
		try {
			/*
			 * 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
			 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/.txt 第二个参数，代表文件的操作模式 MODE_PRIVATE
			 * 私有（只能创建它的应用访问） 重复写入时会文件覆盖 MODE_APPEND 私有
			 * 重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
			FileOutputStream outputStream = context.openFileOutput(filename,
					Activity.MODE_PRIVATE);
			// integer = content.getBytes().length;
			outputStream.write(content.getBytes(Charset.forName("UTF-8")));//
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author fanfan 读取刚才用户保存的内容
	 */
	public static String read(Context context, String fileName) {
		String content = "";
		try {
			FileInputStream inputStream = context.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			content = new String(arrayOutputStream.toByteArray());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 判断网路是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}
	private static float width = -1;
	private static float height = -1;
	public static int getSize(String value) {
		if (value == null || value.equalsIgnoreCase("")/* value.isEmpty() */) {
			return 0;
		}
		float r = 0;
		try {
			if (value.endsWith("dp")) {
				r = Float.parseFloat(value.replace("dp", ""));
				r = getPx2dp(r);
			} else if (value.endsWith("xdp")) {
				r = Float.parseFloat(value.replace("xdp", ""));
				r = getPx2xdp(r);
			} else if (value.endsWith("px")) {
				r = Float.parseFloat(value.replace("px", ""));
			} else if (value.endsWith("sp")) {
				r = Float.parseFloat(value.replace("sp", ""));
				r = getPx2sp(r);
			} else {
				int direction = 0;
				if (width < 0) {
					r = Float.parseFloat(value);
				} else if (direction == 1) {
					r = (Float.parseFloat(value) * height) / 460;
				} else {
					r = (Float.parseFloat(value) * width) / 320;
				}
				r = getPx2dp(r);
			}
		} catch (Exception e) {
			throw new Error("无效的尺寸数字 - " + value);
		}

		return (int) r;
	}
	private static int px2dp(int value) {
		float density = MyApplication.getDensity();
		if (density <= 0)
			return value;
		return (int) (value / density);
	}

	public static int getPx2dpInt(int value) {
		return (int) getPx2dp((float) value);
	}

	public static float getPx2dp(float value) {
		if (MyApplication.getDensity() <= 0) {
			return value;
		}
		return value * MyApplication.getDensity() + 0.5f;
	}

	private static float getPx2sp(float value) {
		if (MyApplication.getScaledDensity() <= 0) {
			return value;
		}
		return getPx2dp(value) / MyApplication.getScaledDensity() + 0.5f;
	}

	private static float getPx2xdp(float value) {
		if (MyApplication.getDensity() <= 0) {
			return value;
		}
		return (value / 320) * (value * MyApplication.getDensity() + 0.5f);
	}
}
