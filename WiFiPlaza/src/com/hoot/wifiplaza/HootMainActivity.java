package com.hoot.wifiplaza;

import net.youmi.android.AdManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hoot.share.adapter.CollectionPagerAdapter;
import com.hoot.ui.fragment.MainFragment;
import com.hoot.util.WifiAdmin;
import com.hoot.util.XLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class HootMainActivity extends BaseActionBarActivity<MainFragment> {

	private static final String TAG = "MainActivity";
	private CollectionPagerAdapter mCollectionPagerAdapter;
	private ViewPager mViewPager;
	private String[] mTitles;
	private ShareActionProvider mShareActionProvider;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// umeng自动更新
		UmengUpdateAgent.update(this);
		// 统计信息上传
		mContext = this;	
		MobclickAgent.updateOnlineConfig( this );
		
		setContentView(R.layout.activity_main);

		AdManager.getInstance(this).init("85aa56a59eac8b3d",
				"a14006f66f58d5d7", false);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mCollectionPagerAdapter = new CollectionPagerAdapter(
				getSupportFragmentManager());

		mViewPager.setAdapter(mCollectionPagerAdapter);
		mTitles = new String[] { getString(R.string.wifi_conn_tab),
				getString(R.string.daohang_tab), getString(R.string.app_tab) };

		final ActionBar actionBar = getSupportActionBar();
		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		initTabListener(actionBar);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// umeng 统计
		MobclickAgent.onResume(this);
		XLog.v(TAG, "onresume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// umeng 统计
		MobclickAgent.onPause(this);
		XLog.v(TAG, "onpause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		XLog.v(TAG, "" + this);
	}

	/**
	 * @param actionBar
	 */
	private void initTabListener(final ActionBar actionBar) {
		TabListener tabListener = new TabListener() {
			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// When the tab is selected, switch to the
				// corresponding page in the ViewPager.
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// hide the given tab
			}

			@Override
			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mTitles[i])
					.setTabListener(tabListener));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// share provider
		MenuItem item = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(item);
		mShareActionProvider.setShareIntent(getDefaultIntent());
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Defines a default (dummy) share intent to initialize the action provider.
	 * However, as soon as the actual content to be used in the intent is known
	 * or changes, you must update the share intent by again calling
	 * mShareActionProvider.setShareIntent()
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT,
				getString(R.string.actionbar_share_msg));
		intent.setType("text/plain");
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	// 测试设备
//	public static String getDeviceInfo(Context context) {
//	    try{
//	      org.json.JSONObject json = new org.json.JSONObject();
//	      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//	          .getSystemService(Context.TELEPHONY_SERVICE);
//	  
//	      String device_id = tm.getDeviceId();
//	      
//	      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//	          
//	      String mac = wifi.getConnectionInfo().getMacAddress();
//	      json.put("mac", mac);
//	      
//	      if( TextUtils.isEmpty(device_id) ){
//	        device_id = mac;
//	      }
//	      
//	      if( TextUtils.isEmpty(device_id) ){
//	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
//	      }
//	      
//	      json.put("device_id", device_id);
//	      
//	      return json.toString();
//	    }catch(Exception e){
//	      e.printStackTrace();
//	    }
//	  return null;
//	}
}
