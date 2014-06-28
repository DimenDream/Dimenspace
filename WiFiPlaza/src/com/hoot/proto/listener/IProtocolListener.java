package com.hoot.proto.listener;

import com.hoot.pojo.Response;

public interface IProtocolListener {
	/**
	 * 协议层的回调，只表明协议是否成功，业务逻辑的成功与否还要根据结构体来判定
	 * @param respCode
	 * @param respStruct
	 */
	void onRequestFinished(int respCode, Response respStruct);
}
