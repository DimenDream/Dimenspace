package com.hoot.manager;

import com.hoot.module.RequestInfo;
import com.hoot.proto.listener.IProtocolListener;

public class ProtocolManagerProxy {
	/**
	 * ��������
	 * @param <T>
	 * 
	 * @param request
	 *            ����
	 * @param protocolListener
	 *            �ذ��Ļص�
	 */
	public static <T> int sendRequest(
			IProtocolListener protocolListener, RequestInfo info) {
		return ProtocolManager.getInstance().sendRequest(
				protocolListener, info);
	}

	/**
	 * ȡ������
	 * 
	 * @param nRequestId
	 *            ����request��Id��ȡ������
	 */
	public static void cancelRequest(int nRequestId) {
		ProtocolManager.getInstance().cancelRequest(nRequestId);
	}
}
