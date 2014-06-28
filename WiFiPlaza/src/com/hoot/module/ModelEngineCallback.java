package com.hoot.module;

import com.hoot.pojo.Response;
import com.hoot.proto.listener.ActionCallback;

public interface ModelEngineCallback extends ActionCallback {
	void onDataLoaded(int seq, int code, Response data);

	void onAuthRequest();
}
