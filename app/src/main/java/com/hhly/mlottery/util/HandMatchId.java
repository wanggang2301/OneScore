package com.hhly.mlottery.util;

import android.content.Context;
import android.widget.Toast;

import com.hhly.mlottery.R;

/**
 * @author: Wangg
 * @name：xxx
 * @description: matchid==-1时不跳转到内页
 * @created on:2017/5/23  11:51.
 */

public class HandMatchId {


    public static boolean handId(Context context, String id) {
        if (id.equals("-1")) {
            Toast.makeText(context, context.getString(R.string.data_updating), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
