package com.hhly.mlottery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.PreferenceUtil;

/**
 * desc:app升级替换处理
 * Created by 107_tangrr on 2017/1/19 0019.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        PackageManager manager = context.getPackageManager();
        // 安装成功
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();

        }
        // 卸载成功
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {

        }
        // 替换成功
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            // 清除首页缓存
            PreferenceUtil.commitString(AppConstants.HOME_PAGER_DATA_KEY, null);
        }
    }
}
