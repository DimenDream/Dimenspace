package com.hoot.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hoot.manager.SystemEventManager;
import com.hoot.module.RequestInfo;
import com.hoot.module.RequestInfo.Api;
import com.hoot.module.RouterEngine;
import com.hoot.pojo.MatchRouter;
import com.hoot.pojo.MatchRouterResponse;
import com.hoot.pojo.Response;
import com.hoot.pojo.Router;
import com.hoot.pojo.RouterResponse;
import com.hoot.share.adapter.WifiAdapter;
import com.hoot.ui.fragment.AppConstants.WiFiConst;
import com.hoot.util.HandlerUtil;
import com.hoot.util.WifiAdmin;
import com.hoot.util.WifiUtils;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.LoginActivity;
import com.hoot.wifiplaza.R;
import com.tencent.assistant.net.APN;
import com.tencent.assistant.net.NetworkUtil;
import com.umeng.analytics.MobclickAgent;

public class RegistWiFiFragment extends BaseFragment {
	private static final String TAG = "RegisterFragment";
	private ListView mWifiResultList;
	private TextView mRegisterStatusTxt;
//	private Button mMatchBtn;
	private ProgressBar mLoadingView;

	private WifiAdapter mAdapter;
	private RequestInfo mRequestInfo;
	private WifiManager mWiFiManager;
	private boolean mIsConnectingWiFi;// wifi 连接状态
	private String mSSID;// 正在连接WiFi的ssid
	private String mPasswd;
	
	private final String mPageName = "RegistWiFiFragmentPage";

	public RegistWiFiFragment() {
	}

	void onScanComplete(List<ScanResult> results) {
		mAdapter.setData(results);
		mLoadingView.setVisibility(View.GONE);
		
		WifiInfo info = mWiFiManager.getConnectionInfo();
		XLog.v(TAG, "wifi scan over, " + info);
		List<ScanResult> availResults = WifiAdmin.getInstance().getWiFiList();
		if (info != null) {
			mRegisterStatusTxt.setText(getString(
					R.string.main_frag_connected_wifi, info.getSSID()));
			mRegisterStatusTxt.setClickable(false);
		} else if (availResults != null && availResults.size() > 0) {
			mRegisterStatusTxt.setText(availResults.get(0).SSID);
		} else {//no connection
			mRegisterStatusTxt.setText(getString(R.string.main_frag_no_connection));
		}
		
//		mMatchBtn.setEnabled(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		SystemEventManager.getInstance().registNetworkListener(this);
		WifiAdmin.getInstance().startScan();
		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SystemEventManager.getInstance().unregistNetworkListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_regist_wifi,
				container, false);
		mWifiResultList = (ListView) rootView.findViewById(R.id.wifi_list);
		mRegisterStatusTxt = (TextView) rootView
				.findViewById(R.id.txt_regist_status);
		mRegisterStatusTxt.setText(R.string.wifi_scanning);
		mLoadingView = (ProgressBar) rootView.findViewById(R.id.loading);

//		mMatchBtn = (Button) rootView.findViewById(R.id.match_btn);
		mAdapter = new WifiAdapter(getActivity(), new ArrayList<ScanResult>());
		mWifiResultList.setAdapter(mAdapter);

		mWifiResultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ScanResult result = mAdapter.getItem(position);
				Toast.makeText(getActivity(),
						R.string.dialog_tip_before_regist_wifi,
						Toast.LENGTH_SHORT).show();
				createInputDialog(getString(R.string.wifi_passwd_tip), result);
			}
		});

		RouterEngine.getInstance().register(this);
		mWiFiManager = WifiAdmin.getInstance().getWiFiManager();
		if (WifiAdmin.getInstance().getWiFiManager().isWifiEnabled()) {
			WifiAdmin.getInstance().startScan();
		} else {
			WifiUtils.initWiFiStatus(getActivity(), mRegisterStatusTxt);
			mLoadingView.setVisibility(View.GONE);
		}
		return rootView;
	}

	private void registerRouterAction(String ssid, String bssid, String password) {
		mRequestInfo = new RequestInfo();
		Router router = new Router();
		router.setSsid(ssid);
		router.setBssid(bssid);
		router.setPasswd(password);
		mRequestInfo.api = Api.createRouter;
		mRequestInfo.reqest = router;
		RouterEngine.getInstance().register(this);
		RouterEngine.getInstance().send(mRequestInfo);
	}
	
	@Override
	public void onConnectivityChanged(APN from, APN to) {
		super.onConnectivityChanged(from, to);
		if (mRegisterStatusTxt != null) {
			WifiUtils.initWiFiStatus(getActivity(), mRegisterStatusTxt);
		}
		WifiAdmin.getInstance().startScan();
	}
	
	@Override
	public void onDataLoaded(int seq, int code, Response data) {
		if (data instanceof MatchRouterResponse) {
			MatchRouterResponse resp = (MatchRouterResponse) data;
			mRegisterStatusTxt.setText(getString(R.string.match_wifi_tips));
			List<Router> routers = resp.getRouters();
			if (routers == null || routers.size() == 0) {
				return;
			}
			String bssid = routers.get(0).getBssid();
			String passwd = routers.get(0).getPasswd();

			XLog.v(TAG, "ssid:" + bssid + ", passwd:" + passwd);
			WifiAdmin.getInstance().connect(bssid, passwd);
		} else if (data instanceof RouterResponse) {
			if (data.getHead().getCode() == WiFiConst.ERROR_ROUTER_EXIST) {
				mRegisterStatusTxt.setText(data.getHead().getMsg());
			} else {
				RouterResponse resp = (RouterResponse) data;
				mRegisterStatusTxt.setText(resp + "");
			}
		}
		if (data == null) {
			XLog.e(TAG, "error");
		}
		XLog.v(TAG, "code:" + code + ", msg data:" + data + ", ");
	}

	private void createInputDialog(String title, final ScanResult result) {
		final EditText inputText = new EditText(getActivity());
		inputText.setHint(R.string.reg_wifi_dialog_input_pws_hint);
		Dialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(
						getString(R.string.reg_wifi_dialog_input_title,
								result.SSID))
				.setView(inputText)
				.setPositiveButton(R.string.reg_wifi_dialog_share_btn_txt,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mIsConnectingWiFi = true;
								mSSID = result.SSID;
								mPasswd = inputText.getEditableText()
										.toString();
								queryConnectStatus();
								WifiAdmin.getInstance().connect(result.BSSID,
										mPasswd);
							}
						})
				.setNegativeButton(R.string.cancel, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		dialog.show();
	}

	private void matchAvailableWiFi(View v) {
		List<String> routersMac = WifiAdmin.getInstance().getAvailWifiBSSID(
				WifiAdmin.getInstance().getWiFiList());
		List<Router> routers = new ArrayList<Router>();
		for (String bssid : routersMac) {
			Router tmp = new Router();
			tmp.setBssid(bssid);
			routers.add(tmp);
		}
		MatchRouter request = new MatchRouter();
		request.setRouters(routers);

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.reqest = request;
		requestInfo.api = Api.MATH_ROUTER;
		RouterEngine.getInstance().send(requestInfo);
	}

	@Override
	public void onAuthRequest() {
		Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT)
				.show();
		startActivityForResult(new Intent(getActivity(), LoginActivity.class),
				AppConstants.InentConst.AUTH_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConstants.InentConst.AUTH_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {

			RouterEngine.getInstance().send(mRequestInfo);
		}
	}

	@Override
	public void onWiFiScanResultsAvaible() {
		XLog.v(TAG, "scan ok");
		onScanComplete(WifiAdmin.getInstance().getWiFiList());
	}

	private static final int MSG_QUERY_WIFI_STATUS = 0;
	private static final int SAVE_WIFI_INFO = 1;
	private static final byte WIFI_CONNECT_TIME = 6;
	@SuppressLint("HandlerLeak")
	private final Handler mQueryWifiConnectionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_QUERY_WIFI_STATUS:
				WifiInfo info = mWiFiManager.getConnectionInfo();
				XLog.v(TAG, "requery count:" + msg.arg1 + ", wifiinfo:" + info
						+ ", mssid:" + mSSID + ", connected:" + NetworkUtil.isNetworkActive());
				if (mIsConnectingWiFi && info != null && info.getSSID() != null
						&& info.getSSID().equals(mSSID) && NetworkUtil.isNetworkActive()) {// 新的WiFi添加成功
					// 连接成功了,将密码传服务器
					XLog.v(TAG, "connected ok, pass pwd to server");
					registerRouterAction(info.getSSID(), info.getBSSID(),
							mPasswd);
				} else if (msg.arg1 < WIFI_CONNECT_TIME) {
					Message qmsg = Message.obtain();
					qmsg.what = MSG_QUERY_WIFI_STATUS;
					qmsg.arg1 = msg.arg1 + 1;
					sendMessageDelayed(qmsg, 1000);
				} else if (msg.arg1 >= WIFI_CONNECT_TIME) {
					mWiFiManager.reconnect();// reconnect to previous network
					Toast.makeText(getActivity(), R.string.regist_wifi_error,
							Toast.LENGTH_SHORT).show();
					XLog.v(TAG, "regist failed, reconnet to previous wifi ");
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 轮询新连接是否成功，不成功则连接上一次有效WiFi
	 */
	private void queryConnectStatus() {
		mQueryWifiConnectionHandler.sendEmptyMessage(MSG_QUERY_WIFI_STATUS);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.quick_reg_wifi, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.quick_link:
			if (mAdapter != null && mAdapter.getCount() != 0){
				matchAvailableWiFi(null);
			} else {
				XLog.i(TAG, "no wifi info");
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart(mPageName); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(mPageName);  // 统计页面结束
	}
}
