package com.hoot.proto;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import com.hoot.encoding.JsonUtil;
import com.hoot.pojo.Request;
import com.hoot.pojo.Response;
import com.hoot.proto.listener.IProtocolListener;
import com.hoot.util.XLog;

public class NetworkTask implements Runnable {
	private static final String UTF_8 = "UTF-8";
	private static final String TAG = "NetworkTask";
	private HttpURLConnection mConn;
	private AtomicInteger mTaskId;
	private Request mReqCmd;

	private IProtocolListener mListener;

	public NetworkTask(HttpURLConnection conn) {
		mConn = conn;
		mTaskId = new AtomicInteger(1);
	}

	public void setSendData(Request data) {
		mReqCmd = data;
	}

	public void setProtocolListener(IProtocolListener listener) {
		mListener = listener;
	}

	@Override
	public void run() {
		OutputStream os;
		try {
			os = mConn.getOutputStream();
			byte[] data = JsonUtil.getBytes(mReqCmd);
			os.write(data);
			os.close();

			BufferedInputStream bis = new BufferedInputStream(
					mConn.getInputStream());
			byte[] buffer = new byte[mConn.getContentLength()];
			int len = bis.read(buffer);
			if (len != mConn.getContentLength()) {
				XLog.v(TAG, "read error");
			}
			String s = new String(buffer, UTF_8);
			Response resp = getResp(mReqCmd);
			XLog.v(TAG, "resp:" + resp + ", data:" + s);
			resp = JsonUtil.getObject(s, resp.getClass());

			onFinished(ResultCode.Code_Ok, resp);
		} catch (IOException e) {
			onFinished(ResultCode.Code_Http_IOErr, null);
			e.printStackTrace();
		}
	}

	private Response getResp(Request req) {
		String className = req.getClass().getName();
		className = req.getClass().getName() + "Response";
		Response resp = null;
		try {
			Class<?> clazz = Class.forName(className);
			resp = (Response) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	public int getTaskId() {
		return mTaskId.getAndIncrement();
	}

	public void cancelTask() {
		if (mConn != null) {
			mConn.disconnect();
			mConn = null;
		}
	}

	private void onFinished(int respCode, Response result) {
		if (mListener != null) {
			mListener.onRequestFinished(respCode, result);
		}
	}
}
