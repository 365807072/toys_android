package com.yyqq.code.toyslease;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.ArrayWheelAdapter;
import com.yyqq.commen.model.CityModel;
import com.yyqq.commen.model.DistrictModel;
import com.yyqq.commen.model.ProvinceModel;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.WheelView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;
import com.yyqq.framework.myinterface.OnWheelChangedListener;
import com.yyqq.framework.myinterface.OnWheelClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 收货地址选择
 * */
public class ToysLeaseAddressSelectActivity extends BaseActivity implements OnClickListener, OnWheelChangedListener {

	private ImageView adrs_back;
	private LinearLayout adrs_name;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private LinearLayout addr_select_all;
	private RelativeLayout addr_select_all_bg;
	private EditText adrs_select;
	private EditText adrs_detail;
	private Button confirm_adrs;
	private TextView btn_confirm;
	private TextView btn_cancel;
	
	private AbHttpUtil ab;
	private List<ProvinceModel> provinceList = null;
	public static ToysLeaseAddressSelectActivity toysLeaseAddressSelectActivity = null;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";
	
	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode ="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toys_lease_address_select);
	}

	@Override
	protected void initView() {
		 adrs_back = (ImageView) findViewById(R.id.adrs_back);
		 adrs_name = (LinearLayout) findViewById(R.id.adrs_name);
//		 addr_select_all = (LinearLayout) findViewById(R.id.addr_select_all);
		 addr_select_all_bg = (RelativeLayout) findViewById(R.id.addr_select_all_bg);
		 adrs_select = (EditText) findViewById(R.id.adrs_select);
		 adrs_detail = (EditText) findViewById(R.id.adrs_detail);
		 confirm_adrs = (Button) findViewById(R.id.confirm_adrs);
		 btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		 btn_cancel = (TextView) findViewById(R.id.btn_cancel);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(ToysLeaseAddressSelectActivity.this);
		toysLeaseAddressSelectActivity = this;
		
		setUpViews();
		setUpListener();
		initSelectList();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void setListener() {
		
		mViewDistrict.addClickingListener(new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
			}
		});
		
		adrs_select.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!addr_select_all_bg.isShown()){
					addr_select_all_bg.setVisibility(View.VISIBLE);
				}
				InputMethodManager mg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mg.hideSoftInputFromWindow(adrs_select.getWindowToken(), 0);
				return true;
			}
		});
		
		addr_select_all_bg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(addr_select_all_bg.isShown()){
					addr_select_all_bg.setVisibility(View.GONE);
				}
			}
		});
		
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(addr_select_all_bg.isShown()){
					addr_select_all_bg.setVisibility(View.GONE);
					showSelectedResult();
				}
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(addr_select_all_bg.isShown()){
					addr_select_all_bg.setVisibility(View.GONE);
				}
			}
		});
		
		adrs_detail.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(adrs_select.getText().length() != 0){
					return false;
				}else{
					addr_select_all_bg.setVisibility(View.VISIBLE);
					InputMethodManager mg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mg.hideSoftInputFromWindow(adrs_select.getWindowToken(), 0);
					return true;
				}
			}
		});
		
		confirm_adrs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editAddressInfo();
			}
		});
		
		findViewById(R.id.adrs_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseAddressSelectActivity.this.finish();
			}
		});
	}
	
	/**
	 * 获取收货地址列表
	 * */
	private void initSelectList(){
		Config.showProgressDialog(ToysLeaseAddressSelectActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseAddressSelectActivity.this).uid);
		ab.get(ServerMutualConfig.GET_CITY_LIST + "&" + params.toString(), new AbStringHttpResponseListener() {
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject jsonObj = new JSONObject(content);
							if(jsonObj.getBoolean("success")){
								provinceList = new ArrayList<ProvinceModel>();
								ProvinceModel provinceModel = new ProvinceModel(); // 城市
								JSONArray jsonArr = jsonObj.getJSONArray("data");
								ArrayList<CityModel> cityList01 = new ArrayList<CityModel>();
								for(int i = 0 ; i < jsonArr.length() ; i++ ){
									JSONObject jsonBean = jsonArr.getJSONObject(i);
									CityModel cityModel = new CityModel(); // 分区
									cityModel.setName(jsonBean.getString("city_name"));
									ArrayList<DistrictModel> disList = new ArrayList<DistrictModel>();
									for(int j = 0 ; j < jsonBean.getJSONArray("children").length() ; j++){
										JSONObject JsonDis = jsonBean.getJSONArray("children").getJSONObject(j);
										DistrictModel districtModel = new DistrictModel(); // 地区
										districtModel.setName(JsonDis.getString("city_name"));
										districtModel.setZipcode(JsonDis.getString("city_id"));
										disList.add(districtModel);
									}
									cityModel.setDistrictList(disList);
									cityList01.add(cityModel);
								}
//								provinceModel.setName("北京市");
								provinceModel.setName("");
								provinceModel.setCityList(cityList01);
								provinceList.add(provinceModel);
								
								
								mProvinceDatas = new String[provinceList.size()];
					        	for (int i=0; i< provinceList.size(); i++) {
					        		// 遍历所有省的数据
					        		mProvinceDatas[i] = provinceList.get(i).getName();
					        		List<CityModel> cityList = provinceList.get(i).getCityList();
					        		String[] cityNames = new String[cityList.size()];
					        		for (int j=0; j< cityList.size(); j++) {
					        			// 遍历省下面的所有市的数据
					        			cityNames[j] = cityList.get(j).getName();
					        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					        			String[] distrinctNameArray = new String[districtList.size()];
					        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					        			for (int k=0; k<districtList.size(); k++) {
					        				// 遍历市下面所有区/县的数据
					        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
					        				// 区/县对于的邮编，保存到mZipcodeDatasMap
					        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
					        				distrinctArray[k] = districtModel;
					        				distrinctNameArray[k] = districtModel.getName();
					        			}
					        			// 市-区/县的数据，保存到mDistrictDatasMap
					        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
					        		}
					        		// 省-市的数据，保存到mCitisDatasMap
					        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
					        	}
					        	
					        	mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(ToysLeaseAddressSelectActivity.this, mProvinceDatas));
					    		// 设置可见条目数量
					    		mViewProvince.setVisibleItems(7);
					    		mViewCity.setVisibleItems(7);
					    		mViewDistrict.setVisibleItems(7);
					    		updateCities();
					    		updateAreas();
					    		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
								mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
							}else{
								Toast.makeText(ToysLeaseAddressSelectActivity.this, "获取地区数据失败，请重试", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
					}
				});
		
	}

	/**
	 * 提交修改地址
	 * */
	private void editAddressInfo(){
		
		if(adrs_detail.getText().toString().trim().length() <= 4){
			Toast.makeText(ToysLeaseAddressSelectActivity.this, "请填写详细地址，不少于5个字", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(adrs_select.getText().toString().trim().equals("")){
			Toast.makeText(ToysLeaseAddressSelectActivity.this, "请选择地区", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Config.showProgressDialog(ToysLeaseAddressSelectActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseAddressSelectActivity.this).uid);
		params.put("city_id", mCurrentZipCode);
		params.put("address", adrs_detail.getText().toString().trim());
		ab.post(ServerMutualConfig.EDIT_ADDRESS, params, new AbStringHttpResponseListener() {
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject jsonObj = new JSONObject(content);
							if(jsonObj.getBoolean("success")){
								ToysLeaseAddressSelectActivity.this.finish();
								ToysLeaseOrderConfirmActivity.UPDATE_ADDR = true;
								ToysLeaseOrderConfirmActivity.ADDR_DETAIL = jsonObj.getJSONObject("data").getString("address");
								ToysLeaseOrderConfirmActivity.ADDR_ID  = jsonObj.getJSONObject("data").getString("city_id");
								ToysLeaseOrderConfirmActivity.DIS_PROMPT = jsonObj.getJSONObject("data").getString("dis_prompt");
							}else{
								Toast.makeText(ToysLeaseAddressSelectActivity.this, jsonObj.getString("reMsg"), Toast.LENGTH_SHORT).show();
							}
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
					}
				});
	}

	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
	}
	
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this);
    	
    }
	
	

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			showSelectedResult();
			break;
		default:
			break;
		}
	}

	private void showSelectedResult() {
		adrs_select.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	    adrs_detail.requestFocus();
	}
}
