package com.yyqq.code.photo;

import com.yyqq.commen.utils.Config;

import android.app.Activity;
import android.os.Bundle;

public class PeoplePhotoList extends Activity {
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
	}
}
