package com.hoot.proto.listener;

import java.util.List;

import android.net.wifi.ScanResult;

public interface WiFiScanResultListener {
	void onScanFinish(List<ScanResult> results);
}
