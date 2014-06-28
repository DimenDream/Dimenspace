package com.hoot.wifiplaza;

import com.hoot.ui.fragment.RegistWiFiFragment;
import com.hoot.wifiplaza.R;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;

public class RegistWiFiActivity extends
		BaseActionBarActivity<RegistWiFiFragment> {
	// RegistWiFiFragment mRegistWiFiFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_wifi);
	}
	// umeng 统计
	public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);// 统计时长
		}
		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this); // 统计时长结束
		}
}
