package com.hhly.mlottery.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 描    述：View的Fragment基类
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public abstract class ViewFragment<P extends IPresenter> extends MvpBaseFragment implements IView {

    protected P mPresenter;

    public static String NODATA = "1";
    public static String NETERROR = "2";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = initPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }


    public abstract P initPresenter();


    /**
     * 此处可以进行一些emptyView的配置。并进行处理
     */
}
