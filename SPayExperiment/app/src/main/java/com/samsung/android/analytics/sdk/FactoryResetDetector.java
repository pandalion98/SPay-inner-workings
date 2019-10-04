/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.util.Log
 *  java.lang.String
 */
package com.samsung.android.analytics.sdk;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FactoryResetDetector
extends BroadcastReceiver {
    @SuppressLint(value={"LongLogTag"})
    public void onReceive(Context context, Intent intent) {
        Log.d((String)"AnalyticsFactoryResetDetector", (String)"Just to disable/enable and detect reset");
    }
}

