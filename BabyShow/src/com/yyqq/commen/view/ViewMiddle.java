package com.yyqq.commen.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.TextAdapter;
import com.yyqq.commen.model.ShaiXuanItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;
import com.yyqq.framework.callback.ViewBaseAction;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

	public static LinearLayout search_root; // 搜索框
	public EditText search_input;
	public ImageView search_start;
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "不限";

	private AbHttpUtil ab;
	private String TAG = "fanfan_ViewMiddle";
	private ArrayList<ShaiXuanItem> data = new ArrayList<ShaiXuanItem>();

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		ab = AbHttpUtil.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		
		search_root = (LinearLayout) findViewById(R.id.search_root);
		search_input = (EditText) findViewById(R.id.search_input);
		ExpandTabView.editText = search_input;
		search_start = (ImageView) findViewById(R.id.search_start);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		onRefresh(context);

		earaListViewAdapter = new TextAdapter(context, groups,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		//左边
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		//右边
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							ExpandTabView.onPressBack();
							mOnSelectListener.getValue(showString);
							
						}

					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	Map<String, String> mCityIds = null;

	public void onRefresh(Context context) {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
//		Log.e(TAG,
//				ServerMutualConfig.BusinessCityList + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.BusinessCityList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								ShaiXuanItem item = new ShaiXuanItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								data.add(item);
							}
							getData();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
					}

				});
	}

	public void getData() {
		mCityIds = new HashMap<String, String>();
		for (int i = 0; i < data.size(); i++) {
			groups.add(data.get(i).city_name);
			LinkedList<String> tItem = new LinkedList<String>();
			for (int j = 0; j < data.get(i).listChildren.size(); j++) {
				tItem.add(data.get(i).listChildren.get(j).city_name);
				mCityIds.put(data.get(i).listChildren.get(j).city_name,
						data.get(i).listChildren.get(j).id);
			}
			children.put(i, tItem);
		}
	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	public Map<String, String> getCityIDs() {
		if (mCityIds == null)
			return mCityIds = new HashMap<String, String>();
		return mCityIds;
	}
}
