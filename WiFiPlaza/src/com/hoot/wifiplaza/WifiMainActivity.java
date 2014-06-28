package com.hoot.wifiplaza;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WifiMainActivity extends Activity {
	private static final long SPLASH_DISPLAY_LENGHT = 0;
	boolean isFirstIn = false;
	protected Intent intent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("first_pref",  
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true); 
        new Handler().postDelayed(new Runnable() { 
        	@Override  
            public void run() {  
                if (isFirstIn) {  
                    // start guideactivity1  
                    intent = new Intent(WifiMainActivity.this, StartActivity.class);  
                } else {  
                    // start TVDirectActivity  
                    intent = new Intent(WifiMainActivity.this, HootMainActivity.class);  
                }  
                WifiMainActivity.this.startActivity(intent);  
                WifiMainActivity.this.finish();  
            }
        }, SPLASH_DISPLAY_LENGHT);     
    }
	// umeng 统计
	public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
		}
		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
		}

}
