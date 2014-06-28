package com.hoot.proto.listener;

import com.tencent.assistant.net.APN;

public interface ConnectivityChangeListener {
	void onConnectivityChanged(APN from, APN to);

	void onConnected(APN apn);

	void onDisconnected(APN apn);

	void onWiFiScanResultsAvaible();
}
