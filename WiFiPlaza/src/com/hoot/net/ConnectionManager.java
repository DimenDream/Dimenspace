package com.hoot.net;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import com.hoot.manager.AuthManager;
import com.hoot.util.XLog;

public class ConnectionManager {
	private static final int CONN_TIME_OUT = 10 * 1000;
	private static final int READ_TIME_OUT = 10 * 1000;
	private static final String FAKE_UA = "Android";
	private static final String TAG = "ConnectionManager";
//	private static final String URL = "http://192.168.1.103:8080";

	private static final String URL = "http://115.28.215.170:8080";
	@SuppressLint("NewApi")
	private static CookieManager cookieManager = new CookieManager();

	private static class Holder {
		public static final ConnectionManager INSTANCE = new ConnectionManager();
	}

	private ConnectionManager() {
		CookieHandler.setDefault(cookieManager);
		disableConnectionReuseIfNecessary();
	}

	public static ConnectionManager getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * @param strUrl
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public HttpURLConnection getHttpURLConnection(String api)
			throws MalformedURLException, IOException {
		api = URL + api;
		Log.d(TAG, "getHttpURLConnection, url:" + api);
		URL url = new URL(api);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		setReqeustProp(urlConnection, true);
		setRequestProperty(urlConnection);
		debug();
		return urlConnection;
	}

	private void setReqeustProp(HttpURLConnection urlConnection, boolean isPost) {
		urlConnection.setConnectTimeout(CONN_TIME_OUT);
		urlConnection.setReadTimeout(READ_TIME_OUT);
		urlConnection.setDoOutput(isPost);// true post, false get
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(!isPost);// post cannot use cache
		String session = AuthManager.getInstance().getSession();
		urlConnection.setRequestProperty("Cookie", session);

	}

	private void disableConnectionReuseIfNecessary() {
		// Work around pre-Froyo bugs in HTTP connection reuse.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	private void setRequestProperty(HttpURLConnection urlConnection) {
		urlConnection.setRequestProperty("user-agent", FAKE_UA);
		urlConnection.setRequestProperty("Content-Type", "application/json");

	}

	@SuppressLint("NewApi")
	private void debug() {
		CookieStore store = cookieManager.getCookieStore();
		for (HttpCookie cookie : store.getCookies()) {
			XLog.d(TAG, "cookie:	" + cookie);
		}
	}
}
