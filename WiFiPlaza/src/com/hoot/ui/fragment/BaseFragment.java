package com.hoot.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.hoot.module.ModelEngineCallback;
import com.hoot.pojo.Response;
import com.hoot.proto.listener.ConnectivityChangeListener;
import com.hoot.util.XLog;
import com.tencent.assistant.net.APN;

public abstract class BaseFragment extends Fragment implements
		ModelEngineCallback, ConnectivityChangeListener {
	private static final String TAG = "BaseFragment";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		XLog.v(TAG, "activity" + activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onAuthRequest() {
	}

	@Override
	public void onDataLoaded(int seq, int code, Response data) {

	}

	@Override
	public void onConnected(APN apn) {
		XLog.v(TAG, "onConnected");
	}

	@Override
	public void onDisconnected(APN apn) {

	}

	@Override
	public void onConnectivityChanged(APN from, APN to) {

	}

	@Override
	public void onWiFiScanResultsAvaible() {

	}

}
