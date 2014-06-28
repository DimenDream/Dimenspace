package com.hoot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hoot.app.App;
import com.hoot.manager.SystemEventManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiAdmin {
	public static final int SECURITY_WPA = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_NONE = 2;
	public static final int SECURITY_EAP = 3;
	private static final String TAG = "WifiAdmin";

	private WifiManager mWiFiManager;
	private List<WifiConfiguration> mConfigurationList;
	private static WifiAdmin mWifiAdamin;

	private WifiAdmin(Context context) {
		mWiFiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	public static final WifiAdmin getInstance() {
		if (mWifiAdamin == null) {
			mWifiAdamin = new WifiAdmin(App.GlobalContext);
		}
		return mWifiAdamin;
	}
	
	
	public WifiInfo getWifiInfo() {
		return mWiFiManager.getConnectionInfo();
	}

	public WifiManager getWiFiManager() {
		return mWiFiManager;
	}

	public List<ScanResult> getWiFiList() {
		return mWiFiManager.getScanResults();
	}

	public void openWifi() {
		XLog.v(TAG, "wifi is enable:" + mWiFiManager.isWifiEnabled());
		if (!mWiFiManager.isWifiEnabled()) {
			mWiFiManager.setWifiEnabled(true);
		}
		XLog.v(TAG, "wifi state:" + mWiFiManager.getWifiState());
	}

	private int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return SECURITY_WEP;
		} else if (result.capabilities.contains("PSK")) {
			return SECURITY_WPA;
		} else if (result.capabilities.contains("EAP")) {
			return SECURITY_EAP;
		}
		return SECURITY_NONE;
	}

	public void startScan() {
		mWiFiManager.startScan();
		mConfigurationList = mWiFiManager.getConfiguredNetworks();
		WifiConfiguration config = null;
		for (int i = 0; mConfigurationList != null
				&& i < mConfigurationList.size(); i++) {
			config = mConfigurationList.get(i);
			XLog.v(TAG, "ssid:" + config.SSID + ", psk" + config.preSharedKey
					+ ", wepkey:" + Arrays.toString(config.wepKeys));
		}
	}

	public List<WifiConfiguration> getWifiConfiguration() {
		return mConfigurationList;
	}

	public void connectConfiguration(int index) {
		if (index > mConfigurationList.size()) {
			throw new RuntimeException("index " + index
					+ " larger then configuration size "
					+ mConfigurationList.size());
		}
		mWiFiManager.enableNetwork(mConfigurationList.get(index).networkId,
				true);
	}

	public StringBuilder lookUpScan() {
		List<ScanResult> wifiList = getWiFiList();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < wifiList.size(); i++) {
			ScanResult scanResult = wifiList.get(i);
			stringBuilder.append("bssid:" + scanResult.BSSID + "  ")
					.append("ssid:" + scanResult.SSID + "   ")
					.append(scanResult.capabilities + "   ")
					.append(scanResult.frequency + "   ")
					.append(scanResult.level + "\n\n");
		}
		return stringBuilder;
	}

	/*
	 * ���һ�����粢���� return networkid
	 */
	private int addNetwork(WifiConfiguration wcg) {
		int configId = mWiFiManager.addNetwork(wcg);
		boolean enabled = mWiFiManager.enableNetwork(configId, true);
		mWiFiManager.saveConfiguration();
		boolean connected = mWiFiManager.reconnect();
		XLog.v(TAG, "configId:" + configId + ", enabled:" + enabled
				+ ", connected:" + connected);
		return configId;
	}

	// �Ͽ�ָ��ID������
	public void disconnectWifi(int netId) {
		mWiFiManager.disableNetwork(netId);
		mWiFiManager.disconnect();
	}

	// Ȼ����һ��ʵ��Ӧ�÷�����ֻ��֤��û������������

	private WifiConfiguration createWifiInfo(String SSID, String Password,
			int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = exist(SSID);
		if (tempConfig != null) {
			mWiFiManager.removeNetwork(tempConfig.networkId);
		}
		// WIFICIPHER_NOPASS
		if (Type == SECURITY_NONE) {
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		// WIFICIPHER_WEP
		if (Type == SECURITY_WEP) {
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		// WIFICIPHER_WPA
		if (Type == SECURITY_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		XLog.v(TAG, "config:" + config.preSharedKey);
		return config;
	}

	public void connect(String bssid, String password) {
		String ssid = getSSID(bssid);
		int type = getBSSIDType(bssid);
		XLog.v(TAG, "ssid:" + ssid + ", type:" + type);
		openWifi();
		WifiConfiguration config = createWifiInfo(ssid, password, type);
		addNetwork(config);
	}

	private WifiConfiguration exist(String SSID) {
		List<WifiConfiguration> existingConfigs = mWiFiManager
				.getConfiguredNetworks();
		if (existingConfigs == null) {
			return null;
		}
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public List<String> getAvailWifiBSSID(List<ScanResult> results) {
		List<String> arrays = new ArrayList<String>();
		if (results == null) {
			return null;
		}
		for (ScanResult result : results) {
			arrays.add(result.BSSID);
		}
		return arrays;
	}

	public int getBSSIDType(String bssid) {
		List<ScanResult> results = getWiFiList();
		if (results == null) {
			return -1;
		}
		for (ScanResult result : results) {
			if (bssid.equals(result.BSSID)) {
				return getSecurity(result);
			}
		}
		return WifiAdmin.SECURITY_NONE;
	}

	private String getSSID(String bssid) {
		List<ScanResult> results = mWiFiManager.getScanResults();
		if (results == null) {
			return null;
		}
		for (ScanResult result : results) {
			if (bssid.equals(result.BSSID)) {
				return result.SSID;
			}
		}
		return null;
	}
}
