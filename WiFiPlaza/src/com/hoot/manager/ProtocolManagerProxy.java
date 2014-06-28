package com.hoot.manager;

import com.hoot.module.RequestInfo;
import com.hoot.proto.listener.IProtocolListener;

public class ProtocolManagerProxy {
	/**
	 * 发送请求
	 * @param <T>
	 * 
	 * @param request
	 *            请求
	 * @param protocolListener
	 *            回包的回调
	 */
	public static <T> int sendRequest(
			IProtocolListener protocolListener, RequestInfo info) {
		return ProtocolManager.getInstance().sendRequest(
				protocolListener, info);
	}

	/**
	 * 取消请求
	 * 
	 * @param nRequestId
	 *            根据request的Id来取消请求
	 */
	public static void cancelRequest(int nRequestId) {
		ProtocolManager.getInstance().cancelRequest(nRequestId);
	}
}
