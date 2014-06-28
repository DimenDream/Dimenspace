package com.hoot.util;

import android.util.Log;

public class XLog {
	public static final boolean DEBUG = true;
	private static final boolean DEBUG_WARN = true;
	private static final boolean DEBUG_ERR = true;

	public static void v(String tag, String msg) {
		Log.v(tag, getCallerInfo() + msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, getCallerInfo() + msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG_WARN) {
			Log.i(tag, getCallerInfo() + msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (DEBUG_WARN) {
			Log.w(tag, getCallerInfo() + msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG_ERR) {
			Log.e(tag, getCallerInfo() + msg);
		}
	}

	public static void e(String tag, String msg, Throwable t) {
		if (DEBUG_ERR) {
			Log.e(tag, getCallerInfo() + msg, t);
		}
	}

	private static String getCallerInfo() {
		StackTraceElement[] stack = new Throwable().getStackTrace();
		StringBuilder sb = new StringBuilder();
		if (stack.length > 2) {
			sb.append(stack[2].getClassName()).append(".")
					.append(stack[2].getMethodName()).append("#")
					.append(stack[2].getLineNumber()).append("==>");
		}
		return sb.toString();
	}
}
