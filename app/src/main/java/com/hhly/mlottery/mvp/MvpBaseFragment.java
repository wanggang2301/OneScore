package com.hhly.mlottery.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.hhly.mlottery.activity.BaseActivity;

/**
 * 描    述：mvp的fragment基类
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public abstract class MvpBaseFragment extends Fragment{
    protected final String TAG = getClass().getSimpleName();
    protected BaseActivity mActivity; //子类可直接使用

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (BaseActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}
