package com.hoot.wifiplaza;


import org.zirco.ui.activities.MainActivity;

import com.hoot.ui.fragment.AppConstants;
import com.hoot.ui.fragment.BrowserFragment;
import com.hoot.wifiplaza.R;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public class BrowserActivity extends BaseActionBarActivity<BrowserFragment> {

	private static final String TAG = null;
	private final String mPageName = "WebViewPage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_browser);
		
//		 Intent intent = new Intent();        
//	        intent.setAction("android.intent.action.VIEW");    
//	        Uri content_url = Uri.parse(value);   
//	        intent.setData(content_url);  
//	        startActivity(intent);
	    }
	@Override  
    protected void onResume() {  
        super.onResume(); 
        Intent intent=getIntent();
		   String value=intent.getStringExtra(
				   AppConstants.InentConst.URL_EXTRA);
        Intent in = new Intent();        
        in.setAction("android.intent.action.VIEW");    
        Uri content_url = Uri.parse(value);   
        in.setData(content_url); 
        in.setClass(BrowserActivity.this, MainActivity.class);
        startActivity(in);
        // umeng 统计
        MobclickAgent.onPageStart(mPageName); //统计页面
        MobclickAgent.onResume(this);          //统计时长
        Log.e(TAG, "start onResume~~~");  
    }  

    @Override  
    protected void onStop() {  
        super.onStop();  
        this.finish();
        Log.e(TAG, "start onStop~~~");  
    }  
    public void onPause() {
    	super.onPause();
    	// umeng 统计
    	MobclickAgent.onPageEnd(mPageName); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
        MobclickAgent.onPause(this);
    	}
}
