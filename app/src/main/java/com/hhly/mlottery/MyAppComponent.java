package com.hhly.mlottery;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  15:22.
 */
@Component(modules = MyAppModule.class)
@Singleton
public interface MyAppComponent {

    void inject(MyApp myApp);
}
