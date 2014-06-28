package com.hoot.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hoot.module.RequestInfo.Api;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.R;

public class BrowserFragment extends BaseFragment {
	private static final String TAG = "BrowserFragment";
	private WebView mWebview;
	private Activity activity;

	public BrowserFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_browser, null);
		mWebview = (WebView) view.findViewById(R.id.webview);
		 activity =  getActivity();
		setUpWebView(mWebview);
		String url = activity.getIntent().getStringExtra(
				AppConstants.InentConst.URL_EXTRA);
		XLog.v(TAG, "url" + url);
		mWebview.loadUrl(url);
		return view;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setUpWebView(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				activity.setProgress(newProgress * 100);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();

			}
		});

	}
}
