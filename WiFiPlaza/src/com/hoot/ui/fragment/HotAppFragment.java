package com.hoot.ui.fragment;

import org.zirco.ui.activities.MainActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hoot.module.RequestInfo.Api;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.R;
import com.umeng.analytics.MobclickAgent;

public class HotAppFragment extends BaseFragment {
	private static final String TAG = "HotAppFragment";
	private WebView mWebview;
	private Activity activity;
	private final String mPageName = "HotAppFragmentPage";

	public HotAppFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_browser, null);
		mWebview = (WebView) view.findViewById(R.id.webview);
		activity =  getActivity();
		setUpWebView(mWebview);
		String url = Api.App_1;
		XLog.v(TAG, "url" + url);
		
		mWebview.loadUrl(url);
		return view;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setUpWebView(WebView webView) {
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				activity.setProgress(newProgress * 100);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			
			@Override
	         public boolean shouldOverrideUrlLoading(
	        		 WebView view, String url) {
				
				if(url==Api.App){
					view.loadUrl(url);   //�ڵ�ǰ��webview����ת���µ�url
					
				}else if(url!=Api.App){
					Intent in = new Intent();        
			        in.setAction("android.intent.action.VIEW");    
			        Uri content_url = Uri.parse(url);   
			        in.setData(content_url); 
			        in.setClass(activity, MainActivity.class);
			        startActivity(in);
				}
				
	          return true;
	         }
			
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();

			}
		});

	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart(mPageName ); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(mPageName );  // 统计页面结束
	}
}