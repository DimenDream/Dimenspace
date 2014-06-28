package com.hoot.net;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;

public class DownloadTask implements
		DownloadRunnable.TaskRunnableDownloadMethods {// The image's URL
	private URL mImageURL;
	/*
	 * Field containing the Thread this task is running on.
	 */
	Thread mThreadThis;
	/*
	 * Fields containing references to the two runnable objects that handle
	 * downloading and decoding of the image.
	 */
	private Runnable mDownloadRunnable;

	// The Thread on which this task is currently running.
	private Thread mCurrentThread;

	private static DownloadManager sDownloadManager;

	public DownloadTask() {

	}

	DownloadTask(HttpGet httpget,
			RespHandler handler) {
		sDownloadManager = DownloadManager.getInstance();
		mDownloadRunnable = new DownloadRunnable(this);

	}

	void initializeDownloaderTask(DownloadManager downloadManager, URL url) {
		// Sets this object's ThreadPool field to be the input argument
		sDownloadManager = downloadManager;
		// Gets the URL for the View
		mImageURL = url;
	}

	@Override
	public void setDownloadThread(Thread currentThread) {
		setCurrentThread(currentThread);
	}

	// Delegates handling the current state of the task to the PhotoManager
	// object
	void handleState(int state) {
		sDownloadManager.handleState(this, state);
	}

	@Override
	public void handleDownloadState(int state) {
		// TODO Auto-generated method stub
		int outState;

		// Converts the download state to the overall state
		switch (state) {
		case DownloadRunnable.HTTP_STATE_COMPLETED:
			outState = DownloadManager.DOWNLOAD_COMPLETE;
			break;
		case DownloadRunnable.HTTP_STATE_FAILED:
			outState = DownloadManager.DOWNLOAD_FAILED;
			break;
		default:
			outState = DownloadManager.DOWNLOAD_STARTED;
			break;
		}
		// Passes the state to the ThreadPool object.
		handleState(outState);
	}

	@Override
	public URL getDownloadURL() {
		// TODO Auto-generated method stub
		return mImageURL;
	}

	Runnable getDownloadRunnable() {
		return mDownloadRunnable;
	}

	public Thread getCurrentThread() {
		synchronized (sDownloadManager) {
			return mCurrentThread;
		}
	}

	public void setCurrentThread(Thread thread) {
		synchronized (sDownloadManager) {
			mCurrentThread = thread;
		}
	}

	/**
	 * Recycles an PhotoTask object before it's put back into the pool. One
	 * reason to do this is to avoid memory leaks.
	 */
	void recycle() {
	}
}
