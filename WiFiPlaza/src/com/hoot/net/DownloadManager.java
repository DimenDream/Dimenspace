package com.hoot.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DownloadManager {
	static final int DOWNLOAD_FAILED = -1;
	static final int DOWNLOAD_STARTED = 1;
	static final int DOWNLOAD_COMPLETE = 2;
	static final int DECODE_STARTED = 3;
	static final int TASK_COMPLETE = 4;

	// A queue of PhotoManager tasks. Tasks are handed to a ThreadPool.
	private final Queue<DownloadTask> mDownloadTaskWorkQueue;

	// An object that manages Messages in a Thread
	private final Handler mHandler;
	// A managed pool of background download threads
	private final ThreadPoolExecutor mDownloadThreadPool;
	// Sets the Time Unit to seconds
	private static TimeUnit KEEP_ALIVE_TIME_UNIT;

	// Sets the initial threadpool size to 8
	private static final int CORE_POOL_SIZE = 8;

	// Sets the maximum threadpool size to 8
	private static final int MAXIMUM_POOL_SIZE = 8;
	// A queue of Runnables for the image download pool
	private final BlockingQueue<Runnable> mDownloadWorkQueue;
	/**
	 * NOTE: This is the number of total available cores. On current versions of
	 * Android, with devices that use plug-and-play cores, this will return less
	 * than the total number of cores. The total number of cores is not
	 * available in current Android implementations.
	 */
	private static int NUMBER_OF_CORES = Runtime.getRuntime()
			.availableProcessors();
	// Sets the amount of time an idle thread will wait for a task before
	// terminating
	private static final int KEEP_ALIVE_TIME = 1;
	private static final DownloadManager sInstance = DownloadManager
			.getInstance();

	private DownloadManager() throws KeyManagementException, KeyStoreException,
			FileNotFoundException, NoSuchAlgorithmException,
			CertificateException, IOException {
		// mHttpClient = buildHttpClient();
		// The time unit for "keep alive" is in seconds
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
		/*
		 * Creates a work queue for the pool of Thread objects used for
		 * downloading, using a linked list queue that blocks when the queue is
		 * empty.
		 */
		mDownloadWorkQueue = new LinkedBlockingQueue<Runnable>();
		// Creates a new pool of Thread objects for the download work queue
		mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
				MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mDownloadWorkQueue);
		mDownloadTaskWorkQueue = new LinkedBlockingQueue<DownloadTask>();
		mHandler = new Handler(Looper.getMainLooper()) {
			/*
			 * handleMessage() defines the operations to perform when the
			 * Handler receives a new Message to process.
			 */
			@Override
			public void handleMessage(Message inputMessage) {
				// Gets the image task from the incoming Message object.
				DownloadTask photoTask = (DownloadTask) inputMessage.obj;
				/*
				 * Chooses the action to take, based on the incoming message
				 */
				switch (inputMessage.what) {

				// If the download has started, sets background color to
				// dark green
				case DOWNLOAD_STARTED:
					break;

				/*
				 * If the download is complete, but the decode is waiting, sets
				 * the background color to golden yellow
				 */
				case DECODE_STARTED:
					break;
				/*
				 * The decoding is done, so this sets the ImageView's bitmap to
				 * the bitmap in the incoming message
				 */
				case TASK_COMPLETE:
					break;
				// The download failed, sets the background color to
				// dark red
				case DOWNLOAD_FAILED:
					break;
				default:
					// Otherwise, calls the super method
					super.handleMessage(inputMessage);
				}
			}

		};
	}

	private static final class SingletonHolder {
		public static final DownloadManager INSTANCE;
		static {
			try {
				INSTANCE = new DownloadManager();
			} catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}

	public static final DownloadManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */

	@SuppressLint("HandlerLeak")
	public void handleState(DownloadTask downTask, int state) {
		switch (state) {

		// The task finished downloading and decoding the image
		case TASK_COMPLETE:
			// Gets a Message object, stores the state in it, and sends it to
			// the Handler
			Message completeMessage = mHandler.obtainMessage(state, downTask);
			completeMessage.sendToTarget();
			break;

		// The task finished downloading the image
		case DOWNLOAD_COMPLETE:
			/*
			 * Decodes the image, by queuing the decoder object to run in the
			 * decoder thread pool
			 */
			// mDecodeThreadPool.execute(photoTask.getPhotoDecodeRunnable());

			// In all other cases, pass along the message without any other
			// action.
		default:
			mHandler.obtainMessage(state, downTask).sendToTarget();
			break;
		}

	}

	/**
	 * Cancels all Threads in the ThreadPool
	 */
	public static void cancelAll() {

		/*
		 * Creates an array of tasks that's the same size as the task work queue
		 */
		DownloadTask[] taskArray = new DownloadTask[sInstance.mDownloadWorkQueue
				.size()];

		// Populates the array with the task objects in the queue
		sInstance.mDownloadWorkQueue.toArray(taskArray);

		// Stores the array length in order to iterate over the array
		int taskArraylen = taskArray.length;

		/*
		 * Locks on the singleton to ensure that other processes aren't mutating
		 * Threads, then iterates over the array of tasks and interrupts the
		 * task's current Thread.
		 */
		synchronized (sInstance) {
			// Iterates over the array of tasks
			for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {
				// Gets the task's current thread
				Thread thread = taskArray[taskArrayIndex].mThreadThis;
				// if the Thread exists, post an interrupt to it
				if (null != thread) {
					thread.interrupt();
				}
			}
		}
	}

	/**
	 * Stops a download Thread and removes it from the threadpool
	 * 
	 * @param downloaderTask
	 *            The download task associated with the Thread
	 * @param pictureURL
	 *            The URL being downloaded
	 */
	static public void removeDownload(DownloadTask downloaderTask,
			URL pictureURL) {

		// If the Thread object still exists and the download matches the
		// specified URL
		if (downloaderTask != null
				&& downloaderTask.getDownloadURL().equals(pictureURL)) {
			/*
			 * Locks on this class to ensure that other processes aren't
			 * mutating Threads.
			 */
			synchronized (sInstance) {
				// Gets the Thread that the downloader task is running on
				Thread thread = downloaderTask.getCurrentThread();

				// If the Thread exists, posts an interrupt to it
				if (null != thread)
					thread.interrupt();
			}
			/*
			 * Removes the download Runnable from the ThreadPool. This opens a
			 * Thread in the ThreadPool's work queue, allowing a task in the
			 * queue to start.
			 */
			sInstance.mDownloadThreadPool.remove(downloaderTask
					.getDownloadRunnable());
		}
	}

	static public DownloadTask startDownload() {
		/*
		 * Gets a task from the pool of tasks, returning null if the pool is
		 * empty
		 */
		DownloadTask downloadTask = sInstance.mDownloadTaskWorkQueue.poll();

		// If the queue was empty, create a new task instead.
		if (null == downloadTask) {
			downloadTask = new DownloadTask();
		}

		// Initializes the task
		downloadTask.initializeDownloaderTask(DownloadManager.sInstance, null);

		/*
		 * "Executes" the tasks' download Runnable in order to download the
		 * image. If no Threads are available in the thread pool, the Runnable
		 * waits in the queue.
		 */
		sInstance.mDownloadThreadPool.execute(downloadTask
				.getDownloadRunnable());

		// Returns a task object, either newly-created or one from the task pool
		return downloadTask;
	}

	/**
	 * Recycles tasks by calling their internal recycle() method and then
	 * putting them back into the task queue.
	 * 
	 * @param downloadTask
	 *            The task to recycle
	 */
	void recycleTask(DownloadTask downloadTask) {

		// Frees up memory in the task
		downloadTask.recycle();

		// Puts the task object back into the queue for re-use.
		mDownloadTaskWorkQueue.offer(downloadTask);
	}
}
