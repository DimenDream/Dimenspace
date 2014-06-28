package com.hoot.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hoot.module.RequestInfo;
import com.hoot.net.ConnectionManager;
import com.hoot.proto.NetworkTask;
import com.hoot.proto.ProtocolReqStruct;
import com.hoot.proto.ResultCode;
import com.hoot.proto.listener.IProtocolListener;
import com.tencent.assistant.net.APN;

public class ProtocolManager implements
		com.hoot.proto.listener.ConnectivityChangeListener {
	private ExecutorService mService;
	private static final short MAX_THREAD = 5;
	private static ProtocolManager mManager;
	protected ConcurrentHashMap<Integer, ProtocolReqStruct> mReqMap = new ConcurrentHashMap<Integer, ProtocolReqStruct>();

	private ProtocolManager() {
		mService = Executors.newFixedThreadPool(MAX_THREAD);
	}

	public static synchronized ProtocolManager getInstance() {
		if (mManager == null) {
			mManager = new ProtocolManager();
		}
		return mManager;
	}

	public <T> int sendRequest(IProtocolListener listener, RequestInfo info) {
		NetworkTask task = send(listener, info);

		ProtocolReqStruct struct = new ProtocolReqStruct();
		struct.mNetworkTask = task;

		mReqMap.put(task.getTaskId(), struct);
		return task.getTaskId();
	}

	public void cancelRequest(int requestId) {
		ProtocolReqStruct reqStruct = mReqMap.remove(requestId);
		if (reqStruct != null) {
			reqStruct.mNetworkTask.cancelTask();
		}
	}

	private NetworkTask send(IProtocolListener listener, RequestInfo info) {
		NetworkTask netTask = null;
		try {
			netTask = new NetworkTask(ConnectionManager.getInstance()
					.getHttpURLConnection(info.api));
			netTask.setProtocolListener(listener);
			netTask.setSendData(info.reqest);

			mService.submit(netTask);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			listener.onRequestFinished(ResultCode.Code_Http_MalformedURLErr,
					null);
		} catch (IOException e) {
			e.printStackTrace();
			listener.onRequestFinished(ResultCode.Code_Http_IOErr, null);
		}
		return netTask;
	}

	@Override
	public void onConnected(APN apn) {
		// TODO reset proxy
	}

	@Override
	public void onDisconnected(APN apn) {
	}

	@Override
	public void onConnectivityChanged(APN from, APN to) {
		// TODO reset
	}

	@Override
	public void onWiFiScanResultsAvaible() {
		// TODO Auto-generated method stub

	}
}
