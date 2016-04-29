package com.hhly.mlottery.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.bean.HotFocusLeagueCup;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.util.net.VolleyContentFast;

public class HotFocusUtils {

	private final static String TAG = "HotFocusUtils";
	private final static String HOT_FOCUS_CUP_JSON = "hot_focus_cup_json";
	private final static String HOT_FOCUS_CUP_DATE = "hot_focus_cup_date";

	public void loadHotFocusData(Context context,final RequestHostFocusCallBack requestCallBack) {
		
		
		String localJson = PreferenceUtil.getString(HOT_FOCUS_CUP_JSON, "");
		String saveDate = PreferenceUtil.getString(HOT_FOCUS_CUP_DATE, "");

		if ("".equals(localJson)) {
			requestHotFocus(context,new RequestHostFocusCallBack() {
				@Override
				public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {
					requestCallBack.callBack(hotFocusLeagueCup);
				}
			});

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String localdate = sdf.format(new Date());
			if (!saveDate.equals(localdate)) {
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				L.d(TAG, "hour = " + hour);
				L.d(TAG, "minute = " + minute);
				if (hour > 11 && minute > 30) {
					requestHotFocus(context,new RequestHostFocusCallBack() {
						@Override
						public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {
							requestCallBack.callBack(hotFocusLeagueCup);
						}
					});
				}else{
					HotFocusLeagueCup hot = null;
					try {
						hot = JSON.parseObject(localJson, HotFocusLeagueCup.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
					requestCallBack.callBack(hot);
				}
			} else {
				HotFocusLeagueCup hot = null;
				try {
					hot = JSON.parseObject(localJson, HotFocusLeagueCup.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				requestCallBack.callBack(hot);
			}
		}
	}

	private void requestHotFocus(final Context context,final RequestHostFocusCallBack requestCallBack) {

//		final ProgressDialog progressDialog = new ProgressDialog(context);
//		progressDialog.show();

		VolleyContentFast.requestStringByGet(BaseURLs.URL_Hot_focus, new VolleyContentFast.ResponseSuccessListener<String>() {
			@Override
			public synchronized void onResponse(final String json) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String localdate = sdf.format(new Date());

				PreferenceUtil.commitString(HOT_FOCUS_CUP_JSON, json);
				PreferenceUtil.commitString(HOT_FOCUS_CUP_DATE, localdate);// 会更新？

				HotFocusLeagueCup hot = null;
				try {
					hot = JSON.parseObject(json, HotFocusLeagueCup.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				requestCallBack.callBack(hot);
			}
		}, new VolleyContentFast.ResponseErrorListener() {
			@Override
			public void onErrorResponse(VolleyContentFast.VolleyException exception) {

			}
		});

	}

}
