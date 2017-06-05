package data;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  14:32.
 */

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {

    void inject(DataManager dataManager);
}
