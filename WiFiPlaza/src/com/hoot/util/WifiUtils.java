package com.hoot.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hoot.wifiplaza.R;

public class WifiUtils {
	private static final String TAG = "WifiUtils";

	public static void initWiFiStatus(Context context, TextView availableWiFiText) {
		WifiManager mWiFiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = mWiFiManager.getConnectionInfo();
		XLog.v(TAG, "info:" + info);
		if (mWiFiManager.isWifiEnabled() && info != null) {
			XLog.v(TAG, "ssid:" + info.getSSID());
			availableWiFiText.setText(context.getString(
					R.string.main_frag_connected_wifi, info.getSSID()));
			availableWiFiText.setClickable(false);
		} else if (!mWiFiManager.isWifiEnabled()) {
			availableWiFiText.setText(R.string.main_frag_click_open_wifi);
			availableWiFiText.setClickable(true);
			availableWiFiText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					WifiAdmin.getInstance().openWifi();
					WifiAdmin.getInstance().startScan();
				}
			});

		}
	}
}
