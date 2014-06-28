package com.hoot.wifiplaza;

import com.hoot.ui.fragment.LoginFragment;
import com.hoot.wifiplaza.R;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.Menu;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActionBarActivity<LoginFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	// umeng 统计
	public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
		}
		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
		}
}
