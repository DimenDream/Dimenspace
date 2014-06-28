package com.hoot.manager;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BaseEventManager<T> implements IEventManager<T> {

	protected ReferenceQueue<T> mRefQueue = new ReferenceQueue<T>();
	protected Collection<WeakReference<? extends T>> mEventQueue = getDefaultEventContainer();

	public boolean regist(T listener) {
		if (listener == null) {
			return false;
		}
		// clean gc object
		Reference<? extends T> eventListener;
		while ((eventListener = mRefQueue.poll()) != null) {
			mEventQueue.remove(eventListener);
		}

		// is registed?
		for (WeakReference<? extends T> weakListener : mEventQueue) {
			T listenerItem = weakListener.get();
			if (listener == listenerItem) {
				return true;
			}
		}

		// regist
		WeakReference<T> connListener = new WeakReference<T>(listener,
				mRefQueue);
		return mEventQueue.add(connListener);
	}

	protected Collection<WeakReference<? extends T>> getDefaultEventContainer() {
		return new ConcurrentLinkedQueue<WeakReference<? extends T>>();
	}

	@Override
	public boolean unregist(T listener) {
		if (listener == null) {
			return false;
		}

		// concurrent modified exception?
		for (WeakReference<? extends T> weakListener : mEventQueue) {
			T listenerItem = weakListener.get();
			if (listenerItem == listener) {
				return mEventQueue.remove(weakListener);
			}
		}
		return false;
	}

}
