package com.hoot.module;

import com.hoot.pojo.Response;

public class UserEngine extends BaseEngine {
	private static UserEngine mEngine;

	private UserEngine() {
	}

	public static UserEngine getInstance() {
		if (mEngine == null) {
			mEngine = new UserEngine();
		}
		return mEngine;
	}

	@Override
	void onRequestSuccess(final int respCode, final Response respStruct) {
		// RefresUI
		runOnUIThread(respCode, respStruct);
	}

	@Override
	void onReqeustFailed(final int respCode) {
		runOnUIThread(respCode, null);
	}

}
