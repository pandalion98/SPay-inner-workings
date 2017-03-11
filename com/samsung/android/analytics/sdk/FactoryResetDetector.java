package com.samsung.android.analytics.sdk;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FactoryResetDetector extends BroadcastReceiver {
    @SuppressLint({"LongLogTag"})
    public void onReceive(Context context, Intent intent) {
        Log.d("AnalyticsFactoryResetDetector", "Just to disable/enable and detect reset");
    }
}
