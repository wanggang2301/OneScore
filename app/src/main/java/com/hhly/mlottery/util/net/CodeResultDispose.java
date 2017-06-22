package com.hhly.mlottery.util.net;

import android.widget.Toast;

import com.hhly.mlottery.MyApp;

/**
 * Created by：XQyi on 2017/6/22 09:49
 * Use: 后台接口返回code 处理（异常code提示语）
 */
public class CodeResultDispose {

    public void CodeToast(Integer code){

        switch (code){

            case 1000:
                Toast.makeText(MyApp.getContext(), "您已在其他设备登录，请重新登录！", Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
