package com.hhly.mlottery.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

/**
 * 创建人：107tangrr
 * 邮  箱：tangrr@13322.com
 * 时  间：2016/5/20
 * 描  述：新版本下载安装广播
 */
public class CompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
//        L.d("xxx", "id = " + intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
//
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        Intent openIntent = new Intent();
//        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        openIntent.setAction(android.content.Intent.ACTION_VIEW);
////        L.d("xxx","url = "+downloadManager.getUriForDownloadedFile(id).toString());
////        L.d("xxx","path = "+downloadManager.getUriForDownloadedFile(id).getPath());
//
//        if (downloadManager.getUriForDownloadedFile(id) != null) {
//            openIntent.setDataAndType(downloadManager.getUriForDownloadedFile(id), "application/vnd.android.package-archive");
//            context.startActivity(openIntent);
//        }else{
//            Toast.makeText(context, context.getResources().getString(R.string.download_error), Toast.LENGTH_SHORT).show();
//        }
    }
}
