package com.hoot.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.diy.DiyManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.hoot.manager.AuthManager;
import com.hoot.manager.SystemEventManager;
import com.hoot.module.RequestInfo.Api;
import com.hoot.pojo.GridItemInfo;
import com.hoot.pojo.GridItemInfo.ActionType;
import com.hoot.pojo.User;
import com.hoot.proto.listener.ConnectivityChangeListener;
import com.hoot.share.adapter.MainAdapter;
import com.hoot.ui.fragment.AppConstants.InentConst;
import com.hoot.util.WifiAdmin;
import com.hoot.util.WifiUtils;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.BaseActionBarActivity;
import com.hoot.wifiplaza.BrowserActivity;
import com.hoot.wifiplaza.LoginActivity;
import com.hoot.wifiplaza.R;
import com.hoot.wifiplaza.RegistWiFiActivity;
import com.tencent.assistant.net.APN;
import com.umeng.analytics.MobclickAgent;

public class MainFragment extends BaseFragment implements
		ConnectivityChangeListener {
	private static final String TAG = "MainFragment";
	private WifiManager mWiFiManager;

	private GridView mGridView;
	private BaseAdapter mFunctionAdapter;
	private List<GridItemInfo> mItemInfos;
	private View mUserStatusLayout;
	private TextView mUserStatusText;
	private TextView mAvailableWiFiText;

	private static final int LOGIN_REQUEST_CODE = 1;
	private String mUsername;

	private final String mPageName = "MainFragmentPage";

	private int[] images = new int[] { R.drawable.wifi1, R.drawable.app,
			R.drawable.tencent, R.drawable.baidu, R.drawable.taobao,
			R.drawable.tmall, R.drawable.jingdong, R.drawable.shiping,
			R.drawable.dazhong, R.drawable.weibo, R.drawable.mogujie,
			R.drawable.jumei, R.drawable.qunar, R.drawable.qidian,
			R.drawable.autohome, R.drawable.jiayuan };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		mGridView = (GridView) rootView.findViewById(R.id.function_gridview);
		mUserStatusLayout = rootView.findViewById(R.id.user_status_layout);
		mUserStatusLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpLogin();
			}
		});
		mUserStatusText = (TextView) rootView
				.findViewById(R.id.user_status_tips);
		mUsername = AuthManager.getInstance().getUsername();
		mAvailableWiFiText = (TextView) rootView
				.findViewById(R.id.available_wifi);

		mWiFiManager = WifiAdmin.getInstance().getWiFiManager();

		if (!TextUtils.isEmpty(mUsername)) {
			mUserStatusText.setText(getString(R.string.welcom_tip, mUsername));
			mUserStatusLayout.setClickable(false);
		} else {
			mUserStatusText.setText(R.string.click_login);
			mUserStatusLayout.setClickable(true);
		}

		WifiUtils.initWiFiStatus(getActivity(), mAvailableWiFiText);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		SystemEventManager.getInstance().registNetworkListener(this);
		WifiAdmin.getInstance().startScan();
		MobclickAgent.onPageStart(mPageName); // 统计页面
		XLog.v(TAG, "registNetworkListener");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName); // 页面统计结束
	}

	@Override
	public void onStop() {
		super.onStop();
		SystemEventManager.getInstance().unregistNetworkListener(this);
		XLog.v(TAG, "unregistNetworkListener");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mFunctionAdapter = new MainAdapter(getActivity(), mItemInfos);
		mGridView.setAdapter(mFunctionAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				handleClick((GridItemInfo) mFunctionAdapter.getItem(position));
			}
		});
	}

	private void initData() {
		mItemInfos = new ArrayList<GridItemInfo>();
		GridItemInfo info1 = new GridItemInfo();
		info1.actionType = ActionType.ACTIVITY;
		info1.itemName = getString(R.string.grid_reg_wifi_txt);
		info1.imageName = images[0];
		info1.actionUrl = "wifi://" + RegistWiFiActivity.class.getName();
		mItemInfos.add(info1);

		GridItemInfo info2 = new GridItemInfo();
		info2.actionType = ActionType.ADS_GAME;
		info2.itemName = "App";
		info2.imageName = images[1];
		info2.actionUrl = "wifi://" + LoginActivity.class.getName();
		mItemInfos.add(info2);

		GridItemInfo info3 = new GridItemInfo();
		info3.actionType = ActionType.URL;
		info3.itemName = getString(R.string.grid_news_qq_txt);
		info3.imageName = images[2];
		info3.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.NEWS);
		mItemInfos.add(info3);

		GridItemInfo info4 = new GridItemInfo();
		info4.actionType = ActionType.URL;
		info4.itemName = getString(R.string.grid_search_txt);
		info4.imageName = images[3];
		info4.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.SEARCH);
		mItemInfos.add(info4);

		GridItemInfo info5 = new GridItemInfo();
		info5.actionType = ActionType.URL;
		info5.itemName = getString(R.string.grid_taobao_txt);
		info5.imageName = images[4];
		info5.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Taobao);
		mItemInfos.add(info5);

		GridItemInfo info6 = new GridItemInfo();
		info6.actionType = ActionType.URL;
		info6.itemName = getString(R.string.grid_tmall_txt);
		info6.imageName = images[5];
		info6.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Tmall);
		mItemInfos.add(info6);

		GridItemInfo info7 = new GridItemInfo();
		info7.actionType = ActionType.URL;
		info7.itemName = getString(R.string.grid_jd_txt);
		info7.imageName = images[6];
		info7.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.JD);
		mItemInfos.add(info7);

		GridItemInfo info8 = new GridItemInfo();
		info8.actionType = ActionType.URL;
		info8.itemName = getString(R.string.grid_shiping_txt);
		info8.imageName = images[7];
		info8.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Shiping);
		mItemInfos.add(info8);

		GridItemInfo info9 = new GridItemInfo();
		info9.actionType = ActionType.URL;
		info9.itemName = getString(R.string.grid_dianping_txt);
		info9.imageName = images[8];
		info9.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Dianping);
		mItemInfos.add(info9);

		GridItemInfo info10 = new GridItemInfo();
		info10.actionType = ActionType.URL;
		info10.itemName = getString(R.string.grid_weibo_txt);
		info10.imageName = images[9];
		info10.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Weibo);
		mItemInfos.add(info10);

		GridItemInfo info11 = new GridItemInfo();
		info11.actionType = ActionType.URL;
		info11.itemName = getString(R.string.grid_mogujie_txt);
		info11.imageName = images[10];
		info11.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Mogujie);
		mItemInfos.add(info11);

		GridItemInfo info12 = new GridItemInfo();
		info12.actionType = ActionType.URL;
		info12.itemName = getString(R.string.grid_jumei_txt);
		info12.imageName = images[11];
		info12.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Jumei);
		mItemInfos.add(info12);

		GridItemInfo info13 = new GridItemInfo();
		info13.actionType = ActionType.URL;
		info13.itemName = getString(R.string.grid_qunar_txt);
		info13.imageName = images[12];
		info13.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Qunar);
		mItemInfos.add(info13);

		GridItemInfo info14 = new GridItemInfo();
		info14.actionType = ActionType.URL;
		info14.itemName = getString(R.string.grid_qidian_txt);
		info14.imageName = images[13];
		info14.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Qidian);
		mItemInfos.add(info14);

		GridItemInfo info15 = new GridItemInfo();
		info15.actionType = ActionType.URL;
		info15.itemName = getString(R.string.grid_autohome_txt);
		info15.imageName = images[14];
		info15.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Autohome);
		mItemInfos.add(info15);

		GridItemInfo info16 = new GridItemInfo();
		info16.actionType = ActionType.URL;
		info16.itemName = getString(R.string.grid_jiayuan_txt);
		info16.imageName = images[15];
		info16.actionUrl = "wifi://" + BrowserActivity.class.getName() + "/"
				+ Uri.encode(Api.Jiayuan);
		mItemInfos.add(info16);
		// XLog.v(TAG, "dataSize:" + mItemInfos);
	}

	@SuppressWarnings("unchecked")
	private void handleClick(GridItemInfo info) {
		if (info.actionUrl == null
				&& (info.actionType == ActionType.ACTIVITY || info.actionType == ActionType.URL)) {
			XLog.w(TAG, "actionUrl null");
			return;
		}

		Uri uri = Uri.parse(info.actionUrl);
		String host = uri.getHost();
		if (host == null) {
			XLog.e(TAG, "host can not be null");
			return;
		}
		Class<BaseActionBarActivity<? extends Fragment>> clazz = null;
		try {
			clazz = (Class<BaseActionBarActivity<? extends Fragment>>) Class
					.forName(host);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazz == null) {
			return;
		}
		Intent intent = new Intent(getActivity(), clazz);
		switch (info.actionType) {
		case ActionType.ADS_GAME:
			DiyManager.showRecommendGameWall(getActivity());
			return;
		case ActionType.ADS_ALL:
			DiyManager.showRecommendWall(getActivity());
			return;
		case ActionType.ADS_APP:
			DiyManager.showRecommendAppWall(getActivity());
			return;
		case ActionType.ACTIVITY:
			break;
		case ActionType.URL:
			List<String> paths = uri.getPathSegments();
			if (paths != null && paths.size() > 0) {
				intent.putExtra(AppConstants.InentConst.URL_EXTRA, paths.get(0));
			}
			break;
		default:
			break;
		}

		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		XLog.v(TAG, "requestCode:" + requestCode + ", resultCOde:" + resultCode);
		if (requestCode == LOGIN_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			User user = (User) data.getSerializableExtra(InentConst.USER_EXTRA);
			mUserStatusText.setText(getString(R.string.welcom_tip,
					user.getUsername()));
			mUserStatusLayout.setClickable(false);
		}
	}

	private void jumpLogin() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivityForResult(intent, LOGIN_REQUEST_CODE);
	}

	@Override
	public void onConnectivityChanged(APN from, APN to) {
		WifiUtils.initWiFiStatus(getActivity(), mAvailableWiFiText);
	}

	@Override
	public void onWiFiScanResultsAvaible() {
		WifiInfo info = mWiFiManager.getConnectionInfo();
		XLog.v(TAG, "wifi scan over, " + info);
		List<ScanResult> results = WifiAdmin.getInstance().getWiFiList();
		if (info != null) {
			mAvailableWiFiText.setText(getString(
					R.string.main_frag_connected_wifi, info.getSSID()));
			mAvailableWiFiText.setClickable(false);
		} else if (results != null && results.size() > 0) {
			mAvailableWiFiText.setText(results.get(0).SSID);
		} else {// no connection
			mAvailableWiFiText
					.setText(getString(R.string.main_frag_no_connection));
		}
	}
}
