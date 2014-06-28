package com.hoot.share.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoot.util.XLog;
import com.hoot.wifiplaza.R;

public class WifiAdapter extends CustomBaseAdapter<ScanResult> {

	private static final String TAG = "WifiAdapter";

	public WifiAdapter(Context context, List<ScanResult> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_wifi_item, null);
			holder = new Holder();
			holder.mWiFiNameView = (TextView) convertView
					.findViewById(R.id.wifi_name);
			holder.mWifiLevelView = (ImageView) convertView.findViewById(R.id.wifi_status);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		ScanResult result = getItem(position);
		holder.mWiFiNameView.setText(result.SSID);
		int level = WifiManager.calculateSignalLevel(result.level, 4);
		XLog.v(TAG, "ssid:" + result.SSID + ", level:" + level + ", orig leve:" + result.level); 
		holder.mWifiLevelView.setImageLevel(level);
		return convertView;
	}

	static final class Holder {
		TextView mWiFiNameView;
		ImageView mWifiLevelView;
	}
}
