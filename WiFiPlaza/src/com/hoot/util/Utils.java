package com.hoot.util;

import java.io.File;
import java.io.InputStream;

import com.hoot.app.App;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class Utils {
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	private static final String TAG = "Utils";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	public static NetworkInfo getNetworkInfo() {
		ConnectivityManager cm = (ConnectivityManager) App.GlobalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	public static File getExternalCacheDir(Context context) {
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	private static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static String getDeviceId(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}

	/**
	 * ??�����褰�����?��?�����������?
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			XLog.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * ??�����褰�����?��?�����������?
	 */
	public static String getAppName(Context context) {
		String name = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			name = pi.applicationInfo.loadLabel(pm).toString();

		} catch (Exception e) {
			XLog.e(TAG, "Exception", e);
		}
		return name;
	}

	/**
	 * ??�����褰�����?��?�����������?
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = -1;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			XLog.e(TAG, "Exception", e);
		}
		return versionCode;
	}

	public static String buildInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("borad:").append(Build.BOARD).append("\nmanufacture:")
				.append(Build.MANUFACTURER).append("\ncodename:")
				.append(Build.VERSION.CODENAME).append("\nbootloader:")
				.append(Build.BOOTLOADER).append("\nbrand:")
				.append(Build.BRAND).append("\ndevice:").append(Build.DEVICE)
				.append("\ndisplay").append(Build.DISPLAY).append("\nid:")
				.append(Build.ID).append("\nfingerprint:")
				.append(Build.FINGERPRINT).append("\nhardware:")
				.append(Build.HARDWARE).append("\nproduct:")
				.append(Build.PRODUCT).append("\nhost:").append(Build.HOST)
				.append("\nmodel:").append(Build.MODEL).append("\nrelease:")
				.append(Build.VERSION.RELEASE);

		return sb.toString();
	}

	public static Options getOption(InputStream in, int w, int h) {
		final BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, sizeOptions);

		int calSampleSize = getSampleSize(w, h, sizeOptions);

		Options options = new Options();
		options.inSampleSize = calSampleSize;
		options.inJustDecodeBounds = false;
		return options;

	}

	public static Options getOption(byte[] data, int w, int h) {
		if (w <= 0) {
			h = 480;
		}

		// Set inJustDecodeBounds to get the current size of the image; does not
		// return a Bitmap
		final BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, sizeOptions);
		XLog.d(TAG, "Bitmap is " + sizeOptions.outWidth + "x"
				+ sizeOptions.outHeight);

		// Now use the size to determine the ratio you want to shrink it
		final int widthSampling = getSampleSize(w, h, sizeOptions);

		sizeOptions.inJustDecodeBounds = false;
		// Note this drops the fractional portion, making it smaller
		sizeOptions.inSampleSize = widthSampling;// log2(widthSampling);
		XLog.d(TAG, "Sample size = " + sizeOptions.inSampleSize);
		return sizeOptions;
	}

	private static int getSampleSize(int w, int h, final Options sizeOptions) {
		int sample = Math.max(sizeOptions.outHeight / h, sizeOptions.outWidth
				/ w);

		final int widthSampling = log2(sample);
		return widthSampling;
	}

	// ������褰���???���?��??����??��2���澶��??��?????��
	public static int log2(int value) {
		return (int) Math.ceil(Math.log(16) / Math.log(2));

	}

}
