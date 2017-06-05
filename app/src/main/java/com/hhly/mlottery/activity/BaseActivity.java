package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.util.FragmentUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenney
 * @ClassName: BaseActivity
 * @Description:所有activity的基类
 * @date 2015-10-16 下午4:18:43
 */
public class BaseActivity extends FragmentActivity {
    public ProgressDialog pd;

    public static String TAG = "BaseFragmentActivity";

    private List<Class<? extends Fragment>> fragments = new ArrayList<Class<? extends Fragment>>();

    private int containerId;
    private int currentPosition = 0;
    protected Context mContext;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		// 初始化PreferenceUtil
//		PreferenceUtil.init(this);
//		// 根据上次的语言设置，重新设置语言
//		switchLanguage(PreferenceUtil.getString("language", "rCN"));
        mContext = this;
        // 设置背景
        View view = this.getWindow().getDecorView(); // getDecorView
        // 获得window最顶层的View
        view.setBackgroundColor(Color.WHITE);// 设置全局背景颜色

//        AppManager.getAppManager().addActivity(this);
        pd = new ProgressDialog(this);
        pd.setMessage("玩命加载中...");
        pd.setCanceledOnTouchOutside(false);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition");
        }

    }

    /**
     * 添加Fragment
     *
     * @param containerId Fragment容器id
     * @param newFragment
     */
    protected void addFragment(int containerId, Class<? extends Fragment>... newFragment) {
        setContainer(containerId);
        for (int i = 0; i < newFragment.length; i++) {
            fragments.add(newFragment[i]);
        }
    }

    /**
     * 设置fragment显示容器的id
     *
     * @param containerId
     */
    protected void setContainer(int containerId) {
        this.containerId = containerId;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int ResId) {
        return (T) this.findViewById(ResId);
    }

    /**
     * 提交
     */
    protected void commit() {
        commit(currentPosition);
    }

    protected void commit(int selectorPosition) {
        if (fragments.size() <= 0)
            return;
        if (containerId == 0)
            throw new RuntimeException("没有设置Fragment容器");
        replaceFragment(selectorPosition);
    }

    /**
     * 切换Fragment
     *
     * @param position
     */
    protected void replaceFragment(int position) {
        replaceFragment(position, null);
    }

    /**
     * 切换Fragment
     *
     * @param position
     * @param bundle   传递参数
     */
    protected void replaceFragment(int position, Bundle bundle) {
        replaceFragment(position, bundle, false);
    }

    /**
     * 切换Fragment
     *
     * @param position
     * @param bundle   传递参数
     */
    protected void replaceFragment(int position, Bundle bundle, boolean isNewFragment) {
        mCurrentFragment = FragmentUtils.switchFragment(getSupportFragmentManager(), containerId, mCurrentFragment,
                fragments.get(position), bundle, false, fragments.get(position).getSimpleName()
                        + position, isNewFragment);
        currentPosition = position;
    }

    /**
     * 获得当前界面显示的Fragment实例
     */
    protected Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
      //  TCAgent.onPageStart(this, getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
       // TCAgent.onPageEnd(this, getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
//        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
        MyApp.getRefWatcher().watch(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);这样就不会保存状态  baseactivity被系统意外回收的时候，
// 他里面的fragment也会被回收，这样可以解决fragment获取getactivity为null的问题
    }
}
