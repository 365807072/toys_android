package com.yyqq.code.personal;

import java.util.Calendar;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditBaby extends Activity {
	private String tag = "EditBaby";
	private TextView delete, name, title_name, birth, sex;
	private RelativeLayout name_root, birth_root, sex_root;
	private JSONObject baby;
	private Activity context;
	private TextView currentEditBirth;
	private int year, month, day;
	private int mYear, mMonth, mDay;
	private AbHttpUtil ah;
	private String born_date;

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.edit_baby);
		context = this;
		ah = AbHttpUtil.getInstance(context);
		delete = (TextView) findViewById(R.id.delete);
		delete.setOnClickListener(deleteClick);
		name = (TextView) findViewById(R.id.name);
		title_name = (TextView) findViewById(R.id.title_name);
		birth = (TextView) findViewById(R.id.birth);
		birth.setOnClickListener(birthClick);
		sex = (TextView) findViewById(R.id.sex);
		name_root = (RelativeLayout) findViewById(R.id.name_root);
		birth_root = (RelativeLayout) findViewById(R.id.birth_root);
		sex_root = (RelativeLayout) findViewById(R.id.sex_root);
		name_root.setOnClickListener(nameClick);
		try {
			baby = new JSONObject(getIntent().getStringExtra("baby"));
			born_date = baby.getString("born_date");
			name.setText(baby.getString("baby_name"));
			title_name.setText(baby.getString("baby_name"));
			birth.setText(born_date);
			Log.e("fanfan", born_date);
			if (baby.getInt("baby_sex") == 0) {
				sex.setText("男");
			} else {
				sex.setText("女");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 修改昵称
	public OnClickListener nameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("修改昵称");
			final EditText edit_name = new EditText(context);
			edit_name.setSingleLine(true);
			edit_name.setMaxEms(14);
			edit_name.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (Config.getMsgNum(s.toString()) < 0) {
						edit_name.setText(s.subSequence(0, s.length() - count));
						Toast.makeText(context, "宝宝名称最多7个汉字或者14个字母",
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			builder.setView(edit_name);
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
							final String sname = edit_name.getText().toString()
									.trim();
							if (sname.length() > 0) {
								Config.showProgressDialog(context, true, null);
								AjaxParams params = new AjaxParams();
								params.put("user_id",
										Config.getUser(context).uid);
								try {
									params.put("baby_id", baby.getInt("id")
											+ "");
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								params.put("baby_name", sname);
								FinalHttp fh = new FinalHttp();
								fh.post(ServerMutualConfig.DoBaby, params,
										new AjaxCallBack<String>() {
											@Override
											public void onFailure(Throwable t,
													String strMsg) {
												super.onFailure(t, strMsg);
												Config.dismissProgress();
												Config.showFiledToast(context);
											}

											@Override
											public void onSuccess(String t) {
												super.onSuccess(t);
												Config.dismissProgress();
												try {
													JSONObject json = new JSONObject(
															t);
													if (json.getBoolean("success")) {
														name.setText(sname);
														title_name
																.setText(sname);
													}
													{
														Toast.makeText(
																context,
																json.getString("reMsg"),
																Toast.LENGTH_SHORT)
																.show();
													}
												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										});
							}
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
		}
	};

	// 修改生日
	public OnClickListener birthClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			currentEditBirth = (TextView) v;
			showDialog(0);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		String[] strs = born_date.split("-");
		if (strs.length == 3) {
			mYear = Integer.valueOf(strs[0]);
			mMonth = Integer.valueOf(strs[1]) - 1;
			mDay = Integer.valueOf(strs[2]);
		} else {
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR); // 获取当前年份
			mMonth = c.get(Calendar.MONTH);// 获取当前月份
			mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		}
		switch (id) {
		case 0:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (year > Calendar.getInstance().get(Calendar.YEAR)) {
				year = Calendar.getInstance().get(Calendar.YEAR);
			}
			EditBaby.this.year = year;
			EditBaby.this.month = monthOfYear + 1;
			EditBaby.this.day = dayOfMonth;
			String tempMonth = month + "", tempDay = day + "";
			if (month < 10) {
				tempMonth = "0" + month;
			}
			if (day < 10) {
				tempDay = "0" + day;
			}
			currentEditBirth.setText(EditBaby.this.year + "-" + tempMonth + "-"
					+ tempDay);
			AbRequestParams params = new AbRequestParams();
			try {
				params.put("birth_date", EditBaby.this.year + "-" + tempMonth
						+ "-" + tempDay);
				params.put("baby_id", baby.getInt("id") + "");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			// Log.e("fanfan", params.toString());
			ah.post(ServerMutualConfig.UpBabyBirthday, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							JSONObject json;
							try {
								json = new JSONObject(content);
								if (json.getBoolean("success")) {
									Toast.makeText(context, "生日修改成功", 0).show();
								} else {
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
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
						}
					});

		}
	};

	// 删除宝宝
	public OnClickListener deleteClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("真的要删除宝宝么?");
			builder.setMessage("如果删除宝宝,他的相册也会被删除哦!");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
							Config.showProgressDialog(context, true, null);
							AjaxParams params = new AjaxParams();
							params.put("user_id", Config.getUser(context).uid);
							try {
								params.put("baby_id", baby.getInt("id") + "");
								params.put("do_type", "1");
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							FinalHttp fh = new FinalHttp();
							fh.post(ServerMutualConfig.DoBaby, params,
									new AjaxCallBack<String>() {
										@Override
										public void onFailure(Throwable t,
												String strMsg) {
											super.onFailure(t, strMsg);
											Config.dismissProgress();
											Config.showFiledToast(context);
										}

										@Override
										public void onSuccess(String t) {
											super.onSuccess(t);
											Config.dismissProgress();
											try {
												JSONObject json = new JSONObject(
														t);
												if (json.getBoolean("success")) {
													finish();
												}
												{
													Toast.makeText(
															context,
															json.getString("reMsg"),
															Toast.LENGTH_SHORT)
															.show();
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
		}
	};
}