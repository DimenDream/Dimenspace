package com.hoot.manager;

import android.net.NetworkInfo;

import com.hoot.app.App;
import com.hoot.proto.listener.ConnectivityChangeListener;
import com.hoot.util.XLog;
import com.tencent.assistant.net.APN;
import com.tencent.assistant.net.NetworkUtil;

public class SystemEventManager {
	private static final String TAG = "SystemEventManager";

	private NetworkMonitor mNetMonitor;

	private static SystemEventManager mInstance;

	private SystemEventManager() {
		mNetMonitor = new NetworkMonitor(App.GlobalContext);
	}

	public static final synchronized SystemEventManager getInstance() {
		if (mInstance == null) {
			mInstance = new SystemEventManager();
		}
		return mInstance;
	}

	public void registSysEvent() {
		mNetMonitor.registReceiver(App.GlobalContext);
	}

	public void unregistSysEvent() {
		mNetMonitor.unregistReceiver(App.GlobalContext);
	}

	public void onConnectivityChanged(NetworkInfo activeNetInfo) {
		// if (activeNetInfo.isConnected()) {
		// mNetMonitor.notifyConnected(activeNetInfo.getType());
		// } else if (!activeNetInfo.isConnected()) {
		// mNetMonitor.notifyDisconnected(activeNetInfo.getType());
		// } else {
		// mNetMonitor.notifyChanged(activeNetInfo.getType());
		// }

		if (activeNetInfo == null) {
			return;
		}
		APN apn1 = NetworkUtil.getApn();
		NetworkUtil.refreshNetwork();
		APN apn2 = NetworkUtil.getApn();
		XLog.v(TAG, "activeInfo:" + activeNetInfo + ", apn1:" + apn1
				+ ", apn2:" + apn2);
		if (apn1 != apn2) {
			if (apn1 == APN.NO_NETWORK && activeNetInfo.isConnected()) {
				mNetMonitor.notifyConnected(apn2);
			} else if (apn2 == APN.NO_NETWORK) {
				mNetMonitor.notifyDisconnected(apn1);
			} else {
				mNetMonitor.notifyChanged(apn1, apn2);
			}
		}
	}

	public void onWiFiScanResultsAvailable() {
		mNetMonitor.notifyWiFiScanResultsAvaible();
	}
	
	public void onWiFiLevelChanged() {
		mNetMonitor.notifyWiFiScanResultsAvaible();
	}

	public void registNetworkListener(ConnectivityChangeListener listener) {
		mNetMonitor.regist(listener);
	}

	public void unregistNetworkListener(ConnectivityChangeListener listener) {
		mNetMonitor.unregist(listener);
	}

}
