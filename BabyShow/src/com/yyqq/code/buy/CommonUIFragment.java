package com.yyqq.code.buy;

import com.yyqq.babyshow.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommonUIFragment extends Fragment {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_selection_common,
				container, false);

		TextView tv_tabName = (TextView) rootView.findViewById(R.id.tv_tabName);

		Bundle bundle = getArguments();

		tv_tabName.setText(bundle.getString(BuyDetailFragment.ARGUMENTS_NAME, ""));

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
