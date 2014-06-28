package com.hoot.module;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.hoot.app.App;
import com.hoot.encoding.JsonUtil;
import com.hoot.pojo.Response;
import com.hoot.pojo.Router;
import com.hoot.ui.fragment.AppConstants;
import com.hoot.util.HandlerUtil;
import com.hoot.util.XLog;

public class RouterEngine extends BaseEngine {

	private static final String TAG = "RouterEngine";
	private static RouterEngine mEngine;

	private RouterEngine() {
	}

	public static RouterEngine getInstance() {
		if (mEngine == null) {
			mEngine = new RouterEngine();
		}
		return mEngine;
	}

	// @Override
	// protected <Router> byte[] makePostData(Router obj) {
	// return JsonUtil.getBytes(obj);
	// }

	@Override
	void onRequestSuccess(int respCode, Response respStruct) {
		XLog.v(TAG, "code:" + respStruct.getHead().getCode());
		if (respStruct != null
				&& respStruct.getHead() != null
				&& respStruct.getHead().getCode() == AppConstants.AuthConst.ERROR_AUTH_REQUEST) {
			onAuthRequired();
			return;
		}

		runOnUIThread(respCode, respStruct);
	}

	@Override
	void onReqeustFailed(int respCode) {
		runOnUIThread(respCode, null);
	}

}
