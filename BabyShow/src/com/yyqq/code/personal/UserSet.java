package com.yyqq.code.personal;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.login.ChangePwd;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.main.AddNewPostActivity.PopupWindows;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class UserSet extends Activity {
	private String fuwuTxt = "重要须知\n自由环球在此特别提醒用户认真阅读、充分理解本《服务协议》（下称《协议》）\n" +
			"\n" +
			"    用户应认真阅读、充分理解本《协议》中各条款，包括免除或者限制自由环球责任的免责条款及对用户的权利限制条款。请您审慎阅读并选择接受或不接受本《协议》（未成年人应在法定监护人陪同下阅读）。除非您接受本《协议》所有条款，否则您无权注册、登录或使用本协议所涉相关服务。您的注册、登录、使用等行为将视为对本《协议》的接受，并同意接受本《协议》各项条款的约束。本《协议》描述自由环球与用户之间关于“自由环球租赁”服务相关方面的权利义务。“用户”是指注册、登录、使用、浏览本服务的个人或组织。本《协议》可由自由环球随时更新，更新后的协议条款一旦公布即代替原来的协议条款，恕不再另行通知，用户可在本网站查阅最新版协议条款。在自由环球修改《协议》条款后，如果用户不接受修改后的条款，请立即停止使用自由环球提供的服务，用户继续使用自由环球提供的服务将被视为已接受了修改后的协议。\n" +
			"\n" +
			"一、使用规则\n" +
			"\n" +
			"    1、用户充分了解并同意，自由环球自由环球租赁仅为用户提供信息分享、传送及获取的平台，用户必须为自己注册账号下的一切行为负责，包括您所传送的任何内容以及由此产生的任何结果。用户应对自由环球自由环球租赁中的内容自行加以判断，并承担因使用内容而引起的所有风险，包括因对内容的正确性、完整性或实用性的依赖而产生的风险。舜鑫国际旅游（北京）有限公司无法且不会对因用户行为而导致的任何损失或损害承担责任。\n" +
			"    2、用户在自由环球自由环球租赁服务中或通过自由环球自由环球租赁服务所传送的任何内容并不反映舜鑫国际旅游（北京）有限公司的观点或政策，舜鑫国际旅游（北京）有限公司对此不承担任何责任。\n" +
			"    3、用户充分了解并同意，自由环球自由环球租赁是一个基于用户关系网的点对点即时通讯产品，用户须对在自由环球自由环球租赁上的注册信息的真实性、合法性、有效性承担全部责任，用户不得冒充他人；不得利用他人的名义传播任何信息；不得恶意使用注册账号导致其他用户误认；否则舜鑫国际旅游（北京）有限公司有权立即停止提供服务，收回自由环球自由环球租赁账号并由用户独自承担由此而产生的一切法律责任。\n" +
			"    4、用户须对在自由环球自由环球租赁上所传送信息的真实性、合法性、无害性、有效性等全权负责，与用户所传播的信息相关的任何法律责任由用户自行承担，与自由环球无关。\n" +
			"    5、自由环球保留因业务发展需要，单方面对本服务的全部或部分服务内容在任何时候不经任何通知的情况下变更、暂停、限制、终止或撤销自由环球自由环球租赁服务的权利，用户需承担此风险。\n" +
			"    6、自由环球自由环球租赁提供的服务中可能包括广告，用户同意在使用过程中显示自由环球自由环球租赁和第三方供应商、合作伙伴提供的广告。\n" +
			"    7、用户不得利用自由环球自由环球租赁或自由环球自由环球租赁服务制作、上载、复制、发送如下内容：\n" +
			"        (1) 反对宪法所确定的基本原则的；\n" +
			"        (2) 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；\n" +
			"        (3) 损害国家荣誉和利益的；\n" +
			"        (4) 煽动民族仇恨、民族歧视，破坏民族团结的；\n" +
			"        (5) 破坏国家宗教政策，宣扬邪教和封建迷信的；\n" +
			"        (6) 散布谣言，扰乱社会秩序，破坏社会稳定的；\n" +
			"        (7) 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；\n" +
			"        (8) 侮辱或者诽谤他人，侵害他人合法权益的；\n" +
			"        (9) 含有法律、行政法规禁止的其他内容的信息。\n" +
			"    8、舜鑫国际旅游（北京）有限公司可依其合理判断，对违反有关法律法规或本协议约定；或侵犯、妨害、威胁任何人权利或安全的内容，或者假冒他人的行为，舜鑫国际旅游（北京）有限公司有权依法停止传输任何前述内容，并有权依其自行判断对违反本条款的任何人士采取适当的法律行动，包括但不限于，从自由环球自由环球租赁服务中删除具有违法性、侵权性、不当性等内容，终止违反者的成员资格，阻止其使用自由环球自由环球租赁全部或部分服务，并且依据法律法规保存有关信息并向有关部门报告等。\n" +
			"    9、用户权利及义务：\n" +
			"        (1) 自由环球自由环球租赁账号的所有权归舜鑫国际旅游（北京）有限公司所有，用户完成申请注册手续后，获得自由环球自由环球租赁账号的使用权，该使用权仅属于初始申请注册人，禁止赠与、借用、租用、转让或售卖。舜鑫国际旅游（北京）有限公司因经营需要，有权回收用户的自由环球自由环球租赁账号。\n" +
			"        (2) 用户有权更改、删除在自由环球自由环球租赁上的个人资料、注册信息及传送内容等，但需注意，删除有关信息的同时也会删除任何您储存在系统中的文字和图片。用户需承担该风险。\n" +
			"        (3) 用户有责任妥善保管注册账号信息及账号密码的安全，用户需要对注册账号以及密码下的行为承担法律责任。用户同意在任何情况下不使用其他成员的账号或密码。在您怀疑他人在使用您的账号或密码时，您同意立即通知舜鑫国际旅游（北京）有限公司。\n" +
			"        (4) 用户应遵守本协议的各项条款，正确、适当地使用本服务，如因用户违反本协议中的任何条款，舜鑫国际旅游（北京）有限公司有权依据协议终止对违约用户自由环球自由环球租赁账号提供服务。同时，自由环球保留在任何时候收回自由环球自由环球租赁账号、用户名的权利。\n" +
			"        (5) 用户注册自由环球自由环球租赁账号后如果长期不登录该账号，自由环球有权回收该账号，以免造成资源浪费，由此带来问题均由用户自行承担。\n" +
			"\n" +
			"二、隐私保护\n" +
			"\n" +
			"    用户同意个人隐私信息是指那些能够对用户进行个人辨识或涉及个人通信的信息，包括下列信息：用户真实姓名，身份证号，手机号码，IP地址。而非个人隐私信息是指用户对本服务的操作状态以及使用习惯等一些明确且客观反映在自由环球服务器端的基本记录信息和其他一切个人隐私信息范围外的普通信息；以及用户同意公开的上述隐私信息；尊重用户个人隐私信息的私有性是自由环球的一贯制度，自由环球将会采取合理的措施保护用户的个人隐私信息，除法律或有法律赋予权限的政府部门要求或用户同意等原因外，自由环球未经用户同意不向除合作单位以外的第三方公开、 透露用户个人隐私信息。 但是，用户在注册时选择同意，或用户与自由环球及合作单位之间就用户个人隐私信息公开或使用另有约定的除外，同时用户应自行承担因此可能产生的任何风险，自由环球对此不予负责。同时，为了运营和改善自由环球的技术和服务，自由环球将可能会自行收集使用或向第三方提供用户的非个人隐私信息，这将有助于自由环球向用户提供更好的用户体验和提高自由环球的服务质量。用户同意，在使用自由环球自由环球租赁服务时也同样受自由环球隐私政策的约束。当您接受本协议条款时，您同样认可并接受舜鑫国际旅游（北京）有限公司隐私政策的条款。\n" +
			"\n" +
			"三、自由环球自由环球租赁商标信息\n" +
			"\n" +
			"    自由环球自由环球租赁服务中所涉及的图形、文字或其组成，以及其他自由环球标志及产品、服务名称，均为舜鑫国际旅游（北京）有限公司之商标（以下简称“自由环球标识”）。未经自由环球事先书面同意，用户不得将自由环球标识以任何方式展示或使用或作其他处理，也不得向他人表明您有权展示、使用、或其他有权处理自由环球标识的行为。\n" +
			"\n" +
			"四、法律责任及免责\n" +
			"\n" +
			"    1、用户违反本《协议》或相关的服务条款的规定，导致或产生的任何第三方主张的任何索赔、要求或损失，包括合理的律师费，用户同意赔偿自由环球与合作公司、关联公司，并使之免受损害。\n" +
			"    2、用户因第三方如电信部门的通讯线路故障、技术问题、网络、电脑故障、系统不稳定性及其他各种不可抗力原因而遭受的一切损失，自由环球及合作单位不承担责任。\n" +
			"    3、因技术故障等不可抗事件影响到服务的正常运行的，自由环球及合作单位承诺在第一时间内与相关单位配合，及时处理进行修复，但用户因此而遭受的一切损失，自由环球及合作单位不承担责任。\n" +
			"    4、本服务同大多数互联网服务一样，受包括但不限于用户原因、网络服务质量、社会环境等因素的差异影响，可能受到各种安全问题的侵扰，如他人利用用户的资料，造成现实生活中的骚扰；用户下载安装的其它软件或访问的其他网站中含有“特洛伊木马”等病毒，威胁到用户的计算机信息和数据的安全，继而影响本服务的正常使用等等。用户应加强信息安全及使用者资料的保护意识，要注意加强密码保护，以免遭致损失和骚扰。\n" +
			"    5、用户须明白，使用本服务因涉及Internet服务，可能会受到各个环节不稳定因素的影响。因此，本服务存在因不可抗力、计算机病毒或黑客攻击、系统不稳定、用户所在位置、用户关机以及其他任何技术、互联网络、通信线路原因等造成的服务中断或不能满足用户要求的风险。用户须承担以上风险，舜鑫国际旅游（北京）有限公司不作担保。对因此导致用户不能发送和接受阅读信息、或接发错信息，舜鑫国际旅游（北京）有限公司不承担任何责任。\n" +
			"    6、用户须明白，在使用本服务过程中存在有来自任何他人的包括威胁性的、诽谤性的、令人反感的或非法的内容或行为或对他人权利的侵犯（包括知识产权）的匿名或冒名的信息的风险，用户须承担以上风险，舜鑫国际旅游（北京）有限公司和合作公司对本服务不作任何类型的担保，不论是明确的或隐含的，包括所有有关信息真实性、适商性、适于某一特定用途、所有权和非侵权性的默示担保和条件，对因此导致任何因用户不正当或非法使用服务产生的直接、间接、偶然、特殊及后续的损害，不承担任何责任。\n" +
			"    7、舜鑫国际旅游（北京）有限公司定义的信息内容包括：文字、软件、声音、相片、录像、图表；在广告中全部内容；舜鑫国际旅游（北京）有限公司为用户提供的商业信息，所有这些内容受版权、商标权、和其它知识产权和所有权法律的保护。所以，用户只能在舜鑫国际旅游（北京）有限公司和广告商授权下才能使用这些内容，而不能擅自复制、修改、编纂这些内容、或创造与内容有关的衍生产品。\n" +
			"    8、在任何情况下，舜鑫国际旅游（北京）有限公司均不对任何间接性、后果性、惩罚性、偶然性、特殊性或刑罚性的损害，包括因用户使用自由环球自由环球租赁而遭受的利润损失，承担责任（即使自由环球自由环球租赁已被告知该等损失的可能性亦然）。尽管本协议中可能含有相悖的规定，舜鑫国际旅游（北京）有限公司对您承担的全部责任，无论因何原因或何种行为方式，始终不超过您在成员期内因使用自由环球自由环球租赁而支付给舜鑫国际旅游（北京）有限公司的费用(如有) 。\n" +
			"\n" +
			"五、自由环球租赁社区管理规则\n" +
			"\n" +
			"    自由环球租赁是和现实相关的社交产品，希望用户相互尊重，遵循和现实社会一样的社交礼仪。\n" +
			"    为避免遭到用户举报而被封禁设备，请您遵守以下原则：\n" +
			"    1、请勿发送涉嫌性骚扰的文字、图片及语音信息；\n" +
			"    2、请勿使用含色情、淫秽意味或其他令人不适的头像或资料；\n" +
			"    3、请勿在交谈中使用辱骂、恐吓、威胁等言论；\n" +
			"    4、请勿发布各类垃圾广告、恶意信息、诱骗信息；\n" +
			"    5、请勿盗用他人头像或资料，请勿伪装他人身份；\n" +
			"    6、请勿发布不当政治言论或者任何违反国家法规政策的言论。\n" +
			"    用户一旦被禁言，可通过关于中的邮箱或电话联系我们，申请解禁。\n" +
			"    如用户违反社区管理规则，自由环球有权依据协议终止对违约用户自由环球租赁帐号提供服务。同时，自由环球保留在任何时候收回自由环球租赁帐号的权力。\n" +
			"    如用户违反社区管理规则，舜鑫国际旅游（北京）有限公司有权依据协议终止对违约用户自由环球自由环球租赁账号提供服务。同时，自由环球保留在任何时候收回自由环球租赁账号的权力。\n" +
			"\n" +
			"六、其他条款\n" +
			"\n" +
			"    1、舜鑫国际旅游（北京）有限公司郑重提醒用户注意本《协议》中免除舜鑫国际旅游（北京）有限公司责任和加重用户义务的条款，请用户仔细阅读，自主考虑风险。未成年人应在法定监护人的陪同下阅读本《协议》。以上各项条款内容的最终解释权及修改权归舜鑫国际旅游（北京）有限公司所有。\n" +
			"    2、本《协议》所定的任何条款的部分或全部无效者，不影响其它条款的效力。\n" +
			"    3、本《协议》的版权由自由环球所有，自由环球保留一切解释和修改权利。";
	private LinearLayout babyRoot;
	private RoundAngleImageView head;
	private TextView name, regist_name, regist_email;
	private Activity context;
	private String TAG = "UserSet";
    private String aboutTxt = " \n舜鑫国际旅游（北京）有限公司\n";
	private RelativeLayout about, fuwu, logout, head_root, name_root,
			change_pwd, bind;
	private SharedPreferences shared;
	private String user_type = "0";
	private AlertDialog.Builder OutDialog;
	private GridView noScrollgridview;
	
	// 调用相册后返回标记
	public static final String UserSet_TAG = "UserSet_TAG";
	private static final int TAKE_PICTURE = 0x000000;
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		getBabyInfo();
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.user_set);
		shared = this.getSharedPreferences("babyshow_user", this.MODE_PRIVATE);
		if ("1".equals(shared.getString("client_type", "0"))) {
			user_type = "1";
		} else if ("2".equals(shared.getString("client_type", "0"))) {
			user_type = "2";
		} else {
			user_type = "0";
		}
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		babyRoot = (LinearLayout) findViewById(R.id.baby_root);
		head = (RoundAngleImageView) findViewById(R.id.head);
		head.setOnClickListener(headClick);
		head_root = (RelativeLayout) findViewById(R.id.head_root);
		head_root.setOnClickListener(headClick);
		name_root = (RelativeLayout) findViewById(R.id.name_root);
		name_root.setOnClickListener(nameClick);
		context = this;
		bind = (RelativeLayout) findViewById(R.id.bind);
		bind.setOnClickListener(bindClick);
		MyApplication.getInstance().display(Config.getUser(context).head_thumb,
				head, R.drawable.def_head);
		name = (TextView) findViewById(R.id.name);
		name.setText(Config.getUser(context).nickname);
		name.setOnClickListener(nameClick);

		regist_name = (TextView) findViewById(R.id.regist_name);
		regist_name.setText(Config.getUser(context).getUsername());

		regist_email = (TextView) findViewById(R.id.regist_email);
		regist_email.setText(Config.getUser(context).getEmail());

		String pkName = getPackageName();
		try {
			aboutTxt = "当前版本："
					+ getPackageManager().getPackageInfo(pkName, 0).versionName
					+ aboutTxt;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		about = (RelativeLayout) findViewById(R.id.about);
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("联系我们");
				dialog.setMessage(aboutTxt);
				dialog.setNegativeButton("关闭", null);
				dialog.show();
			}
		});
		fuwu = (RelativeLayout) findViewById(R.id.tiaokuan);
		fuwu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("服务条款");
				dialog.setMessage(fuwuTxt);
				dialog.setNegativeButton("关闭", null);
				dialog.show();
			}
		});
		logout = (RelativeLayout) findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Config.isConn(UserSet.this)){
					UserSet.this.showDialog(UserSet.this);
				}else{
					Config.showProgressDialog(context, false, null);
					SharedPreferences sp_user = context.getSharedPreferences(
							"babyshow_user", Context.MODE_PRIVATE);
					Editor user = sp_user.edit();
					user.clear();
					user.commit();
					SharedPreferences sp = context.getSharedPreferences(
							"babyshow_login", Context.MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.clear();
					edit.commit();
					MyApplication.getInstance().stopAll();
					startActivity(new Intent(context, StartActivity.class));
				}
			}
		});

		change_pwd = (RelativeLayout) findViewById(R.id.change_pwd);
		change_pwd.setOnClickListener(changePwdClick);
	}

	private void showDialog(final Activity context){
		OutDialog = new Builder(context);
		OutDialog.setTitle("退出失败提示：");
		OutDialog.setMessage("退出登录失败，当前网络未连接，请检查网络状态。");
		OutDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
			
		});
		OutDialog.create().show();
	}

	public OnClickListener bindClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent;
			if (TextUtils.isEmpty(Config.getUser(context).source)
					|| "0".equals(Config.getUser(context).source)) {
				intent = new Intent(context, BindList.class);
			} else {
				intent = new Intent(context, Bind.class);
				intent.putExtra("user_type", user_type);
				intent.putExtra("bind", "bind");
			}
			startActivity(intent);
		}
	};

	public OnClickListener changePwdClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(context, ChangePwd.class);
			intent.putExtra("user_id", Config.getUser(context).uid);
			startActivity(intent);
		}
	};

	public OnClickListener nameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("修改昵称");
			final EditText edit_name = new EditText(context);
			edit_name.setSingleLine(true);
			edit_name.setMaxEms(10);
			builder.setView(edit_name);
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
							String sname = edit_name.getText().toString()
									.trim();
							if (sname.length() > 0) {
								Config.showProgressDialog(context, true, null);
								AjaxParams params = new AjaxParams();
								params.put("user_id",
										Config.getUser(context).uid);
								params.put("nick_name", sname);
								FinalHttp fh = new FinalHttp();
								fh.post(ServerMutualConfig.EditUser, params,
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
														SharedPreferences sp_user = context
																.getSharedPreferences(
																		"babyshow_user",
																		Context.MODE_PRIVATE);
														Editor edit_user = sp_user
																.edit();
														edit_user.putString(
																"user", t);
														edit_user.commit();
														name.setText(Config
																.getUser(context).nickname);
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
	public OnClickListener headClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
//			noScrollgridview.setVisibility(View.VISIBLE);
//			new PopupWindows(context, noScrollgridview);
//			InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputMethodManager.hideSoftInputFromWindow(msg.getWindowToken(), 0);
			
//			456789
			AlertDialog.Builder builder = new Builder(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate(R.layout.image_dialog, null);
			builder.setTitle("选择");
			builder.setView(convertView);
			TextView image = (TextView) convertView.findViewById(R.id.image);
			TextView camera = (TextView) convertView.findViewById(R.id.camera);
			image.setOnClickListener(selectImageClick);
			camera.setOnClickListener(selectCameraClick);
			dialog = builder.create();
			dialog.show();
		}
	};
	Dialog dialog;
	public OnClickListener selectImageClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				Config.deleteFile(Config.ImageFile + "temp_image.jpg");
				Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");  
				startActivityForResult(intent, Config.IMAGE_IMAGE_RESULT);  
				dialog.dismiss();
			}
		}
	};
	public OnClickListener selectCameraClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				Config.deleteFile(Config.ImageFile + "temp_image.jpg");
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						Config.imageUri);
				startActivityForResult(i, Config.IMAGE_CAMERA_RESULT);
				dialog.dismiss();
			}
		}
	};
	
	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/user_icon.jpg");
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
	}
	
	
	public void getBabyInfo() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		AbHttpUtil ab = AbHttpUtil.getInstance(context);
		ab.get(ServerMutualConfig.GetBabys + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								babyRoot.removeAllViews();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									LayoutInflater inflater = (LayoutInflater) context
											.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									View view = inflater.inflate(
											R.layout.user_set_baby, null);
									TextView baby = (TextView) view
											.findViewById(R.id.name);
									final JSONObject jbaby = json.getJSONArray(
											"data").getJSONObject(i);
									baby.setText(jbaby.getString("baby_name"));
									view.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent intent = new Intent(context,
													EditBaby.class);
											intent.putExtra("baby",
													jbaby.toString());
											startActivity(intent);
										}
									});
									babyRoot.addView(view);
								}
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
						Config.showFiledToast(context);
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Config.IMAGE_IMAGE_RESULT) {
				Uri uri = intent.getData();
				Config.cropImageUri(context, uri, 640, 640,
						Config.CROP_BIG_PICTURE);
			} else if (requestCode == Config.IMAGE_CAMERA_RESULT) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				int imgWidth = 0;
				int imgHeight = 0;
				Bitmap b = BitmapFactory.decodeFile(Config.ImageFile
						+ "temp_image.jpg", opts);
				imgWidth = opts.outWidth;
				imgHeight = opts.outHeight;
				opts = null;
				BitmapFactory.Options newOpts = new BitmapFactory.Options();
				Config.setNewopts(context, newOpts, imgWidth, imgHeight);
				b = BitmapFactory.decodeFile(Config.ImageFile
						+ "temp_image.jpg", newOpts);
				Config.cropImageUri(context, Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), b, null, null)),640, 640, Config.CROP_BIG_PICTURE);
			} else if (requestCode == Config.CROP_BIG_PICTURE) {
				Config.showProgressDialog(context, true, null);
				FinalHttp fh = new FinalHttp();
				AjaxParams params = new AjaxParams();
				params.put("user_id", Config.getUser(context).uid);
				try {
					params.put("avatar", new File(Config.ImageFile
							+ "temp_image.jpg"));
					fh.post(ServerMutualConfig.EditUser, params,
							new AjaxCallBack<String>() {
								@Override
								public void onFailure(Throwable t, String strMsg) {
									super.onFailure(t, strMsg);
									Config.dismissProgress();
									Config.showFiledToast(context);
								}

								@Override
								public void onSuccess(String t) {
									super.onSuccess(t);
									Config.dismissProgress();
									try {
										JSONObject json = new JSONObject(t);
										if (json.getBoolean("success")) {
											SharedPreferences sp_user = context
													.getSharedPreferences(
															"babyshow_user",
															Context.MODE_PRIVATE);
											Editor edit_user = sp_user.edit();
											edit_user.putString("user", t);
											edit_user.commit();
											MyApplication
													.getInstance()
													.display(
															Config.getUser(context).head_thumb,
															head,
															R.drawable.def_head);
										}
										{
											Toast.makeText(context,
													json.getString("reMsg"),
													Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
