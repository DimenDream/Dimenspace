package com.hoot.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManager {
	private static final int POOL_SIZE = 5;
	private ExecutorService mService;
	private static TaskManager mManger;

	private TaskManager() {
		mService = Executors.newFixedThreadPool(POOL_SIZE);
	}

	public static final synchronized TaskManager getInstance() {
		if (mManger == null) {
			mManger = new TaskManager();
		}
		return mManger;
	}

	public Future<?> addTask(Runnable task) {
		return mService.submit(task);
	}

	public <T> Future<T> submit(Callable<T> task) {
		return mService.submit(task);
	}
}
