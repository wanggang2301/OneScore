package com.hhly.mlottery.util;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
	private static volatile RxBus defaultInstance;
	/**
	 * Subject同时充当了Observer和Observable的角色，
	 * Subject是非线程安全的，要避免该问题，需要将 Subject转换为一个 SerializedSubject ，上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
	 */
	private final Subject bus;

	public RxBus() {
		bus = new SerializedSubject<>(PublishSubject.create());
	}

	public static RxBus getDefault() {
		RxBus rxBus = defaultInstance;
		if (defaultInstance == null) {
			synchronized (RxBus.class) {
				rxBus = defaultInstance;
				if (defaultInstance == null) {
					rxBus = new RxBus();
					defaultInstance = rxBus;
				}
			}
		}
		return rxBus;
	}

	// 提供了一个新的事件
	public void post(Object o) {
		bus.onNext(o);
	}

	public <T> Observable<T> toObserverable(final Class<T> eventType) {
		//		return bus.ofType(eventType);
		return bus.filter(new Func1<Object, Boolean>() {
			@Override public Boolean call(Object o) {
				return eventType.isInstance(o);
			}
		}).cast(eventType);
	}
}