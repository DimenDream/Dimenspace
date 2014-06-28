package com.hoot.manager;

public interface IEventManager<T> {
	boolean regist(T event);

	boolean unregist(T event);

}
