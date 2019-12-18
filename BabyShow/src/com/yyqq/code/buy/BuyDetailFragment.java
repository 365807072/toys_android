package com.yyqq.code.buy;

import com.ab.http.AbHttpUtil;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.SyncHorizontalScrollView;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuyDetailFragment extends FragmentActivity {

	public static final String ARGUMENTS_NAME = "arg";
	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ViewPager mViewPager;
	private int tabitemWidth;
	// public static String[] tabTitle = { "", "", "", "" }; // 标题
	private static String[] tabTitle ; // 标题
	// public static String[] tabTitle; // 标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	private Activity context;
	private AbHttpUtil ab;
	private static String buy_type;
	private TextView title;
	
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
		setContentView(R.layout.buy_detail_fragment);
		ab = AbHttpUtil.getInstance(context);
		buy_type = getIntent().getStringExtra("buy_type");
		// if("5".equals(buy_type)){
		// String[] tabTitle = { "童装", "童鞋配饰", "书本玩具", "婴幼用品" }; // 标题
		// }else{
		// String[] tabTitle = { "服装", "美妆", "鞋包配饰", "辣妈日用" }; // 标题
		// }
		//"服装,美妆,鞋包配饰"
		tabTitle = getIntent().getStringExtra("tabTitle").split(",");
		findViewById();
		initView();
		setListener();
	}

	private void setListener() {

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (rg_nav_content != null
						&& rg_nav_content.getChildCount() > position) {
					((RadioButton) rg_nav_content.getChildAt(position))
							.performClick();
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		rg_nav_content
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (rg_nav_content.getChildAt(checkedId) != null) {

							LayoutParams cursor_Params = iv_nav_indicator
									.getLayoutParams();
							cursor_Params.width = ((RadioButton) rg_nav_content
									.getChildAt(checkedId)).getWidth();// 初始化滑动下标的宽
							iv_nav_indicator.setLayoutParams(cursor_Params);

							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									((RadioButton) rg_nav_content
											.getChildAt(checkedId)).getLeft(),
									0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);
							// 执行位移动画
							iv_nav_indicator.startAnimation(animation);

							mViewPager.setCurrentItem(checkedId); // ViewPager
																	// 跟随一起 切换

							// 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) rg_nav_content
									.getChildAt(checkedId)).getLeft();

							mHsv.smoothScrollTo(
									(checkedId > 1 ? ((RadioButton) rg_nav_content
											.getChildAt(checkedId)).getLeft()
											: 0)
											- ((RadioButton) rg_nav_content
													.getChildAt(2)).getLeft(),
									0);
						}
					}
				});
	}

	private void initView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		mHsv.setSomeParam(rl_nav, iv_nav_indicator, iv_nav_indicator, this);

		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		// 另一种方式获取
		// LayoutInflater mInflater = LayoutInflater.from(this);

		initNavigationHSV();

		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}

	private int getIndicatorWidth(String text) {
		Paint paint = new Paint();
		paint.setTextSize(21.6f);// 设置字符大小
		return (int) paint.measureText(text, 0, text.length());
	}

	private void initNavigationHSV() {

		rg_nav_content.removeAllViews();
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		tabitemWidth = dm.widthPixels / 4;
		for (int i = 0; i < tabTitle.length; i++) {
			// indicatorWidth[i] = getIndicatorWidth(tabTitle[i]);

			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(tabitemWidth,
					LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = tabitemWidth;// 初始化滑动下标的宽
		((RadioButton) rg_nav_content.getChildAt(0)).performClick();
		iv_nav_indicator.setLayoutParams(cursor_Params);
	}

	private void findViewById() {
		context = this;
		title = (TextView) findViewById(R.id.title);
		if ("1".equals(buy_type)) {
			title.setText("0-1岁");
		} else if ("2".equals(buy_type)) {
			title.setText("1-3岁");
		} else if ("3".equals(buy_type)) {
			title.setText("3-6岁");
		} else if ("4".equals(buy_type)) {
			title.setText("6-12岁");
		} else if ("5".equals(buy_type)) {
			title.setText("辣妈");
		}

		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);

		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);

		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);

		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment ft = new BuyFragment();
			Bundle args = new Bundle();
			args.putString("buy_type", buy_type);
			switch (arg0) {
			case 0:
				args.putString("buy_class", "1");
				ft.setArguments(args);
				break;
			case 1:
				args.putString("buy_class", "2");
				ft.setArguments(args);
				break;
			case 2:
				args.putString("buy_class", "3");
				ft.setArguments(args);
				break;
			default:
				args.putString("buy_class", "4");
				ft.setArguments(args);
				break;
			}
			return ft;
		}

		@Override
		public int getCount() {

			return tabTitle.length;
		}

	}

}
