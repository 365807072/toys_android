package com.yyqq.commen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GPSLocationUtils {

	private static SharedPreferences sp_Location = null; // 存储定位信息的sp文件
    private static Editor ed_Location = null; // 修改sp文件的对象
    private static String LOCATION_INFO = "location_Info"; // 存储地理位置信息的sp文件的文件名
    private static String LOCATION_LATITUDE = "location_Latitude"; // 经度的key
    private static String LOCATION_LONGITUDE = "location_Longitude"; // 纬度的key
	
	// 获取经度
	public static double getLatitude(Context context){
		sp_Location = context.getSharedPreferences(LOCATION_INFO, 0);
		String jingdu = sp_Location.getString(LOCATION_LATITUDE, 0.0+"");
		return Double.parseDouble(jingdu);
	}
	
	// 获取纬度
	public static double getLongitude(Context context){
		sp_Location = context.getSharedPreferences(LOCATION_INFO, 0);
		String weidu = sp_Location.getString(LOCATION_LONGITUDE, 0.0+"");
		return Double.parseDouble(weidu);
	}
	
	// 更新经度
	public static void setLatitude(Context context, double jingdu){
		sp_Location = context.getSharedPreferences(LOCATION_INFO, 0);
		ed_Location = sp_Location.edit();
		ed_Location.putString(LOCATION_LATITUDE, jingdu + "");
		ed_Location.commit();
	}
	
	// 更新纬度
	public static void setLongitude(Context context, double weidu){
		sp_Location = context.getSharedPreferences(LOCATION_INFO, 0);
		ed_Location = sp_Location.edit();
		ed_Location.putString(LOCATION_LONGITUDE, weidu + "");
		ed_Location.commit();
	}
	
}
