package com.hoot.proto.listener;

import com.hoot.pojo.Response;

public interface IProtocolListener {
	/**
	 * Э���Ļص���ֻ����Э���Ƿ�ɹ���ҵ���߼��ĳɹ����Ҫ���ݽṹ�����ж�
	 * @param respCode
	 * @param respStruct
	 */
	void onRequestFinished(int respCode, Response respStruct);
}
