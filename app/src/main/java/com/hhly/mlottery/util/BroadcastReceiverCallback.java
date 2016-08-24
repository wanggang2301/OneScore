package com.hhly.mlottery.util;

import android.content.Context;
import android.content.Intent;

public interface BroadcastReceiverCallback {
	void receiver(Context context, Intent intent);
}
