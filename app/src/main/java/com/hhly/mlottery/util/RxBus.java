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
    private final Subject<Object,Object> _bus;

    public RxBus() {
        _bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return RxBusInstance.rxBus;
    }

    private static class RxBusInstance {
        private static final RxBus rxBus = new RxBus();
    }

    public Observable register(Class eventType) {
        return _bus.ofType(eventType);
    }

    // 提供了一个新的事件
    public void post(Object o) {
        _bus.onNext(o);
    }

    public <T> Observable<T> toObserverable(final Class<T> eventType) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }

    /** subscribe to the event */
    public Observable<Object> toObserverable() {
        return _bus;
    }
}