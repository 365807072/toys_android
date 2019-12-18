package com.yyqq.code.map;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class TencetMapActivity extends MapActivity implements
		TencentLocationListener {
	private String businessLocation;
	private String businessTitle;
	private LinearLayout ly_guide;
	private TextView business_title;
	private TextView business_location;
	private Activity context;
	private PopupWindowsPicture mPopWindowPicture;
	private MapView mapview;
	private View myGps;
	private TencentLocationRequest request;
	private TencentLocationManager locationManager;
	private int myPosition;
	private int bosPosition;
	private double myLatitude;
	private double myLongitude;
	private double t_myLatitude;
	private double t_myLongitude;
	private double latitude;
	private double longitude;
	private MapController mMapController = null;
	
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
		setContentView(R.layout.tencent_map);
		context = this;
		init();

		// 商家名称
		if (!TextUtils.isEmpty(getIntent().getStringExtra("business_title"))) {
			businessTitle = getIntent().getStringExtra("business_title");
			business_title.setText(businessTitle);
		}
		// 商家地址
		if (!TextUtils.isEmpty(getIntent().getStringExtra("business_location"))) {
			businessLocation = getIntent().getStringExtra("business_location");
			business_location.setText(businessLocation);
		}
		// 地图
		mapview = (MapView) findViewById(R.id.mapview);
		mapview.onCreate(savedInstanceState);
		// 获取地图控制器
		mMapController = mapview.getMapController();

		// 创建定位请求
		request = TencentLocationRequest.create();
		locationManager = TencentLocationManager.getInstance(this);
		// 画商家位置
		t_myLatitude = getIntent().getDoubleExtra("lat1", 0.0);
		t_myLongitude = getIntent().getDoubleExtra("log1", 0.0);
		latitude = getIntent().getDoubleExtra("lat", 0.0);
		longitude = getIntent().getDoubleExtra("log", 0.0);
		LatLng mLatLng = new LatLng(t_myLatitude, t_myLongitude);

		mMapController.animateTo(new GeoPoint((int) (t_myLatitude * 1e6),
				(int) (t_myLongitude * 1e6)));

		Marker mMarker = mapview.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.defaultMarker())
				.position(mLatLng).draggable(true));
//		Marker mMarker = mapview.addMarker(new MarkerOptions()
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.location_business))
//				.position(mLatLng).draggable(true));
		mMapController.setZoom(14);
	}

	private void init() {
		business_title = (TextView) findViewById(R.id.business_title);
		business_location = (TextView) findViewById(R.id.business_location);
		ly_guide = (LinearLayout) findViewById(R.id.ly_guide);
		ly_guide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopWindowPicture = new PopupWindowsPicture(context, ly_guide);
			}
		});

		// 我的位置
		myGps = findViewById(R.id.mygps);
		myGps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				myPosition = locationManager.requestLocationUpdates(request,
						TencetMapActivity.this);
			}
		});
	}

	int falg = 0;

	@Override
	public void onLocationChanged(TencentLocation arg0, int arg1, String arg2) {
		if (TencentLocation.ERROR_OK == myPosition) {
			// Toast.makeText(this, "" + arg0.toString(), 0).show();
			// 我的位置
			myLatitude = arg0.getLatitude();
			myLongitude = arg0.getLongitude();
			mMapController.animateTo(new GeoPoint(
					(int) (arg0.getLatitude() * 1e6), (int) (arg0
							.getLongitude() * 1e6)));
			MarkerOptions markOp = new MarkerOptions();
			markOp.position(new LatLng(myLatitude, myLongitude)).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.location_my));
			if (falg == 0) {
				falg++;
				mapview.addMarker(markOp);
			} else {
				mapview.refreshMap();
			}
			mMapController.setZoom(14);
		} else {
			// 定位失败
			Toast.makeText(this, "定位失败", 0).show();
		}
		locationManager.removeUpdates(this);
	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {

	}

	/**
	 * 检查手机上是否安装了指定的软件
	 * 
	 * @param context
	 * @param packageName
	 *            ：应用包名
	 * @return
	 */
	private boolean isAvilible(Context context, String packageName) {
		// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	/* 选择图片的pop */
	public class PopupWindowsPicture extends PopupWindow {
		public PopupWindowsPicture(Context mContext, View parent) {
			Button bd, gd, no, ly_cancel;
			LinearLayout ly_no, ly_bd, ly_gd;
			View view = View.inflate(mContext, R.layout.map_item_popupwindows,
					null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_1));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			// 布局
			ly_bd = (LinearLayout) view.findViewById(R.id.ly_bd);
			ly_gd = (LinearLayout) view.findViewById(R.id.ly_gd);
			ly_no = (LinearLayout) view.findViewById(R.id.ly_no);
			// 按钮
			bd = (Button) view.findViewById(R.id.bd);
			gd = (Button) view.findViewById(R.id.gd);
			ly_cancel = (Button) view.findViewById(R.id.ly_cancel);
			no = (Button) view.findViewById(R.id.no);

			if (!isAvilible(context, "com.baidu.BaiduMap")
					&& !isAvilible(context, "com.autonavi.minimap")) {
				ly_no.setVisibility(View.VISIBLE);
			}
			if (!isAvilible(context, "com.baidu.BaiduMap")) {
				ly_bd.setVisibility(View.GONE);
			}
			if (!isAvilible(context, "com.autonavi.minimap")) {
				ly_gd.setVisibility(View.GONE);
			}

			/**
			 * 高德地图
			 */
			gd.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							"android.intent.action.VIEW",
							android.net.Uri
									.parse("androidamap://navi?sourceApplication=宝宝秀秀&poiid=BGVIS1&lat="
											+ t_myLatitude
											+ "&lon="
											+ t_myLongitude
											+ "&style=2&level=10&dev=0"));
					intent.setPackage("com.autonavi.minimap");
					startActivity(intent);
					dismiss();
				}
			});
			/**
			 * 百度地图
			 */
			bd.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						Intent intent = Intent
								.getIntent("intent://map/direction?destination=latlng:"
										+ latitude
										+ ","
										+ longitude
										+ "|src=美美科技|宝宝秀秀#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
						startActivity(intent); // 启动调用
						dismiss();
					} catch (URISyntaxException e) {
						Log.e("intent", e.getMessage());
					}

				}
			});

			/**
			 * 取消
			 */
			ly_cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

}
