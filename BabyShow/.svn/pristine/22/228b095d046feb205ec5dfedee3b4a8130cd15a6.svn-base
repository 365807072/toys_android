package com.yyqq.commen.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ab.http.AbHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 异常收集
 * 
 * lyl
 * */
public class CrashHandlerUtils implements UncaughtExceptionHandler {

	private Map<String, String> infos = new HashMap<String, String>();  
	public static final String TAG = "CrashHandlerUtils";
	private static CrashHandlerUtils INSTANCE = new CrashHandlerUtils();
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private AbHttpUtil ab;
	private Context context;
	private Timer timer;

	private CrashHandlerUtils() {
	}

	public static CrashHandlerUtils getInstance() {
		return INSTANCE;
	}

	public void init(Context ctx) {
		context = ctx;
		ab = AbHttpUtil.getInstance(ctx);
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		
		Writer writer = new StringWriter();  
		PrintWriter printWriter = new PrintWriter(writer);  
		ex.printStackTrace(printWriter);  
		Throwable cause = ex.getCause();  
		while (cause != null) {  
			cause.printStackTrace(printWriter);  
			cause = cause.getCause();  
		}  
		printWriter.close();  
        String result = writer.toString();  
		
        ab = AbHttpUtil.getInstance(context);
        AbRequestParams params = new AbRequestParams();
        params.put("login_user_id", Config.getUser(context).uid);
        params.put("app_model", getVersion());
        params.put("title", result);
        
        SharedPreferences sp = context.getSharedPreferences("error_info", 0);
        Editor ed = sp.edit();
        if(sp.getString("result", "").equals(result)){
        	System.exit(0);
        }else{
        	ed.clear();
        	ed.commit();
        	ed.putString("result", result);
        	ed.commit();
        	ab.post(ServerMutualConfig.CollectInfo , params, new AbHttpResponseListener(){});
        }
		
		return false;
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "获取失败";
	    }
	}
	
}
