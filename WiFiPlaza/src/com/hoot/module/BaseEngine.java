package com.hoot.module;

import com.hoot.manager.ProtocolManagerProxy;
import com.hoot.pojo.Response;
import com.hoot.proto.ResultCode;
import com.hoot.proto.listener.IProtocolListener;
import com.hoot.util.HandlerUtil;
import com.hoot.util.XLog;

public abstract class BaseEngine implements IProtocolListener {
	private static final String TAG = "BaseEngine";

	private ModelEngineCallback mCallback;

	public int send(RequestInfo info) {
		return ProtocolManagerProxy.sendRequest(this, info);
	}

	public void register(ModelEngineCallback callback) {
		mCallback = callback;
	}

	@Override
	public void onRequestFinished(int respCode, Response respStruct) {
		XLog.v(TAG, "respCode:" + respCode + ", data:" + respStruct);
		if (respCode == ResultCode.Code_Ok && respStruct != null) {
			onRequestSuccess(respCode, respStruct);
		} else {
			onReqeustFailed(respCode);
		}
	}

	protected void runOnUIThread(final int respCode, final Response respStruct) {
		HandlerUtil.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (mCallback != null) {
					mCallback.onDataLoaded(0, respCode, respStruct);
				}
			}
		});
	}

	/**
	 * 协议有问题
	 * @param respCode
	 * @param respStruct
	 */
	abstract void onRequestSuccess(int respCode, Response respStruct);

	abstract void onReqeustFailed(int respCode);

	protected void onAuthRequired() {
		HandlerUtil.getMainHandler().post(new Runnable() {
			
			@Override
			public void run() {
				mCallback.onAuthRequest();
			}
		});
	}
}
