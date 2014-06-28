package com.hoot.manager;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.hoot.proto.listener.ConnectivityChangeListener;
import com.hoot.receiver.NetworkMonitorReceiver;
import com.hoot.util.XLog;
import com.tencent.assistant.net.APN;

public class NetworkMonitor extends
		BaseEventManager<ConnectivityChangeListener> {
	private static final String TAG = "NetworkMonitor";
	private NetworkMonitorReceiver mNetReceiver;

	protected NetworkMonitor(Context context) {
		mNetReceiver = new NetworkMonitorReceiver();
	}

	public void notifyWiFiScanResultsAvaible() {
		for (WeakReference<? extends ConnectivityChangeListener> weakListener : mEventQueue) {
			ConnectivityChangeListener listener = weakListener.get();
			XLog.v(TAG, "listener:" + listener);
			if (listener != null) {
				listener.onWiFiScanResultsAvaible();
			}
		}
	}

	public void notifyConnected(APN apn) {
		for (WeakReference<? extends ConnectivityChangeListener> weakListener : mEventQueue) {
			ConnectivityChangeListener listener = weakListener.get();
			if (listener != null) {
				listener.onConnected(apn);
			}
		}
	}

	public void notifyDisconnected(APN apn) {
		for (WeakReference<? extends ConnectivityChangeListener> weakListener : mEventQueue) {
			ConnectivityChangeListener listener = weakListener.get();
			if (listener != null) {
				listener.onDisconnected(apn);
			}
		}
	}

	public void notifyChanged(APN from, APN to) {
		for (WeakReference<? extends ConnectivityChangeListener> weakListener : mEventQueue) {
			ConnectivityChangeListener listener = weakListener.get();
			if (listener != null) {
				listener.onConnectivityChanged(from, to);
			}
		}
	}

	protected void registReceiver(Context context) {
		mNetReceiver = new NetworkMonitorReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(mNetReceiver, filter);
	}

	protected void unregistReceiver(Context context) {
		context.unregisterReceiver(mNetReceiver);
	}
}
