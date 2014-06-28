package com.hoot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.hoot.manager.SystemEventManager;
import com.hoot.util.WifiAdmin;
import com.hoot.util.XLog;

/**
 * wifi scan event and network change event
 * 
 * @author yuan
 * 
 */
public class NetworkMonitorReceiver extends BroadcastReceiver {

	private static final String TAG = "NetworkMonitorReceiver";

	public NetworkMonitorReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		XLog.v(TAG, "action:" + action + ",extra:" + intent.getExtras()
				+ WifiAdmin.getInstance().getWifiInfo());
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			NetworkInfo activeNetInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			XLog.v(TAG, "activeNetInfo:" + activeNetInfo);
			SystemEventManager.getInstance().onConnectivityChanged(
					activeNetInfo);
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			SystemEventManager.getInstance().onWiFiScanResultsAvailable();
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			XLog.v(TAG, "RSSI_CHANGED_ACTION");
			SystemEventManager.getInstance().onWiFiLevelChanged();
		}
	}
}
