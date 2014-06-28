package com.hoot.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {

	private static Handler mManinHandler;

	/**
	 * 取得UI线程Handler
	 * 
	 * @return
	 */
	public static synchronized Handler getMainHandler() {
		if (mManinHandler == null) {
			if (mManinHandler == null) {
				mManinHandler = new Handler(Looper.getMainLooper());
			}

		}
		return mManinHandler;
	}
}
