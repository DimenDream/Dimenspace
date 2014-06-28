package com.hoot.wifiplaza;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.hoot.ui.fragment.BaseFragment;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActionBarActivity<T extends Fragment> extends
		ActionBarActivity {
	private static final String TAG = "BaseActionBarActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!(this instanceof HootMainActivity)) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayUseLogoEnabled(false);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}
	}


//	public void onClickDelegate(View v) {
//		XLog.v(TAG, "click, frag:" + mBaseFragment);
//		mBaseFragment.onClickDelegate(v);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
}
